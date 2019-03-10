package models.Glo;

import com.google.gson.annotations.JsonAdapter;
import models.gson.JsonCardDeserializer;

@JsonAdapter(JsonCardDeserializer.class)
public class Card {
    private String id;
    private Description description;
    private String name;

    public Card(String id, Description description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }


}
