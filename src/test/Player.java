/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Player {


    public static final Color BORDER_COLOR = Color.GREEN.darker().darker();
    public static final Color INNER_COLOR = Color.GREEN;

    private static final int DEF_MOVE_AMOUNT = 5;

    private Rectangle playerBoard;
    private Point boardPosition;
    private int moveAmount;
    private int min;
    private int max;


    /**
     * @param boardPosition
     * @param width
     * @param height
     * @param container
     */
    public Player(Point boardPosition,int width,int height,Rectangle container) {
        this.boardPosition = boardPosition;
        moveAmount = 0;
        playerBoard = makeRectangle(width, height);
        min = container.x + (width / 2);
        max = min + container.width - width;

    }

    /**
     * @param width
     * @param height
     * @return
     */
    private Rectangle makeRectangle(int width,int height){
        Point p = new Point((int)(boardPosition.getX() - (width / 2)),(int)boardPosition.getY());
        return  new Rectangle(p,new Dimension(width,height));
    }

    /**
     * @param b
     * @return
     */
    public boolean impact(Ball b){
        return playerBoard.contains(b.getPosition()) && playerBoard.contains(b.down) ;
    }

    /**
     *
     */
    public void movement(){
        double x = boardPosition.getX() + moveAmount;
        if(x < min || x > max)
            return;
        boardPosition.setLocation(x,boardPosition.getY());
        playerBoard.setLocation(boardPosition.x - (int)playerBoard.getWidth()/2,boardPosition.y);
    }

    /**
     *
     */
    public void moveLeft(){
        moveAmount = -DEF_MOVE_AMOUNT;
    }

    /**
     *
     */
    public void movRight(){
        moveAmount = DEF_MOVE_AMOUNT;
    }

    /**
     *
     */
    public void stop(){
        moveAmount = 0;
    }

    /**
     * @return
     */
    public Shape getPlayerBoard(){
        return  playerBoard;
    }

    /**
     * @param p
     */
    public void moveTo(Point p){
        boardPosition.setLocation(p);
        playerBoard.setLocation(boardPosition.x - (int)playerBoard.getWidth()/2,boardPosition.y);
    }
}
