package storage;

import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PGStorage implements Storage {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final Connection connection;

    public PGStorage(String dbname, String userName, String passWord) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, userName, passWord);
    }


    @Override
    public Contact save(Contact contact) {
        try {
            if (contact.getId() == 0) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "insert into contact (name, adress,phones) values (?,?,?)")) {
                    ps.setString(1, contact.getName());
                    ps.setString(2, contact.getAddress());
                    ps.setString(3, contact.getPhone());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save contact", e);
        }
        return contact;
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
        try (Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery("select id, name,adress, phones from contact ")) {
                List<Contact> contacts = new ArrayList<>();
                while (rs.next()) {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt("id"));
                    contact.setName(rs.getString("name"));
                    contact.setAddress(rs.getString("adress"));
                    contact.setPhone(rs.getString("phone"));
                }
                return contacts;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed", e);
        }
    }

    @Override
    public List<Contact> find(String keyword) {
        return null;
    }

    @Override
    public List<Contact> call(String keyword) {
        try (Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery("select id, name,adress, phones from contact ")) {
                List<Contact> contacts = new ArrayList<>();

                while (rs.next()) {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt("id"));
                    contact.setName(rs.getString("name"));
                    contact.setAddress(rs.getString("adress"));
                    contact.setPhone(rs.getString("phone"));
                }
                return contacts;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed", e);
        }
    }

    @Override
    public List<Contact> call() {
        return null;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
