package models.gson;

import com.google.gson.*;
import models.Glo.*;
import models.JGloHelper;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class JsonBoardDeserializer implements JsonDeserializer<Board> {

    @Override
    public Board deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        Board board = new Board(
                obj.get("name").getAsString(),
                obj.get("id").getAsString()
        );

        try {
            JsonArray membersArrJson = obj.getAsJsonArray("members");
            if (membersArrJson != null) {

                LinkedList<User> boardMembers = new LinkedList<>();

                for (JsonElement memberJson : membersArrJson) {
                    boardMembers.add(new Gson().fromJson(memberJson.toString(), User.class));
                }

                board.setMembers(boardMembers);
            }

            JsonArray columnsArrJson = obj.getAsJsonArray("columns");
            if (columnsArrJson != null) {
                LinkedList<Column> columns = new LinkedList<>();

                for (JsonElement columnArr : columnsArrJson) {
                    columns.add(new Gson().fromJson(columnArr.toString(), Column.class));
                }

                board.setColumns(columns);
            }

        } catch (IllegalArgumentException ie) {
            System.out.println(ie.getMessage());
        }

        return board;
    }
}

/*

"members": [
    {
      "created_date": "2018-02-28T20:53:36.871Z",
      "username": "ricardomaltez",
      "name": "Ricardo Maltez",
      "email": "ricardomaltez@gmail.com",
      "updated_date": "2019-03-04T10:42:22.922Z",
      "first_login": "2019-01-20T20:49:34.933Z",
      "board_invites": {},
      "sync_provider_identities": [
        {
          "type": "GitHub",
          "id": "7486476"
        },
        {
          "type": "GitHub",
          "id": "7486476"
        },
        {
          "type": "GitHub",
          "id": "7486476"
        }
      ],
      "glo_user": null,
      "email_confirmed": true,
      "id": "106c7e95-6f01-44ff-86e4-65e2fa4df920",
      "role": "owner"
    },
 */