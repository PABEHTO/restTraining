package com.app.rest.controller;

import com.app.rest.entity.Dog;
import com.app.rest.repository.DogRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dogs")
@Tag(name = "dog_controller")
public class DogController {
    private final DogRepo dogRepository;

    public DogController(DogRepo dogRepository) {
        this.dogRepository = dogRepository;
    }

    @GetMapping("/all")
    public List<Dog> getAllDogs() {
        return dogRepository.findAll();
    }

    @GetMapping
    public ResponseEntity<Dog> getDogByName(@RequestParam String name) {
        Dog dog = dogRepository.findByName(name);

        if (dog == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(dog);
    }

    @PostMapping
    public void createDog(@RequestBody Dog dog) {
        dog.setId(UUID.randomUUID());
        dogRepository.save(dog);
    }
}
