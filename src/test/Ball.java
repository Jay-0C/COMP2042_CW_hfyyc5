package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * Created by filippo on 04/09/16.
 *
 */
abstract public class Ball {

    private Shape ballSize;

    private Point2D position;

    Point2D up;
    Point2D down;
    Point2D left;
    Point2D right;

    private Color borderColor;
    private Color innerColor;

    public int speedX = 0;
    public int speedY = 0;

    /**
     * @param position
     * @param ballLength
     * @param ballWidth
     * @param innerColor
     * @param borderColor
     * Declares up, down, left, right and ball colors
     */
    public Ball(Point2D position,int ballLength,int ballWidth,Color innerColor,Color borderColor){
        this.position = position;

        up = new Point2D.Double();
        down = new Point2D.Double();
        left = new Point2D.Double();
        right = new Point2D.Double();

        ballSize = makeBall(position,ballLength,ballWidth);
        this.borderColor = borderColor;
        this.innerColor  = innerColor;
    }

    /**
     * @param position
     * @param ballLength
     * @param ballWidth
     * @return
     * Creates an abstract class for use in RubberBall
     */
    protected abstract Shape makeBall(Point2D position,int ballLength,int ballWidth);

    /**
     * Ball movement
     */
    public void movement(){
        RectangularShape tmp = (RectangularShape) ballSize;
        position.setLocation((position.getX() + speedX),(position.getY() + speedY));
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((position.getX() -(w / 2)),(position.getY() - (h / 2)),w,h);
        setPoints(w,h);


        ballSize = tmp;
    }

    /**
     * @param x
     * @param y
     * Sets ball speed
     */
    public void setSpeed(int x,int y){
        speedX = x;
        speedY = y;
    }


    /**
     * Reverses horizontal vector of the ball
     */
    public void reverseX(){
        speedX *= -1;
    }

    /**
     * Reverses vertical vector of the ball
     */
    public void reverseY(){
        speedY *= -1;
    }

    /**
     * @return
     * Gets border color for ball
     */
    public Color getBorderColor(){
        return borderColor;
    }

    /**
     * @return
     * Gets inner color for ball
     */
    public Color getInnerColor(){
        return innerColor;
    }

    /**
     * @return
     * Gets initial position for ball
     */
    public Point2D getPosition(){
        return position;
    }

    /**
     * @return
     * Gets size of ball
     */
    public Shape getBallSize(){
        return ballSize;
    }

    /**
     * @param p
     * Moves ball back to starting position
     */
    public void moveTo(Point p){
        position.setLocation(p);

        RectangularShape tmp = (RectangularShape) ballSize;
        double w = tmp.getWidth();
        double h = tmp.getHeight();

        tmp.setFrame((position.getX() -(w / 2)),(position.getY() - (h / 2)),w,h);
        ballSize = tmp;
    }

    /**
     * @param width
     * @param height
     * Checks where the ball moves next
     */
    private void setPoints(double width,double height){
        up.setLocation(position.getX(),position.getY()-(height / 2));
        down.setLocation(position.getX(),position.getY()+(height / 2));

        left.setLocation(position.getX()-(width / 2),position.getY());
        right.setLocation(position.getX()+(width / 2),position.getY());
    }

    /**
     * @return
     * Gets the ball's horizontal speed
     */
    public int getSpeedX(){
        return speedX;
    }

    /**
     * @return
     * Gats the ball's vertical speed
     */
    public int getSpeedY(){
        return speedY;
    }


}
