package actions;

import API.AuthHandler;
import API.GloAPIHandler;
import callbacks.AuthCallback;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import models.CustomError;
import models.JGloHelper;
import org.jetbrains.annotations.NotNull;

public class LoginAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        GloAPIHandler handler = new GloAPIHandler();
        handler.triggerLogin(new AuthCallback() {
            @Override
            public void success() {
                JGloHelper.showMessage("You can now enjoy JGlo!", "Logged in", Messages.getInformationIcon());
                Project project = e.getProject();
                if (project != null){
                    ApplicationManager.getApplication().invokeLater(() ->
                        JGloHelper.getMainWindow(e.getProject()).initializeComponents()
                    );
                }
            }

            @Override
            public void error(CustomError customError) {
                if (customError == CustomError.SOCKET_ERROR) {
                    JGloHelper.showMessage("An error occurred connecting to the remote server", "Error", Messages.getErrorIcon());
                }
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(!AuthHandler.isLoggedIn());
        super.update(e);
    }
}
