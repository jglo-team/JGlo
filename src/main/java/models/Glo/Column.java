package models.Glo;

import java.util.Date;

public class Column {
    String name;
    String id;
    Date created_date;


    public Column(String name, String id, Date created_date) {
        this.name = name;
        this.id = id;
        this.created_date = created_date;
        //this.created_by = created_by;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
