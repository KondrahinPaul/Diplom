package storage;

import model.Contact;

import java.util.List;
import java.util.Optional;

public interface Storage extends AutoCloseable {
    Contact save(Contact contact);
    Contact remove(int id);
    Optional<Contact> find(int id);
    List<Contact> find();
    List<Contact> find(String keyword);
}
