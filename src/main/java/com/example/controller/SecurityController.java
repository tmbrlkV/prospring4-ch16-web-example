package com.example.controller;

import com.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/security")
public class SecurityController {
    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping("/loginfail")
    public String loginFail(Model model, Locale locale) {
        model.addAttribute("message", new Message("error",
                messageSource.getMessage("message_login_fail", new Object[]{}, locale)));
        return "list";
    }
}
