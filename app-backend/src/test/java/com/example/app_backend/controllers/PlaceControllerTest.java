package com.example.app_backend.controllers;

import com.example.app_backend.dtos.PlaceDto;
import com.example.app_backend.entities.Employee;
import com.example.app_backend.entities.Place;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlaceControllerTest {

    @InjectMocks
    private PlaceController placeController;

    @Mock
    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePlace_Success() {
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("Nivel 0");
        placeDto.setPlace("Lugar");
        placeDto.setFkCompany(1);

        Place place = new Place();
        place.setId(1);
        place.setName(placeDto.getName());
        place.setPlace(placeDto.getPlace());
        place.setFkCompany(placeDto.getFkCompany());
        place.setAvailable(true);

        when(placeRepository.save(any(Place.class))).thenReturn(place);

        ResponseEntity<ApiResponse> response = placeController.createPlace(placeDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Lugar creado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void testGetPlacesByCompany_Success() {
        List<Place> places = new ArrayList<>();

        Place place1 = new Place();
        place1.setId(1);
        place1.setName("Nivel 1");
        place1.setPlace("Lugar 1");
        place1.setFkCompany(1);

        Place place2 = new Place();
        place2.setId(2);
        place2.setName("Nivel 2");
        place2.setPlace("Lugar 2");
        place2.setFkCompany(1);

        places.add(place1);
        places.add(place2);

        when(placeRepository.findByFkCompany(1)).thenReturn(places);

        ResponseEntity<List<PlaceDto>> response = placeController.getPlacesByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        PlaceDto dto1 = response.getBody().get(0);
        assertEquals(1, dto1.getId());
        assertEquals("Nivel 1", dto1.getName());
        assertEquals("Lugar 1", dto1.getPlace());
        assertEquals(1, dto1.getFkCompany());

        PlaceDto dto2 = response.getBody().get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Nivel 2", dto2.getName());
        assertEquals("Lugar 2", dto2.getPlace());
        assertEquals(1, dto2.getFkCompany());

        verify(placeRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetPlacesByCompany_EmptyList() {
        List<Place> places = new ArrayList<>();

        when(placeRepository.findByFkCompany(1)).thenReturn(places);

        ResponseEntity<List<PlaceDto>> response = placeController.getPlacesByCompany(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());

        verify(placeRepository, times(1)).findByFkCompany(1);
    }

    @Test
    void testGetPlaceById_Success() {
        Place place = new Place();
        place.setId(1);
        place.setName("Nivel 1");
        place.setPlace("Lugar 1");
        place.setFkCompany(1);

        when(placeRepository.findById(1)).thenReturn(Optional.of(place));

        ResponseEntity<PlaceDto> response = placeController.getPlaceById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("Nivel 1", response.getBody().getName());
        assertEquals("Lugar 1", response.getBody().getPlace());
        assertEquals(1, response.getBody().getFkCompany());

        verify(placeRepository, times(1)).findById(1);
    }

    @Test
    void testGetPlaceById_NotFound() {
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<PlaceDto> response = placeController.getPlaceById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(placeRepository, times(1)).findById(1);
    }

    @Test
    void testUpdatePlace_Success() {
        Place existingPlace = new Place();
        existingPlace.setId(1);
        existingPlace.setName("Nivel 1");
        existingPlace.setPlace("Lugar 1");
        existingPlace.setFkCompany(1);

        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(1);
        placeDto.setName("Nivel 2");
        placeDto.setPlace("Lugar 2");
        placeDto.setFkCompany(1);

        when(placeRepository.findById(1)).thenReturn(Optional.of(existingPlace));

        when(placeRepository.save(any(Place.class))).thenReturn(existingPlace);

        ResponseEntity<ApiResponse> response = placeController.updatePlace(1, placeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lugar actualizado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        assertEquals("Nivel 2", existingPlace.getName());
        assertEquals("Lugar 2", existingPlace.getPlace());

        verify(placeRepository, times(1)).findById(1);
        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void testUpdatePlace_NotFound() {
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(1);
        placeDto.setName("Nivel 1");
        placeDto.setPlace("Lugar 1");
        placeDto.setFkCompany(1);

        ResponseEntity<ApiResponse> response = placeController.updatePlace(1, placeDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lugar no encontrado", response.getBody().getMessage());

        verify(placeRepository, times(1)).findById(1);
        verify(placeRepository, never()).save(any(Place.class));
    }

    @Test
    void testDeletePlace_Success() {
        Place place = new Place();
        place.setId(1);
        place.setName("Nivel 1");
        place.setPlace("Lugar 1");

        when(placeRepository.findById(1)).thenReturn(Optional.of(place));

        ResponseEntity<ApiResponse> response = placeController.deletePlace(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lugar eliminado con éxito", response.getBody().getMessage());
        assertEquals(1, response.getBody().getId());

        verify(placeRepository, times(1)).deleteById(1);

        verify(placeRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = placeController.deletePlace(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lugar no encontrado", response.getBody().getMessage());

        verify(placeRepository, times(1)).findById(1);

        verify(placeRepository, never()).deleteById(anyInt());
    }
}
