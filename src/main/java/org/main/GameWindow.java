package org.main;

import javax.swing.JFrame;
import java.awt.*;
import org.environment.*;

public class GameWindow extends JFrame{
    public GameWindow(GamePanel gamePanel) {
        this.setTitle("BomberGirl OOP");
        this.setLayout(new BorderLayout());
        this.add(gamePanel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(Resources.SCREEN_W, Resources.SCREEN_H));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
