package com.example.app_backend.controllers;

import com.example.app_backend.dtos.PlaceDto;
import com.example.app_backend.entities.Place;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPlace(@RequestBody PlaceDto placeDto) {
        Place place = new Place();
        place.setName(placeDto.getName());
        place.setPlace(placeDto.getPlace());
        place.setFkCompany(placeDto.getFkCompany());
        place.setAvailable(true);

        Place savedPlace = placeRepository.save(place);

        ApiResponse response = new ApiResponse("Lugar creado con éxito", savedPlace.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/company/{fkCompany}")
    public ResponseEntity<List<PlaceDto>> getPlacesByCompany(@PathVariable Integer fkCompany) {
        List<Place> places = placeRepository.findByFkCompany(fkCompany);

        List<PlaceDto> placeDtos = places.stream()
                .map(place -> {
                    PlaceDto dto = new PlaceDto();
                    dto.setId(place.getId());
                    dto.setName(place.getName());
                    dto.setPlace(place.getPlace());
                    dto.setFkCompany(place.getFkCompany());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(placeDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Integer id) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if (placeOptional.isPresent()) {
            Place place = placeOptional.get();
            PlaceDto dto = new PlaceDto();
            dto.setId(place.getId());
            dto.setName(place.getName());
            dto.setPlace(place.getPlace());
            dto.setFkCompany(place.getFkCompany());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updatePlace(@PathVariable Integer id, @RequestBody PlaceDto placeDto) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if (placeOptional.isPresent()) {
            Place place = placeOptional.get();
            place.setName(placeDto.getName());
            place.setPlace(placeDto.getPlace());

            placeRepository.save(place);

            ApiResponse response = new ApiResponse("Lugar actualizado con éxito", place.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Lugar no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePlace(@PathVariable Integer id) {

        Optional<Place> placeOptional = placeRepository.findById(id);

        if (placeOptional.isPresent()) {
            placeRepository.deleteById(id);
            ApiResponse response = new ApiResponse("Lugar eliminado con éxito", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse("Lugar no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
