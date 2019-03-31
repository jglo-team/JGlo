package UI;

import javax.swing.*;
//import java.beans.JavaBean;
//@JavaBean(defaultProperty = "UI", description = "A generic lightweight container.")
public class JGloJPanel extends JPanel {

    private MainJGloWindow mainJGloWindow;

    public void setMainJGloWindow(MainJGloWindow mainJGloWindow) {
        this.mainJGloWindow = mainJGloWindow;
    }

    public MainJGloWindow getMainJGloWindow() {
        return mainJGloWindow;
    }
}
