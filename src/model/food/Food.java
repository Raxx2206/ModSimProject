package model.food;

import core.Point2D;

public class Food {
    public final int     ENERGY;
    private      Point2D pos;

    public Food( Point2D pos ) {
        this.pos = pos;
        ENERGY   = 1;
    }

    public Food( Point2D pos, int energy ) {
        this.pos = pos;
        ENERGY   = energy;
    }

    public Point2D getPos() {
        return pos;
    }
}
