package callbacks;

import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

// TODO: Complete this class
public abstract class JGloCallback implements Callback {

    @Override
    public void failed(UnirestException e) {
        Messages.showMessageDialog(e.getMessage(), "Callback error", Messages.getErrorIcon());
    }

    @Override
    public void cancelled() {
        Messages.showMessageDialog("Canceled", "Callback error", Messages.getErrorIcon());
    }
}
