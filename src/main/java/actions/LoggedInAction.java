package actions;

import API.AuthHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public abstract class LoggedInAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(AuthHandler.isLoggedIn());
        super.update(e);
    }

}
