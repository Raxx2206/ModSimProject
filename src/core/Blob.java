package core;


import lib.quadtree.Point;
import model.Field;
import model.blob.BlobPoint;
import model.food.Food;
import model.food.FoodPoint;
import org.jetbrains.annotations.NotNull;


public abstract class Blob {

    public static final  int     BASIC_ENERGY = 100;
    public static final  double  BASIC_SPEED  = 1.;
    public static final  double  BASIC_SIZE   = 1.;
    public static final  double  BASIC_SENSE  = 1.;
    private final static Field   FIELD        = Field.getInstance();
    private final        int     SCALE        = 10;
    protected            boolean atHome       = false;
    protected            int     stamina;
    protected            int     energy;
    protected            double  speed;
    protected            double  size;
    protected            double  sense;
    protected            int     foodCounter;
    protected            Point2D pos;

    public Blob( Point2D pos ) {
        energy  = BASIC_ENERGY;
        speed   = BASIC_SPEED;
        size    = BASIC_SIZE;
        sense   = BASIC_SENSE;
        stamina = (int) (SCALE * speed);

        this.pos = pos;
    }

    public Blob( int energy, double speed, double size, double sense, Point2D pos ) {
        this.energy = energy;
        this.speed  = speed;
        this.size   = size;
        this.sense  = sense;
        this.pos    = pos;
        stamina     = (int) (SCALE * speed);
    }

    public static Point2D randomMove( Point2D currentPos, int moveDistance ) {
        var angle = Math.random() * Math.PI * 2;

        int x = (int) (Math.cos( angle ) * moveDistance);
        int y = (int) (Math.sin( angle ) * moveDistance);

        return new BlobPoint( currentPos.getX() + x, currentPos.getY() + y );
    }

    public double energyCostPerMove() {
        return (size * size * size) + (speed * speed) + sense;
    }

    protected int maxDistanceToGo() {
        return (int) Math.min( stamina, sense * SCALE );
    }

    public void move() {
        while (getStamina() > 0) {
            if ( foodCounter == 0 ) { // init food search
                tryFindFood();
            } else {
                moveStrategy();
            }
        }

        energy -= energyCostPerMove();
        resetStamina();
    }

    protected int energyToBorder() {
        int distance = (int) pos.distanceSqrt( Point2D.calcClosestBorder( pos ) );
        return (int) (energyCostPerMove() * (distance / (speed * SCALE)));
    }

    protected void tryFindFood() {
        Point<Food>[] nearestFood = getNearestFood();

        if ( nearestFood.length == 0 ) { // random move if cant see food
            int moveDistance = maxDistanceToGo();
            pos = randomMove( pos, moveDistance );
//            pos = new BlobPoint( randomMove( pos, moveDistance ) );
//            setPos(randomMove( pos, moveDistance ));
            stamina -= moveDistance;
        } else { // can see food
            Food   closestFood = closestFood( nearestFood );
            double distance    = pos.distanceSqrt( closestFood.getPos() );

            if ( distance <= stamina ) { // eat food
                pos = closestFood.getPos();
                eat( closestFood, (int) distance );
            } else { // go towards food
                pos     = Point2D.moveTowardsPoint( pos, closestFood.getPos(), getStamina() );
                stamina = 0;
            }
        }
    }

    protected void goHome() {
        Point2D closestBorder = Point2D.calcClosestBorder( pos );

        // cant move out of bounds
        int moveDistance = (int) Math.min( pos.distanceSqrt( closestBorder ), getStamina() );

        pos     = Point2D.moveTowardsPoint( pos, closestBorder, moveDistance );
        stamina = 0;

        if ( pos.isAtHome( closestBorder ) )
            atHome = true;
    }

    protected Point<Food>[] getNearestFood() {
        return FIELD.foodQuadTree.searchWithin( getPos().getX() - getSense() * SCALE,
                                                getPos().getY() - getSense() * SCALE,
                                                getPos().getX() + getSense() * SCALE,
                                                getPos().getY() + getSense() * SCALE );
    }

    protected Food closestFood( Point<Food>[] food ) {
        if ( food.length == 0 )
            return null;

        // TODO check if food is in circle range
        Point  closestFood     = food[0];
        double closestDistance = pos.distanceNoSqrt( food[0].getValue().getPos() );

        double distance = .0;

        for (int i = 0; i < food.length; i++) {
            distance = pos.distanceNoSqrt( food[i].getValue().getPos() );
            if ( distance < closestDistance ) {
                closestFood     = food[i];
                closestDistance = distance;
            }
        }

        return new Food( new FoodPoint( (int) closestFood.getX(), (int) closestFood.getY() ) );
    }

    public int eat( @NotNull Food closestFood, int distance ) {
        energy += closestFood.ENERGY;
        stamina -= distance;
        FIELD.foodQuadTree.remove( closestFood.getPos().getX(), closestFood.getPos().getY() );
        return ++foodCounter;
    }

    public abstract void moveStrategy();

    @Override
    public String toString() {
        return "Blob{" +
               "energy=" + energy +
               ", eaten Food:" + foodCounter +
               ", speed=" + speed +
               ", size=" + size +
               ", sense=" + sense +
               ", pos=" + pos +
               '}';
    }

    public boolean isAtHome() {
        return atHome;
    }

    public void setAtHome( boolean b ) {
        atHome = b;
    }

    public int getStamina() {
        return stamina;
    }

    public int getEnergy() {
        return energy;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSize() {
        return size;
    }

    public double getSense() {
        return sense;
    }

    public int getFoodCounter() {
        return foodCounter;
    }

    public Point2D getPos() {
        return pos;
    }

    private void setPos( Point2D point2D ) {
        pos.setX( point2D.getX() );
        pos.setY( point2D.getY() );
    }

    public void resetEnergy() {
        energy = BASIC_ENERGY;
    }

    public void resetStamina() {
        stamina = (int) (SCALE * speed);
    }

    public void resetFood() {
        foodCounter = 0;
    }
}
