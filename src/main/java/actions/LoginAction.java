package actions;

import API.AuthHandler;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import models.JGloHelper;
import models.SecureTokenGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class LoginAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String accessToken = PropertiesComponent.getInstance().getValue("accessToken");

        String clientId = "509qtoevfhlzeinkmiom";
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

        AuthHandler.startSocket(token);


    }
}
