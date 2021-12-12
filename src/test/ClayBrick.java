package test;

import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;


/**
 * Created by filippo on 04/09/16.
 *
 */
public class ClayBrick extends Brick {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = new Color(178, 34, 34).darker();
    private static final Color DEF_BORDER = Color.GRAY;
    private static final int CLAY_STRENGTH = 1;


    /**
     * @param point
     * @param size
     * Creates the initial clay brick
     */
    public ClayBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH);
    }

    /**
     * @param pos
     * @param size
     * @return
     * Determines the size of the clay brick
     */
    @Override
    protected Shape makeBrickSize(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * @return
     * Gets clay brick shape
     */
    @Override
    public Shape getBrick() {
        return super.brickSize;
    }


}
