package org.environment;

import org.gameCharacter.Player;

import java.awt.*;
import java.util.Random;

public class Stage extends Object {
    public final int STAGE_SIZE = 5; // kich co stage.
    private int i;
    private Room[][] rooms;
    private final Player player;
    private int stage;
    private boolean pause;

    public Player getPlayer() {
        return this.player;
    }

    public void setPause(boolean b) {
        this.pause = b;
    }

    public boolean isPause() {
        return pause;
    }

    /**
     * khoi tao stage, chon rooms[4][2] la phong khoi dau.
     */
    public Stage() {
        pause = false;
        i = 0;
        stage = 1;
        rooms = new Room[STAGE_SIZE][STAGE_SIZE];
        rooms[4][2] = new Room(Room.START, stage);

        buildStage();
        player = new Player();

        player.Init(rooms[4][2], Resources.Animation.ALICE.getAnimation(),
                Resources.Animation.RUN.getAnimation(),
                Resources.Animation.ALICE_DEAD.getAnimation());
        player.setRoom(4, 2, rooms[4][2]);
    }

    public void pause() {
        pause = !pause;
    }

    /**
     * xay stage.
     */

    public void buildStage() {
        Random ran = new Random();
        boolean up = false;
        boolean right = false;
        boolean down = true;
        boolean left = true;
        // chon phong END sau do tao cac phong noi phong END den START.
        int x = ran.nextInt(5);
        int y = ran.nextInt(2);
        rooms[y][x] = new Room(Room.END, stage);
        rooms[y][x].setMainGate(5);
        while (y != 4 || x != 2) {
            int r = ran.nextInt(2);
            int gate = 0;
            if (r == 0 && y < 4) {
                y++;
                gate = 1;
            }
            if (r == 1 && x < 2) {
                x++;
                gate = 4;
            }
            if (r == 1 && x > 2) {
                x--;
                gate = 2;
            }
            if (gate != 0) {
                rooms[y][x] = new Room(Room.DUNGEON, stage);
                rooms[y][x].setMainGate(gate);
            }
        }
        // xay DUNGEON xung quanh.
        buildDungeon();
        // khoi tao phong START.
        int gate = rooms[4][2].getMainGate();
        rooms[4][2] = new Room(Room.START, stage);
        rooms[4][2].show = true;
        rooms[4][2].setMainGate(gate);
        // neu co phong xung quanh thi xay cua den phong do.
        for (int i = 0; i < STAGE_SIZE; i++) {
            for (int j = 0; j < STAGE_SIZE; j++) {
                if (rooms[i][j] != null) {
                    up = false;
                    right = false;
                    down = false;
                    left = false;
                    if (i - 1 >= 0 && rooms[i - 1][j] != null) up = true;
                    if (i + 1 < STAGE_SIZE && rooms[i + 1][j] != null) down = true;
                    if (j - 1 >= 0 && rooms[i][j - 1] != null) left = true;
                    if (j + 1 < STAGE_SIZE && rooms[i][j + 1] != null) right = true;
                    rooms[i][j].buildRoom(up, right, down, left);
                }
            }
        }
    }

    /**
     * xay DUNGEON ben canh nhung phong khac null.
     */
    public void buildDungeon() {
        Random ran = new Random();
        for (int i = 0; i < STAGE_SIZE; i++) {
            for (int j = 0; j < STAGE_SIZE; j++) {
                if (rooms[i][j] == null && ran.nextInt() % 3 == 0) {
                    if ((i + 1 < STAGE_SIZE && rooms[i + 1][j] != null)
                            || (i - 1 >= 0 && rooms[i - 1][j] != null)
                            || (j + 1 < STAGE_SIZE && rooms[i][j + 1] != null)
                            || (j - 1 >= 0 && rooms[i][j - 1] != null)) {
                        rooms[i][j] = new Room(Room.DUNGEON, stage);
                    }
                }
            }
        }
    }

    /**
     * cap nhat cac phong va xu ly su kien giup nhan vat di qua lai cac phong.
     *
     * @return 0 neu khong co gi say ra.
     */
    public int update() {
        if (!pause) {
            i++;
            int u = player.update();
            if (u > 0) {
                int y = player.getStageY();
                int x = player.getStageX();
                if (u == 1) x--;
                else if (u == 2) y++;
                else if (u == 3) x++;
                else if (u == 4) y--;
                else if (u == 5) {
                    x = 4;
                    y = 2;
                    stage++;
                    rooms = new Room[STAGE_SIZE][STAGE_SIZE];
                    rooms[4][2] = new Room(Room.START, stage);
                    buildStage();
                }
                player.setRoom(x, y, rooms[x][y]);
                rooms[x][y].show = true;
                player.enterRoom((u + 1) % 4 + 1);
                i = 0;
            } else if (u < 0) replay();
            if (i % 4 == 0 || u > 0) {
                for (int i = 0; i < STAGE_SIZE; i++) {
                    for (int j = 0; j < STAGE_SIZE; j++) {
                        if (rooms[i][j] != null) {
                            rooms[i][j].update(player);
                        }
                    }
                }
            }
        }
        return 0;
    }

    private void replay() {
        stage = 1;
        rooms = new Room[STAGE_SIZE][STAGE_SIZE];
        rooms[4][2] = new Room(Room.START, stage);
        buildStage();
        player.Init(rooms[4][2], Resources.Animation.ALICE.getAnimation(),
                Resources.Animation.RUN.getAnimation(),
                Resources.Animation.ALICE_DEAD.getAnimation());
        player.setRoom(4, 2, rooms[4][2]);
        rooms[4][2].show = true;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("Arial", 0, 20));
        g.setColor(Color.white);
        player.room.draw(g, player);
        int x = 525;
        if (player.xBlock > 7 && player.yBlock < 7) x = 5;
        g.drawImage(Resources.Images.BACKGROUND.getImage(), x, 0, null);
        g.drawImage(Resources.Images.COIN.getImage(), 535 - x, 35, null);
        g.drawImage(Resources.Images.LIFE.getImage(), 535 - x, 5, null);

        g.drawString(String.valueOf(player.life), 570 - x, 33);
        g.drawString(String.valueOf(player.gold), 570 - x, 63);
        g.drawString("Stage: " + stage, 450, 33);

        for (int i = 0; i < STAGE_SIZE; i++) {
            for (int j = 0; j < STAGE_SIZE; j++) {
                if (rooms[j][i] != null && rooms[j][i].show)
                    if (j == player.getStageX() && i == player.getStageY()) {
                        g.drawImage(Resources.Images.MINIMAP2.getImage(), x + i * 23 + 2, j * 23, null);
                    } else {
                        g.drawImage(Resources.Images.MINIMAP.getImage(), x + i * 23 + 2, j * 23, null);
                    }
            }
        }

        if (pause) {
            g.drawImage(Resources.Images.PAUSE.getImage(), 0, 0, null);
            drawHub(g, 420, 250);
        }


    }

    /**
     * ve bang thong tin
     *
     * @param g do hoa
     */

    private void drawHub(Graphics g, int x, int y) {
        g.setColor(Color.white);
        g.drawString("Stage: " + stage, x, y);
        g.setColor(Color.ORANGE);
        g.drawString("Gold: " + player.gold, x, y + 40);
        g.setColor(Color.white);
        g.drawString("Life: " + player.life, x, y + 80);
        g.drawString("Number of bombs: " + player.bombNumber, x, y + 120);
        g.drawString("Power: " + player.power, x, y + 160);
        g.drawString("Speed: " + player.speed, x, y + 200);
    }
}
