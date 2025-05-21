package app;

import gui.SetupWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SetupWindow::new);
    }
}
