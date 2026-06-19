"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { reservationsApi, courtsApi, clientsApi, type GetReservationResponse, type GetCourtResponse, type GetClientResponse } from "@/lib/api"
import { Calendar, Users, CalendarDays, Clock } from "lucide-react"
import { format } from "date-fns"
import { es } from "date-fns/locale"

export default function DashboardPage() {
  const [reservations, setReservations] = useState<GetReservationResponse[]>([])
  const [courts, setCourts] = useState<GetCourtResponse[]>([])
  const [clients, setClients] = useState<GetClientResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    async function fetchData() {
      try {
        const [reservationsData, courtsData, clientsData] = await Promise.all([
          reservationsApi.getAll(),
          courtsApi.getAll(),
          clientsApi.getAll(),
        ])
        setReservations(reservationsData)
        setCourts(courtsData)
        setClients(clientsData)
      } catch (error) {
        console.error("[v0] Error fetching dashboard data:", error)
      } finally {
        setIsLoading(false)
      }
    }
    fetchData()
  }, [])

  const todayReservations = reservations.filter((r) => {
    const reservationDate = new Date(r.startDatetime)
    const today = new Date()
    return (
      reservationDate.getDate() === today.getDate() &&
      reservationDate.getMonth() === today.getMonth() &&
      reservationDate.getFullYear() === today.getFullYear()
    )
  })

  const upcomingReservations =
      reservations.filter((r) => {

        const reservationDate =
            new Date(r.startDatetime)

        const now =
            new Date()

        return (
            reservationDate > now
            &&
            r.statusName === "ACTIVE"
        )
      })

  const stats = [
    {
      title: "Total Reservas",
      value: reservations.length,
      icon: Calendar,
      description: "Reservas en el sistema",
    },
    {
      title: "Reservas Hoy",
      value: todayReservations.length,
      icon: Clock,
      description: format(new Date(), "EEEE, d 'de' MMMM", { locale: es }),
    },
    {
      title: "Canchas",
      value: courts.length,
      icon: CalendarDays,
      description: "Canchas disponibles",
    },
    {
      title: "Clientes",
      value: clients.length,
      icon: Users,
      description: "Clientes registrados",
    },
  ]

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Dashboard</h1>
          <p className="text-muted-foreground">Cargando estadísticas...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Dashboard</h1>
        <p className="text-muted-foreground">Bienvenido al sistema de reservas</p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                {stat.title}
              </CardTitle>
              <stat.icon className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">{stat.description}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Reservas de Hoy</CardTitle>
          </CardHeader>
          <CardContent>
            {todayReservations.length === 0 ? (
              <p className="text-sm text-muted-foreground">No hay reservas para hoy</p>
            ) : (
              <div className="space-y-3">
                {todayReservations.slice(0, 5).map((reservation) => (
                  <div
                    key={reservation.id}
                    className="flex items-center justify-between rounded-lg border p-3"
                  >
                    <div>
                      <p className="font-medium">{reservation.clientName}</p>
                      <p className="text-sm text-muted-foreground">{reservation.courtName}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium">
                        {format(new Date(reservation.startDatetime), "HH:mm")} -{" "}
                        {format(new Date(reservation.endDatetime), "HH:mm")}
                      </p>
                      <span
                        className={`inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ${
                          reservation.statusName?.toLowerCase() === "ACTIVE"
                            ? "bg-green-100 text-green-700"
                            : reservation.statusName?.toLowerCase() === "COMPLETED"
                            ? "bg-yellow-100 text-yellow-700"
                            : "bg-gray-100 text-gray-700"
                        }`}
                      >
                        {reservation.statusName}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Próximas Reservas</CardTitle>
          </CardHeader>
          <CardContent>
            {upcomingReservations.length === 0 ? (
              <p className="text-sm text-muted-foreground">No hay reservas futuras</p>
            ) : (
              <div className="space-y-3">
                {upcomingReservations.slice(0, 5).map((reservation) => (
                  <div
                    key={reservation.id}
                    className="flex items-center justify-between rounded-lg border p-3"
                  >
                    <div>
                      <p className="font-medium">{reservation.clientName}</p>
                      <p className="text-sm text-muted-foreground">{reservation.courtName}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium">
                        {format(new Date(reservation.startDatetime), "dd/MM/yyyy")}
                      </p>
                      <p className="text-xs text-muted-foreground">
                        {format(new Date(reservation.startDatetime), "HH:mm")} -{" "}
                        {format(new Date(reservation.endDatetime), "HH:mm")}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
