package actions;

import API.AuthHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class LogoutAction extends LoggedInAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AuthHandler.logOut();
    }

}
