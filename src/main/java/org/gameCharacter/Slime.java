package org.gameCharacter;

import org.environment.*;

public class Slime extends Enemy{
    public Slime(int x, int y, Room room, int level) {
        super(x, y, room, level);
        this.animation = Resources.Animation.SLIME.getAnimation();
        this.deadAnimation = Resources.Animation.BAT_DEAD.getAnimation();
        this.setSpeed(5 * level);
    }

    @Override
    protected void hurt() {
        Resources.Sound.SLIME.reStart();
    }

}