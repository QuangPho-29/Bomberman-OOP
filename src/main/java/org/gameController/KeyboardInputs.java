package org.gameController;

import org.environment.*;
import org.main.GamePanel;
import org.gameCharacter.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private GamePanel gamePanel;
    private Player player;
    private Stage stage;

    public KeyboardInputs(GamePanel g, Player p, Stage stage) {
        this.gamePanel = g;
        this.player = p;
        this.stage = stage;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     *  sự kiện ấn phím.
     */
    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("keyPr1");
        if (gamePanel.isStart()) {
            if (e.getKeyCode() == KeyEvent.VK_S) {
//                System.out.println("Begin");
                gamePanel.init();
                gamePanel.setStart(false);

            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                gamePanel.init();
                gamePanel.setStart(false);
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                player.leftPressed();
            }
            else if (e.getKeyCode() == KeyEvent.VK_D) {
                player.rightPressed();
            }

            if (e.getKeyCode() == KeyEvent.VK_S) player.downPressed();
            else if (e.getKeyCode() == KeyEvent.VK_W) {
                player.upPressed();
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE) player.plantBomb();
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) stage.pause();
        }
    }

    /**
     * sự kiện thả phím.
     */
    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println("keyRe1");
        if (e.getKeyCode() == KeyEvent.VK_A) player.leftReleased();
        if (e.getKeyCode() == KeyEvent.VK_S) player.downReleased();
        if (e.getKeyCode() == KeyEvent.VK_D) player.rightReleased();
        if (e.getKeyCode() == KeyEvent.VK_W) player.upReleased();
//        System.out.println("keyRe2");
    }
}

