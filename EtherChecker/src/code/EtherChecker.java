package code;

public class EtherChecker {

    /**
     * Executing the main method spawns an instance of the EtherCheckerGUI and updates it every 5sec.
     *
     *
     * @param args default
     * @throws Exception Throws an Exception if API call is unsuccessful or Thread is interrupted while sleeping
     */
    public static void main(String[] args) throws Exception {
        EtherCheckerGUI gui = new EtherCheckerGUI();

        long timer1 = 0, timer2 = 0, difference = 0;
        boolean stop = false;
		while (!stop) {
            try {
                timer1 = System.currentTimeMillis();
                gui.update();
                timer2 = System.currentTimeMillis();
                difference = timer2 - timer1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (difference < 1000) {
                Thread.sleep(1000 - difference);
            }
        }
    }
}