package model;

import core.Point2D;
import lib.quadtree.QuadTree;
import model.food.Food;
import model.food.FoodPoint;

import java.util.Random;

public class Field {
    private static Random         random         = new Random();
    public final   int            MAX_X;
    public final   int            MAX_Y;
    public final   double         DEAD_ZONE_FOOD = 0.1;
    public         QuadTree<Food> foodQuadTree;

    private Field() {
        MAX_X = 500;
        MAX_Y = 500;

        foodQuadTree = new QuadTree<Food>( 0, 0, MAX_X, MAX_Y );
    }

    public static Field getInstance() {
        return Inner.field;
    }

    public void generateRandomFood( int amount ) {
        Food food;

        for (int i = 0; i < amount; i++) {
            Point2D pos;

            do {
                pos = FoodPoint.randomPoint();
            } while (foodQuadTree.contains( pos.getX(), pos.getY() ));

            food = new Food( pos );
            foodQuadTree.set( pos.getX(), pos.getY(), food );
        }
    }

    private static class Inner {
        private static Field field = new Field();
    }
}
