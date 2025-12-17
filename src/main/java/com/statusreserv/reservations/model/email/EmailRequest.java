package com.statusreserv.reservations.model.email;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@With
public class EmailRequest {
    private String from;
    private String to;
    private String subject;
    private String content;
    private LocalDateTime scheduledTime;
}