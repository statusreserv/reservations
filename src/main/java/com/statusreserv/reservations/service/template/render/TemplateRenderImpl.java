package com.statusreserv.reservations.service.template.render;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateRenderImpl implements TemplateRender {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String renderTemplate(String html, Map<String, Object> variables) {
        var context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(html, context);
    }
}
