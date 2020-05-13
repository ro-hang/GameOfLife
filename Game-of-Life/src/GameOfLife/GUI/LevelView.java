package GameOfLife.GUI;

import GameOfLife.Model.Field;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

/**
 * Created by Rohan Ghiya on 4/20/20.
 *
 * This class is the graphical engine for grid.
 * It is responsible to draw the cells and grid
 * by taking into account a scaling factor.
 *
 * It is also the even listener for all
 * mouse activities by the user.
 *
 */

public class LevelView extends JPanel implements MouseMotionListener, MouseListener {

    private final int GRID_SCALING_FACTOR = 8; //Change cell size here.
    private int levelHeight;
    private int levelWidth;
    private Dimension levelSize;
    private Field currentLevel;
    private boolean gridGraphics = false;
    private boolean drawMode = true;
    private boolean mouseIsPressed = false;
    private Point lastChangedByUser;



    public LevelView(Field currentLevel) {

        //Set the panel's size according to the
        //scaling factor
        this.currentLevel = currentLevel;
        levelHeight = this.currentLevel.getHeight();
        levelWidth = this.currentLevel.getWidth();
        setSize(levelWidth * GRID_SCALING_FACTOR,
                levelHeight * GRID_SCALING_FACTOR);
        levelSize = getSize();
        lastChangedByUser = new Point(getSize().width, getSize().height);

        addMouseMotionListener(this);
        addMouseListener(this);

        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //The current position of the canvas's pen.
        int hPosition = 0;
        int wPosition = 0;

        for(int h = 0; h < levelHeight; h++) {
            for(int w = 0; w < levelWidth; w++) {
                if (currentLevel.getCellStateAt(h, w)) {
                    //if the cell is alive, paint a green square
                    g.setColor(currentLevel.getCellAt(h, w).getMyColor());
                }
                else {
                    g.setColor(Color.WHITE);   //if the cell is dead, paint a white square
                }

                //Paint a square with the given color.
                g.fillRect(wPosition,
                        hPosition,
                        wPosition + GRID_SCALING_FACTOR,
                        hPosition + GRID_SCALING_FACTOR);

                //Advance the canvas pen right one scaling factor position.
                wPosition += GRID_SCALING_FACTOR;
            }
            //Advance the canvas pen down one scaling factor position.
            hPosition += GRID_SCALING_FACTOR;
            wPosition = 0;  //Put the canvas pen back to the left.
        }

        if(gridGraphics)    //if grid wanted, draw the grid.
            drawGrid(g);

    }

    /**
     * Advances the current graphical view of the
     * field one generation into the future, and
     * repaint the canvas.
     */
    public void nextGeneration() {
        currentLevel.updateAllCells();
        currentLevel.nextGeneration();
        repaint();
    }

    /**
     * Flip the grid state.
     */
    public void flipGrid() {
        gridGraphics = !gridGraphics;
        repaint();  //in case the user chooses to turn on the grid while paused.
    }

    public void toggleDrawMode() { drawMode = !drawMode; }

    /**
     * Set a new initial state and update the graphics.
     */
    public void reloadLevel() {
        currentLevel.setInitialGeneration();
        repaint();
    }

    /**
     * Kill all cells and update the graphics.
     */
    public void clearLevel () {
        currentLevel.clearGrid();
        repaint();
    }

    /**
     * @return the current iteration/generation.
     */
    public int getGenerationNum() { return currentLevel.getGenerationNum(); }

    public void drawCellAt(int y, int x) {
        if(currentLevel.getCellStateAt(y, x)) {
            currentLevel.getCellAt(y, x).kill();
        } else {
            currentLevel.getCellAt(y, x).revive();
        }
    }

    /**
     * Draws straight lines from one end of the canvas to the other
     * with consideration of the scaling factor.
     * @param g2 the graphic element used in the paintComponent() method.
     */
    private void drawGrid(Graphics g2) {
        g2.setColor(Color.GRAY);

        for(int w = GRID_SCALING_FACTOR; w < getSize().getWidth();
            w += GRID_SCALING_FACTOR) {
            g2.drawLine(w, 0, w, (int)getSize().getHeight()-1);
        }

        for(int h = GRID_SCALING_FACTOR; h < getSize().getHeight();
            h += GRID_SCALING_FACTOR) {

            g2.drawLine(0, h, (int)getSize().getWidth()-1, h);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseIsPressed = true;


        if(drawMode) {
            if((e.getX() >= 0 && e.getX() <= getSize().width-1) &&
                    (e.getY() >= 0 && e.getY() <= getSize().height-1)) {
                int x = e.getX()/GRID_SCALING_FACTOR;
                int y = e.getY()/GRID_SCALING_FACTOR;
                Point currentPoint = new Point(x, y);

                if(!currentPoint.equals(lastChangedByUser)) {
                    drawCellAt(y, x);
                    repaint();
                }

                lastChangedByUser = currentPoint;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseIsPressed = false;
        lastChangedByUser = this.getParent().getLocationOnScreen();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(drawMode) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(drawMode) {
            mousePressed(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
