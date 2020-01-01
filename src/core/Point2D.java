package core;

import model.Field;
import model.blob.BlobPoint;

import java.util.Random;

public abstract class Point2D {
    protected static final Field  FIELD  = Field.getInstance();
    protected static       Random random = new Random();
    protected              int    x;
    protected              int    y;

    public Point2D( int x, int y ) {
        setX( x );
        setY( y );
    }

    public Point2D( Point2D point ) {
        x = point.getX();
        y = point.getY();
    }

    public static int isLeftOrUp( int currentPos, int destinationPos ) {
        return destinationPos < currentPos ? -1 : 1;
    }

    public static Point2D moveTowardsPoint( Point2D currentPos, Point2D destinationPos, int moveDistance ) {
        int newX = currentPos.getX() + isLeftOrUp( currentPos.getX(), destinationPos.getX() ) * moveDistance;
        int newY = currentPos.getY() + isLeftOrUp( currentPos.getY(), destinationPos.getY() ) * moveDistance;

        return new BlobPoint( newX, newY );
    }

    public static BlobPoint calcClosestBorder( Point2D pos ) {
        int x = pos.getX();
        int y = pos.getY();

        if ( x <= FIELD.MAX_X - x
             && x <= y
             && x <= FIELD.MAX_Y - y )                   //Left
            return new BlobPoint( 0, pos.getY() );

        else if ( FIELD.MAX_X - x <= x
                  && FIELD.MAX_X - x <= y
                  && FIELD.MAX_X - x <= FIELD.MAX_Y - y )    //Right
            return new BlobPoint( Point2D.FIELD.MAX_X, pos.getY() );

        else if ( y <= FIELD.MAX_Y - y
                  && y <= x
                  && y <= FIELD.MAX_X - x )                   //Bottom
            return new BlobPoint( pos.getX(), 0 );

        else
            return new BlobPoint( pos.getX(), Point2D.FIELD.MAX_Y ); //TOP

//        if ( field.MAX_Y - y <= y
//             && field.MAX_Y - y <= x
//             && field.MAX_Y - y <= field.MAX_X - x )    //Top
//            return new BlobPoint( pos.getX(), FIELD.MAX_Y );
    }

    @Override
    public boolean equals( Object obj ) {
        return getClass() == obj.getClass() && x == ((Point2D) obj).getX() && y == ((Point2D) obj).getY();
    }

    public boolean isAtHome(Point2D point) {
        return x == point.getX() || y == point.getY();
    }

    public int getX() {
        return x;
    }

    public abstract void setX( int x );

    public int getY() {
        return y;
    }

    public abstract void setY( int y );

    public abstract boolean inBoundsX( int x );

    public abstract boolean inBoundsY( int y );

    public boolean inBounds( Point2D point ) {
        return inBoundsX( point.getX() ) && inBoundsY( point.getY() );
    }

    public boolean inBounds( int x, int y ) {
        return inBoundsX( x ) && inBoundsY( y );
    }

    public double distanceNoSqrt( Point2D pointB ) {
        return (pointB.x - x) * (pointB.x - x) + (pointB.y - y) * (pointB.y - y);
    }

    public double distanceSqrt( Point2D pointB ) {
        return Math.sqrt( distanceNoSqrt( pointB ) );
    }

    @Override
    public String toString() {
        return "P(" + x + ',' + y + ')';
    }
}
