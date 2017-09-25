package code;

import java.awt.Dimension;

public class Main {

	public static void main(String[] args) throws Exception {

		GUI gui = new GUI(new Dimension(600, 400));

		boolean stop = false;
		while (!stop) {
			gui.updatePrice();
			Thread.sleep(5000);
		}

	}

}
