package com.statusreserv.reservations.service.template.resolver;

import com.statusreserv.reservations.model.email.Email;
import com.statusreserv.reservations.model.email.EmailTemplateRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmailTemplateResolver<T extends EmailTemplateRequest> {
    Email solve(T request);
    Class<T> getSupportedType();
}
