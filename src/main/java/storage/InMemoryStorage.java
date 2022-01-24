package storage;

import model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStorage implements Storage {
    private final List<Contact> contacts = new ArrayList<>();
    private int ids;

    @Override
    public Contact save(Contact newContact) {
        if (newContact.getId() == 0) {
            newContact.setId(++ids);
            contacts.add(newContact);
            return newContact;
        } else {
            for (Contact actualContact : contacts) {
                if (actualContact.getId() == newContact.getId()) {
                    actualContact.setValues(newContact);
                    return newContact;
                }
            }
            newContact.setId(0);
            return save(newContact);
        }
    }

    @Override
    public Contact remove(int id) {
        // TODO реализуйте удаление
        return contacts.remove(id);
    }

    @Override
    public Optional<Contact> find(int id) {
        return contacts.stream()
                .filter(c -> c.getId() == id)
                .findAny();
    }

    @Override
    public List<Contact> find() {
        return new ArrayList<>(contacts);
    }

    @Override
    public List<Contact> find(String keyword) {
        // TODO переписать на stream api
        List<Contact> result = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contains(contact, keyword)) {
                result.add(contact);
            }
        }
        return result;
    }

    @Override
    public List<Contact> call(String keyword) {
        // TODO переписать на stream api
        List<Contact> result = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contains(contact, keyword)) {
                result.add(contact);
            }
        }
        return result;
    }

    @Override
    public List<Contact> call() {
        return new ArrayList<>(contacts);
    }

    private boolean contains(Contact contact, String keyword) {
        return contains(contact.getName(), keyword)
                || contains(contact.getAddress(), keyword)
                || contains(contact.getPhone(), keyword);
    }

    private boolean contains(/*nullable*/ String string, String keyword) {
        return string != null
                && string.toLowerCase().contains(keyword.toLowerCase());
    }

    @Override
    public void close() throws Exception {
// TODO реализуйте
    }
}
