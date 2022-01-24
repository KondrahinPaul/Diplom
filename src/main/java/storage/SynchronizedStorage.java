package storage;

import model.Contact;

import java.util.List;
import java.util.Optional;

// Используем здесь шаблон Decorator, чтобы можно было применить синхронизирующий функционал к любой
// реализации Storage.
public class SynchronizedStorage implements Storage {
    private final Storage storage;

    public SynchronizedStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Contact save(Contact contact) {
        return storage.save(contact);
    }

    @Override
    public Contact remove(int id) {
        return storage.remove(id);
    }

    @Override
    public Optional<Contact> find(int id) {
        return storage.find(id);
    }

    @Override
    public List<Contact> find() {
        return storage.find();
    }

    @Override
    public List<Contact> find(String keyword) {
        return storage.find(keyword);
    }

    @Override
    public List<Contact> call(String keyword) {
        return storage.call(keyword);
    }

    @Override
    public List<Contact> call() {
        return storage.call();
    }

    @Override
    public void close() throws Exception {
        storage.close();

    }
}
