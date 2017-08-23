package com.ninegold.ninegoldapi.services.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String templateName, Map<String, Object> model, String type) {
        Context context = new Context();
        model.forEach((key, val) -> context.setVariable(key, val));
        return templateEngine.process(templateName, context);
    }
}