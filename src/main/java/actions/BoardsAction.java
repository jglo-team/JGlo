package actions;

import API.GloAPIHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import models.JGloCallback;
import models.JGloHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BoardsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        GloAPIHandler apiHandler = new GloAPIHandler();

        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body =  (JsonNode) response.getBody();
                LinkedList<String> keys = new LinkedList<>(Arrays.asList("name", "id"));

                List<Map> result = JGloHelper.parseJson(body, keys);

                String name = (String) result.get(0).get("name");
            }
        });
    }
}
