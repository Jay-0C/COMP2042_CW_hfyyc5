package test;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


public class CementBrick extends Brick {


    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = new Color(147, 147, 147);
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private Crack crack;
    private Shape brickSize;


    /**
     * @param point
     * @param size
     * Creates the initial cement brick
     */
    public CementBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH);
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickSize = super.brickSize;
    }

    /**
     * @param pos
     * @param size
     * @return
     * Determines the size of the cement brick
     */
    @Override
    protected Shape makeBrickSize(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * @param point
     * @param dir
     * @return
     * Checks if the cement brick is broken
     */
    @Override
    public boolean realiseImpact(Point2D point, int dir) {
        if(super.isBroken())
            return false;
        super.impact();
        if(!super.isBroken()){
            crack.makeCrack(point,dir);
            updateBrick();
            return false;
        }
        return true;
    }


    /**
     * @return
     * Gets cement brick shape
     */
    @Override
    public Shape getBrick() {
        return brickSize;
    }

    /**
     * update brick with brick shape
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.draw();
            gp.append(super.brickSize,false);
            brickSize = gp;
        }
    }

    /**
     * Repairs the cement brick
     */
    public void repair(){
        super.repair();
        crack.reset();
        brickSize = super.brickSize;
    }
}
