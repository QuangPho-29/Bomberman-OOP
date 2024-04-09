package org.gameCharacter;

import org.environment.*;

import java.util.Random;

public class SnowEnemy extends Enemy{
    Player player;
    public SnowEnemy(int x, int y, Room room, int level) {
        super(x, y, room, level);
        this.animation = Resources.Animation.SNOW_ENEMY.getAnimation();
        this.speed = 15;
    }

    @Override
    protected boolean condition(int x, int y) {
        int x_  = player.getXBlock();
        int y_  = player.getYBlock();
        return x == x_ && y == y_;
    }

    @Override
    public int update(Player player) {
        this.player = player;
        if (!isUp() && !isDown() && !isRight() && !isLeft()) {
            Random ran = new Random();
            int r = ran.nextInt(4);
            if (r == 0 && (room.get(xBlock - 1, yBlock).charAt(0) == ' ' || room.get(xBlock - 1, yBlock).charAt(0) == 'X')) xBlock--;
            if (r == 1 && (room.get(xBlock, yBlock + 1).charAt(0) == ' ' || room.get(xBlock, yBlock + 1).charAt(0) == 'X')) yBlock++;
            if (r == 2 && (room.get(xBlock + 1, yBlock).charAt(0) == ' ' || room.get(xBlock + 1, yBlock).charAt(0) == 'X')) xBlock++;
            if (r == 3 && (room.get(xBlock, yBlock - 1).charAt(0) == ' ' || room.get(xBlock, yBlock - 1).charAt(0) == 'X')) yBlock--;
        }
        move();
        return 0;
    }
}
