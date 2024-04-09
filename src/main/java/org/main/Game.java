package org.main;

import org.environment.Resources;

import java.awt.*;

public class Game {
    static private GameWindow gameWindow;
    private GamePanel game;
    public Game() {
        Resources.readFiles();
        game = new GamePanel();
        gameWindow = new GameWindow(game);
        game.requestFocus();
        gameWindow.add(game, BorderLayout.CENTER);
    }
}
