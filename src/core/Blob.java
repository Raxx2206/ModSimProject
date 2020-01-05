package core;


import lib.quadtree.Point;
import model.Field;
import model.blob.BlobPoint;
import model.food.Food;
import model.food.FoodPoint;
import org.jetbrains.annotations.NotNull;


public abstract class Blob {

    public static final  int    BASIC_ENERGY = 150;
    public static final  double BASIC_SPEED  = 1.;
    public static final  double BASIC_SIZE   = 1.;
    public static final  double BASIC_SENSE  = 1.;
    private final static Field  FIELD        = Field.getInstance();
    private final        int    SCALE        = 10;

    protected boolean atHome = false;
    protected int     stamina;
    protected int     energy;
    protected double  speed;
    protected double  size;
    protected double  sense;
    protected int     foodCounter;
    protected Point2D pos;

    //Basic Constructor
    public Blob( Point2D pos ) {
        energy  = BASIC_ENERGY;
        speed   = BASIC_SPEED;
        size    = BASIC_SIZE;
        sense   = BASIC_SENSE;
        stamina = (int) (SCALE * speed);

        this.pos = pos;
    }

    //Constructor w/ parameters
    public Blob( int energy, double speed, double size, double sense, Point2D pos ) {
        this.energy = energy;
        this.speed  = speed;
        this.size   = size;
        this.sense  = sense;
        this.pos    = pos;
        stamina     = (int) (SCALE * speed);
    }

    private static Point2D randomMove( Point2D currentPos, int moveDistance ) {
        var angle = Math.random() * Math.PI * 2;

        int x = (int) (Math.cos( angle ) * moveDistance);
        int y = (int) (Math.sin( angle ) * moveDistance);

        return new BlobPoint( currentPos.getX() + x, currentPos.getY() + y );
    }

    protected double energyCostPerMove() {
        return (size * size * size) + (speed * speed) + sense;
    }

    //determine if sense extends stamina
    private int maxDistanceToGo() {
        return (int) Math.min( stamina, sense * SCALE );
    }


    //Main move method
    public void move() {
        while (getStamina() > 0) {
            if ( foodCounter == 0 ) {                       // init food search
                tryFindFood();
            } else {
                moveStrategy();                             // strategy implemented in children
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
        Point<Food>[] nearestFood = getNearestFood();                           // Array of closest food sources in sight

        if ( nearestFood.length == 0 ) {                                        // food not in sight --> random move
            int moveDistance = maxDistanceToGo();
            pos = randomMove( pos, moveDistance );
            stamina -= moveDistance;
        } else {                                                                // food in sight --> move towards it
            Food   closestFood = closestFood( nearestFood );                    // fetch closest Food out of List
            double distance    = pos.distanceSqrt( closestFood.getPos() );

            if ( distance <= stamina ) {                                        // when food is in range for pick-up -> move & eat
                pos = closestFood.getPos();
                eat( closestFood, (int) distance );
            } else {                                                            // when food not in range --> move towards its location
                pos     = Point2D.moveTowardsPoint( pos, closestFood.getPos(), getStamina() );
                stamina = 0;
            }
        }
    }

    protected void goHome() {
        Point2D closestBorder = Point2D.calcClosestBorder( pos );               //fetch closest border as point

        // cant move out of bounds
        int moveDistance = (int) Math.min( pos.distanceSqrt( closestBorder ), getStamina() );

        pos     = Point2D.moveTowardsPoint( pos, closestBorder, moveDistance );
        stamina = 0;

        if ( pos.isAtHome( closestBorder ) )
            atHome = true;
    }

    //fetch neared Food in sight
    private Point<Food>[] getNearestFood() {
        return FIELD.foodQuadTree.searchWithin( getPos().getX() - getSense() * SCALE,
                                                getPos().getY() - getSense() * SCALE,
                                                getPos().getX() + getSense() * SCALE,
                                                getPos().getY() + getSense() * SCALE );
    }

    //return closest food obj out of foods in sight
    private Food closestFood( Point<Food>[] food ) {
        if ( food.length == 0 )
            return null;

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

    private int eat( @NotNull Food closestFood, int distance ) {
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
