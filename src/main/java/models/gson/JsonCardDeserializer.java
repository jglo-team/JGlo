package models.gson;

import com.google.gson.*;
import models.Glo.Card;
import models.Glo.Description;
import models.Glo.User;

import java.lang.reflect.Type;
import java.util.LinkedList;


// Custom deserializer for card object, this is necessary due to description field being an object
public class JsonCardDeserializer implements JsonDeserializer<Card> {

    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        Card card = new Card(
                obj.get("id").getAsString(),
                null,
                obj.get("name").getAsString()
        );

        try {
            JsonElement descriptionJson = jsonElement.getAsJsonObject().get("description");
            if (descriptionJson != null) {
                card.setDescription(new Gson().fromJson(descriptionJson.toString(), Description.class));
            }

        } catch (IllegalArgumentException ie) {
            System.out.println(ie.getMessage());
        }
        // TODO: Get this from board instead
        try {
            JsonArray assigneesArrJson = obj.getAsJsonArray("assignees");
            if (assigneesArrJson != null) {

                LinkedList<User> assignees = new LinkedList<>();

                for (JsonElement memberJson : assigneesArrJson) {
                    assignees.add(new Gson().fromJson(memberJson.toString(), User.class));
                }

                card.setAssignees(assignees);
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return card;
    }
}