package com.example.app_backend.controllers;

import com.example.app_backend.dtos.AppointmentByCompanyDto;
import com.example.app_backend.dtos.CompanyResponseDto;
import com.example.app_backend.dtos.RoleDto;
import com.example.app_backend.entities.Role;
import com.example.app_backend.entities.RoleHasPage;
import com.example.app_backend.helpers.ApiResponse;
import com.example.app_backend.repositories.RoleHasPageRepository;
import com.example.app_backend.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleHasPageRepository roleHasPageRepository;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleDto roleDto) {
        Role rol = new Role();
        rol.setName(roleDto.getName());
        rol.setDescription(roleDto.getDescription());

        Role savedRole = roleRepository.save(rol);
        ApiResponse response = new ApiResponse("Rol creado con exito", savedRole.getId());

        Integer roleId = savedRole.getId();

        List<RoleHasPage> rhp = roleHasPageRepository.findByFkRole(1);

        List<RoleHasPage> rhpSaved = rhp.stream().map(result -> {
            RoleHasPage newRhp = new RoleHasPage();
            newRhp.setFkRole(roleId);
            newRhp.setFkPage(result.getFkPage());
            newRhp.setAllowCreate(roleDto.getAllowCreate());
            newRhp.setAllowDelete(roleDto.getAllowDelete());
            newRhp.setAllowEdit(roleDto.getAllowEdit());
            return newRhp;
        }).collect(Collectors.toList());

        roleHasPageRepository.saveAll(rhpSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        List<RoleDto> roleDtos = roles.stream()
                .map(company -> new RoleDto(
                        company.getId(),
                        company.getName(),
                        company.getDescription()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Integer id) {

        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setName(role.getName());
            roleDto.setDescription(role.getDescription());

            List<Object[]> results = roleHasPageRepository.getAllows(id);

            for (Object[] result : results) {
                roleDto.setAllowCreate(Boolean.parseBoolean(result[0].toString()));
                roleDto.setAllowEdit(Boolean.parseBoolean(result[1].toString()));
                roleDto.setAllowDelete(Boolean.parseBoolean(result[2].toString()));
            }

            return new ResponseEntity<>(roleDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PutMapping("/update/{roleId}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Integer roleId, @RequestBody RoleDto roleDto) {

        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);
        if (!existingRoleOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Rol no encontrado", null));
        }

        Role existingRole = existingRoleOpt.get();
        existingRole.setName(roleDto.getName());
        existingRole.setDescription(roleDto.getDescription());

        Role updatedRole = roleRepository.save(existingRole);

        roleHasPageRepository.deleteByFkRole(roleId);

        List<RoleHasPage> rhp = roleHasPageRepository.findByFkRole(1);

        List<RoleHasPage> rhpSaved = rhp.stream().map(result -> {
            RoleHasPage newRhp = new RoleHasPage();
            newRhp.setFkRole(roleId);
            newRhp.setFkPage(result.getFkPage());
            newRhp.setAllowCreate(roleDto.getAllowCreate());
            newRhp.setAllowDelete(roleDto.getAllowDelete());
            newRhp.setAllowEdit(roleDto.getAllowEdit());
            return newRhp;
        }).collect(Collectors.toList());

        roleHasPageRepository.saveAll(rhpSaved);

        ApiResponse response = new ApiResponse("Rol actualizado con éxito", updatedRole.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Integer roleId) {
        Optional<Role> existingRoleOpt = roleRepository.findById(roleId);
        if (!existingRoleOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Rol no encontrado", null));
        }

        roleHasPageRepository.deleteByFkRole(roleId);

        roleRepository.deleteById(roleId);

        ApiResponse response = new ApiResponse("Rol eliminado con éxito", roleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
