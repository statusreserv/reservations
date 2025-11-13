package com.statusreserv.reservations.repository;

import com.statusreserv.reservations.model.constants.Language;
import com.statusreserv.reservations.model.emailtemplate.EmailTemplate;
import com.statusreserv.reservations.model.constants.EmailTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, UUID> {
    Optional<EmailTemplate> findByTenantIdAndTypeAndLanguage(UUID tenantId, EmailTemplateType templateType, Language language);
}
