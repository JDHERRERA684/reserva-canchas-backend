package co.edu.usbcali.reservas_suarez.controller;
import co.edu.usbcali.reservas_suarez.model.Court;

import co.edu.usbcali.reservas_suarez.dto.request.CreateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.request.UpdateCourtRequest;
import co.edu.usbcali.reservas_suarez.dto.response.CreateCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.GetCourtResponse;
import co.edu.usbcali.reservas_suarez.dto.response.UpdateCourtResponse;
import co.edu.usbcali.reservas_suarez.service.CourtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    @GetMapping("/ping")
    public String ping (){
        return "pong";
    }

    @GetMapping("/all")
    public List<GetCourtResponse> getAllCourts() {
        return courtService.getAllCourts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCourtResponse> getCourtById(@PathVariable Integer id){
        GetCourtResponse courtResponse = courtService.getCourtById(id);
        return new ResponseEntity<>(
                courtResponse,
                HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateCourtResponse> createCourt(@RequestBody CreateCourtRequest createCourtRequest) throws Exception {
        CreateCourtResponse courtCreated = courtService.createCourt(createCourtRequest);
        return new ResponseEntity<>(
                courtCreated,
                HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateCourtResponse> updateCourt(
            @PathVariable Integer id,
            @RequestBody UpdateCourtRequest updateCourtRequest)throws Exception{

        UpdateCourtResponse courtUpdated = courtService.updateCourt(id, updateCourtRequest);
        return new ResponseEntity<>(
                courtUpdated,
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourt(@PathVariable Integer id){
        courtService.deleteCourt(id);
        return new ResponseEntity<>(
                "Cancha eliminada",
                HttpStatus.OK);
    }
}
