package com.example.dao;

import com.example.entity.Contact;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@Service("contactService")
public class SimpleContactService implements ContactService {
    private ContactRepository repository;

    @Autowired
    public void setRepository(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Contact findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Contact save(Contact contact) {
        return repository.save(contact);
    }
}
