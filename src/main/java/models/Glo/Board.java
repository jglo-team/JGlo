package models.Glo;

import java.util.LinkedList;
import java.util.List;

public class Board {
    private String name, id;

    private List<Column> columns;

    public Board() {
        this.columns = new LinkedList<>();
    }

    public Board(String name, String id) {
        this();
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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
