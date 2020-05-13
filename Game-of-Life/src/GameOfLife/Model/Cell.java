package GameOfLife.Model;

import java.awt.*;

/**
 * Created by Rohan Ghiya on 4/20/20.
 *
 * This class will eventually be Abstract.
 * It represent the most basic form of cell.
 * All other kinds of cells will conform
 * to this class' interface and structure.
 *
 */

public class Cell {
    private boolean living;
    private int numOfNeighbors;
    private final Color myBaseColor = new Color(0, 255, 0);
    private Color myCurrentColor;
    private int[] survivalConditions = {2,3};
    private int[] birthConditions = {3};

    /**
     * All instances of Model.Cell are stillborn.
     */
    public Cell() {
        living = false;
        numOfNeighbors = 0;
        myCurrentColor = myBaseColor;
    }

    public boolean isAlive() {
        return living;
    }

    public void kill() {
        living = false;
    }

    public void revive() {
        living = true;
    }

    public void setNumOfNeighbors(int num) {
        numOfNeighbors = num;
    }

    public int getNumOfNeighbors() {
        return numOfNeighbors;
    }

    public Color getMyColor() {
        return myCurrentColor;
    }

    public void updateCurrentColor() {
        myCurrentColor = myBaseColor;
        for(int i = 0; i < numOfNeighbors/2; i++) {
            myCurrentColor = myCurrentColor.darker();
        }
    }

    public void advanceOnce() {
        if (living) {
            living = false;
            for(int condition : survivalConditions) {
                if (numOfNeighbors == condition) {
                    living = true;
                    break;
                }
            }
        } else {
            for(int condition : birthConditions) {
                if (numOfNeighbors == condition) {
                    living = true;
                    break;
                }
            }
        }
    }
}

