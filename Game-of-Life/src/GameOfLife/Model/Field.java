package GameOfLife.Model;

import java.util.Random;
import java.util.ArrayList;

/**
 * Created by Rohan Ghiya on 4/18/20.
 *
 * This class is the data structure for the application.
 * It holds all the cells (of any kind) as private
 * variable members, and will define all work functions.
 *
 */

public class Field {

    private int fieldHeight;
    private int fieldWidth;
    private int generationNum;
    private Random rnd = new Random();
    private ArrayList<ArrayList<Cell>> currentStep, nextStep;

    public Field() {
        fieldWidth = 100;
        fieldHeight = (fieldWidth/16)*9;
        generationNum = 0;

        currentStep = new ArrayList<ArrayList<Cell>>(fieldHeight);

        for(int h = 0; h < fieldHeight; h++) {
            currentStep.add(h, new ArrayList<Cell>(fieldWidth));
        }

        populateGrid();
    }

    public Field(int inputHeight, int inputWidth) {
        fieldHeight = inputHeight;
        fieldWidth = inputWidth;
        generationNum = 0;

        currentStep = new ArrayList<ArrayList<Cell>>(fieldHeight);

        for(int h = 0; h < fieldHeight; h++) {
            currentStep.add(h, new ArrayList<Cell>(fieldWidth));
        }

        populateGrid();
//        nextStep = new ArrayList<ArrayList<Cell>>(currentStep);
    }

    /**
     * This method will serve as an initializer for
     * the first generation of the cell culture.
     * For now it is merely randomized. In the future
     * the user should be able to input the initial
     * state of generation 0.
     */
    public void setInitialGeneration() {
        clearGrid();

        int numOfAttempts = rnd.nextInt((fieldHeight*fieldWidth)/2);
        for(int i = 0; i < numOfAttempts; i++) {
            if(rnd.nextBoolean()) {
                Cell currentCell = getCellAt(rnd.nextInt(fieldHeight), rnd.nextInt(fieldWidth));
                if(!currentCell.isAlive()) {
                    currentCell.revive();
                }
            }
        }

    }

    /**
     * The Game of Life rules:
     * Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
     * Any live cell with more than three live neighbours dies, as if by overcrowding.
     * Any live cell with two or three live neighbours lives on to the next generation.
     * Any dead cell with exactly three live neighbours becomes a live cell.
     *
     * Update all the cells so they will be aware of their neighbors.
     */
    public void nextGeneration() {

//        nextStep = new ArrayList<ArrayList<Cell>>(currentStep);
        generationNum++;

        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < currentStep.get(h).size(); w++) {
                getCellAt(h, w).advanceOnce();
//                nextStep.get(h).get(w).setNumOfNeighbors(countNeighbors(h, w));
//                nextStep.get(h).get(w).advanceOnce();
            }
        }
//        currentStep = new ArrayList<ArrayList<Cell>>(nextStep);
    }


    /**
     * In order to follow the four rules of the Game of Life,
     * all cells must be aware of their neighbors before any
     * work is done on the map.
     */
    public void updateAllCells() {
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < currentStep.get(h).size(); w++) {
                getCellAt(h, w).setNumOfNeighbors(countNeighbors(h, w));
            }
        }
    }

    public int getHeight() { return fieldHeight; }

    public int getWidth() { return fieldWidth; }

    public void clearGrid() {
        generationNum = 0;
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < fieldWidth; w++) {
                getCellAt(h, w).kill();

            }
        }
    }

    public Cell getCellAt(int h, int w) {
        return currentStep.get(h).get(w);
    }

    public boolean getCellStateAt(int h, int w) { return getCellAt(h, w).isAlive(); }

    public int getGenerationNum() { return generationNum; }

    /**
     * Populates the currentStep with dead cells.
     */
    private void populateGrid() {
        for (int h = 0; h < currentStep.size(); h++) {
            for (int w = 0; w < fieldWidth; w++) {
                currentStep.get(h).add(w, new Cell());
            }
        }
    }

    /**
     * Counts the number of neighbors surrounding a cell.
     * @param height - Y location on the currentStep grid.
     * @param width - X location on the currentStep grid.
     * @return 4 or less.
     */
    private int countNeighbors(int height, int width) {
        int neighborCount = 0;

        for(int i = -1; i < 2; i ++) {
            for(int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) {
                    //do nothing.
                } else {
                    if ((height + i >= 0 && height + i < fieldHeight) &&
                            (width + j >= 0 && width + j < fieldWidth)) {
                        if (getCellAt(height + i, width + j).isAlive()) {
                            neighborCount++;
                        }
                    }
                }
            }
        }
        return neighborCount;
    }

}
