package models;

import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JGloHelper {
    /*
        Receives JSON object, and the list with the keys to find
        Returns Map with Key => List of objects
    */
    public static LinkedList<Map> parseJson(JsonNode jsonData, List<String> keys) {
        JSONArray arr = jsonData.getArray();

        LinkedList<Map> result = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++) {

            JSONObject jsonObject = (JSONObject) arr.get(i);
            Map<String, Object> map = new HashMap<>();

            for (String key : keys) {
                map.put(key, jsonObject.get(key));
            }

            result.add(map);

        }
        return result;
    }
}
