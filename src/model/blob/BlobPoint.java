package model.blob;

import core.Point2D;

public class BlobPoint extends Point2D {

    public BlobPoint( int x, int y ) {
        super( x, y );
    }

    public static BlobPoint randomPoint() {
        int x = 0, y = 0;

        // 0=left, 1=top, 2=right, 3=bottom
        switch (random.nextInt( 4 )) {
            case 0:
                x = 0;
                y = random.nextInt( FIELD.MAX_Y + 1 );
                break;
            case 1:
                x = random.nextInt( FIELD.MAX_X + 1 );
                y = FIELD.MAX_Y;
                break;
            case 2:
                x = FIELD.MAX_X;
                y = random.nextInt( FIELD.MAX_Y + 1 );
                break;
            case 3:
                x = random.nextInt( FIELD.MAX_X + 1 );
                y = 0;
                break;
        }
        return new BlobPoint( x, y );
    }

    @Override
    public void setX( int x ) {
        this.x = inBoundsX( x ) ? x : randomPoint().x;
    }

    @Override
    public void setY( int y ) {
        this.y = inBoundsY( y ) ? y : randomPoint().y;
    }

    @Override
    public boolean inBoundsX( int x ) {
        return x >= 0 && x <= FIELD.MAX_X;
    }

    @Override
    public boolean inBoundsY( int y ) {
        return y >= 0 && y <= FIELD.MAX_Y;
    }
}
