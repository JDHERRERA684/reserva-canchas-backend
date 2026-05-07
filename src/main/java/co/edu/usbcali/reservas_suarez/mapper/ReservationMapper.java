package co.edu.usbcali.reservas_suarez.mapper;


import co.edu.usbcali.reservas_suarez.dto.response.CreateReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetReservationResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateReservationResponse;
import co.edu.usbcali.reservas_suarez.model.Reservation;

import java.util.List;
import java.util.Objects;

public class ReservationMapper {

    public static CreateReservationResponse entityToCreateReservationResponse(Reservation reservation) {

        return CreateReservationResponse.builder()
                .id(reservation.getId())
                .clientName(Objects.nonNull(reservation.getClient()) ? reservation.getClient().getName() : null)
                .courtName(Objects.nonNull(reservation.getCourt()) ? reservation.getCourt().getName() : null)
                .statusName(Objects.nonNull(reservation.getStatus()) ? reservation.getStatus().getName() : null)
                .startDatetime(reservation.getStartDatetime())
                .endDatetime(reservation.getEndDatetime())
                .notes(reservation.getNotes())
                .build();
    }


    public static UpdateReservationResponse entityToUpdateReservationResponse(Reservation reservation) {

        return new UpdateReservationResponse(
                reservation.getId(),
                Objects.nonNull(reservation.getClient()) ? reservation.getClient().getName() : null,
                Objects.nonNull(reservation.getCourt()) ? reservation.getCourt().getName() : null,
                Objects.nonNull(reservation.getStatus()) ? reservation.getStatus().getName() : null,
                reservation.getStartDatetime(),
                reservation.getEndDatetime(),
                reservation.getNotes()
        );
    }




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
