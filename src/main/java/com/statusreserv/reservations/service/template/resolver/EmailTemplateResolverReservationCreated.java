package com.statusreserv.reservations.service.template.resolver;

import com.statusreserv.reservations.model.constants.EmailTemplateField;
import com.statusreserv.reservations.model.constants.EmailTemplateType;
import com.statusreserv.reservations.model.constants.Language;
import com.statusreserv.reservations.model.email.Email;
import com.statusreserv.reservations.model.email.EmailReservationTemplateCreatedRequest;
import com.statusreserv.reservations.model.emailtemplate.EmailTemplate;
import com.statusreserv.reservations.model.reservation.Reservation;
import com.statusreserv.reservations.model.reservation.ReservationService;
import com.statusreserv.reservations.repository.EmailTemplateRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.template.render.TemplateRender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateResolverReservationCreated implements EmailTemplateResolver<EmailReservationTemplateCreatedRequest> {

    private final TemplateRender templateRender;
    private final EmailTemplateRepository templateRepository;
    private final CurrentUserService currentUserService;

    @Override
    public Email solve(EmailReservationTemplateCreatedRequest request) {
        var template = getTemplate();
        var content = templateRender.renderTemplate(template.getContent(), getVariables(request.getReservation()));
        return new Email()
                .withContent(content)
                .withRecipient(request.getRecipient())
                .withSender(request.getSender())
                .withSubject(template.getSubject())
                .withTenant(currentUserService.getCurrentTenant());
    }

    @Override
    public Class<EmailReservationTemplateCreatedRequest> getSupportedType() {
        return EmailReservationTemplateCreatedRequest.class;
    }

    private EmailTemplate getTemplate() {
        return templateRepository.findByTenantIdAndTypeAndLanguage(currentUserService.getCurrentTenantId(), EmailTemplateType.RESERVATION_CREATED, Language.EN)
                        .orElseThrow(() -> new IllegalArgumentException("Template not found: " + EmailTemplateType.RESERVATION_CREATED));
    }

    public Map<String, Object> getVariables(Reservation reservation) {
        var variables = new HashMap<String, Object>();
        var services = reservation.getServices().stream().map(ReservationService::getName)
                .reduce((a, b) -> a + ", "+  b)
                .orElse("");
        var reservationCode = reservation.getId().toString().substring(0, 5).toUpperCase();
        variables.put(EmailTemplateField.RESERVATION_CODE.toString(), reservationCode);
        variables.put(EmailTemplateField.DATE.toString(), reservation.getDate().toString());
        variables.put(EmailTemplateField.START_TIME.toString(), reservation.getStartTime().toString());
        variables.put(EmailTemplateField.END_TIME.toString(), reservation.getEndTime().toString());
        variables.put(EmailTemplateField.TOTAL_PRICE.toString(), reservation.getTotalPrice().toString());
        variables.put(EmailTemplateField.CUSTOMER_NAME.toString(), reservation.getCustomer().getName());
        variables.put(EmailTemplateField.SERVICES.toString(), services);
        return variables;
    }
}
