package com.statusreserv.reservations.service.template.render;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TemplateRender {
    String renderTemplate(String html, Map<String, Object> variables);
}
