package com.statusreserv.reservations.service.email.sender;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
    void send(String from, String to, String subject, String bodyHtml);
}
