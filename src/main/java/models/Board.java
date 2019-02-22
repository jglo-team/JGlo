package models;

import java.util.Map;

public class Board {
    private String name, id;

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

    public static Board parseFromMap(Map data) {

        return new Board("abc", "adua");
    }
}
