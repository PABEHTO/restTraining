package com.app.rest.controller;

import com.app.rest.broker.KafkaProducer;
import com.app.rest.entity.Cat;
import com.app.rest.repository.CatRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Tag(name = "kafka_controller")
public class ControllerKafka {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private CatRepo catRepository;

    @PostMapping("/kafka/send")
    public String send(@RequestParam int id) {
        var cat = catRepository.findById(id).get();
        kafkaProducer.sendMessage(cat.toString());
        return "Success";
    }

//    @GetMapping("/test/test")
//    public String test() {
//        return "Success";
//    }
}
