package com.statusreserv.reservations.service.template;

import com.statusreserv.reservations.model.email.EmailTemplateRequest;
import com.statusreserv.reservations.service.template.resolver.EmailTemplateResolver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailTemplateResolverFactory {

    private final Map<Class<?>, EmailTemplateResolver<?>> resolvers = new HashMap<>();

    public EmailTemplateResolverFactory(List<EmailTemplateResolver<?>> services) {
        for (EmailTemplateResolver<?> s : services) {
            resolvers.put(s.getSupportedType(), s);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends EmailTemplateRequest> EmailTemplateResolver<T> getResolver(T request) {
        return (EmailTemplateResolver<T>) resolvers.get(request.getClass());
    }
}
