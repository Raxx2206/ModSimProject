package model.blob;

import core.Blob;
import core.Point2D;

public class GreedyBlob extends Blob {

    public GreedyBlob( Point2D pos ) {
        super( pos );
    }

    public GreedyBlob( int energy, double speed, double size, double sense, Point2D pos ) {
        super( energy, speed, size, sense, pos );
    }

    @Override
    public void moveStrategy() {
        if ( getEnergy() > energyToBorder() + energyCostPerMove() ) {
            tryFindFood();
        } else {
            if ( !atHome )
                goHome();
        }
    }
}
