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
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;


public class Wall {

    private static final int LEVELS_COUNT = 8;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;

    private Random rnd;
    private Rectangle area;

    Brick[] bricks;
    Ball ball;
    Player player;

    private Brick[][] levels;
    private int level;
    public int currentscore = 0;
    public int highscore;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;


    /**
     * @param drawArea
     * @param brickCount
     * @param lineCount
     * @param brickDimensionRatio
     * @param ballPos
     * Creates ball and player paddle on their initial positions, and sets a random ball speed
     */
    public Wall(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos) {

        this.startPoint = new Point(ballPos);

        levels = makeLevels(drawArea, brickCount, lineCount, brickDimensionRatio);
        level = 0;

        ballCount = 3;
        ballLost = false;

        rnd = new Random();

        makeBall(ballPos);
        int speedX, speedY;
        do {
            speedX = rnd.nextInt(5) - 2;
        } while (speedX == 0);
        do {
            speedY = -rnd.nextInt(3);
        } while (speedY == 0);

        ball.setSpeed(speedX, speedY);

        player = new Player((Point) ballPos.clone(), 150, 10, drawArea);

        area = drawArea;

        try {
            File hstxt = new File("HighScore.txt");
            Scanner read = new Scanner(hstxt);
            while (read.hasNextLine()) {
                String data = read.nextLine();
                highscore = Integer.parseInt(data);
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * @param drawArea
     * @param brickCnt
     * @param lineCnt
     * @param brickSizeRatio
     * @param type
     * @return
     * Generates the level's bricks when there is only one type of brick
     */
    private Brick[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,type);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = new ClayBrick(p,brickSize);
        }
        return tmp;

    }

    /**
     * @param drawArea
     * @param brickCnt
     * @param lineCnt
     * @param brickSizeRatio
     * @param typeA
     * @param typeB
     * @return
     * Generates the level's bricks when there are 2 kinds of bricks
     */
    private Brick[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / 2 - 1;
        int centerRight = brickOnLine / 2 + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        Brick[] tmp  = new Brick[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ?  makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    /**
     * @param ballPos
     * Creates the ball in the starting position
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBall(ballPos);
    }

    /**
     * @param drawArea
     * @param brickCount
     * @param lineCount
     * @param brickDimensionRatio
     * @return
     * Generates the levels
     */
    private Brick[][] makeLevels(Rectangle drawArea,int brickCount,int lineCount,double brickDimensionRatio){
        Brick[][] tmp = new Brick[LEVELS_COUNT][];
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CEMENT);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[4] = makeSingleTypeLevel(drawArea,50,5,brickDimensionRatio,CLAY);
        tmp[5] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        tmp[6] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL);
        tmp[7] = makeSingleTypeLevel(drawArea,40,4,brickDimensionRatio,CEMENT);
        return tmp;
    }

    /**
     * Calls the methods that move the player and the ball
     */
    public void movement(){
        player.movement();
        ball.movement();
    }

    /**
     * Reverses the vertical vector of the ball when it collides with the player paddle
     * Reverses the vector of the ball when it collides with a brick, depending on the angle of impact
     */
    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY();
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            brickCount--;
            currentscore++;
            try {
                FileWriter whstxt = new FileWriter("HighScore.txt");
                if(currentscore>highscore) {
                    highscore = currentscore;
                    whstxt.write(""+highscore);
                    whstxt.close();
                    System.out.println("Successfully wrote to the file.");
                }
                else{
                    whstxt.write(""+highscore);
                    whstxt.close();
                    System.out.println("Successfully wrote to the file.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        else if(impactBorder()) {
            ball.reverseX();
        }
        else if(ball.getPosition().getY() < area.getY()){
            ball.reverseY();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){
            ballCount--;
            ballLost = true;
        }
    }

    /**
     * @return
     * Changes the direction of the ball when it collides with a brick, depending on the angle of impact
     */
    private boolean impactWall(){
        for(Brick b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case Brick.UP_IMPACT:
                    ball.reverseY();
                    return b.realiseImpact(ball.down, Brick.Crack.UP);
                case Brick.DOWN_IMPACT:
                    ball.reverseY();
                    return b.realiseImpact(ball.up,Brick.Crack.DOWN);

                //Horizontal Impact
                case Brick.LEFT_IMPACT:
                    ball.reverseX();
                    return b.realiseImpact(ball.right,Brick.Crack.RIGHT);
                case Brick.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.realiseImpact(ball.left,Brick.Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * @return
     * Changes the vector of the ball when it collides with the border of the game board
     */
    private boolean impactBorder(){
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /**
     * @return
     * Returns the number of remaining bricks
     */
    public int getBrickCount(){
        return brickCount;
    }

    /**
     * @return
     * Returns the number of remaining balls
     */
    public int getBallCount(){
        return ballCount;
    }

    /**
     * @return
     * Returns true if ball is lost, false if it is not lost
     */
    public boolean isBallLost(){
        return ballLost;
    }

    /**
     * Resets the ball and player paddle to their starting positions and sets a random ball speed
     */
    public void ballReset(){
        player.moveTo(startPoint);
        ball.moveTo(startPoint);
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);
        ballLost = false;
    }

    /**
     * Repairs all bricks
     */
    public void wallReset(){
        for(Brick b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    /**
     * @return
     * Returns true if there are no balls remaining, false otherwise
     */
    public boolean ballEnd(){
        return ballCount == 0;
    }

    /**
     * @return
     * Returns true if no more bricks remain, false otherwise
     */
    public boolean isDone(){
        return brickCount == 0;
    }

    /**
     * Changes the bricks to the design of the next level and resets brickCount
     */
    public void nextLevel(){
        bricks = levels[level++];
        this.brickCount = bricks.length;
    }

    /**
     * @return
     * Returns true if there are levels remaining, else returns false
     */
    public boolean hasLevel(){
        return level < levels.length;
    }

    /**
     * @param s
     * Sets the ball's horizontal speed
     */
    public void setBallXSpeed(int s){
        ball.speedX = s;
    }

    /**
     * @param s
     * Sets the ball's vertical speed
     */
    public void setBallYSpeed(int s){
        ball.speedY = s;
    }

    /**
     *Resets the number of balls
     */
    public void resetBallCount(){
        ballCount = 3;
    }

    /**
     * @param point
     * @param size
     * @param type
     * @return
     * returns to the corresponding brick
     */
    private Brick makeBrick(Point point, Dimension size, int type){
        Brick out;
        switch(type){
            case CLAY:
                out = new ClayBrick(point,size);
                break;
            case STEEL:
                out = new SteelBrick(point,size);
                break;
            case CEMENT:
                out = new CementBrick(point, size);
                break;
            default:
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
        return  out;
    }

}
