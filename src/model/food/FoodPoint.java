package model.food;

import core.Point2D;

public class FoodPoint extends Point2D {
    private static final double DEAD_ZONE = 0.1;                        //Prevent food spawn in a 10% border of field

    public FoodPoint( int x, int y ) {
        super( x, y );
    }

    public static FoodPoint randomPoint() {
        int x = randomPointX();
        int y = randomPointY();

        return new FoodPoint( x, y );
    }

    public static int randomPointY() {
        return (int) (random.nextInt( (int) (FIELD.MAX_Y - (FIELD.MAX_Y * DEAD_ZONE * 2)) + 1 ) +
                      FIELD.MAX_Y * DEAD_ZONE);
    }

    public static int randomPointX() {
        return (int) (random.nextInt( (int) (FIELD.MAX_X - (FIELD.MAX_X * DEAD_ZONE * 2)) + 1 ) +
                      FIELD.MAX_X * DEAD_ZONE);
    }

    @Override
    public void setX( int x ) {
        this.x = inBoundsX( x ) ? x : randomPointX();
    }

    @Override
    public void setY( int y ) {
        this.y = inBoundsY( y ) ? y : randomPointY();
    }

    @Override
    public boolean inBoundsX( int x ) {
        return (x >= FIELD.MAX_X * DEAD_ZONE) && (x <= FIELD.MAX_X * (1 - DEAD_ZONE));
    }

    @Override
    public boolean inBoundsY( int y ) {
        return (y >= FIELD.MAX_Y * DEAD_ZONE) && (y <= FIELD.MAX_Y * (1 - DEAD_ZONE));
    }
}
