package co.edu.usbcali.reservas_suarez.service;


import co.edu.usbcali.reservas_suarez.model.Court;

import java.util.List;

public interface CourtService {

    List<Court> getAllCourts();
    Court getCourtById(Integer id);
    Court createCourt(Court court);
    Court updateCourt(Integer id, Court court);
    void deleteCourt(Integer id);
}