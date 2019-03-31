package actions;

import UI.AddBoardDialog;
import UI.JGloJPanel;
import UI.MainJGloWindow;
import callbacks.JGloCallback;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.mashape.unirest.http.HttpResponse;
import interfaces.ApiRequestHandler;
import org.jetbrains.annotations.NotNull;

public class CreateBoardAction extends LoggedInAction implements ApiRequestHandler {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        AddBoardDialog addBoardDialog = new AddBoardDialog(new JGloCallback() {
            @Override
            public void completed(HttpResponse response) {
                if (successfullyResponse(response)){
                    Project project = e.getProject();
                    if (project != null){
                        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("JGlo");
                        Content content = toolWindow.getContentManager().getContent(0);

                        ((JGloJPanel)content.getComponent()).getMainJGloWindow().getBoards();
                    }

                }
            }
        });
        addBoardDialog.setVisible(true);


    }

}