import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;
import server.StorageProxy;
import storage.InMemoryStorage;
import storage.PGStorage;
import storage.Storage;
import storage.SynchronizedStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ContactList {
    private final static Logger log = LoggerFactory.getLogger(ContactList.class);

    private static final String HELP_STRING = """
            Команды:
            find [keyword]
            remove <id>
            add
            update<id>
            quit
            help
            """;
    private final Storage storage;

    public ContactList(Storage storage) {
        this.storage = storage;
    }

    public static void main(String[] args) throws Exception {
        final Configuration config = new Configuration(args);
        log.info("ClientBook started with configuration: {}", config);
        try (
                final Storage storage = createStorage(config)) {
            ContactList phoneBook = new ContactList(storage);
            if (config.getMode() == RunMode.SERVER) {
                try (Server server = new Server(storage, config.getPort())) {
                    server.start();
                    phoneBook.start();
                }
            } else {
                phoneBook.start();
            }
        }
    }

    public void start() {
        help();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (isBlank(command)) {
                System.err.println("The command is not specified");
                help();
                continue;
            }

            String[] commndArgs = command.split("\s");
            switch (commndArgs[0]) {
                case "help":
                    help();
                    break;
                case "add":
                    add(scanner);
                    break;
                case "update":
                    update(scanner, Integer.parseInt(commndArgs[1]));
                    break;
                case "find":
                    final String keyword = commndArgs.length > 1
                            ? commndArgs[1]
                            : "";
                    find(keyword);
                    break;
                case "quit":
                    System.out.println("BYE");
                    return;
                default:
                    System.err.println("unknow command: " + commndArgs[0]);
                    help();
                    break;
            }
        }
    }

    private void find(String keyword) {
        final List<Contact> contacts = keyword.isBlank()
                ? storage.find()
                : storage.find(keyword);
        System.out.println("Contacts found" + contacts.size());
        contacts.forEach(System.out::println);
    }

    private void update(Scanner scanner, int id) {
        final Optional<Contact> contactOpt = storage.find(id);
        contactOpt.ifPresentOrElse(
                contact -> {
                    fillContact(scanner, contact);
                    Contact savedContact = storage.save(contact);
                    System.out.println("Contact changed:");
                    System.out.println(savedContact);
                },
                () -> System.err.println("Contact with id" + id + "not found")
        );


    }

    private void add(Scanner scanner) {
        Contact newContact = createDefaultContact();
        fillContact(scanner, newContact);

        Contact savedContact = storage.save(newContact);
        System.out.println("Contact added");
        System.out.println(savedContact);
    }

    private void fillContact(Scanner scanner, Contact newContact) {
        System.out.println("Enter name [" + newContact.getName() + "]:");
        String name = scanner.nextLine();
        if (!isBlank(name)) {
            newContact.setName(name);

        }
        System.out.println("Enter adress [ " + newContact.getAddress() + " ]:");
        String adress = scanner.nextLine();
        if (!isBlank(adress)) {
            newContact.setAddress(adress);
        }

        System.out.println("Enter phone [" + newContact.getPhone() + "]:");
        String phones = scanner.nextLine();
        if (!isBlank(phones)) {
            newContact.setPhone(phones);
        }
        System.out.println("Enter age [" + newContact.getAge() + "]:");
        String age = scanner.nextLine();
        if (!isBlank(age)) {
            newContact.setAge(age);
        }
        System.out.println("Enter preferense [" + newContact.getPreferense() + "]:");
        String preferense = scanner.nextLine();
        if (!isBlank(preferense)){
            newContact.setPreferense(preferense);
        }

        System.out.println("Enter shopLike [" + newContact.getShopLike()+"]:");
        String shopLike = scanner.nextLine();
        if (!isBlank(preferense)){
            newContact.setShopLike(shopLike);
        }
    }

    private void help() {
        System.out.println(HELP_STRING);
    }

    private boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    private Contact createDefaultContact() {
        return new Contact(0, "000", "Anonymos", "Missed address", "000", "VIP or NOT", "Type of shop");
    }

    private static Storage createStorage(Configuration config) throws IOException, SQLException {
        return switch (config.getMode()) {
            case LOCAL -> new InMemoryStorage();
            case SERVER -> new SynchronizedStorage(new InMemoryStorage());
            case CLIENT -> {
                var storage = new StorageProxy(config.getHost(), config.getPort());
                storage.connect();
                yield storage;
            }
            case POSTGRES -> new PGStorage(config.dbname, config.userName, config.password);
        };
    }

    private static class Configuration {
        private final RunMode mode;
        private int port;
        private String host;
        private String dbname;
        private String userName;
        private String password;

        public Configuration(String[] args) {
            this.mode = RunMode.fromArgs(args);
            if (mode == RunMode.SERVER) {
                port = Integer.parseInt(args[1]);
            } else if (mode == RunMode.CLIENT) {
                final String[] split = args[1].split(":");
                host = split[0];
                port = Integer.parseInt(split[1]);

            } else if (mode == RunMode.POSTGRES) {
                dbname = args[1];
                userName = args[2];
                password = args[3];
            }
        }

        public RunMode getMode() {
            return mode;
        }

        public int getPort() {
            return port;
        }

        public String getHost() {
            return host;
        }

        @Override
        public String toString() {
            return "Configuration {" +
                    "mode= " + mode +
                    ", port= " + port +
                    ", host= " + host + '\'' +
                    '}';

        }
    }

    private enum RunMode {
        LOCAL,
        SERVER,
        CLIENT,
        POSTGRES;

        public static RunMode fromArgs(String[] args) {
            if (args.length == 0 || "local".equals(args[0])) {
                return LOCAL;
            } else if (args.length > 1) {
                return switch (args[0]) {
                    case "server" -> SERVER;
                    case "client" -> CLIENT;
                    case "postgres" -> POSTGRES;
                    default -> throw new IllegalArgumentException("Unknown mode: " + args[0]);
                };
            }
            throw new IllegalArgumentException("Not happen");
        }
    }
}

