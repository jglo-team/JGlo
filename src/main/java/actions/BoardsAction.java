package actions;

import API.GloAPIHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;
import models.Board;
import models.JGloCallback;
import models.JGloHelper;

import io.netty.handler.codec.http.websocketx.*;


import java.util.List;


public class BoardsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        GloAPIHandler apiHandler = new GloAPIHandler();

        apiHandler.getBoards(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                JsonNode body =  (JsonNode) response.getBody();

                try {
                    //List<Board> result = JGloHelper.parseJsonArray(body, Board.class);

                    Board result = JGloHelper.parseObjectJson(body, Board.class);
                    JGloHelper.showMessage("Ola mundo", "Funcionei", Messages.getInformationIcon());
                } catch (Exception e) {
                    JGloHelper.showMessage(e.getMessage(), "Error", Messages.getErrorIcon());
                }


                //String name = (String) result.get(0).get("name");
            }
        });
    }
}
