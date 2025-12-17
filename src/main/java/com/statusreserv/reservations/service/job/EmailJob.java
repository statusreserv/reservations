package com.statusreserv.reservations.service.job;

import com.statusreserv.reservations.model.constants.EmailStatus;
import com.statusreserv.reservations.model.email.Email;
import com.statusreserv.reservations.repository.EmailRepository;
import com.statusreserv.reservations.service.email.sender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class EmailJob {

    private final EmailRepository repository;
    private final EmailSenderService emailSenderService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendPending() {
        log.info("Sending pending emails");
        List<Email> emails = repository.fetchEmailsByStatus(EmailStatus.PENDING.toString(), 0);

        for (Email email : emails) {
            try {
                emailSenderService.send(email.getSender(), email.getRecipient(), email.getSubject(), email.getContent());
                email.setStatus(EmailStatus.SENT);
            } catch (Exception e) {
               email.setAttempts(email.getAttempts() + 1);
               email.setStatus(EmailStatus.FAILED);
               log.error("Error while sending pending email, with message {}", e.getMessage());
            }
            repository.save(email);
        }

    }

    @Scheduled(cron = "0 0/30 * * * *")
    @Transactional
    public void retrySend() {
        log.info("Retrying sending emails");
        List<Email> emails = repository.fetchEmailsByStatus(EmailStatus.FAILED.toString(), 10);

        for (Email email : emails) {
            try {
                emailSenderService.send(email.getSender(), email.getRecipient(), email.getSubject(), email.getContent());
                email.setStatus(EmailStatus.SENT);
            } catch (Exception e) {
                email.setAttempts(email.getAttempts() + 1);
                log.error("Error while sending pending email, with message {}", e.getMessage());
            }
            repository.save(email);
        }

    }

}
