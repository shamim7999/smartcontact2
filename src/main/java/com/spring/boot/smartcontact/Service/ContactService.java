package com.spring.boot.smartcontact.Service;

import com.spring.boot.smartcontact.Repository.ContactRepository;
import com.spring.boot.smartcontact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<Contact> findAll(Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 2);
        return contactRepository.findAll(pageable);
    }

    public Page<Contact> getContactsByUserId(int userId, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 2);
        return contactRepository.getContactsByUserId(userId, pageable);
    }

    public Contact findById(int userId) {
        return contactRepository.findById(userId).get();
    }

    public void save(Contact contact) {
        contactRepository.save(contact);
    }
}
