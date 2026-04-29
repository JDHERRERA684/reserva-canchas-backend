package co.edu.usbcali.reservas_suarez.controller;
import co.edu.usbcali.reservas_suarez.model.Court;
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
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Integer id){
        Court court = courtService.getCourtById(id);
        return new ResponseEntity<>(
                court,
                HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Court> createCourt(@RequestBody Court court){
        Court Created = courtService.createCourt(court);
        return new ResponseEntity<>(
                Created,
                HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Court> updateCourt(
            @PathVariable Integer id,
            @RequestBody Court court
    ){

        Court Updated = courtService.updateCourt(id, court);
        return new ResponseEntity<>(
                Updated,
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
