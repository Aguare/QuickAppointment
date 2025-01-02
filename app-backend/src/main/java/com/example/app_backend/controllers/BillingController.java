package com.example.app_backend.controllers;

import com.example.app_backend.dtos.BillingDto;
import com.example.app_backend.entities.Billing;
import com.example.app_backend.entities.User;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.BillingRepository;
import com.example.app_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBilling(@RequestBody BillingDto billingDto) {
        Billing billing = new Billing();
        billing.setNit(billingDto.getNit());
        billing.setCui(billingDto.getCui());
        billing.setDirection(billingDto.getDirection());
        billing.setFkUser(billingDto.getFkUser());

        Billing savedBilling = billingRepository.save(billing);
        ApiResponse response = new ApiResponse("Datos guardados con exito", savedBilling.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{fkUser}")
    public ResponseEntity<BillingDto> getBillingByUser(@PathVariable Integer fkUser) {
        List<Billing> billings = billingRepository.findByfkUser(fkUser);
        List<BillingDto> billingDtos = billings.stream()
                .map(billing -> {
                    BillingDto dto = new BillingDto();
                    dto.setId(billing.getId());
                    dto.setNit(billing.getNit());
                    dto.setCui(billing.getCui());
                    dto.setDirection(billing.getDirection());
                    dto.setFkUser(billing.getFkUser());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(billingDtos.get(0), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateBilling(@PathVariable Integer id, @RequestBody BillingDto update) {

        Optional<Billing> billingOptional = billingRepository.findById(id);

        if (billingOptional.isPresent()) {
            Billing billing = billingOptional.get();
            billing.setNit(update.getNit());
            billing.setCui(update.getCui());
            billing.setDirection(update.getDirection());
            billingRepository.save(billing);
            ApiResponse response = new ApiResponse("Los datos se actualizaron correctamente", billing.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}