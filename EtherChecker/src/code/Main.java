package code;

import java.awt.Dimension;

public class Main {

	private static boolean stop = false;

	public static void main(String[] args) throws Exception {

		GUI gui = new GUI("Current ETHUSD Price on GDAX - via cryptowat.ch", new Dimension(600, 400));

		while (stop == false) {
			gui.updatePrice();
			Thread.sleep(5000);
		}

	}

}
