package org.environment;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Character extends Object {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean isDead;
    private boolean flip;

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public int speed;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    protected int xBlock;
    protected int yBlock;

    public int getXBlock() {
        return xBlock;
    }

    public void setXBlock(int xBlock) {
        this.xBlock = xBlock;
    }

    public int getYBlock() {
        return yBlock;
    }

    public void setYBlock(int yBlock) {
        this.yBlock = yBlock;
    }

    public Room room;
    BufferedImage image;
    protected BufferedImage[] animation;
    protected BufferedImage[] deadAnimation;
    protected BufferedImage[] runAnimation;
    private Queue<node> queue;

    public void Init(Room room, BufferedImage[] animation, BufferedImage[] run, BufferedImage[] dead) {
        this.room = room;
        this.animation = animation;
        this.runAnimation = run;
        this.deadAnimation = dead;
        this.image = this.animation[0];
        up = false;
        right = false;
        down = false;
        left = false;
        isDead = false;
        flip = false;
        speed = 4;
        xBlock = 0;
        yBlock = 0;
    }

    public void move() {
        int x = xBlock * Resources.BLOCK_SIZE + 25;
        int y = yBlock * Resources.BLOCK_SIZE + 25;
        this.left = false;
        this.right = false;
        this.up = false;
        this.down = false;

        if (yBlock == 0 && room.mainGate == 1) this.up = true;
        else if (xBlock == 0 && room.mainGate == 4) this.left = true;
        else if (yBlock == 12 && room.mainGate == 3) this.down = true;
        else if (xBlock == 12 && room.mainGate == 2) this.right = true;
        else {
            if (xPosition + 26 > x + this.speed) this.left = true;
            else if (xPosition + 26 < x - this.speed) this.right = true;
            else xPosition = x - 26;
            if (yPosition + 26 < y - this.speed) this.down = true;
            else if (yPosition + 26 > y + this.speed) this.up = true;
            else yPosition = y - 26;
        }

        if (up) {
            this.yPosition -= this.speed;
        } else if (down) {
            this.yPosition += this.speed;
        }

        if (left) {
            this.xPosition -= this.speed;
        } else if (right) {
            this.xPosition += this.speed;
        }
    }

    protected boolean condition(int x, int y) {
        return false;
    }

    public void findTheWay(int range) {
        boolean[][] arr = new boolean[13][13];
        queue = new LinkedList<>();
        if (!isBlocked(xBlock, yBlock - 1, arr)) queue.add(new node(xBlock, yBlock - 1, 0, range));
        if (!isBlocked(xBlock, yBlock + 1, arr)) queue.add(new node(xBlock, yBlock + 1, 2, range));
        if (!isBlocked(xBlock + 1, yBlock, arr)) queue.add(new node(xBlock + 1, yBlock, 1, range));
        if (!isBlocked(xBlock - 1, yBlock, arr)) queue.add(new node(xBlock - 1, yBlock, 3, range));
        if (queue.size() == 0) return;
        while (!queue.isEmpty()) {
            node n = queue.poll();
            if (n.count == 0) {
                return;
            }
            if (condition(n.x, n.y)) {
                //System.out.print(n.z);
                if (n.z == 0) yBlock--;
                if (n.z == 1) xBlock++;
                if (n.z == 2) yBlock++;
                if (n.z == 3) xBlock--;
                return;
            }
            if (!isBlocked(n.x, n.y - 1, arr)) queue.add(new node(n.x, n.y - 1, n.z, n.count - 1));
            if (!isBlocked(n.x, n.y + 1, arr)) queue.add(new node(n.x, n.y + 1, n.z, n.count - 1));
            if (!isBlocked(n.x + 1, n.y, arr)) queue.add(new node(n.x + 1, n.y, n.z, n.count - 1));
            if (!isBlocked(n.x - 1, n.y, arr)) queue.add(new node(n.x - 1, n.y, n.z, n.count - 1));
            arr[n.x][n.y] = true;
        }
    }

    protected boolean isBlocked(int x, int y, boolean[][] arr) {
        if (x == xBlock && y == yBlock) return true;
        if (x >= 0 && y >= 0 && x < 13 && y < 13) {
            if (arr[x][y]) return true;
        }
        if (room.get(x, y).charAt(0) == 'F') return true;
        return room.wallBlocked(x, y);
    }
}

class node {
    public int x;
    public int y;
    public int z;
    public int count;

    node(int x, int y, int z, int count) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }
}
