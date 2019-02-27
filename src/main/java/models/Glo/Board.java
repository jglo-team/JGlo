package models.Glo;

public class Board {
    private String name, id;

    public Board() {
    }

    public Board(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
