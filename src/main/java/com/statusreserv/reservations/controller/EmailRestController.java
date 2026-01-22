package com.statusreserv.reservations.controller;

import com.statusreserv.reservations.model.customer.Customer;
import com.statusreserv.reservations.model.email.EmailReservationTemplateCreatedRequest;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationServiceProvided;
import com.statusreserv.reservations.service.email.create.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.statusreserv.reservations.constants.Endpoints.API;

@RestController
@RequestMapping(API)
@RequiredArgsConstructor
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping("/sendNotification")
    public String sendNotification() {
        var services = List.of(new ReservationServiceProvided().withName("Corte de cabelo"));
        var customer = new Customer().withName("Francisco");
        var reservation = new Reservation()
                .withId(UUID.randomUUID())
                .withDate(LocalDate.now())
                .withStartTime(LocalTime.now())
                .withTotalPrice(BigDecimal.TEN)
                .withCustomer(customer)
                .withEndTime(LocalTime.now().plusHours(1))
                .withReservationServices(services);
        var emailTemplateRequest = new EmailReservationTemplateCreatedRequest("reiriocm@gmail.com","statusreserv@gmail.com", reservation);
        emailService.create(emailTemplateRequest);
        return "Message queued";
    }
}
