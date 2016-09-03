package com.example.controller;

import com.example.dao.ContactService;
import com.example.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/contacts")
@Controller
public class ContactController {
    private ContactService contactService;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Contact> contacts = contactService.findAll();
        model.addAttribute("contacts", contacts);
        return "list";
    }
}
