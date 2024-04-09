package org.environment;

import org.gameCharacter.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Room extends Object {
    public static final int START = 0;
    public static final int DUNGEON = 1;
    public static final int END = 3;

    private final String[][] map;
    private final int[][] mapStatus;
    private final int mapHeight;
    private final int mapWeight;
    private final int type;
    private final int stage;
    protected boolean show;
    protected int mainGate;

    protected ArrayList<Enemy> enemies = new ArrayList<>();

    public Room(int type, int stage) {
        this.type = type;
        this.stage = stage;
        this.show = false;

        String[][] mapArray;
        if (this.type == 0) {
            mapArray = Resources.Maps.get(0).map.clone();
        } else if (this.type == 1) {
            Random randomNumb = new Random();
            int random = randomNumb.nextInt(4);
            if (random == 0) {
                mapArray = Resources.Maps.get(3).map.clone();
            } else if (random == 1) {
                mapArray = Resources.Maps.get(4).map.clone();
            } else if (random == 2) {
                mapArray = Resources.Maps.get(5).map.clone();
            } else {
                mapArray = Resources.Maps.get(6).map.clone();
            }
        } else if (this.type == 2) {
            mapArray = Resources.Maps.get(1).map.clone();
        } else {
            mapArray = Resources.Maps.get(2).map.clone();
        }
        map = mapArray;
        for (int i = 0; i < map.length; i++) {
            map[i] = map[i].clone();
        }

        mapHeight = map.length;
        mapWeight = map[0].length;
        mapStatus = new int[mapHeight][mapWeight];
        this.xPosition = 0;
        this.yPosition = 0;
    }

    public int getMainGate() {
        return mainGate;
    }

    public void setMainGate(int g) {
        mainGate = g;
    }

    public int getType() {
        return type;
    }

    public int getMapHeight() {

        return mapHeight;
    }

    public int getMapWeight() {

        return mapWeight;
    }

    public int isExit(int x, int y) {
        if (map[y][x].charAt(0) == '1') return 1;
        else if (map[y][x].charAt(0) == '2') return 2;
        else if (map[y][x].charAt(0) == '3') return 3;
        else if (map[y][x].charAt(0) == '4') return 4;
        else return -1;
    }

    public void buildRoom(boolean up, boolean right, boolean down, boolean left) {
        Random ran = new Random();
        for (int j = 0; j < mapHeight; j++) {
            for (int i = 0; i < mapWeight; i++) {
                if (map[j][i].charAt(0) != '#' && map[j][i].charAt(0) != 'W') { // thay doi tat ca ki hieu tru '#' va 'W'
                    char c = map[j][i].charAt(0);
                    if (c == '/') {                                            // '/' thung se khong sinh ra o vi tri nay
                        map[j][i] = " ";
                    } else if (c == 'B') {                                      // 'B' sinh doi
                        map[j][i] = " ";
                        enemies.add(new Enemy(i, j, this, stage));
                    } else if (c == 'G') {                                      // 'G' sinh ma
                        map[j][i] = " ";
                        enemies.add(new Ghost(i, j, this, stage));
                    } else if (c == 'S') {                                      // 'S' sinh Silme
                        map[j][i] = " ";
                        enemies.add(new Slime(i, j, this, stage));
                    } else if (c == 'P') {
                        map[j][i] = " ";
                        enemies.add(new SnowEnemy(i, j, this, stage));
                    } else if ((c == '1' && up) || (c == '2' && right) || (c == '3' && down) || (c == '4' && left)) {
                        //map[j][i] = " ";
                    } else if (c == '1' || c == '2' || c == '3' || c == '4') {  // neu khong co phong ben canh thi xoa cua
                        map[j][i] = "#";
                    } else {
                        if (ran.nextInt() % 2 != 0 && type == DUNGEON) {
                            map[j][i] = "X";                                    // con lai thay bang thung
                        }
                    }
                }
            }
        }
    }

    public int touchEnemies(int x, int y) {
        for (Enemy e : enemies) {
            int x_ = (e.xPosition + 24) / Resources.BLOCK_SIZE;
            int y_ = (e.yPosition + 48) / Resources.BLOCK_SIZE;
            if (x == x_ && y == y_) {
                if (!(e instanceof SnowEnemy)) {
                    return 1;
                } else return 2;
            }
        }
        return 0;
    }

    public void dropCoin(int x, int y) {
        if (map[y][x].charAt(0) == 'I') {
            if (map[y][x].charAt(1) == 'G') {
                map[y][x] = "IG" + (char) (map[y][x].charAt(2) + 1);
            }
        } else {
            map[y][x] = "IG" + (char) 1;
        }
    }

    public int getCoin(int x, int y) {
        if (map[y][x].charAt(0) == 'I' && map[y][x].charAt(1) == 'G') {
            Resources.Sound.COIN.reStart();
            return map[y][x].charAt(2);
        } else return 0;
    }

    public String get(int x, int y) {
        if (x < 0 || x >= mapWeight || y < 0 || y >= mapHeight) return "#";
        return map[y][x];
    }

    public boolean wallBlocked(int x, int y) {
        if (x >= mapWeight || x < 0 || y >= mapHeight || y < 0) return true;
        return map[y][x].charAt(0) == 'W'
                || map[y][x].charAt(0) == 'X'
                || map[y][x].charAt(0) == 'B'
                || map[y][x].charAt(0) == '#';
    }

    public int getItem(int x, int y) {
        if (map[y][x].charAt(0) == 'F') return 0;
        if (map[y][x].charAt(0) == 'I') {
            int a = map[y][x].charAt(1) - '0';
            map[y][x] = " ";
            return a;
        } else return -1;
    }

    public void plantBomb(int x, int y, int bombNumber, int pow, char id) {
        for (int j = 0; j < mapHeight; j++) {
            for (int i = 0; i < mapWeight; i++) {
                if (map[j][i].charAt(0) == 'B' && map[j][i].charAt(2) == id) {
                    bombNumber--;
                }
            }
        }

        if (bombNumber > 0 && map[y][x].charAt(0) == ' ') {
            map[y][x] = "B" + ((char) pow) + id;
            mapStatus[y][x] = 13;
            Resources.Sound.CRYSTAL.reStart();
        }
    }

    public int update(Player player) {
        for (int i = 0; i < mapWeight; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (mapStatus[j][i] >= 0) {
                    mapStatus[j][i]--;
                }
            }
        }
        for (int i = 0; i < mapWeight; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (mapStatus[j][i] == -1) {
                    if (map[j][i].charAt(0) == 'F') {
                        map[j][i] = " ";
                    }
                    if (map[j][i].charAt(0) == '#' && map[j][i].length() > 1) {
                        mapStatus[j][i] = 3;
                    }
                    if (map[j][i].charAt(0) == 'B') {
                        explosion(j, i);
                    }
                    if (map[j][i].charAt(0) == 'S') {
                        Random ran = new Random();
                        int r = ran.nextInt() % 10;
                        if (r == 1) map[j][i] = "I1";
                        else if (r == 2) map[j][i] = "I2";
                        else if (r == 3) map[j][i] = "I3";
                        else map[j][i] = " ";
                    }
                }
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            int u = enemies.get(i).update(player);
            if (u < 0) {
                if (enemies.get(i) instanceof Slime) {
                    if (enemies.get(i).getSpeed() < (20 * stage)) {
                        Slime a = new Slime(enemies.get(i).xBlock, enemies.get(i).yBlock, this, stage);
                        a.setSpeed(enemies.get(i).getSpeed() * 2);
                        a.height = enemies.get(i).height - 10;
                        a.width = enemies.get(i).width - 10;

                        Slime b = new Slime(enemies.get(i).xBlock, enemies.get(i).yBlock, this, stage);
                        b.setSpeed(enemies.get(i).getSpeed() * 2);
                        b.height = enemies.get(i).height - 10;
                        b.width = enemies.get(i).width - 10;
                        enemies.add(a);
                        enemies.add(b);
                        enemies.remove(i);
                        i--;
                    } else {
                        enemies.remove(i);
                        i--;
                    }
                } else {
                    enemies.remove(i);
                    i--;
                }
            }
        }
        return 0;
    }

    private void explosion(int j, int i) {
        int pow = 0;
        if (map[j][i].length() >= 2) {
            pow = map[j][i].charAt(1);
        }
        Resources.Sound.EXPLOSION.reStart();
        map[j][i] = "F";
        mapStatus[j][i] = 3;

        int[] a = new int[4];
        for (int z = 1; z <= pow; z++) {
            for (int c = 0; c < 4; c++) {
                a[c]++;
            }
            if (immortal(j, i + a[0])) a[0]--;
            if (immortal(j, i - a[1])) a[1]--;
            if (immortal(j + a[2], i)) a[2]--;
            if (immortal(j - a[3], i)) a[3]--;

            map[j][i + a[0]] = "F";
            map[j][i - a[1]] = "F";
            map[j + a[2]][i] = "F";
            map[j - a[3]][i] = "F";

            mapStatus[j][i + a[0]] = 3;
            mapStatus[j][i - a[1]] = 3;
            mapStatus[j + a[2]][i] = 3;
            mapStatus[j - a[3]][i] = 3;
        }
    }

    private boolean immortal(int j, int i) {
        if (j <= 0 || i <= 0 || j >= mapHeight - 1 || i >= mapWeight - 1)
            return true; // moi phan tu ngoai map deu bat kha xam pham
        if (map[j][i].charAt(0) == 'X') { // gap thung thi dot chay thung do X là thung, S là thung dang chay
            map[j][i] = "S";
            mapStatus[j][i] = 3;
        }

        if (map[j][i].charAt(0) == 'B') { // neu gap bomb thi kich no qua bomb do
            explosion(j, i);
        }

        // neu gap tuong thi tra ve true
        return (map[j][i].charAt(0) == 'W' || map[j][i].charAt(0) == '#' || map[j][i].charAt(0) == 'S' || map[j][i].charAt(0) == '+');
    }

    //Draw
    public void draw(Graphics g, Player player) {
        if (type == Room.START) {
            g.drawImage(Resources.Images.START_FLOOR.getImage(), 0, 0, null);
        } else {
            for (int j = 0; j < mapHeight; j++) {
                for (int i = 0; i < mapWeight; i++) {
                    g.drawImage(Resources.Images.FLOOR.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                }
            }
        }
        for (int j = 0; j < mapHeight; j++) {
            for (int i = 0; i < mapWeight; i++) {
                switch (map[j][i].charAt(0)) {
                    case 'W' ->
                            g.drawImage(Resources.Images.COLUMN.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 15, null);
                    case 'X' ->
                            g.drawImage(Resources.Images.BOX.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 15, null);
                    case '#' -> {
                        if (map[j][i].length() == 1)
                            g.drawImage(Resources.Images.WALL.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 15, null);
                    }
                    case '5' ->
                            g.drawImage(Resources.Images.GATE.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                }

                switch (map[j][i].charAt(0)) {
                    case 'S' ->
                            g.drawImage(Resources.Animation.BOX.get(3 - mapStatus[j][i]), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 15, null);
                    case 'B' ->
                            g.drawImage(Resources.Animation.BOMB.get(mapStatus[j][i] % 4), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 15, null);
                    case 'K' -> {
                    }
                    case 'I' -> {
                        switch (map[j][i].charAt(1)) {
                            case 'G' ->
                                    g.drawImage(Resources.Images.COIN.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                            case '1' ->
                                    g.drawImage(Resources.Images.POWER_UPGRADE.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                            case '2' ->
                                    g.drawImage(Resources.Images.SPEED_UPGRADE.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                            case '3' ->
                                    g.drawImage(Resources.Images.ADD_BOMB_UPGRADE.getImage(), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE, null);
                        }
                    }
                    case '#' -> {
                        if (map[j][i].length() > 1)
                            g.drawImage(Resources.Animation.TORCH.get(3 - mapStatus[j][i]), i * Resources.BLOCK_SIZE, j * Resources.BLOCK_SIZE - 20, null);
                    }
                    case 'F' ->
                            g.drawImage(Resources.Animation.FIRE.get(3 - mapStatus[j][i]), i * Resources.BLOCK_SIZE - 7, j * Resources.BLOCK_SIZE - 15, null);
                }
            }
            if (player.getY() == j) {
                player.draw(g);
            }
            for (Enemy e : enemies) {
                if (e.getY() == j)
                    e.draw(g);
            }
        }
        for (int j = 0; j < mapHeight; j++) {
            for (int i = 0; i < mapWeight; i++) {
                if (map[j][i].charAt(0) == 'F' || map[j][i].charAt(0) == 'S')
                    g.drawImage(Resources.Images.LIGHT.getImage(), i * Resources.BLOCK_SIZE - 25, j * Resources.BLOCK_SIZE - 35, null);
                if (map[j][i].charAt(0) == '#' && map[j][i].length() > 1)
                    g.drawImage(Resources.Images.LIGHTP.getImage(), i * Resources.BLOCK_SIZE - 25, j * Resources.BLOCK_SIZE - 20, null);
            }
        }
    }
}
