package server.message;

import model.Contact;

public class Remove implements Command{
    private final Contact contact;

    public Remove(Contact contact) {
        this.contact = contact;
    }

public int getId() {
        return contact.getId();
    }

}
