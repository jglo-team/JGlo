package models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import models.Glo.Description;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JGloHelper {
    /*
        Receives JSON object, and the list with the keys to find
        Returns Map with Key => List of objects
    */
    public static <TargetClass> LinkedList<TargetClass> parseJsonArray(JSONArray jsonData, Class modelClass) {
        LinkedList<TargetClass> result = new LinkedList<>();

        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonData.get(i);

            Gson gson = new Gson();
            try {
                TargetClass newObject = gson.fromJson(jsonObject.toString(), (Type) modelClass);
                result.add(newObject);

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        return result;
    }

    public static <TargetClass> TargetClass parseObjectJson(JSONObject jsonData, Class modelClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData.toString(), (Type) modelClass);
    }


    public static void showMessage(String message, String title, Icon icon) {
        ApplicationManager.getApplication().invokeLater(() ->
                Messages.showMessageDialog(message, title, icon)
        );
    }

    public static <T> void initializeList(List<T> items, JBList listComponent) {
        ListModel model = new CollectionListModel();
        for (T obj : items) {
            ((CollectionListModel) model).add(obj);
        }
        listComponent.setModel(model);
    }

    public static <T, V> String mapToJson(Map<T, V> items) {
        String json = "{";

        int currentItem = 0;
        boolean deleted = false;

        for (T key : items.keySet()) {
            currentItem++;
            if (items.get(key) == null) {
                if (!deleted) {
                    deleted = true;
                    json = json.substring(0, json.length() - 1);
                }
                continue;
            }

            if (items.get(key) instanceof Map) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> innerMap = mapper.convertValue(items.get(key), Map.class);
                json += "\"" + key.toString() + "\": " + mapToJson(innerMap);

            } else if (items.get(key) instanceof List)
                json += "\"" + key.toString() + "\": " + items.get(key).toString();

            else json += "\"" + key.toString() + "\": \"" + items.get(key).toString() + "\"";

            deleted = false;
            if (currentItem < items.size())
                json += ",";

        }
        json += "}";
        return json;
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
