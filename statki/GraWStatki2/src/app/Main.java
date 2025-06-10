package app;

import gui.SetupWindow;

import javax.swing.*;

// Główna metoda uruchamiająca aplikacje, SetupWindow to pierwsze okno gdzie gracz ustawia statki

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SetupWindow::new);
    }
}
