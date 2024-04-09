package org.environment;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * lưu tài nguyên game (những thứ đọc từ bên ngoài).
 */

public class Resources {

    public static final int BLOCK_SIZE = 50;
    public static final int SCREEN_H = 695;
    public static final int SCREEN_W = 662;

    public enum Images {
        BACKGROUND,
        ADD_BOMB_UPGRADE,
        COIN,
        POWER_UPGRADE,
        FLOOR,
        FLOOR2,
        GATE,
        COLUMN,
        ICON,
        LIFE,
        LIGHT,
        LIGHTP,
        MINIMAP,
        MINIMAP2,
        PAUSE,
        BOX,
        SPEED_UPGRADE,
        START_FLOOR,
        TRANSITION,
        ICE,
        WALL;

        private BufferedImage image = null;

        public BufferedImage getImage() {
            return this.image;
        }
    }

    /**
     * animation.
     */

    public enum Animation {
        ALICE,
        ALICE_DEAD,
        BAT,
        BAT_DEAD,
        BOX,
        BOMB,
        COIN,
        FIRE,
        GHOST,
        GHOST_DEAD,
        MENU,
        RUN,
        SNOW_ENEMY,
        SLIME,
        TORCH;

        private final BufferedImage[] animation = new BufferedImage[4];

        public BufferedImage get(int i) {
            return this.animation[i];
        }

        public BufferedImage[] getAnimation() {
            return this.animation;
        }
    }

    public enum Sound {
        MENU,
        EXPLOSION,
        COIN,
        BAT,
        GHOST,
        HURT,
        CRYSTAL,
        SLIME;

        private Clip clip;

        public void start() {
            clip.start();
        }

        public void reStart() {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }

        public void setLoop() {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static ArrayList<Map> Maps = new ArrayList<>();

    /**
     * hàm đọc file.
     */

    public static void readFiles() {
        try {
            String res = "/Img/";
            System.out.println(Resources.class.getResource(""));

            Images.ICE.image = ImageIO.read(Resources.class.getResource(res + "Ice.png"));
            Images.ICON.image = ImageIO.read(Resources.class.getResource(res + "Box.png"));
            Images.COIN.image = ImageIO.read(Resources.class.getResource(res + "Coin.png"));
            Images.GATE.image = ImageIO.read(Resources.class.getResource(res + "Gate.png"));
            Images.LIFE.image = ImageIO.read(Resources.class.getResource(res + "Life.png"));
            Images.MINIMAP.image = ImageIO.read(Resources.class.getResource(res + "Map.png"));
            Images.LIGHT.image = ImageIO.read(Resources.class.getResource(res + "LIGHT.png"));
            Images.LIGHTP.image = ImageIO.read(Resources.class.getResource(res + "LightP.png"));
            Images.FLOOR.image = ImageIO.read(Resources.class.getResource(res + "Floor.png"));
            Images.WALL.image = ImageIO.read(Resources.class.getResource(res + "Wall.png"));
            Images.MINIMAP2.image = ImageIO.read(Resources.class.getResource(res + "Map2.png"));
            Images.BOX.image = ImageIO.read(Resources.class.getResource(res + "Box.png"));
            Images.POWER_UPGRADE.image = ImageIO.read(Resources.class.getResource(res + "FireUp.png"));
            Images.ADD_BOMB_UPGRADE.image = ImageIO.read(Resources.class.getResource(res + "BombUp.png"));
            Images.FLOOR2.image = ImageIO.read(Resources.class.getResource(res + "Floor2.png"));
            Images.START_FLOOR.image = ImageIO.read(Resources.class.getResource(res + "Start.png"));
            Images.TRANSITION.image = ImageIO.read(Resources.class.getResource(res + "Transitions.png"));
            Images.BACKGROUND.image = ImageIO.read(Resources.class.getResource(res + "MapBg.png"));
            Images.SPEED_UPGRADE.image = ImageIO.read(Resources.class.getResource(res + "SpeedUp.png"));
            Images.COLUMN.image = ImageIO.read(Resources.class.getResource(res + "Column.png"));

            res = "/Hub/";
            Images.PAUSE.image = ImageIO.read(Resources.class.getResource(res + "Pause.png"));

            res = "/Animations/";
            for (int i = 1; i <= 4; i++) {
                Animation.MENU.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Menu" + i + ".png"));
                Animation.BOMB.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Bomb" + i + ".png"));
                Animation.BOX.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Box" + i + ".png"));
                Animation.TORCH.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Torch" + i + ".png"));
                Animation.FIRE.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Fire" + i + ".png"));
                Animation.ALICE.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Alice" + i + ".png"));
                Animation.ALICE_DEAD.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "AliceDead" + i + ".png"));
                Animation.BAT.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Bat" + i + ".png"));
                Animation.BAT_DEAD.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "BatDead" + i + ".png"));
                Animation.GHOST.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Ghost" + i + ".png"));
                Animation.GHOST_DEAD.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "GhostDead" + i + ".png"));
                Animation.SNOW_ENEMY.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "IceSkull" + i + ".png"));
                Animation.SLIME.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Slime" + i + ".png"));
            }

            for (int i = 1; i <= 3; i++) {
                Animation.RUN.animation[i - 1] = ImageIO.read(Resources.class.getResource(res + "Run" + i + ".png"));
            }

            res = "/Sound/";
            Sound.EXPLOSION.clip = AudioSystem.getClip();
            Sound.MENU.clip = AudioSystem.getClip();
            Sound.COIN.clip = AudioSystem.getClip();
            Sound.BAT.clip = AudioSystem.getClip();
            Sound.GHOST.clip = AudioSystem.getClip();
            Sound.HURT.clip = AudioSystem.getClip();
            Sound.CRYSTAL.clip = AudioSystem.getClip();
            Sound.SLIME.clip = AudioSystem.getClip();

            Sound.EXPLOSION.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Explosion.wav")));
            Sound.MENU.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "MenuTheme.wav")));
            Sound.COIN.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Coin.wav")));
            Sound.BAT.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Bat.wav")));
            Sound.GHOST.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Ghost.wav")));
            Sound.HURT.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Hurt.wav")));
            Sound.CRYSTAL.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Crystal.wav")));
            Sound.SLIME.clip.open(AudioSystem.getAudioInputStream(Resources.class.getResource(res + "Slime.wav")));

            Scanner in = new Scanner(Paths.get("MapData.txt"), StandardCharsets.UTF_8);
            int n = in.nextInt();
            for (int z = 0; z < n; z++) {
                Map map = new Map(13);
                for (int i = 0; i < 13; i++) {
                    for (int j = 0; j < 13; j++) {
                        String s = in.next();
                        if (s.charAt(0) == '-') map.map[i][j] = " ";
                        else map.map[i][j] = s;
                    }
                }
                Maps.add(map);
            }

        } catch (IOException e) {
            System.err.println(e + ": Cannot read image file");
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

class Map {
    public String[][] map;

    Map(int size) {
        map = new String[size][size];
    }
}
