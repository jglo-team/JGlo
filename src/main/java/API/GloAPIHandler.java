package API;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

// API Documentation
// https://gloapi.gitkraken.com/v1/docs/

public class GloAPIHandler {
    private static final String SERVER = "https://gloapi.gitkraken.com/v1/glo";

    // My personal token
    private String userToken = "pe7bf5f7c217d709a840c00da0ecb79ce5e9209f0";
    private AsyncHttpClient asyncHttpClient = asyncHttpClient();

    public GloAPIHandler() {}

    public boolean isAuthenticated() {
        return this.userToken != null;
    }

    // Handler example
    // https://github.com/AsyncHttpClient/async-http-client
    public void getBoards(AsyncHandler handler) {
        String targetEndpoint = "/boards";
        request(HttpConstants.Methods.GET, SERVER + targetEndpoint, handler);
    }

    private Future<Response> request(String method, String endpoint, AsyncHandler handler) {
        return makeRequest(method, endpoint, handler, null).execute(handler);
    }

    private Future<Response> request(String method, String endpoint, AsyncHandler handler, String body) {
        return makeRequest(method, endpoint, handler, body).execute(handler);
    }

    private BoundRequestBuilder makeRequest(String method, String endpoint, AsyncHandler handler, String body) {
        BoundRequestBuilder response;
        switch (method) {
            case "Get":
                response = this.asyncHttpClient.prepareGet(endpoint);
                break;
            case "Post":
                response = this.asyncHttpClient.preparePost(endpoint);
                break;
            default:
                response = null;
        }
        // TODO: Trow exception if userToken is not set
        response.setHeader("Authorization", "Bearer " + this.userToken);

        if (response != null  && body != null) {
            response.setBody(body);
        }

        return response;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!this.asyncHttpClient.isClosed()) {
            this.asyncHttpClient.close();
        }
    }
}
