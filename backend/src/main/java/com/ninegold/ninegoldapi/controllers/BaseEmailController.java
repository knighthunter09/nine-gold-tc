package com.ninegold.ninegoldapi.controllers;

import com.ninegold.ninegoldapi.exceptions.ConfigurationException;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import com.ninegold.ninegoldapi.services.springdata.MailContentBuilder;
import com.ninegold.ninegoldapi.utils.Helper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * The BaseEmail REST controller to provide email related methods.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEmailController {

    /**
     * The java mail sender.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    /**
     * The default from email address.
     */
    @Value("${mail.from}")
    private String fromAddress;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(javaMailSender, "javaMailSender");
        Helper.checkConfigState(Helper.isEmail(fromAddress), "fromAddress should be valid email address!");
    }

    /**
     * Render email template with template name and model params.
     *
     * @param name  the template name
     * @param model the model params.
     * @return the email template after render with model params
     */
    protected String getTemplate(String name, Map<String, Object> model, String type) {
        return mailContentBuilder.build(name, model, type);
    }


    /**
     * Send email with to email address, email name and model params.
     *
     * @param toEmail   the to email address.
     * @param emailName the email name.
     * @param model     the model params.
     * @throws NineGoldException throws if error to send email.
     */
    protected void sendEmail(String toEmail, String emailName, Map<String, Object> model) throws NineGoldException {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(toEmail);
            helper.setFrom(fromAddress);
            helper.setSubject(getTemplate(emailName + "-subject", model, ".txt"));
            helper.setText(getTemplate(emailName + "-body", model, ".html"), true);

            javaMailSender.send(mail);
        } catch (MessagingException | MailException e) {
            throw new NineGoldException("Error in sending email", e);
        }
    }
}
