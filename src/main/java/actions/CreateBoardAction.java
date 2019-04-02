package actions;

import UI.AddBoardDialog;
import callbacks.JGloCallback;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.mashape.unirest.http.HttpResponse;
import interfaces.ApiRequestHandler;
import models.JGloHelper;
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
                        ApplicationManager.getApplication().invokeLater(() ->
                            JGloHelper.getMainWindow(e.getProject()).getBoards()
                        );
                    }

                }
            }
        });
        addBoardDialog.setVisible(true);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setText("New Board");
        e.getPresentation().setDescription("Create a new Board in Glo");
        e.getPresentation().setIcon(AllIcons.General.Add);
        super.update(e);
    }
}