package co.edu.usbcali.reservas_suarez.mapper;


import co.edu.usbcali.reservas_suarez.dto.request.CreateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.response.*;
import co.edu.usbcali.reservas_suarez.model.Court;

import java.util.List;

public class CourtMapper {
    public static Court createCourtRequestToEntity(
            CreateCourtRequest request
    ) {

        return Court.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static GetCourtResponse entityToGetCourtResponse(
            Court court
    ) {

        return GetCourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .description(court.getDescription())
                .build();
    }

    public static CreateCourtResponse entityToCreateCourtResponse(
            Court court
    ) {

        return CreateCourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .description(court.getDescription())
                .build();
    }

    public static UpdateCourtResponse entityToUpdateCourtResponse(
            Court court
    ) {

        return new UpdateCourtResponse(
                court.getId(),
                court.getName(),
                court.getDescription()
        );
    }

    public static List<GetCourtResponse> entityToListGetCourtResponse(
            List<Court> courts
    ) {

        return courts.stream()
                .map(CourtMapper::entityToGetCourtResponse)
                .toList();
    }
}
