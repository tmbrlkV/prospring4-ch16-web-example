package com.example.dao;

import com.example.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();
    Contact findById(Long id);
    Contact save(Contact contact);
}
