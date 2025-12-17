package com.statusreserv.reservations.service.email.sender;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
    void send(String sender, String recipient, String subject, String bodyHtml);
}
