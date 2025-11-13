package com.statusreserv.reservations.model.email;

import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_emails")
public class Email {

    @Id
    @GeneratedValue
    private Long id;

    private String to;

    private String from;

    private String subject;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

}