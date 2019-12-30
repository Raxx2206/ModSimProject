package model.blob;

import core.Blob;
import core.Point2D;

public class NormalBlob extends Blob {

    public NormalBlob( Point2D pos ) {
        super( pos );
    }

    public NormalBlob( int energy, double speed, double size, double sense, Point2D pos ) {
        super( energy, speed, size, sense, pos );
    }

    @Override
    public void moveStrategy() {
        if ( !atHome )
            goHome();
    }
}
