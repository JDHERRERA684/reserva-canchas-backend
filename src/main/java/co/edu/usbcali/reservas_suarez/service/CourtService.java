package co.edu.usbcali.reservas_suarez.service;

import co.edu.usbcali.reservas_suarez.dto.request.CreateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateCourtResponse;
import co.edu.usbcali.reservas_suarez.model.Court;

import java.util.List;

public interface CourtService {

    CreateCourtResponse createCourt(CreateCourtRequest createCourtRequest) throws Exception;
    List<GetCourtResponse> getAllCourts();
    GetCourtResponse getCourtById(Integer id);
    UpdateCourtResponse updateCourt(Integer id, UpdateCourtRequest updateCourtRequest) throws Exception;
    void deleteCourt(Integer id);

}