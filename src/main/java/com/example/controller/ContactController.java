package com.example.controller;

import com.example.dao.ContactService;
import com.example.entity.Contact;
import com.example.entity.Message;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@RequestMapping("/contacts")
@Controller
public class ContactController {
    private ContactService contactService;
    private MessageSource messageSource;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Contact> contacts = contactService.findAll();
        model.addAttribute("contacts", contacts);
        return "list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable Long id, Model model) {
        Contact contact = contactService.findById(id);
        model.addAttribute("contact", contact);
        return "show";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Contact contact, BindingResult bindingResult,
                         Model uiModel, RedirectAttributes redirectAttributes,
                         Locale locale, @RequestParam(value = "file", required = false) Part file) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("contact_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("contact", contact);
            return "update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("contact_save_success", new Object[]{}, locale)));
        getFile(contact, file);
        contactService.save(contact);
        return "redirect:/contacts/" + contact.getId();
    }

    private void getFile(@Valid Contact contact, @RequestParam(value = "file", required = false) Part file) {
        if (file != null) {
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();

                fileContent = IOUtils.toByteArray(inputStream);
                contact.setPhoto(fileContent);
            } catch (IOException ignored) {

            }
            contact.setPhoto(fileContent);
        }
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("contact", contactService.findById(id));
        return "update";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Contact contact, BindingResult bindingResult,
                         Model uiModel, RedirectAttributes redirectAttributes,
                         Locale locale,
                         @RequestParam(value = "file", required = false) Part file) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("contact_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("contact", contact);
            return "create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("contact_save_success", new Object[]{}, locale)));

        getFile(contact, file);

        contactService.save(contact);
        return "redirect:/contacts/";
    }

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Contact contact = contactService.findById(id);
        return contact.getPhoto();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        Contact contact = new Contact();
        uiModel.addAttribute("contact", contact);

        return "create";
    }
}
