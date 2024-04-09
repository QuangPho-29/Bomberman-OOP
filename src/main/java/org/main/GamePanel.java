package org.main;

import org.gameController.*;
import org.environment.*;

import java.awt.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    private boolean menu;
    private Stage stage;
    private int frame;
    private boolean running;
    private Thread gameThread;

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GamePanel() {
        super();
        this.running = true;
        this.frame = 0;
        this.menu = true;
        this.stage = new Stage();
        Resources.Sound.MENU.setLoop();
        Resources.Sound.MENU.start();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(Resources.SCREEN_W, Resources.SCREEN_H));
        this.addKeyListener(new KeyboardInputs(this, stage.getPlayer(), stage));

        if (this.gameThread == null) {
            this.gameThread = new Thread(this, "GameThread");
            this.gameThread.start();
        }
    }

    public void init() {
        this.stage =  new Stage();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(Resources.SCREEN_W, Resources.SCREEN_H));
        this.addKeyListener(new KeyboardInputs(this, stage.getPlayer(), stage));
    }
    
    public void setStart(boolean check) {
        this.menu = check;
        if (!check) {
            this.frame = 0;
        }
    }
    public boolean isStart() {
        return this.menu;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (menu) {
            g.drawImage(Resources.Animation.MENU.get((frame / 20) % 4), 0, 0 , null);
        } else {
            this.stage.draw(g);
            trans(g);
        }
    }

    @Override
    public void run() {
        while (running) {
            frame++;
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stage.update();
            this.repaint();
        }
        System.exit(0);
    }

    private void trans(Graphics g) {
        if (frame < 20) {
            g.drawImage(Resources.Images.TRANSITION.getImage(), 0, -frame * 65, null);
        }
    }
}