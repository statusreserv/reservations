package com.statusreserv.reservations.service.email.create;

import com.statusreserv.reservations.model.email.EmailRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmailCreateService<T extends EmailRequest> {
    void create(T request);
}
