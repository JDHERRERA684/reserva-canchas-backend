"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { reservationsApi, courtsApi, ApiError, type GetPublicReservationResponse, type GetCourtResponse } from "@/lib/api"
import { CalendarDays, Clock, Search, Loader2, Pencil, X, AlertCircle } from "lucide-react"
import { format, startOfWeek, endOfWeek, eachDayOfInterval, isSameDay, addWeeks, subWeeks } from "date-fns"
import { es } from "date-fns/locale"
import Link from "next/link"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Textarea } from "@/components/ui/textarea"

export default function PublicReservationsPage() {
  const [reservations, setReservations] = useState<GetPublicReservationResponse[]>([])
  const [courts, setCourts] = useState<GetCourtResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [currentWeek, setCurrentWeek] = useState(new Date())
  const [selectedCourt, setSelectedCourt] = useState<number | null>(null)
  const [isReservationDialogOpen, setIsReservationDialogOpen] = useState(false)
  const [reservationCode, setReservationCode] = useState("")
  const [isSearching, setIsSearching] = useState(false)
  const [searchResult, setSearchResult] = useState<GetPublicReservationResponse | null>(null)
  const [searchError, setSearchError] = useState("")

  // Edit reservation state
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)
  const [editForm, setEditForm] = useState({
    courtId: 0,
    startDatetime: "",
    endDatetime: "",
    notes: "",
  })
  const [isUpdating, setIsUpdating] = useState(false)
  const [isCancelling, setIsCancelling] = useState(false)

  // New reservation form
  const [isNewReservationDialogOpen, setIsNewReservationDialogOpen] = useState(false)
  const [newReservationForm, setNewReservationForm] = useState({
    clientName: "",
    clientPhone: "",
    courtId: 0,
    startDatetime: "",
    endDatetime: "",
    notes: "",
  })
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [createError, setCreateError] = useState("")
  const [updateError, setUpdateError] = useState("")
  const [cancelError, setCancelError] = useState("")

  useEffect(() => {
    async function fetchData() {
      try {
        const [reservationsData, courtsData] = await Promise.all([
          reservationsApi.getPublic(),
          courtsApi.getAll(),
        ])
        setReservations(reservationsData)
        setCourts(courtsData)
      } catch (error) {
        console.error("[v0] Error fetching public data:", error)
      } finally {
        setIsLoading(false)
      }
    }
    fetchData()
  }, [])

  const weekStart = startOfWeek(currentWeek, { weekStartsOn: 1 })
  const weekEnd = endOfWeek(currentWeek, { weekStartsOn: 1 })
  const daysOfWeek = eachDayOfInterval({ start: weekStart, end: weekEnd })

  const getReservationsForDayAndCourt = (date: Date, courtId: number) => {
    return reservations.filter((r) => {
      const reservationDate = new Date(r.startDatetime)
      return isSameDay(reservationDate, date) && r.courtId === courtId
    })
  }

  const handleSearchReservation = async () => {
    if (!reservationCode.trim()) return
    setIsSearching(true)
    setSearchError("")
    setSearchResult(null)

    const found = reservations.find(
      (r) => r.reservationCode?.toLowerCase() === reservationCode.toLowerCase()
    )

    if (found) {
      setSearchResult(found)
    } else {
      setSearchError("No se encontró ninguna reserva con ese código")
    }
    setIsSearching(false)
  }

  const handleOpenEditDialog = () => {
    if (searchResult) {
      setUpdateError("")
      setEditForm({
        courtId: searchResult.courtId,
        startDatetime: searchResult.startDatetime.slice(0, 16),
        endDatetime: searchResult.endDatetime.slice(0, 16),
        notes: "",
      })
      setIsEditDialogOpen(true)
    }
  }

  const handleUpdateReservation = async () => {
    if (!searchResult || !reservationCode) return
    setIsUpdating(true)
    setUpdateError("")
    try {
      await reservationsApi.updateClient(searchResult.id, {
        courtId: editForm.courtId,
        startDatetime: editForm.startDatetime,
        endDatetime: editForm.endDatetime,
        reservationCode: reservationCode,
        notes: editForm.notes || undefined,
      })
      setIsEditDialogOpen(false)
      setIsReservationDialogOpen(false)
      setSearchResult(null)
      setReservationCode("")
      // Refresh reservations
      const data = await reservationsApi.getPublic()
      setReservations(data)
    } catch (error: unknown) {
      if (error instanceof ApiError) {
        setUpdateError(error.message)
      } else if (error instanceof Error) {
        setUpdateError(error.message)
      } else {
        setUpdateError("Error al actualizar la reserva")
      }
    } finally {
      setIsUpdating(false)
    }
  }

  const handleCancelReservation = async () => {
    if (!searchResult || !reservationCode) return
    if (!confirm("¿Estás seguro de que deseas cancelar esta reserva?")) return
    setIsCancelling(true)
    setCancelError("")
    try {
      await reservationsApi.cancel(searchResult.id, {
        reservationCode: reservationCode,
      })
      setIsReservationDialogOpen(false)
      setSearchResult(null)
      setReservationCode("")
      // Refresh reservations
      const data = await reservationsApi.getPublic()
      setReservations(data)
    } catch (error: unknown) {
      if (error instanceof ApiError) {
        setCancelError(error.message)
      } else if (error instanceof Error) {
        setCancelError(error.message)
      } else {
        setCancelError("Error al cancelar la reserva")
      }
    } finally {
      setIsCancelling(false)
    }
  }

  const handleCreateReservation = async () => {
    setIsSubmitting(true)
    setCreateError("")
    try {
      await reservationsApi.create({
        ...newReservationForm,
        statusId: 1, // ACTIVE
        startDatetime: newReservationForm.startDatetime,
        endDatetime: newReservationForm.endDatetime,
      })
      setIsNewReservationDialogOpen(false)
      setNewReservationForm({
        clientName: "",
        clientPhone: "",
        courtId: 0,
        startDatetime: "",
        endDatetime: "",
        notes: "",
      })
      // Refresh reservations
      const data = await reservationsApi.getPublic()
      setReservations(data)
    } catch (error: unknown) {
      if (error instanceof ApiError) {
        setCreateError(error.message)
      } else if (error instanceof Error) {
        setCreateError(error.message)
      } else {
        setCreateError("Error al crear la reserva")
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const filteredCourts = selectedCourt
    ? courts.filter((c) => c.id === selectedCourt)
    : courts

  if (isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b bg-card">
        <div className="mx-auto flex max-w-7xl flex-col gap-4 px-4 py-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary">
              <CalendarDays className="h-5 w-5 text-primary-foreground" />
            </div>
            <div>
              <h1 className="text-lg font-semibold">Reservas Suárez</h1>
              <p className="text-sm text-muted-foreground">Sistema de reservas de canchas</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Button variant="outline" onClick={() => setIsReservationDialogOpen(true)}>
              <Search className="mr-2 h-4 w-4" />
              Buscar mi reserva
            </Button>
            <Button asChild variant="ghost">
              <Link href="/login">Iniciar sesión</Link>
            </Button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl p-4 lg:p-6">
        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-bold tracking-tight">Disponibilidad de Canchas</h2>
            <p className="text-muted-foreground">
              Semana del {format(weekStart, "d 'de' MMMM", { locale: es })} al{" "}
              {format(weekEnd, "d 'de' MMMM, yyyy", { locale: es })}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              onClick={() => setCurrentWeek(subWeeks(currentWeek, 1))}
            >
              Anterior
            </Button>
            <Button
              variant="outline"
              onClick={() => setCurrentWeek(new Date())}
            >
              Hoy
            </Button>
            <Button
              variant="outline"
              onClick={() => setCurrentWeek(addWeeks(currentWeek, 1))}
            >
              Siguiente
            </Button>
          </div>
        </div>

        {/* Court filter */}
        <div className="mb-6 flex flex-wrap gap-2">
          <Button
            variant={selectedCourt === null ? "default" : "outline"}
            size="sm"
            onClick={() => setSelectedCourt(null)}
          >
            Todas las canchas
          </Button>
          {courts.map((court) => (
            <Button
              key={court.id}
              variant={selectedCourt === court.id ? "default" : "outline"}
              size="sm"
              onClick={() => setSelectedCourt(court.id)}
            >
              {court.name}
            </Button>
          ))}
        </div>

        {/* New Reservation Button */}
        <div className="mb-6">
          <Button onClick={() => setIsNewReservationDialogOpen(true)}>
            Hacer una reserva
          </Button>
        </div>

        {/* Calendar Grid */}
        <div className="space-y-6">
          {filteredCourts.map((court) => (
            <Card key={court.id}>
              <CardHeader className="pb-2">
                <CardTitle className="flex items-center gap-2 text-lg">
                  <CalendarDays className="h-5 w-5 text-primary" />
                  {court.name}
                </CardTitle>
                {court.description && (
                  <p className="text-sm text-muted-foreground">{court.description}</p>
                )}
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-7 gap-2">
                  {daysOfWeek.map((day) => (
                    <div key={day.toISOString()} className="min-h-[120px]">
                      <div
                        className={`mb-2 text-center text-sm font-medium ${
                          isSameDay(day, new Date())
                            ? "text-primary"
                            : "text-muted-foreground"
                        }`}
                      >
                        <div className="capitalize">
                          {format(day, "EEE", { locale: es })}
                        </div>
                        <div
                          className={`mx-auto flex h-7 w-7 items-center justify-center rounded-full ${
                            isSameDay(day, new Date()) ? "bg-primary text-primary-foreground" : ""
                          }`}
                        >
                          {format(day, "d")}
                        </div>
                      </div>
                      <div className="space-y-1">
                        {getReservationsForDayAndCourt(day, court.id).map((reservation) => (
                          <div
                            key={reservation.id}
                            className={`rounded px-2 py-1 text-xs ${
                              reservation.statusName?.toLowerCase() === "ACTIVE"
                                ? "bg-green-100 text-green-700"
                                : reservation.statusName?.toLowerCase() === "COMPLETED"
                                ? "bg-yellow-100 text-yellow-700"
                                : "bg-gray-100 text-gray-700"
                            }`}
                          >
                            <div className="flex items-center gap-1">
                              <Clock className="h-3 w-3" />
                              {format(new Date(reservation.startDatetime), "HH:mm")} -{" "}
                              {format(new Date(reservation.endDatetime), "HH:mm")}
                            </div>
                          </div>
                        ))}
                        {getReservationsForDayAndCourt(day, court.id).length === 0 && (
                          <div className="text-center text-xs text-muted-foreground">
                            Disponible
                          </div>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </main>

      {/* Search Reservation Dialog */}
      <Dialog open={isReservationDialogOpen} onOpenChange={(open) => { setIsReservationDialogOpen(open); if (!open) { setSearchError(""); setCancelError(""); setSearchResult(null); setReservationCode(""); } }}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Buscar mi reserva</DialogTitle>
            <DialogDescription>
              Ingresa el código de tu reserva para ver los detalles
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="flex gap-2">
              <Input
                placeholder="Código de reserva"
                value={reservationCode}
                onChange={(e) => setReservationCode(e.target.value)}
              />
              <Button onClick={handleSearchReservation} disabled={isSearching}>
                {isSearching ? <Loader2 className="h-4 w-4 animate-spin" /> : "Buscar"}
              </Button>
            </div>
            {searchError && <p className="text-sm text-destructive">{searchError}</p>}
            {cancelError && (
              <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
                <AlertCircle className="h-4 w-4 shrink-0" />
                <span>{cancelError}</span>
              </div>
            )}
            {searchResult && (
              <Card>
                <CardContent className="pt-4">
                  <div className="space-y-2">
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Cancha:</span>
                      <span className="font-medium">{searchResult.courtName}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Fecha:</span>
                      <span className="font-medium">
                        {format(new Date(searchResult.startDatetime), "dd/MM/yyyy", { locale: es })}
                      </span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Horario:</span>
                      <span className="font-medium">
                        {format(new Date(searchResult.startDatetime), "HH:mm")} -{" "}
                        {format(new Date(searchResult.endDatetime), "HH:mm")}
                      </span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Estado:</span>
                      <span
                        className={`inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ${
                          searchResult.statusName?.toLowerCase() === "ACTIVE"
                            ? "bg-green-100 text-green-700"
                            : searchResult.statusName?.toLowerCase() === "COMPLETED"
                            ? "bg-yellow-100 text-yellow-700"
                            : "bg-gray-100 text-gray-700"
                        }`}
                      >
                        {searchResult.statusName}
                      </span>
                    </div>
                  </div>
                  {searchResult.statusName !== "CANCELLED"
                      &&
                      searchResult.statusName !== "COMPLETED" && (
                    <div className="mt-4 flex gap-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={handleOpenEditDialog}
                      >
                        <Pencil className="mr-2 h-4 w-4" />
                        Editar
                      </Button>
                      <Button
                        variant="destructive"
                        size="sm"
                        onClick={handleCancelReservation}
                        disabled={isCancelling}
                      >
                        {isCancelling ? (
                          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                        ) : (
                          <X className="mr-2 h-4 w-4" />
                        )}
                        Cancelar reserva
                      </Button>
                    </div>
                  )}
                </CardContent>
              </Card>
            )}
          </div>
        </DialogContent>
      </Dialog>

      {/* New Reservation Dialog */}
      <Dialog open={isNewReservationDialogOpen} onOpenChange={(open) => { setIsNewReservationDialogOpen(open); if (!open) setCreateError(""); }}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Nueva Reserva</DialogTitle>
            <DialogDescription>Completa los datos para hacer tu reserva</DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="clientName">Tu nombre</Label>
              <Input
                id="clientName"
                value={newReservationForm.clientName}
                onChange={(e) =>
                  setNewReservationForm({ ...newReservationForm, clientName: e.target.value })
                }
                placeholder="Nombre completo"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="clientPhone">Tu teléfono</Label>
              <Input
                id="clientPhone"
                value={newReservationForm.clientPhone}
                onChange={(e) =>
                  setNewReservationForm({ ...newReservationForm, clientPhone: e.target.value })
                }
                placeholder="Número de teléfono"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="courtId">Cancha</Label>
              <select
                id="courtId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={newReservationForm.courtId}
                onChange={(e) =>
                  setNewReservationForm({ ...newReservationForm, courtId: Number(e.target.value) })
                }
              >
                <option value={0}>Seleccionar cancha</option>
                {courts.map((court) => (
                  <option key={court.id} value={court.id}>
                    {court.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="grid gap-2">
                <Label htmlFor="startDatetime">Inicio</Label>
                <Input
                  id="startDatetime"
                  type="datetime-local"
                  value={newReservationForm.startDatetime}
                  onChange={(e) =>
                    setNewReservationForm({ ...newReservationForm, startDatetime: e.target.value })
                  }
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="endDatetime">Fin</Label>
                <Input
                  id="endDatetime"
                  type="datetime-local"
                  value={newReservationForm.endDatetime}
                  onChange={(e) =>
                    setNewReservationForm({ ...newReservationForm, endDatetime: e.target.value })
                  }
                />
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="notes">Notas (opcional)</Label>
              <Textarea
                id="notes"
                value={newReservationForm.notes}
                onChange={(e) =>
                  setNewReservationForm({ ...newReservationForm, notes: e.target.value })
                }
                placeholder="Información adicional..."
              />
            </div>
            {createError && (
              <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
                <AlertCircle className="h-4 w-4 shrink-0" />
                <span>{createError}</span>
              </div>
            )}
          </div>
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setIsNewReservationDialogOpen(false)}>
              Cancelar
            </Button>
            <Button
              onClick={handleCreateReservation}
              disabled={
                isSubmitting ||
                !newReservationForm.clientName ||
                !newReservationForm.clientPhone ||
                !newReservationForm.courtId ||
                !newReservationForm.startDatetime ||
                !newReservationForm.endDatetime
              }
            >
              {isSubmitting && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Reservar
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Edit Reservation Dialog */}
      <Dialog open={isEditDialogOpen} onOpenChange={(open) => { setIsEditDialogOpen(open); if (!open) setUpdateError(""); }}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Editar Reserva</DialogTitle>
            <DialogDescription>
              Modifica los datos de tu reserva. Codigo: {reservationCode}
            </DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="edit-courtId">Cancha</Label>
              <select
                id="edit-courtId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={editForm.courtId}
                onChange={(e) =>
                  setEditForm({ ...editForm, courtId: Number(e.target.value) })
                }
              >
                <option value={0}>Seleccionar cancha</option>
                {courts.map((court) => (
                  <option key={court.id} value={court.id}>
                    {court.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="grid gap-2">
                <Label htmlFor="edit-startDatetime">Inicio</Label>
                <Input
                  id="edit-startDatetime"
                  type="datetime-local"
                  value={editForm.startDatetime}
                  onChange={(e) =>
                    setEditForm({ ...editForm, startDatetime: e.target.value })
                  }
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="edit-endDatetime">Fin</Label>
                <Input
                  id="edit-endDatetime"
                  type="datetime-local"
                  value={editForm.endDatetime}
                  onChange={(e) =>
                    setEditForm({ ...editForm, endDatetime: e.target.value })
                  }
                />
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="edit-notes">Notas (opcional)</Label>
              <Textarea
                id="edit-notes"
                value={editForm.notes}
                onChange={(e) =>
                  setEditForm({ ...editForm, notes: e.target.value })
                }
                placeholder="Informacion adicional..."
              />
            </div>
            {updateError && (
              <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
                <AlertCircle className="h-4 w-4 shrink-0" />
                <span>{updateError}</span>
              </div>
            )}
          </div>
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setIsEditDialogOpen(false)}>
              Cancelar
            </Button>
            <Button
              onClick={handleUpdateReservation}
              disabled={
                isUpdating ||
                !editForm.courtId ||
                !editForm.startDatetime ||
                !editForm.endDatetime
              }
            >
              {isUpdating && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Guardar cambios
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
