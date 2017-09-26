package code;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.URL;

public class EtherCheckerGUI {
    private JPanel priceDisplayPanel;
    private JPanel selectionInfoPanel;
    private JPanel generalInfoPanel;
    private JCheckBox alwaysOnTopCheckBox;
    private JPanel topLevelPanel;
    private JLabel priceLabel;
    private JPanel listPanel;
    private JList exchangesList;
    private JList tradingPairList;
    private JLabel exchangeLabel;
    private JLabel marketLabel;
    private JProgressBar progressBar;
    private static final CryptowatchAPIHandler api = new CryptowatchAPIHandler();
    private Exchange currentExchange = Exchange.GDAX;
    private TradingPair currentTradingPair = TradingPair.ETHUSD;

    /**
     * Creating an Object of EtherCheckerGUI spawns a frame in the center of the screen,
     * fills it with components and displays the current ETHUSD price on GDAX. The frame can be set to be always on top
     * by using the alwaysOnTopCheckBox. "Always on top" is the default setting.
     */
    EtherCheckerGUI() {
        JFrame frame = new JFrame("EtherCheckerGUI");
        frame.setContentPane(this.topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
        frame.setVisible(true);

        alwaysOnTopCheckBox.addItemListener(e -> {
            Object source = e.getItemSelectable();
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                frame.setAlwaysOnTop(false);
            } else {
                frame.setAlwaysOnTop(true);
            }
        });


        exchangesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (exchangesList.isSelectedIndex(0)) {
                    currentExchange = Exchange.GDAX;
                } else if (exchangesList.isSelectedIndex(1)) {
                    currentExchange = Exchange.KRAKEN;
                } else if (exchangesList.isSelectedIndex(2)) {
                    currentExchange = Exchange.BITFINEX;
                } else {
                    System.out.println("Exchange Error");
                }
                System.out.println(currentExchange.toString());
                try {
                    update();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Fetches the current ETHUSD price on GDAX by calling the getCryptoWatchPrice() method of the CryptoWatchAPIHandler
     * class.
     *
     * @throws Exception update() can throw an Exception if the API call fails.
     */
    void update() throws Exception {
        System.out.println("tick");

        URL url = new URL(determineURL(currentExchange, currentTradingPair));

        PriceResponse priceResponse;

        progressBar.setIndeterminate(true);
        priceResponse = api.getCryptowatchPrice(url);
        priceLabel.setText(priceResponse.result.price.toString());
        progressBar.setIndeterminate(false);

        if (currentExchange == Exchange.GDAX) {
            exchangeLabel.setText("GDAX");
        } else if (currentExchange == Exchange.KRAKEN) {
            exchangeLabel.setText("Kraken");
        } else if (currentExchange == Exchange.BITFINEX) {
            exchangeLabel.setText("Bitfinex");
        } else {
            //TODO not a nice Error message
            System.out.println("not a valid exchange");
        }

        if (currentTradingPair == TradingPair.ETHUSD) {
            marketLabel.setText("ETH/USD");
        } else if (currentTradingPair == TradingPair.ETHEUR) {
            marketLabel.setText("ETH/EUR");
        } else if (currentTradingPair == TradingPair.ETHBTC) {
            marketLabel.setText("ETH/BTC");
        } else {
            System.out.println("not a valid trading pair");
        }

        String exchange;

    }

    private String determineURL(Exchange currentExchange, TradingPair currentTradingPair) {
        String url = new String("https://api.cryptowat.ch/markets/");

        switch (currentExchange) {
            case GDAX:
                url = url.concat("gdax/");
                break;
            case KRAKEN:
                url = url.concat("kraken/");
                break;
            case BITFINEX:
                url = url.concat("bitfinex/");
                break;
        }

        switch (currentTradingPair) {
            case ETHUSD:
                url = url.concat("ethusd/price");
                break;
            case ETHEUR:
                url = url.concat("etheur/price");
                break;
            case ETHBTC:
                url = url.concat("ethbtc/price");
                break;
        }

        return url;
    }


    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.setPreferredSize(new Dimension(600, 400));
        selectionInfoPanel = new JPanel();
        selectionInfoPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.add(selectionInfoPanel, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Price:");
        selectionInfoPanel.add(label1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exchangeLabel = new JLabel();
        exchangeLabel.setText("Exchange");
        selectionInfoPanel.add(exchangeLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        marketLabel = new JLabel();
        marketLabel.setText("Market");
        selectionInfoPanel.add(marketLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        selectionInfoPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        selectionInfoPanel.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        priceDisplayPanel = new JPanel();
        priceDisplayPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.add(priceDisplayPanel, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        priceDisplayPanel.add(spacer3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        priceLabel = new JLabel();
        priceLabel.setFocusable(false);
        Font priceLabelFont = this.$$$getFont$$$(null, -1, 28, priceLabel.getFont());
        if (priceLabelFont != null) priceLabel.setFont(priceLabelFont);
        priceLabel.setText("PRICE");
        priceDisplayPanel.add(priceLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        priceDisplayPanel.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        priceDisplayPanel.add(spacer5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        priceDisplayPanel.add(spacer6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        generalInfoPanel = new JPanel();
        generalInfoPanel.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.add(generalInfoPanel, new GridConstraints(3, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Price Updated every 1sec");
        generalInfoPanel.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        generalInfoPanel.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        generalInfoPanel.add(spacer8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        generalInfoPanel.add(spacer9, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("©Robin Becher 2017");
        generalInfoPanel.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        alwaysOnTopCheckBox = new JCheckBox();
        alwaysOnTopCheckBox.setSelected(true);
        alwaysOnTopCheckBox.setText("Always on top");
        generalInfoPanel.add(alwaysOnTopCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progressBar = new JProgressBar();
        generalInfoPanel.add(progressBar, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.add(listPanel, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        listPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        exchangesList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("GDAX");
        defaultListModel1.addElement("Kraken");
        defaultListModel1.addElement("Bitfinex");
        exchangesList.setModel(defaultListModel1);
        exchangesList.setSelectedIndex(0);
        scrollPane1.setViewportView(exchangesList);
        final JScrollPane scrollPane2 = new JScrollPane();
        listPanel.add(scrollPane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tradingPairList = new JList();
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        defaultListModel2.addElement("ETH/USD");
        defaultListModel2.addElement("ETH/EUR");
        defaultListModel2.addElement("ETH/BTC");
        tradingPairList.setModel(defaultListModel2);
        tradingPairList.setSelectedIndex(0);
        scrollPane2.setViewportView(tradingPairList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return topLevelPanel;
    }
}
