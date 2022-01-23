package server.message;

import model.Contact;

public class Save implements Command{
    private final Contact contact;

    public Save(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
