package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by filippo on 04/09/16.
 *
 */
abstract public class Brick  {

    public static final int DEF_CRACK_DEPTH = 1;
    public static final int DEF_STEPS = 35;


    public static final int UP_IMPACT = 100;
    public static final int DOWN_IMPACT = 200;
    public static final int LEFT_IMPACT = 300;
    public static final int RIGHT_IMPACT = 400;



    public class Crack{

        private static final int CRACK_SECTIONS = 3;
        private static final double JUMP_PROBABILITY = 0.7;

        public static final int LEFT = 10;
        public static final int RIGHT = 20;
        public static final int UP = 30;
        public static final int DOWN = 40;
        public static final int VERTICAL = 100;
        public static final int HORIZONTAL = 200;



        private GeneralPath crack;

        private int crackDepth;
        private int steps;


        /**
         * @param crackDepth
         * @param steps
         * Makes a crack in the brick
         */
        public Crack(int crackDepth, int steps){

            crack = new GeneralPath();
            this.crackDepth = crackDepth;
            this.steps = steps;

        }

        /**
         * @return
         * Returns the shape of the drawn crack
         */
        public GeneralPath draw(){

            return crack;
        }

        /**
         * Resets the cracks
         */
        public void reset(){
            crack.reset();
        }

        /**
         * @param point
         * @param direction
         * Determines the start and end of the crack drawn on the brick
         */
        protected void makeCrack(Point2D point, int direction){
            Rectangle bounds = Brick.this.brickSize.getBounds();

            Point impact = new Point((int)point.getX(),(int)point.getY());
            Point start = new Point();
            Point end = new Point();


            switch(direction){
                case LEFT:
                    start.setLocation(bounds.x + bounds.width, bounds.y);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    Point tmp = makeRandomPoint(start,end,VERTICAL);
                    makeCrack(impact,tmp);

                    break;
                case RIGHT:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,VERTICAL);
                    makeCrack(impact,tmp);

                    break;
                case UP:
                    start.setLocation(bounds.x, bounds.y + bounds.height);
                    end.setLocation(bounds.x + bounds.width, bounds.y + bounds.height);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    makeCrack(impact,tmp);
                    break;
                case DOWN:
                    start.setLocation(bounds.getLocation());
                    end.setLocation(bounds.x + bounds.width, bounds.y);
                    tmp = makeRandomPoint(start,end,HORIZONTAL);
                    makeCrack(impact,tmp);

                    break;

            }
        }

        /**
         * @param start
         * @param end
         * Draws the crack on the brick
         */
        protected void makeCrack(Point start, Point end){

            GeneralPath path = new GeneralPath();


            path.moveTo(start.x,start.y);

            double w = (end.x - start.x) / (double)steps;
            double h = (end.y - start.y) / (double)steps;

            int bound = crackDepth;
            int breakRate  = bound * 5;

            double x,y;

            for(int i = 1; i < steps;i++){

                x = (i * w) + start.x;
                y = (i * h) + start.y + randomInBounds(bound);

                if(inMiddle(i,CRACK_SECTIONS,steps))
                    y += breakCheck(breakRate,JUMP_PROBABILITY);

                path.lineTo(x,y);

            }

            path.lineTo(end.x,end.y);
            crack.append(path,true);
        }

        /**
         * @param bound
         * @return
         * Returns a random point within brick boundaries
         */
        private int randomInBounds(int bound){
            int n = (bound * 2) + 1;
            return rnd.nextInt(n) - bound;
        }

        /**
         * @param i
         * @param steps
         * @param divisions
         * @return
         * Returns true if i is in the middle brick
         */
        private boolean inMiddle(int i,int steps,int divisions){
            int low = (steps / divisions);
            int up = low * (divisions - 1);

            return  (i > low) && (i < up);
        }

        /**
         * @param bound
         * @param probability
         * @return
         * Checks if the brick is broken
         */
        private int breakCheck(int bound,double probability){

            if(rnd.nextDouble() > probability)
                return randomInBounds(bound);
            return  0;

        }

        /**
         * @param from
         * @param to
         * @param direction
         * Returns a random position within the brick for the crack to be made
         * @return
         */
        private Point makeRandomPoint(Point from,Point to, int direction){

            Point out = new Point();
            int pos;

            switch(direction){
                case HORIZONTAL:
                    pos = rnd.nextInt(to.x - from.x) + from.x;
                    out.setLocation(pos,to.y);
                    break;
                case VERTICAL:
                    pos = rnd.nextInt(to.y - from.y) + from.y;
                    out.setLocation(to.x,pos);
                    break;
            }
            return out;
        }

    }

    private static Random rnd;

    private String name;
    Shape brickSize;

    private Color border;
    private Color inner;

    private int fullStrength;
    private int strength;

    private boolean broken;


    /**
     * @param name
     * @param pos
     * @param size
     * @param border
     * @param inner
     * @param strength
     * Creates an initial brick
     */
    public Brick(String name, Point pos,Dimension size,Color border,Color inner,int strength){
        rnd = new Random();
        broken = false;
        this.name = name;
        brickSize = makeBrickSize(pos,size);
        this.border = border;
        this.inner = inner;
        this.fullStrength = this.strength = strength;

    }

    /**
     * @param pos
     * @param size
     * @return
     * Determines the brick size
     */
    protected abstract Shape makeBrickSize(Point pos,Dimension size);

    /**
     * @param point
     * @param dir
     * @return
     * Checks if the brick is broken
     */
    public  boolean realiseImpact(Point2D point , int dir){
        if(broken)
            return false;
        impact();
        return  broken;
    }

    /**
     * @return
     * Used to get the brick by the other classes
     */
    public abstract Shape getBrick();


    /**
     * @return
     * Gets tne border color for the brick
     */
    public Color getBorderColor(){
        return  border;
    }

    /**
     * @return
     * Gets the inner color for the brick
     */
    public Color getInnerColor(){
        return inner;
    }


    /**
     * @param b
     * @return
     * Returns how the ball will collide with the brick
     */
    public final int findImpact(Ball b){
        if(broken)
            return 0;
        int out  = 0;
        if(brickSize.contains(b.right))
            out = LEFT_IMPACT;
        else if(brickSize.contains(b.left))
            out = RIGHT_IMPACT;
        else if(brickSize.contains(b.up))
            out = DOWN_IMPACT;
        else if(brickSize.contains(b.down))
            out = UP_IMPACT;
        return out;
    }

    /**
     * @return
     * Returns 0 if brick is broken
     */
    public final boolean isBroken(){
        return broken;
    }

    /**
     * Repairs the brick
     */
    public void repair() {
        broken = false;
        strength = fullStrength;
    }

    /**
     *Reduces the brick strength until it's broken
     */
    public void impact(){
        strength--;
        broken = (strength == 0);
    }



}
