package API;


import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import models.Glo.Card;

import java.util.HashMap;
import java.util.Map;

// API Documentation
// https://gloapi.gitkraken.com/v1/docs/
public class GloAPIHandler {
    private static final String SERVER = "https://gloapi.gitkraken.com/v1/glo";

    // My personal token
    private String userToken = "pe7bf5f7c217d709a840c00da0ecb79ce5e9209f0";
    private HttpRequest pendingRequest;

    public GloAPIHandler() {}

    public boolean isAuthenticated() {
        return this.userToken != null;
    }

    // Handler example
    // http://unirest.io
    public void getBoards(Callback callbackHandler) {
        String targetEndpoint = "/boards";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void getBoardById(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id;
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void getBoardColumns(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id + "?fields=columns";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void getBoardCards(String id, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + id + "/cards";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    public void createCard(String boardId, Card newCard, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/cards";
        request(HttpMethod.POST, SERVER + targetEndpoint, callbackHandler);
    }

    /*
    {
        "name": "Get board by ID",
        "description": {
            "text": "qwertyui",
            "updated_date": "2019-02-27T15:15:21.847Z",
            "created_by": {
                "id": "3beed461-389c-4c80-9850-086677f5f3e9"
            },
            "created_date": "2019-02-27T15:15:21.847Z",
            "updated_by": {
                "id": "3beed461-389c-4c80-9850-086677f5f3e9"
            }
        },
        "id": "5c70687ee245740010bc43b6"
    }
     */
    public void getBoardCardsByColumn(String boardId, String columnId, Callback callbackHandler) {
        String targetEndpoint = "/boards/" + boardId + "/columns/" + columnId + "/cards?fields=name&fields=description";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, null);
        request.asJsonAsync(callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Map<String,String> body, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, body);
        request.asJsonAsync(callbackHandler);
    }

    // TODO: Handle get parameters
    private HttpRequest buildRequest(HttpMethod method, String endpoint, Map<String, String> body) {
        HttpRequest pendingRequest = null;
        switch (method) {
            case GET:
                pendingRequest = Unirest.get(endpoint);
                break;
            case POST:
                pendingRequest = Unirest.post(endpoint);
                break;
            default:
                pendingRequest = null;
                break;
        }

        if (pendingRequest != null) {
            pendingRequest.header("Authorization", "Bearer " + this.userToken);
        }

        if (body != null) {
            for (String key : body.keySet()) {
                ((HttpRequestWithBody) pendingRequest).field(key, body.get(key));
            }
        }
        return pendingRequest;
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }



    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }
}
