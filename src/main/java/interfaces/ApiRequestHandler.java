package interfaces;

import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.HttpResponse;
import models.JGloHelper;


public interface ApiRequestHandler {

    default boolean successfullyResponse(HttpResponse response){
        switch (response.getStatus()) {
            case 200:
            case 201:
                return true;
            case 400:
                JGloHelper.showMessage("Bad request", "Error", Messages.getErrorIcon());
                break;
            case 500:
                JGloHelper.showMessage("Server error", "Error", Messages.getErrorIcon());
            default:
                JGloHelper.showMessage("Unknow error", "Error", Messages.getErrorIcon());
        }
        return false;
    }
}
