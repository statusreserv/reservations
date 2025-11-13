package com.statusreserv.reservations.service.templateresolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateResolverImpl implements TemplateResolver {
    private final TemplateEngine templateEngine;

    @Override
    public String renderTemplate(String html, Map<String, Object> variables) {
        var context = new Context();
        context.setVariables(variables);
        return templateEngine.process(html, context);
    }
}
