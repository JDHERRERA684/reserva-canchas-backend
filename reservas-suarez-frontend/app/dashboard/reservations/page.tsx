"use client"

import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { reservationsApi, courtsApi, ApiError, type GetReservationResponse, type GetCourtResponse, type CreateReservationRequest } from "@/lib/api"
import { Plus, MoreHorizontal, Pencil, Trash2, XCircle, Search, Loader2, AlertCircle } from "lucide-react"
import { format } from "date-fns"
import { es } from "date-fns/locale"
import { Textarea } from "@/components/ui/textarea"

const STATUS_OPTIONS = [
  { id: 1, name: "ACTIVE" },
  { id: 2, name: "CANCELLED" },
  { id: 3, name: "COMPLETED" },
]

export default function ReservationsPage() {
  const [reservations, setReservations] = useState<GetReservationResponse[]>([])
  const [courts, setCourts] = useState<GetCourtResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState("")
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [isCancelDialogOpen, setIsCancelDialogOpen] = useState(false)
  const [selectedReservation, setSelectedReservation] = useState<GetReservationResponse | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  
  // Error states for each operation
  const [createError, setCreateError] = useState("")
  const [updateError, setUpdateError] = useState("")
  const [deleteError, setDeleteError] = useState("")
  const [cancelError, setCancelError] = useState("")

  const [formData, setFormData] = useState<CreateReservationRequest>({
    clientName: "",
    clientPhone: "",
    courtId: 0,
    statusId: 1,
    startDatetime: "",
    endDatetime: "",
    notes: "",
  })

  const fetchData = async () => {
    try {
      const [reservationsData, courtsData] = await Promise.all([
        reservationsApi.getAll(),
        courtsApi.getAll(),
      ])
      setReservations(reservationsData)
      setCourts(courtsData)
    } catch (error) {
      console.error("[v0] Error fetching reservations:", error)
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const filteredReservations = reservations.filter(
    (r) =>
      r.clientName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.courtName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.reservationCode?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const handleCreate = async () => {
    setIsSubmitting(true)
    setCreateError("")
    try {
      await reservationsApi.create({
        ...formData,
        startDatetime: formData.startDatetime,
        endDatetime: formData.endDatetime,
      })
      setIsCreateDialogOpen(false)
      resetForm()
      fetchData()
    } catch (error) {
      console.error("[v0] Error creating reservation:", error)
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

  const handleUpdate = async () => {
    if (!selectedReservation) return
    setIsSubmitting(true)
    setUpdateError("")
    try {
      await reservationsApi.updateAdmin(selectedReservation.id, {
        courtId: formData.courtId,
        statusId: formData.statusId,
        startDatetime: formData.startDatetime,
        endDatetime: formData.endDatetime,
        notes: formData.notes,
      })
      setIsEditDialogOpen(false)
      setSelectedReservation(null)
      resetForm()
      fetchData()
    } catch (error) {
      console.error("[v0] Error updating reservation:", error)
      if (error instanceof ApiError) {
        setUpdateError(error.message)
      } else if (error instanceof Error) {
        setUpdateError(error.message)
      } else {
        setUpdateError("Error al actualizar la reserva")
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!selectedReservation) return
    setIsSubmitting(true)
    setDeleteError("")
    try {
      await reservationsApi.delete(selectedReservation.id)
      setIsDeleteDialogOpen(false)
      setSelectedReservation(null)
      fetchData()
    } catch (error) {
      console.error("[v0] Error deleting reservation:", error)
      if (error instanceof ApiError) {
        setDeleteError(error.message)
      } else if (error instanceof Error) {
        setDeleteError(error.message)
      } else {
        setDeleteError("Error al eliminar la reserva")
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleCancel = async () => {
    if (!selectedReservation) return
    setIsSubmitting(true)
    setCancelError("")
    try {
      await reservationsApi.cancel(selectedReservation.id, {
        reservationCode: selectedReservation.reservationCode,
      })
      setIsCancelDialogOpen(false)
      setSelectedReservation(null)
      fetchData()
    } catch (error) {
      console.error("[v0] Error canceling reservation:", error)
      if (error instanceof ApiError) {
        setCancelError(error.message)
      } else if (error instanceof Error) {
        setCancelError(error.message)
      } else {
        setCancelError("Error al cancelar la reserva")
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  const resetForm = () => {
    setFormData({
      clientName: "",
      clientPhone: "",
      courtId: courts[0]?.id || 0,
      statusId: 1,
      startDatetime: "",
      endDatetime: "",
      notes: "",
    })
  }

  const openEditDialog = (reservation: GetReservationResponse) => {
    setSelectedReservation(reservation)
    setUpdateError("")
    setFormData({
      clientName: reservation.clientName,
      clientPhone: "",
      courtId: reservation.courtId,
      statusId: reservation.statusId,
      startDatetime: format(new Date(reservation.startDatetime), "yyyy-MM-dd'T'HH:mm"),
      endDatetime: format(new Date(reservation.endDatetime), "yyyy-MM-dd'T'HH:mm"),
      notes: reservation.notes || "",
    })
    setIsEditDialogOpen(true)
  }

  const getStatusColor = (status: string) => {
    switch (status?.toLowerCase()) {
      case "active":
        return "bg-green-100 text-green-700"
      case "completed":
        return "bg-yellow-100 text-yellow-700"
      case "cancelled":
        return "bg-red-100 text-red-700"
      default:
        return "bg-gray-100 text-gray-700"
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Reservas</h1>
          <p className="text-muted-foreground">Cargando reservas...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Reservas</h1>
          <p className="text-muted-foreground">Gestiona las reservas del sistema</p>
        </div>
        <Button onClick={() => { resetForm(); setCreateError(""); setIsCreateDialogOpen(true) }}>
          <Plus className="mr-2 h-4 w-4" />
          Nueva Reserva
        </Button>
      </div>

      <Card>
        <CardHeader>
          <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <CardTitle>Lista de Reservas</CardTitle>
            <div className="relative w-full sm:w-72">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="Buscar reservas..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-9"
              />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Código</TableHead>
                  <TableHead>Cliente</TableHead>
                  <TableHead>Cancha</TableHead>
                  <TableHead>Fecha</TableHead>
                  <TableHead>Horario</TableHead>
                  <TableHead>Estado</TableHead>
                  <TableHead className="w-[70px]"></TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredReservations.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={7} className="text-center text-muted-foreground">
                      No se encontraron reservas
                    </TableCell>
                  </TableRow>
                ) : (
                  filteredReservations.map((reservation) => (
                    <TableRow key={reservation.id}>
                      <TableCell className="font-mono text-sm">
                        {reservation.reservationCode}
                      </TableCell>
                      <TableCell className="font-medium">{reservation.clientName}</TableCell>
                      <TableCell>{reservation.courtName}</TableCell>
                      <TableCell>
                        {format(new Date(reservation.startDatetime), "dd/MM/yyyy", { locale: es })}
                      </TableCell>
                      <TableCell>
                        {format(new Date(reservation.startDatetime), "HH:mm")} -{" "}
                        {format(new Date(reservation.endDatetime), "HH:mm")}
                      </TableCell>
                      <TableCell>
                        <span
                          className={`inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ${getStatusColor(
                            reservation.statusName
                          )}`}
                        >
                          {reservation.statusName}
                        </span>
                      </TableCell>
                      <TableCell>
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={() => openEditDialog(reservation)}>
                              <Pencil className="mr-2 h-4 w-4" />
                              Editar
                            </DropdownMenuItem>
                            <DropdownMenuItem
                              onClick={() => {
                                setSelectedReservation(reservation)
                                setIsCancelDialogOpen(true)
                              }}
                            >
                              <XCircle className="mr-2 h-4 w-4" />
                              Cancelar
                            </DropdownMenuItem>
                            <DropdownMenuItem
                              onClick={() => {
                                setSelectedReservation(reservation)
                                setIsDeleteDialogOpen(true)
                              }}
                              className="text-destructive"
                            >
                              <Trash2 className="mr-2 h-4 w-4" />
                              Eliminar
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>

      {/* Create Dialog */}
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Nueva Reserva</DialogTitle>
            <DialogDescription>Completa los datos para crear una nueva reserva</DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="clientName">Nombre del Cliente</Label>
              <Input
                id="clientName"
                value={formData.clientName}
                onChange={(e) => setFormData({ ...formData, clientName: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="clientPhone">Teléfono</Label>
              <Input
                id="clientPhone"
                value={formData.clientPhone}
                onChange={(e) => setFormData({ ...formData, clientPhone: e.target.value })}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="courtId">Cancha</Label>
              <select
                id="courtId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={formData.courtId}
                onChange={(e) => setFormData({ ...formData, courtId: Number(e.target.value) })}
              >
                <option value={0}>Seleccionar cancha</option>
                {courts.map((court) => (
                  <option key={court.id} value={court.id}>
                    {court.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="statusId">Estado</Label>
              <select
                id="statusId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={formData.statusId}
                onChange={(e) => setFormData({ ...formData, statusId: Number(e.target.value) })}
              >
                {STATUS_OPTIONS.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.name}
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
                  value={formData.startDatetime}
                  onChange={(e) => setFormData({ ...formData, startDatetime: e.target.value })}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="endDatetime">Fin</Label>
                <Input
                  id="endDatetime"
                  type="datetime-local"
                  value={formData.endDatetime}
                  onChange={(e) => setFormData({ ...formData, endDatetime: e.target.value })}
                />
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="notes">Notas</Label>
              <Textarea
                id="notes"
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
              />
            </div>
            {createError && (
              <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
                <AlertCircle className="h-4 w-4 shrink-0" />
                <span>{createError}</span>
              </div>
            )}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleCreate} disabled={isSubmitting}>
              {isSubmitting && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Crear
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Edit Dialog */}
      <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
        <DialogContent className="max-w-md">
          <DialogHeader>
            <DialogTitle>Editar Reserva</DialogTitle>
            <DialogDescription>Modifica los datos de la reserva</DialogDescription>
          </DialogHeader>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="editCourtId">Cancha</Label>
              <select
                id="editCourtId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={formData.courtId}
                onChange={(e) => setFormData({ ...formData, courtId: Number(e.target.value) })}
              >
                {courts.map((court) => (
                  <option key={court.id} value={court.id}>
                    {court.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="editStatusId">Estado</Label>
              <select
                id="editStatusId"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                value={formData.statusId}
                onChange={(e) => setFormData({ ...formData, statusId: Number(e.target.value) })}
              >
                {STATUS_OPTIONS.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.name}
                  </option>
                ))}
              </select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="grid gap-2">
                <Label htmlFor="editStartDatetime">Inicio</Label>
                <Input
                  id="editStartDatetime"
                  type="datetime-local"
                  value={formData.startDatetime}
                  onChange={(e) => setFormData({ ...formData, startDatetime: e.target.value })}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="editEndDatetime">Fin</Label>
                <Input
                  id="editEndDatetime"
                  type="datetime-local"
                  value={formData.endDatetime}
                  onChange={(e) => setFormData({ ...formData, endDatetime: e.target.value })}
                />
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="editNotes">Notas</Label>
              <Textarea
                id="editNotes"
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
              />
            </div>
            {updateError && (
              <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
                <AlertCircle className="h-4 w-4 shrink-0" />
                <span>{updateError}</span>
              </div>
            )}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsEditDialogOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleUpdate} disabled={isSubmitting}>
              {isSubmitting && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Guardar
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Delete Dialog */}
      <Dialog open={isDeleteDialogOpen} onOpenChange={(open) => { setIsDeleteDialogOpen(open); if (!open) setDeleteError(""); }}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Eliminar Reserva</DialogTitle>
            <DialogDescription>
              ¿Estás seguro de que deseas eliminar esta reserva? Esta acción no se puede deshacer.
            </DialogDescription>
          </DialogHeader>
          {deleteError && (
            <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
              <AlertCircle className="h-4 w-4 shrink-0" />
              <span>{deleteError}</span>
            </div>
          )}
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDeleteDialogOpen(false)}>
              Cancelar
            </Button>
            <Button variant="destructive" onClick={handleDelete} disabled={isSubmitting}>
              {isSubmitting && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Eliminar
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Cancel Dialog */}
      <Dialog open={isCancelDialogOpen} onOpenChange={(open) => { setIsCancelDialogOpen(open); if (!open) setCancelError(""); }}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Cancelar Reserva</DialogTitle>
            <DialogDescription>
              ¿Estás seguro de que deseas cancelar esta reserva?
            </DialogDescription>
          </DialogHeader>
          {cancelError && (
            <div className="flex items-center gap-2 rounded-md border border-destructive bg-destructive/10 p-3 text-sm text-destructive">
              <AlertCircle className="h-4 w-4 shrink-0" />
              <span>{cancelError}</span>
            </div>
          )}
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsCancelDialogOpen(false)}>
              Volver
            </Button>
            <Button variant="destructive" onClick={handleCancel} disabled={isSubmitting}>
              {isSubmitting && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
              Cancelar Reserva
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}
