package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class InfoPage extends JComponent implements MouseListener, MouseMotionListener {

    private static final String PAGE_TITLE = "Info Page";
    private static final String RULES = "Rules:";
    private static final String FIRST_RULE = "1. Press the SPACE Bar to start or pause the game.";
    private static final String SECOND_RULE = "2. Use the A and D keys to move the player paddle.";
    private static final String THIRD_RULE = "3. Escape opens the game menu.";
    private static final String FOURTH_RULE = "4. Use ALT + Shift + F1 to open the debug console.";
    private static final String FIFTH_RULE = "5. Break all bricks to win.";
    private static final String MENU_TEXT = "Menu";
    private static final String START_TEXT = "Start";

    private static final Color BG_COLOR = new Color(0,0,0,0);
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = new Color(20, 170, 100);//green
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    private Rectangle infoLayout;
    private Rectangle menuButton;
    private Rectangle startButton;


    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font titleFont;
    private Font ruleFont;
    private Font numberRulesFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean menuClicked;
    private boolean startClicked;


    /**
     * @param owner
     * @param area
     * Creates the info page and it's buttons
     */
    public InfoPage(GameFrame owner,Dimension area){

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        infoLayout = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(150, 25);
        menuButton = new Rectangle(btnDim);
        startButton = new Rectangle(btnDim);

        borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

        titleFont = new Font("Noto Mono",Font.BOLD,50);
        ruleFont = new Font("Noto Mono",Font.BOLD,35);
        numberRulesFont = new Font("Noto Mono",Font.PLAIN,24);
        buttonFont = new Font("Noto Mono",Font.PLAIN,startButton.height-2);

    }


    /**
     * @param g
     * Paints the info page over the info page board
     */
    public void paint(Graphics g){
        drawInfoPage((Graphics2D)g);
    }


    /**
     * @param g2d
     * Draws the info page's text and buttons
     */
    public void drawInfoPage(Graphics2D g2d){

        colorMenu(g2d);

        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = infoLayout.getX();
        double y = infoLayout.getY();

        g2d.translate(x,y);

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * @param g2d
     * Paints colors onto thee page and background
     */
    private void colorMenu(Graphics2D g2d){
        Color prev = g2d.getColor();

        Image backgroundimage = Toolkit.getDefaultToolkit().getImage("Brick.jpg");
        g2d.drawImage(backgroundimage, 0, 0, this);

        g2d.setColor(BG_COLOR);
        g2d.fill(infoLayout);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStoke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(infoLayout);

        g2d.setStroke(borderStoke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(infoLayout);

        g2d.setStroke(tmp);

        g2d.setColor(prev);
    }

    /**
     * @param g2d
     * Draws the text
     */
    private void drawText(Graphics2D g2d){

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D pageTitleRect = titleFont.getStringBounds(PAGE_TITLE,frc);
        Rectangle2D rulesRect = ruleFont.getStringBounds(RULES,frc);
        Rectangle2D firstRuleRect = numberRulesFont.getStringBounds(FIRST_RULE,frc);
        Rectangle2D secondRuleRect = numberRulesFont.getStringBounds(SECOND_RULE,frc);
        Rectangle2D thirdRuleRect = numberRulesFont.getStringBounds(THIRD_RULE,frc);
        Rectangle2D fourthRuleRect = numberRulesFont.getStringBounds(FOURTH_RULE,frc);
        Rectangle2D fifthRuleRect = numberRulesFont.getStringBounds(FIFTH_RULE,frc);

        int sX,sY;

        sX = (int)(infoLayout.getWidth() - pageTitleRect.getWidth()) / 2;
        sY = (int)(infoLayout.getHeight() / 7);

        g2d.setFont(titleFont);
        g2d.drawString(PAGE_TITLE,sX,sY);

        sX = (int)(infoLayout.getWidth() - rulesRect.getWidth()) / 2;
        sY += (int) rulesRect.getHeight() * 2;//add 100% of String height between the two strings

        g2d.setFont(ruleFont);
        g2d.drawString(RULES,sX,sY);

        sX = (int)(infoLayout.getWidth() - firstRuleRect.getWidth()) / 2;
        sY += (int) firstRuleRect.getHeight() * 2;

        g2d.setFont(numberRulesFont);
        g2d.drawString(FIRST_RULE,sX,sY);

        sX = (int)(infoLayout.getWidth() - secondRuleRect.getWidth()) / 2;
        sY += (int) secondRuleRect.getHeight() * 1.1;

        g2d.setFont(numberRulesFont);
        g2d.drawString(SECOND_RULE,sX,sY);

        sX = (int)(infoLayout.getWidth() - thirdRuleRect.getWidth()) / 2;
        sY += (int) thirdRuleRect.getHeight() * 1.1;

        g2d.setFont(numberRulesFont);
        g2d.drawString(THIRD_RULE,sX,sY);

        sX = (int)(infoLayout.getWidth() - fourthRuleRect.getWidth()) / 2;
        sY += (int) fourthRuleRect.getHeight() * 1.1;

        g2d.setFont(numberRulesFont);
        g2d.drawString(FOURTH_RULE,sX,sY);

        sX = (int)(infoLayout.getWidth() - fifthRuleRect.getWidth()) / 2;
        sY += (int) fifthRuleRect.getHeight() * 1.1;

        g2d.setFont(numberRulesFont);
        g2d.drawString(FIFTH_RULE,sX,sY);


    }

    /**
     * @param g2d
     * Draws the buttons
     */
    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);
        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = 400;
        int y =(int) ((infoLayout.height - startButton.height) * 0.9);

        startButton.setLocation(x,y);

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);


        if(startClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
        }

        x = 50;
        y = startButton.y;



        menuButton.setLocation(x,y);


        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        if(menuClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT,x,y);
        }

    }

    /**
     * @param mouseEvent
     * Starts the game if the Start button was pressed
     * Returns the the home menu if the Menu button was pressed
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            owner.enableGameBoard();

        }
        else if (menuButton.contains(p)){
            owner.enableHomeMenu();
        }
    }

    /**
     * @param mouseEvent
     * Changes the color of the mouse when pressed
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        }
        else if(menuButton.contains(p)){
            menuClicked = true;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
    }

    /**
     * @param mouseEvent
     * Returns the color of the buttons back to normal when mouse is released
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        }
        else if(menuClicked){
            menuClicked = false;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    /**
     * @param mouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * @param mouseEvent
     * Changes the cursor to a hand when hovering over the buttons
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p) || menuButton.contains(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());

    }
}
