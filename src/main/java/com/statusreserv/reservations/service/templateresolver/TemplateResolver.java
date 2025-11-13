package com.statusreserv.reservations.service.templateresolver;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TemplateResolver {
    String renderTemplate(String html, Map<String, Object> variables);
}
