package co.edu.usbcali.reservas_suarez.service.impl;


import co.edu.usbcali.reservas_suarez.model.Court;
import co.edu.usbcali.reservas_suarez.repository.CourtRepository;
import co.edu.usbcali.reservas_suarez.service.CourtService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    // GET ALL
    @Override
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    // GET BY ID
    @Override
    public Court getCourtById(Integer id) {

        return courtRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );
    }

    // CREATE
    @Override
    public Court createCourt(Court court) {

        if (court == null) {
            throw new RuntimeException("La cancha no puede ser nula");
        }

        if (court.getName() == null || court.getName().isBlank()) {
            throw new RuntimeException("El nombre de la cancha es requerido");
        }

        if (court.getName().length() > 50) {
            throw new RuntimeException("El nombre solo soporta hasta 50 caracteres");
        }

        return courtRepository.save(court);
    }

    // UPDATE
    @Override
    public Court updateCourt(Integer id, Court courtData) {

        Court court = courtRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cancha no encontrada con id: " + id)
                );

        if (courtData.getName() != null && !courtData.getName().isBlank()) {
            court.setName(courtData.getName());
        }

        court.setDescription(courtData.getDescription());

        return courtRepository.save(court);
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