package models;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

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
                TargetClass newObject = gson.fromJson(jsonObject.toString(),(Type) modelClass);
                result.add(newObject);

            } catch (Exception e) {

            }
        }
        return result;
    }

    public static <TargetClass> TargetClass parseObjectJson(JSONObject jsonData, Class modelClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonData.toString(),(Type) modelClass);
    }


    public static void showMessage(String message, String title, Icon icon) {
        ApplicationManager.getApplication().invokeLater(() ->
                Messages.showMessageDialog(message, title, icon)
         );
    }

    public static <T> void initializeList(List<T> items, JBList listComponent) {
        ListModel model = new CollectionListModel();
        for(T obj : items) {
            ((CollectionListModel) model).add(obj);
        }
        listComponent.setModel(model);
    }
}
