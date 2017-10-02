package code;

@SuppressWarnings("ConstantConditions")
class EtherChecker {

    /**
     * Executing the main method spawns an instance of the EtherCheckerGUI and updates it every 1sec.
     *
     * @param args default
     * @throws Exception Throws an Exception if API call is unsuccessful or Thread is interrupted while sleeping
     */
    public static void main(String[] args) throws Exception {
        EtherCheckerGUI gui = new EtherCheckerGUI();

        long timer1, timer2, difference = 0;
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