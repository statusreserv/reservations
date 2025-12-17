package com.statusreserv.reservations.service.email.create;

import com.statusreserv.reservations.model.email.EmailTemplateRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void create(EmailTemplateRequest request);
}
