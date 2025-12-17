package com.statusreserv.reservations.service.email.create;

import com.statusreserv.reservations.model.constants.EmailStatus;
import com.statusreserv.reservations.model.email.Email;
import com.statusreserv.reservations.model.email.EmailTemplateRequest;
import com.statusreserv.reservations.repository.EmailRepository;
import com.statusreserv.reservations.service.auth.CurrentUserService;
import com.statusreserv.reservations.service.template.EmailTemplateResolverFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final CurrentUserService currentUserService;
    private final EmailTemplateResolverFactory templateResolverFactory;

    @Override
    public void create(EmailTemplateRequest emailTemplateRequest) {
        var resolver = templateResolverFactory.getResolver(emailTemplateRequest);
        var data = resolver.solve(emailTemplateRequest);
        var email = new Email()
                .withContent(data.getContent())
                .withRecipient(data.getRecipient())
                .withSender(data.getSender())
                .withSubject(data.getSubject())
                .withTenant(currentUserService.getCurrentTenant())
                .withAttempts(0)
                .withStatus(EmailStatus.PENDING);
        emailRepository.save(email);
    }

}
