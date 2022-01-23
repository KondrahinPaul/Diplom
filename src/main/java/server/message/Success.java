package server.message;

import model.Contact;

import java.util.List;

public class Success implements Response{
    private final List<Contact> contacts;

    public Success(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
