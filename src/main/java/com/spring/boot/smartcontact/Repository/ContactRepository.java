package com.spring.boot.smartcontact.Repository;

import com.spring.boot.smartcontact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    //Current Page
    // Contact Per Page - 5
    public Page<Contact> getContactsByUserId(int userId, Pageable pageable);
}
