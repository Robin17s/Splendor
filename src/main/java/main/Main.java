package main;

import gui.ApplicationStart;

/**
 * Main entrypoint for Splendor's GUI Application.
 * <p>
 * This class contains one single method, being {@link #main(String[])}, solely responsible for launching the GUI components of the game.
 */
public class Main {
    /**
     * Responsible for starting the GUI Section of Splendor. This method will call the GUI main method.
     * @param args The JVM startup args
     */
    public static void main(String[] args) { ApplicationStart.main(args); }
}