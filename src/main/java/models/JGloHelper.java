package models;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.io.Compressor;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class JGloHelper {
    /*
        Receives JSON object, and the list with the keys to find
        Returns Map with Key => List of objects
    */
    public static <TargetClass> LinkedList<TargetClass> parseJsonArray(JsonNode jsonData, Class modelClass) {
        JSONArray arr = jsonData.getArray();
        LinkedList<TargetClass> result = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++) {

            JSONObject jsonObject = (JSONObject) arr.get(i);

            Gson gson = new Gson();
            TargetClass newObject = gson.fromJson(jsonObject.toString(),(Type) modelClass);

            result.add(newObject);
        }
        return result;
    }

    public static <TargetClass> TargetClass parseObjectJson(JsonNode jsonData, Class modelClass) {
        JSONObject obj = jsonData.getObject();

        Gson gson = new Gson();
        return gson.fromJson(obj.toString(),(Type) modelClass);
    }


    public static void showMessage(String message, String title, Icon icon) {
        ApplicationManager.getApplication().invokeLater(() ->
                Messages.showMessageDialog(message, title, icon)
         );
    }
}
