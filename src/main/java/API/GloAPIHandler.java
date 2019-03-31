package API;


import callbacks.AuthCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import models.CustomError;
import models.Glo.Board;
import models.Glo.Card;
import models.Glo.Column;
import models.JGloHelper;
import models.SecureTokenGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// API Documentation
// https://gloapi.gitkraken.com/v1/docs/
public class GloAPIHandler {
    private static final String SERVER = "https://gloapi.gitkraken.com/v1/glo";

    // My personal token
    private String userToken;

    public GloAPIHandler() {
        userToken = AuthHandler.loadAccessToken();

        /*
        if (userToken == null) {
            triggerLogin(new AuthCallback() {
                @Override
                public void success() {
                    userToken = AuthHandler.loadAccessToken();
                    if (userToken == null) {
                        // Error ocorred
                    }
                }

                @Override
                public void error(CustomError customError) {
                    if (customError == CustomError.SOCKET_ERROR){
                        JGloHelper.showMessage("An error occurred connecting to the remote server", "Error", Messages.getErrorIcon());
                    }
                }
            });
        }
        */
    }

    public void triggerLogin(AuthCallback callback) {
        String clientId = "1qx3w9xgzcm3a4086cqy";
        String scope = "board:write";

        String token = SecureTokenGenerator.nextToken();

        String url ="https://app.gitkraken.com/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&scope=" + scope +
                "&state=" + token;

        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (IOException ex) {
            JGloHelper.showMessage("An error occurred opening the OAuth URL", "Error", Messages.getErrorIcon());
        }

        AuthHandler.startSocket(token, callback);
        userToken = PropertiesComponent.getInstance().getValue("accessToken");
    }

    public boolean isAuthenticated() {
        return this.userToken != null;
    }

    // Handler example
    // http://unirest.io
    public void getBoards(Callback callbackHandler) {
        String targetEndpoint = "/boards";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void createBoard(Board newBoard, Callback callbackHandler) {
        String targetEndpoint = "/boards";

        // Cleaning object to correspond to API specification
        newBoard.setMembers(null);
        newBoard.setColumns(null);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(newBoard, Map.class);
        request(HttpMethod.POST, SERVER + targetEndpoint, map, callbackHandler);
    }

    public void getBoardById(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id;
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void getBoardColumns(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id + "?fields=name&fields=columns&fields=members";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void getBoardCards(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id + "/cards";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void createCard(String boardId, String columnId, Card newCard, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/cards";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(newCard, Map.class);
        map.put("column_id", columnId);

        request(HttpMethod.POST, SERVER + targetEndpoint, map, callbackHandler);
    }

    public void editCard(String boardId, Card card, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/cards/" + card.getId();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(card, Map.class);

        request(HttpMethod.POST, SERVER + targetEndpoint, map, callbackHandler);
    }

    public void getBoardCardsByColumn(String boardId, String columnId, Callback callbackHandler) {
        String targetEndpoint = "/boards/" +
                boardId +
                "/columns/" +
                columnId +
                "/cards?fields=name&fields=description&fields=assignees";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void createColumn(String boardId, Column column, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/columns";
        column.setCards(null);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(column, Map.class);

        request(HttpMethod.POST, SERVER + targetEndpoint, map, callbackHandler);

    }

    public void deleteCard(String boardId, Card cardToDelete, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/cards/" + cardToDelete.getId();
        request(HttpMethod.DELETE, SERVER + targetEndpoint, callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, null);
        request.asJsonAsync(callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Map<String,Object> body, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, body);
        request.asJsonAsync(callbackHandler);
    }

    // TODO: Handle get parameters
    private HttpRequest buildRequest(HttpMethod method, String endpoint, Map<String, Object> body) {
        HttpRequest pendingRequest;
        switch (method) {
            case GET:
                pendingRequest = Unirest.get(endpoint);
                break;
            case POST:
                pendingRequest = Unirest.post(endpoint);
                break;
            case DELETE:
                pendingRequest = Unirest.delete(endpoint);
                break;
            default:
                pendingRequest = null;
                break;
        }

        if (pendingRequest != null) {
            pendingRequest.header("Authorization", "Bearer " + this.userToken);
        }

        if (body != null) {
            pendingRequest.header("accept", "application/json");
            pendingRequest.header("Content-Type", "application/json");

            String jsonBody = JGloHelper.mapToJson(body);

            ((HttpRequestWithBody) pendingRequest).body(jsonBody);
        }
        return pendingRequest;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }


}
