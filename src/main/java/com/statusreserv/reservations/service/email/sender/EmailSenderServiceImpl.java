package com.statusreserv.reservations.service.email.sender;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final SesClient sesClient;

    @Override
    public void send(String from, String to, String subject, String bodyHtml){
        try {
            var rawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(createRawMessage(createMimeMessage(from, to, subject, bodyHtml)))
                    .build();

            sesClient.sendRawEmail(rawEmailRequest);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);

        }
    }

    private MimeMessage createMimeMessage(String from, String to, String subject, String bodyHtml) throws MessagingException {
        var session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        setSubject(message, subject);
        setRecipients(message, to);
        setFrom(message, from);
        setBody(message, bodyHtml);
        return message;
    }

    private RawMessage createRawMessage(MimeMessage message) throws MessagingException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());

        SdkBytes data = SdkBytes.fromByteBuffer(buffer);
        return RawMessage.builder().data(data).build();
    }

    private void setSubject(MimeMessage message, String subject) throws MessagingException {
        message.setSubject(subject, "UTF-8");
    }

    private void setRecipients(MimeMessage message, String to) throws MessagingException {
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
    }

    private void setFrom(MimeMessage message, String from) throws MessagingException {
        message.setFrom(new InternetAddress(from));
    }

    private void setBody(MimeMessage message, String bodyHtml) throws MessagingException {
        message.setContent(bodyHtml, "text/html; charset=UTF-8");
    }
}
