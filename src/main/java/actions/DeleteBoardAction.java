package actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteBoardAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setText("Delete");
        e.getPresentation().setDescription("Delete the selected board");
        e.getPresentation().setIcon(AllIcons.Actions.Cancel);

        super.update(e);
    }
}
