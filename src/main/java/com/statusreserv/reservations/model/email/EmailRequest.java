package com.statusreserv.reservations.model.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailRequest {
    private String from;
    private String to;
}