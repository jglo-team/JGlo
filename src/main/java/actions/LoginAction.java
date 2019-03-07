package actions;

import API.GloAPIHandler;
import callbacks.AuthCallback;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import models.CustomError;
import models.JGloHelper;
import org.jetbrains.annotations.NotNull;

public class LoginAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String accessToken = PropertiesComponent.getInstance().getValue("accessToken");
        GloAPIHandler handler = new GloAPIHandler();
        handler.triggerLogin(new AuthCallback() {
            @Override
            public void success() {
                JGloHelper.showMessage("Login success", "Error", Messages.getInformationIcon());
            }

            @Override
            public void error(CustomError customError) {
                if (customError == CustomError.SOCKET_ERROR){
                    JGloHelper.showMessage("An error occurred connecting to the remote server", "Error", Messages.getErrorIcon());
                }
            }
        });
    }
}
