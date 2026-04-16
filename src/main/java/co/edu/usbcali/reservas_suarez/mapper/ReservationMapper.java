package co.edu.usbcali.reservas_suarez.mapper;


import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.model.Reservation;

import java.util.List;
import java.util.Objects;

public class ReservationMapper {
 public static GetReservationResponse entityToGetReservationResponse (Reservation reservation){
     return GetReservationResponse.builder()
             .id(reservation.getId())

             .clientId(Objects.nonNull(reservation.getClient()) ? reservation.getClient().getId() : null)
             .clientName(Objects.nonNull(reservation.getClient()) ? reservation.getClient().getName() : null)

             .courtId(Objects.nonNull(reservation.getCourt()) ? reservation.getCourt().getId() : null)
             .courtName(Objects.nonNull(reservation.getCourt()) ? reservation.getCourt().getName() : null)

             .statusId(Objects.nonNull(reservation.getStatus()) ? reservation.getStatus().getId() : null)
             .statusName(Objects.nonNull(reservation.getStatus()) ? reservation.getStatus().getName() : null)

             .startDatetime(reservation.getStartDatetime())
             .endDatetime(reservation.getEndDatetime())

             .createdBy(reservation.getCreatedBy())
             .notes(reservation.getNotes())

             .createdAt(reservation.getCreatedAt())
             .build();
 }

    public static List<GetReservationResponse> entityToListGetReservationResponse(List<Reservation> reservations){
        return reservations
                .stream()
                .map(ReservationMapper::entityToGetReservationResponse)
                .toList();
    }
}
