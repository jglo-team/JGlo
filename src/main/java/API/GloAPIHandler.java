package API;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.Body;
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
        String targetEndpoint = "/boards/" + boardId + "/columns/" + columnId + "/cards?fields=name&fields=description";
        request(HttpMethod.GET, SERVER + targetEndpoint, callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, null);
        request.asJsonAsync(callbackHandler);
    }

    private void request(HttpMethod method, String endpoint, Map<String,Object> body, Callback callbackHandler) {
        HttpRequest request = buildRequest(method, endpoint, body);

        try {
            HttpResponse<JsonNode> res = request.asJson();
            res.getStatus();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        //request.asJsonAsync(callbackHandler);
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

            String jsonBody = mapToJson(body);

            ((HttpRequestWithBody) pendingRequest).body(jsonBody);
            /*
            ((HttpRequestWithBody) pendingRequest)
                    .field("column_id", body.get("column_id").toString(), "application/json")
                    .field("name", body.get("name").toString(), "application/json");
            */
        }
        Body b = pendingRequest.getBody();
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

    private <T,V> String mapToJson(Map<T,V> items) {
        String json = "{";

        int numItems = items.size();
        int currentItem = 0;

        for (T key : items.keySet()) {
            currentItem++;
            json += "\"" + key.toString() + "\": \"" + items.get(key).toString() + "\"";

            if (currentItem < items.size()) {
                json += ",";
            }
        }
        json += "}";
        return json;
    }



    @Override
    protected void finalize() throws Throwable {
        super.finalize();

    }
}
