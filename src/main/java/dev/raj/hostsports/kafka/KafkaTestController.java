package dev.raj.hostsports.kafka;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    private final KafkaProducerService producerService;

    public KafkaTestController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public String send(@RequestParam String message) {

        producerService.sendMessage(message);

        return "Message sent: " + message;
    }
}