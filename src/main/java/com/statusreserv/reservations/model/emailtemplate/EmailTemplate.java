package com.statusreserv.reservations.model.emailtemplate;

import com.statusreserv.reservations.model.constants.EmailTemplateType;
import com.statusreserv.reservations.model.constants.Language;
import com.statusreserv.reservations.model.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Table(name = "tbl_email_templates")
public class EmailTemplate {
    @Id
    @GeneratedValue
    private UUID id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private EmailTemplateType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

}