package models.Glo;

import com.google.gson.annotations.JsonAdapter;
import models.gson.JsonBoardDeserializer;

import java.util.LinkedList;
import java.util.List;

@JsonAdapter(JsonBoardDeserializer.class)
public class Board {
    private String name, id;

    private List<Column> columns;
    private List<User> members;


    public Board() {
        this.columns = new LinkedList<>();
    }

    public Board(String name, String id) {
        this();
        this.name = name;
        this.id = id;
    }

    public Board(String name, String id, List<Column> columns, List<User> members) {
        this(name, id);
        this.columns = columns;
        this.members = members;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
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
