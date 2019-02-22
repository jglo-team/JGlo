package models;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.awt.image.ImageWatched;

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
    public static <targetClass> LinkedList parseJson(JsonNode jsonData, Class targetClass) {
        JSONArray arr = jsonData.getArray();

        LinkedList result = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++) {

            JSONObject jsonObject = (JSONObject) arr.get(i);

            Gson gson = new Gson();
            targetClass newObject = (targetClass) gson.fromJson(jsonObject.toString(), targetClass);

            result.add(newObject);
        }
        return result;
    }

    public static void showMessage(String message, String title, Icon icon) {
        ApplicationManager.getApplication().invokeLater(() ->
                Messages.showMessageDialog(message, title, icon)
         );
    }
}
