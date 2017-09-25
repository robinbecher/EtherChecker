package code;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.*;

class GUI extends JFrame implements ItemListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final CryptowatchAPIHandler api = new CryptowatchAPIHandler();
    private JLabel label2;
    private JCheckBox checkBox1;

    GUI(Dimension dim) {
        this.setTitle("Current ETHUSD Price on GDAX - via cryptowat.ch");
        this.setSize(dim);
        this.setPreferredSize(dim);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createContents();
        this.setVisible(true);
        this.setAlwaysOnTop(true);
    }

    private void createContents() {
        JPanel panel1 = new JPanel();
        this.setContentPane(panel1);

        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel label1 = new JLabel("GDAX ETH/USD Price: ");
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
        JLabel label3 = new JLabel("Price updated every 5sec");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        panel1.add(label3, c);
        JLabel label4 = new JLabel("© Robin Becher 2017");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        panel1.add(label4, c);
        checkBox1 = new JCheckBox("Always on top");
        checkBox1.setSelected(true);
        checkBox1.addItemListener(this);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        panel1.add(checkBox1, c);

    }

    void updatePrice() throws Exception {
        Double price = api.getCryptowatchPrice(new URL("https://api.cryptowat.ch/markets/gdax/ethusd/price"));
        label2.setText(price.toString());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        Object source = e.getItemSelectable();

        if (source == checkBox1) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                this.setAlwaysOnTop(false);
            } else {
                this.setAlwaysOnTop(true);
            }

        }
    }

}
