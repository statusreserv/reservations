package com.statusreserv.reservations.model.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailTemplateRequest {
    private String sender;
    private String recipient;
}