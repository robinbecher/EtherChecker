package code;

import java.awt.Dimension;

public class Main {

    /**
     * Executing the main method spawns an instance of the EtherCheckerGUI and updates it every 5sec.
     *
     * @param args default
     * @throws Exception Throws an Exception if API call is unsuccessful or Thread is interrupted while sleeping
     */
    public static void main(String[] args) throws Exception {
        EtherCheckerGUI gui = new EtherCheckerGUI();

		boolean stop = false;
		while (!stop) {
            gui.update();
            Thread.sleep(5000);
        }
    }
}
