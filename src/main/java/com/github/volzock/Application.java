package com.github.volzock;

import com.github.volzock.gui.FractalExplorer;

public class Application {
    public static void main(String[] args) {
        FractalExplorer application = new FractalExplorer(800, 800);
        application.createAndShowGUI();
    }
}
