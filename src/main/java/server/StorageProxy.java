package server;

import model.Contact;
import server.message.*;
import storage.Storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class StorageProxy implements Storage, Cloneable {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public StorageProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public Contact save(Contact contact) {
        try {
            objOut.writeObject(new Save(contact));
            final Response response = (Response) objIn.readObject();
            final List<Contact> res = getResponse(response);
            return res.isEmpty()
                    ? null
                    : res.get(0);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed saving contact", e);
        }
    }

    @Override
    public Contact remove(int id) {
        return null;
    }

    @Override
    public Optional<Contact> find(int id) {
        return Optional.empty();
    }

    @Override
    public List<Contact> find() {
        try {
            objOut.writeObject(new FindAll());
            final Response response = (Response) objIn.readObject();
            return getResponse(response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed finding contacts", e);
        }
    }

    @Override
    public List<Contact> find(String keyword) {
        try {
            objOut.writeObject(new Find(keyword));
            final Response response = (Response) objIn.readObject();
            return getResponse(response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed finding contacts", e);
        }
    }


    private List<Contact> getResponse(Response response) {
if (response instanceof Success success) {
    return success.getContacts();
}else if (response instanceof Failure failure){
    throw new RuntimeException(failure.getErrorMessage());
}
throw new RuntimeException("Unknown server response");
    }

    @Override
    public void close() throws Exception {
        if (socket != null) {
            socket.close();
        }

    }
}
