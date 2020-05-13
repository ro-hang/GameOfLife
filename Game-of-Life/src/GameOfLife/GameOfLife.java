package GameOfLife;

import GameOfLife.GUI.MainView;

/**
 * Created by Rohan Ghiya on 4/20/20 for CSE355 Project
 */
public class GameOfLife {

    //constructor
    public GameOfLife() {

    }

    //run the GUI
    public void runGUI() {
        MainView mainView = new MainView();
        mainView.run();
    }

    //main
    public static void main(String[] args) {
        GameOfLife init = new GameOfLife();
        init.runGUI();
    }
}
