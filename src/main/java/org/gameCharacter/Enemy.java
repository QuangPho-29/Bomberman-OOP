package org.gameCharacter;

import org.environment.*;

import java.awt.*;
import java.util.Random;

public class Enemy extends org.environment.Character {
    int frame;

    public int getY() {return (int) ((yPosition + 50) / Resources.BLOCK_SIZE);}
    public void dead() {
        this.setDead(true);
        frame = 0;
    }

    public Enemy (int x, int y, Room room,int level) {
        super();
        super.Init(room, Resources.Animation.BAT.getAnimation(),
                Resources.Animation.RUN.getAnimation(),
                Resources.Animation.BAT_DEAD.getAnimation());
        this.setDead(false);
        this.height = 60;
        this.width = 52;
        this.xBlock = x;
        this.yBlock = y;
        this.xPosition = x * Resources.BLOCK_SIZE;
        this.yPosition = y * Resources.BLOCK_SIZE;
        frame = 0;
        this.setSpeed(3 + 7 * level);
    }

    protected void hurt() {
        Resources.Sound.BAT.reStart();
    }

    /**
     * kiem tra nhan vat co di vao o lua ko.
     */
    protected void burn() {
        int x  = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
        int y  = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
        if(room.get(x, y).charAt(0) == 'F') {
            setDead(true);
            hurt();
        }
    }

    public int update(Player player) {
        if (isDead()) {
            if (frame >= 20) {
                int x  = (int) ((xPosition + 24) / Resources.BLOCK_SIZE);
                int y  = (int) ((yPosition + 48) / Resources.BLOCK_SIZE);
                room.dropCoin(x, y);
                return -1;
            }
        } else {
            burn();
            if (!isUp() && !isDown() && !isRight() && !isLeft()) {
                Random ran = new Random();
                int r = ran.nextInt(4);
                if (r == 0 && check(xBlock - 1, yBlock)) xBlock--;
                if (r == 1 && check(xBlock, yBlock + 1)) yBlock++;
                if (r == 2 && check(xBlock + 1, yBlock)) xBlock++;
                if (r == 3 && check(xBlock, yBlock - 1)) yBlock--;
            }
            move();
        }
        return 0;
    }
    
    private boolean check(int x, int y) {
        return  room.get(x, y).charAt(0) == ' '
                || room.get(x, y).charAt(0) == 'I';
    }

    public void draw(Graphics g) {
        frame ++;
        if (isDead()) {
            if (frame < 12)  g.drawImage(this.deadAnimation[frame / 3] ,xPosition, yPosition ,width ,height ,null);
        } else {
            if (frame >= 12) frame = 0;
            if (isFlip())   g.drawImage(this.animation[frame / 3] ,xPosition + width ,yPosition ,-width ,height ,null);
            else g.drawImage(this.animation[frame / 3] ,xPosition ,yPosition ,width ,height ,null);
        }
    }
}
