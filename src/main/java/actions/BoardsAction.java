package actions;

import API.GloAPIHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;

public class BoardsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        GloAPIHandler apiHandler = new GloAPIHandler();

        apiHandler.getBoards(new AsyncHandler() {
            @Override
            public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                return State.CONTINUE;
            }

            @Override
            public State onHeadersReceived(HttpHeaders headers) throws Exception {
                return State.CONTINUE;
            }

            @Override
            public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                Messages.showMessageDialog(e.getProject(), bodyPart.toString(), "Response", Messages.getInformationIcon());
                return State.CONTINUE;
            }

            @Override
            public void onThrowable(Throwable t) {

            }

            @Override
            public Object onCompleted() throws Exception {
                return null;
            }
        });
    }
}
