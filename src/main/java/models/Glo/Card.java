package models.Glo;

import com.google.gson.annotations.JsonAdapter;
import models.gson.JsonCardDeserializer;

import java.util.LinkedList;
import java.util.List;

@JsonAdapter(JsonCardDeserializer.class)
public class Card {
    private String id;
    private Description description;
    private String name;
    private List<User> assignees;

    public Card(String id, Description description, String name) {
        this.id = id;
        this.description = description;

        if (description == null) {
            this.description = new Description("");
        }
        this.assignees = new LinkedList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAssignee(User user) {
        this.assignees.add(user);
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);

        if (assignees.size() > 0) {
            stringBuilder.append(" - Assigned To: ");
            for (User assignee : assignees) {
            stringBuilder.append(assignee.getUsername() + '|');

            String cardRep = stringBuilder.toString();
                return cardRep.substring(0, cardRep.length() -1);
            }
        }

        return stringBuilder.toString();
    }


}
