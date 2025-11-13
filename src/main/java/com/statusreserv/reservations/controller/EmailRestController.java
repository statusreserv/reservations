package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.service.email.sender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.statusreserv.reservations.constants.Endpoints.API;

@RestController
@RequestMapping(API)
@RequiredArgsConstructor
public class EmailRestController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/sendNotification")
    public String sendNotification() {

        emailSenderService.send(
                "statusreserv@gmail.com",
                "ricardomartins145@gmail.com",
                "Urgente",
                "Vou te roubar a conta bancaria hahahhahahah"
        );

        return "Message queued";
    }
}
