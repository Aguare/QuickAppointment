package com.example.app_backend.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.example.app_backend.helpers.ImgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    static final String UPLOAD_DIR = "src/main/resources/public/imgs/";

    @PostMapping("/upload")
    public ResponseEntity<ImgResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ImgResponse("Por favor, selecciona un archivo", null, null), HttpStatus.BAD_REQUEST);
        }

        try {

            String uniqueFileName = UUID.randomUUID().toString() + ".jpg";

            Path path = Paths.get(UPLOAD_DIR + uniqueFileName);

            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            Files.write(path, file.getBytes());

            String relativePath = "/imgs/" + uniqueFileName;

            return new ResponseEntity<>(new ImgResponse("Archivo subido correctamente", path.toString(), relativePath), HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ImgResponse("Error al subir el archivo", null, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
