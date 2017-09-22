package code;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CryptowatchAPIHandler api = new CryptowatchAPIHandler();
	private JPanel panel1;
	private JLabel label1, label2, label3;
	private JLabel label4;

	public GUI(String title, Dimension dim) throws Exception {
		this.setTitle(title);
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		createContents();
		this.setVisible(true);
	}

	private void createContents() {
		panel1 = new JPanel();
		this.setContentPane(panel1);

		panel1.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		label1 = new JLabel("GDAX ETH/USD Price: ");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		panel1.add(label1, c);
		label2 = new JLabel("000.00");
		label2.setFont(label2.getFont().deriveFont((float) 32.0));
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		panel1.add(label2, c);
		label3 = new JLabel("Price updates every 5sec");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		panel1.add(label3, c);
		label4 = new JLabel("© Robin Becher 2017");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		panel1.add(label4, c);

	}

	public void updatePrice() throws MalformedURLException, Exception {
		Double price = api.getCryptowatchPrice(new URL("https://api.cryptowat.ch/markets/gdax/ethusd/price"));
		label2.setText(price.toString());
	}

}
