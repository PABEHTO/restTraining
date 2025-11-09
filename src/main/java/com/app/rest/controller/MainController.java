package com.app.rest.controller;

import com.app.rest.dto.CatDto;
import com.app.rest.entity.Cat;
import com.app.rest.repository.CatRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@Tag(name = "main_controller")
public class MainController {

    @Autowired
    private CatRepo catRepo;

    @Operation(
            summary = "puts a cat",
            description = "receives catDto and puts into db",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cat data to be added to database",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CatDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example cat",
                                            summary = "Typical example of a cat",
                                            value = "{ \"name\": \"Whiskers\", \"age\": 3, \"weight\": 4.5 }"
                                    ),
                                    @ExampleObject(
                                            name = "One more example cat",
                                            summary = "One more typical example of a cat",
                                            value = "{\"name\" : \"Barsik\", \"age\" : 5, \"weight\" : 9}"
                                    )

                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cat successfully saved"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/api/add")
    public void addCat(@RequestBody CatDto catDto) {
        catRepo.save(Cat.builder()
                .name(catDto.getName())
                .age(catDto.getAge())
                .weight(catDto.getWeight())
                .build());
        log.info("A cat has been saved");
    }

    @SneakyThrows
    @GetMapping("/api/all")
    public List<Cat> allCats() {
        return catRepo.findAll();
    }

    @Operation(
            summary = "Get cat by ID",
            description = "Returns a cat from database by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cat successfully found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Cat.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example cat",
                                            summary = "Example cat response",
                                            value = "{\"id\" : 1, \"name\" : \"Barsik\", \"age\" : 4, \"weight\" : 8}"
                                    )
                    }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/api")
    public Cat getCat(
            @Parameter(
                    name = "id",
                    description = "cat's ID",
                    required = true,
                    example = "1"
            )
            @RequestParam int id) {
        return catRepo.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    @Operation(
            summary = "Delete a cat by ID",
            description = "Removes a cat from db by cat's ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cat deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cat not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @DeleteMapping("/api")
    public void deleteCat(
            @Parameter(
                    name = "id",
                    description = "cat's ID for deleting",
                    required = true,
                    example = "1"
            )
            @RequestParam int id) {
        catRepo.deleteById(id);
    }

    @PutMapping("/api")
    public String updateCat(@RequestBody Cat cat, @RequestParam int id) {
        if (catRepo.existsById(id)) {
            return catRepo.save(cat).toString();
        } else {
            return "No cat was found";
        }
    }
}
