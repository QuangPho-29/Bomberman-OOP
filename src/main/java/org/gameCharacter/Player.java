package org.gameCharacter;

import org.environment.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends org.environment.Character {
    protected int immortalTime;
    protected int stageX;
    protected int stageY;
    protected int bombBlockX;
    protected int bombBlockY;
    protected int frame;
    int frozen;

    char id;
    public int gold;
    public int life;
    public int bombNumber;
    public int power;
    String name = "";

    public int getY() {return (int) ((yPosition + 55) / Resources.BLOCK_SIZE);}
    public Room getRoom() {return room;}
    public int getStageX() {return stageX;}
    public int getStageY() {return stageY;}
    public void setID(int i) {this.id = (char)i;}
    public void setRoom(int x, int y, Room room) {
        stageX = x;
        stageY = y;
        this.room = room;
    }

    public void Init(Room room, BufferedImage[] animation, BufferedImage[] run, BufferedImage[] dead) {
        super.Init(room, animation, run, dead);
        stageX = 0;
        stageY = 0;
        bombBlockX = -1;
        stageY = -1;
        this.gold = 0;
        this.immortalTime = 0;
        this.setSpeed(5);
        this.height = 60;
        this.width = 52;
        this.frame = 0;
        this.life = 5;
        this.power = 1;
        this.bombNumber = 1;
        this.setID(0);
        this.xPosition = 6 * Resources.BLOCK_SIZE;
        this.yPosition = 5 * Resources.BLOCK_SIZE;
        this.xBlock = 6;
        this.yBlock = 5;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void plantBomb() {
        bombBlockX = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
        bombBlockY = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
        room.plantBomb(bombBlockX, bombBlockY, bombNumber , power, id);
    }

    protected void hurt() {
        Resources.Sound.HURT.reStart();
    }

    public void upPressed() {setUp(true);}
    public void downPressed() {setDown(true);}
    public void rightPressed() {
        setRight(true);
        setFlip(false);
    }
    public void leftPressed() {
        setLeft(true);
        setFlip(true);
    }

    public void upReleased() {
        setUp(false);
    }
    public void downReleased() {
        setDown(false);
    }
    public void rightReleased() {
        setRight(false);
    }
    public void leftReleased() {
        setLeft(false);
    }

    protected void pickItem() {
        int x  = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
        int y  = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
        gold += room.getCoin(x, y);
        switch (room.getItem(x, y)) {
            case 0 -> {
                dead();
            }
            case 1 -> power ++;
            case 2 -> setSpeed(getSpeed() + 2);
            case 3 -> bombNumber ++;
        }
        if (power > 23) power--;
    }

    public void enterRoom(int gate) {
        boolean first_time = false;
    }

    protected void dead() {
        hurt();
        immortalTime = 46;
        frame = 0;
        life--;
    }

    public int update() {
        if (immortalTime > 0) immortalTime--;
        if (life <= 0 && frame >= 11) {
            return -1;
        } else {
            if (immortalTime == 0) {
                pickItem();
                int h = room.touchEnemies(xBlock, yBlock);
                if (h == 1) dead();
                if(h == 2) frozen = 70;
            }
            if (frozen == 0) {
                if (isUp())     this.yPosition -= this.getSpeed();
                if (isDown())   this.yPosition += this.getSpeed();
                if (isRight())  this.xPosition += this.getSpeed();
                if (isLeft())   this.xPosition -= this.getSpeed();
                int x1 = (int) ((xPosition + 10)  / Resources.BLOCK_SIZE); // 4 điểm tạo nên 1 hình chữ nhật
                int x2 = (int) ((xPosition + 40) / Resources.BLOCK_SIZE); // hình chữ nhật đó là cơ thể vật lý của nhân vật
                int y1 = (int) ((yPosition + 40) / Resources.BLOCK_SIZE); // 4 điểm được chuyển từ tọa độ trên màn hình location thành tọa độ trên map
                int y2 = (int) ((yPosition + 60) / Resources.BLOCK_SIZE); // kiểm tra va chạm bằng cách kiểm tra 4 điểm này có vào ô bị chặn ko

                if (x1 < 0) {
                    xPosition = room.getMapWeight() * Resources.BLOCK_SIZE - 40;
                    return 4;
                }
                if (y2 >= room.getMapHeight()) {
                    yPosition = -10;
                    return 3;
                }
                if (x2 >= room.getMapWeight()) {
                    xPosition = 0;
                    return 2;
                }
                if (y1 < 0) {
                    yPosition = room.getMapHeight() * Resources.BLOCK_SIZE - 65;
                    return 1;
                }
                collisionTest(x1, x2, y1, y2);
            } else {
                frozen--;
            }
            xBlock  = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
            yBlock = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
            if (room.get(xBlock, yBlock).charAt(0) == '5') return 5;
        }
        return 0;
    }

    private void collisionTest(int x1, int x2, int y1, int y2) {
        int a = 0;
        if (isBlocked(x1, y1)) {
            this.yPosition += this.getSpeed() / 2;
            this.xPosition += this.getSpeed() / 2;
            a++;
        }
        if (isBlocked(x2, y1)) {
            this.yPosition += this.getSpeed() / 2;
            this.xPosition -= this.getSpeed() / 2;
            a--;
        }
        if (isBlocked(x1, y2)) {
            this.yPosition -= this.getSpeed() / 2;
            this.xPosition += this.getSpeed() / 2;
            a--;
        }
        if (isBlocked(x2, y2)) {
            this.yPosition -= this.getSpeed() / 2;
            this.xPosition -= this.getSpeed() / 2;
            a++;
        }

        if (a == 2 || a == -2) {
            if (isUp())     this.yPosition += this.getSpeed();
            if (isDown())   this.yPosition -= this.getSpeed();
            if (isRight())  this.xPosition -= this.getSpeed();
            if (isLeft())   this.xPosition += this.getSpeed();
        }

        if (a == 1 || a == -1) {
            if (isUp())     this.yPosition += this.getSpeed() / 2;
            if (isDown())   this.yPosition -= this.getSpeed() / 2;
            if (isRight())  this.xPosition -= this.getSpeed() / 2;
            if (isLeft())   this.xPosition += this.getSpeed() / 2;
        }

        if (((x1 != bombBlockX) && (x2 != bombBlockX)) || ((y1 != bombBlockY) && (y2 != bombBlockY))) {
            this.bombBlockX = -1;
            this.bombBlockY = -1;
        }
    }

    private boolean isBlocked(int x, int y) {
        if (x == bombBlockX && y == bombBlockY) return false;
        return room.wallBlocked(x, y);
    }

    public void draw(Graphics g) {
        frame ++;
        if (life == 0) {
            if (frame >= 12) frame --;
            else g.drawImage(this.deadAnimation[frame / 3] ,xPosition, yPosition - 200 + height , 50, 200,null);
        } else {
            if (immortalTime > 0 && frame % 6 > 2) return;
            if (frame >= 12) frame = 0;
//            g.drawString(name, xPosition ,yPosition - 2);
            drawShadow(g);
            if (isFlip())   g.drawImage(this.animation[frame / 3] ,xPosition + width ,yPosition , -width, height,null);
            else        g.drawImage(this.animation[frame / 3] ,xPosition ,yPosition ,width,height,null);
        }
        if (frozen != 0) {
            g.drawImage(Resources.Images.ICE.getImage(),xPosition ,yPosition + 20,null);
        }
    }

    private void drawShadow(Graphics g) {
        if(isUp() || isDown() || isRight() || isLeft()) {
            if (isFlip())   g.drawImage(this.runAnimation[frame / 4], xPosition + width, yPosition + 15, -width, height, null);
            else        g.drawImage(this.runAnimation[frame / 4], xPosition , yPosition + 15, width, height, null);
        } else {
            if (isFlip())   g.drawImage(this.runAnimation[1], xPosition + width, yPosition + 15, -width, height, null);
            else        g.drawImage(this.runAnimation[1], xPosition , yPosition + 15, width, height, null);
        }
    }
}
