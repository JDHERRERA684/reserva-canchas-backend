package co.edu.usbcali.reservas_suarez.service.impl;

import co.edu.usbcali.reservas_suarez.dto.request.CreateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateCourtResponse;
import co.edu.usbcali.reservas_suarez.mapper.CourtMapper;

import co.edu.usbcali.reservas_suarez.model.Court;
import co.edu.usbcali.reservas_suarez.repository.CourtRepository;
import co.edu.usbcali.reservas_suarez.service.CourtService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    // GET ALL
    @Override
    public List <GetCourtResponse> getAllCourts() {
        List<Court> courts = courtRepository.findAll();
        return CourtMapper.entityToListGetCourtResponse(courts);
    }

    // GET BY ID
    @Override
    public GetCourtResponse getCourtById(Integer id) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );
        return CourtMapper.entityToGetCourtResponse(court);
    }


    // CREATE
    @Override
    public CreateCourtResponse createCourt(CreateCourtRequest createCourtRequest) throws Exception {

        try {
        // Validar request
        if (Objects.isNull(createCourtRequest)) {
            throw new RuntimeException("La cancha no puede ser nula");
        }
        // Validar name
        if (Objects.isNull (createCourtRequest.getName()) || createCourtRequest.getName().isBlank()) {
            throw new RuntimeException("El nombre de la cancha es requerido");
        }

        if (createCourtRequest.getName().length() > 50) {
            throw new RuntimeException("El nombre solo soporta hasta 50 caracteres");
        }

            // Convertir Request a Entity usando Mapper
            Court court =
                    CourtMapper.createCourtRequestToEntity(
                            createCourtRequest
                    );

            // Persistir en base de datos
            court = courtRepository.save(court);

            // Retornar DTO Response
            return CourtMapper.entityToCreateCourtResponse(court);

        } catch (Exception e) {
            throw e;
        }
    }

    // UPDATE
    @Override
    public UpdateCourtResponse updateCourt(Integer id, UpdateCourtRequest updateCourtRequest) throws Exception {
        try {
            // Validar request
            if (Objects.isNull(updateCourtRequest)) {
                throw new Exception("El objeto UpdateCourtRequest no puede ser nulo");
            }

            // Validar id
            if (Objects.isNull(id) || id <= 0) {
                throw new Exception("El id no puede ser nulo o menor igual a 0");
            }

            // Buscar cancha
            Court court = courtRepository.findById(id)
                    .orElseThrow(() ->
                            new Exception(
                                    "No se ha encontrado la cancha con id " + id
                            )
                    );

            // Actualizar name
            if (Objects.nonNull(updateCourtRequest.getName())
                    && !updateCourtRequest.getName().isBlank()) {

                if (updateCourtRequest.getName().length() > 50) {
                    throw new Exception("El nombre solo soporta hasta 50 caracteres");
                }

                court.setName(updateCourtRequest.getName());
            }
            // Actualizar description
            if (Objects.nonNull(updateCourtRequest.getDescription())) {
                court.setDescription(updateCourtRequest.getDescription());
            }
            // Persistir cambios
            court = courtRepository.save(court);

            // Retornar DTO Response
            return CourtMapper.entityToUpdateCourtResponse(court);

        } catch (Exception e) {
            throw e;
        }
    }

    // DELETE
    @Override
    public void deleteCourt(Integer id) {

        Court court = courtRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );

        courtRepository.delete(court);
    }
}