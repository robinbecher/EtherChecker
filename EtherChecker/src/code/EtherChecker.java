package code;

public class EtherChecker {

    /**
     * Executing the main method spawns an instance of the EtherCheckerGUI and updates it every 5sec.
     *
     * TODO write logs to log frame
     *
     * @param args default
     * @throws Exception Throws an Exception if API call is unsuccessful or Thread is interrupted while sleeping
     */
    public static void main(String[] args) throws Exception {
        EtherCheckerGUI gui = new EtherCheckerGUI();

		boolean stop = false;
		while (!stop) {
            try {
                gui.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
        }
    }
}
