package org.gameCharacter;

import org.environment.*;
import java.awt.*;

public class Ghost extends Enemy{
    Player player;

    public Ghost (int x, int y, Room room, int level) {
        super(x, y, room, level);
        this.animation = Resources.Animation.GHOST.getAnimation();
        this.deadAnimation = Resources.Animation.GHOST_DEAD.getAnimation();
    }

    @Override
    protected void hurt() {
        Resources.Sound.GHOST.reStart();
    }

    @Override
    protected boolean condition(int x, int y) {
        int x_  = player.getXBlock();
        int y_  = player.getYBlock();
        if (x_ <= 0) x = 1;
        if (x_ >= 12) x = 11;
        if (y_ <= 0) y = 1;
        if (y_ >= 12) x = 11;

        return x == x_ && y == y_;
    }

    @Override
    public int update(Player player) {
        this.player = player;
        if (isDead()) {
            if (frame >= 20) {
                int x  = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
                int y  = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
                room.dropCoin(x, y); // chet roi tien
                return -1;
            }
        } else {
            burn();
            if (!isUp() && !isRight() && !isDown() && !isLeft() && player.room == room)
                findTheWay(6);
            move();
        }
        return 0;
    }

    @Override
    public void draw(Graphics g) {
        frame ++;
        if (isDead()) {
            if (frame < 12)  g.drawImage(this.deadAnimation[frame / 3] ,xPosition, yPosition ,width ,height ,null);
        } else {
            if (frame >= 12) frame = 0;
            if (!isFlip())   g.drawImage(this.animation[frame / 3] ,xPosition + width ,yPosition ,-width ,height ,null);
            else        g.drawImage(this.animation[frame / 3] ,xPosition ,yPosition ,width ,height ,null);
        }
    }
}
