package server.message;

public class Find implements Command{
    private final String keyword;

    public Find(String keyword) {
        this.keyword=keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
