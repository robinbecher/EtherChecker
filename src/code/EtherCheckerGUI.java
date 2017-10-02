package code;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

/**
 *
 */
public class EtherCheckerGUI implements ListSelectionListener, ItemListener, ActionListener {
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
    private JButton logButton;
    private JTabbedPane tabbedPane1;
    private JPanel priceTab;
    private JPanel walletTab;
    private JTextField walletTextField;
    private JLabel ethAmount;
    private JLabel walletInfoLabel;
    private JTextField apiKeyTextField;
    private JButton checkButton;
    private static final APIHandler api = new APIHandler();
    private Exchange currentExchange = Exchange.GDAX;
    private TradingPair currentTradingPair = TradingPair.ETHUSD;
    private JFrame frame;
    private LogGUI logGui;
    private long coolDown, startTime;
    private String apiKey;
    private CryptowatchMarketPriceResponse priceResponse;
    private URL url;

    /**
     * Creating an Object of EtherCheckerGUI spawns a frame in the center of the screen,
     * fills it with components and displays the current ETHUSD price on GDAX. The frame can be set to be always on top
     * by using the alwaysOnTopCheckBox. "Always on top" is the default setting.
     */
    EtherCheckerGUI() {
        frame = new JFrame("EtherChecker");
        frame.setContentPane(this.topLevelPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
        frame.setVisible(true);

        logGui = new LogGUI();
        logGui.setLocationRelativeTo(logButton);

        startTime = System.currentTimeMillis();

        alwaysOnTopCheckBox.addItemListener(this);
        exchangesList.addListSelectionListener(this);
        tradingPairList.addListSelectionListener(this);
        logButton.addActionListener(this);
        checkButton.addActionListener(this);
        walletTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                doStuff();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doStuff();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                doStuff();
            }

            private void doStuff() {
                if (!isCoolingDown()) {
                    try {
                        if (walletFormatIsOkay())
                            checkWallet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Constants.COOLDOWN_ERROR);
                    logGui.log(Constants.COOLDOWN_ERROR);
                }
            }
        });

    }

    private void checkWallet() throws Exception {

        Thread thread = new Thread(() -> {
            String walletAddress = walletTextField.getText();
            apiKey = apiKeyTextField.getText();

            walletInfoLabel.setText(Constants.VALID_ADDRESS);
            coolDown = System.currentTimeMillis();
            EtherscanWalletResponse response = null;
            try {
                response = api.getEtherscanWalletInfo(new URL(determineURLEtherscanWallet(walletAddress)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response != null) {
                System.out.println(response.result);
                ethAmount.setText(StringManipulator.trimETHValue(response.result) + " Ether");
            }

        });
        thread.start();


    }

    private String determineURLEtherscanWallet(String walletAddress) {

        return "https://api.etherscan.io/api?module=account&action=balance&address=" + walletAddress + "&tag=latest&apikey=" + apiKey;
    }

    /**
     * Fetches the current ETHUSD price on GDAX by calling the getCryptoWatchPrice() method of the CryptoWatchAPIHandler
     * class.
     *
     * @throws Exception update() can throw an Exception if the API call fails.
     */
    void update() throws Exception {

        long nowtime = System.currentTimeMillis();
        if ((nowtime - startTime) > 600000) {
            logGui.log.setText("");
            startTime = nowtime;
        }
        url = new URL(determineURLCryptowatchMarketPrice(currentExchange, currentTradingPair));

        progressBar.setIndeterminate(true);
        priceResponse = api.getCryptowatchPrice(url);
        priceLabel.setText(priceResponse.result.price.toString());
        progressBar.setIndeterminate(false);


        if (currentExchange == Exchange.GDAX) {
            exchangeLabel.setText("GDAX");
        } else if (currentExchange == Exchange.KRAKEN) {
            exchangeLabel.setText("Kraken");
        } else if (currentExchange == Exchange.BITSTAMP) {
            exchangeLabel.setText("Bitstamp");
        } else {
            logGui.log(Constants.EXCHANGE_ERROR);
            System.out.println(Constants.EXCHANGE_ERROR);
        }

        switch (currentTradingPair) {
            case ETHUSD:
                marketLabel.setText("ETH/USD");
                break;
            case ETHEUR:
                marketLabel.setText("ETH/EUR");
                break;
            case ETHBTC:
                marketLabel.setText("ETH/BTC");
                break;
            default:
                logGui.log(Constants.TRADINGPAIR_ERROR);
                System.out.println(Constants.TRADINGPAIR_ERROR);
                break;
        }
        logGui.log(currentExchange + "," + currentTradingPair + "," + priceResponse.result.price + "," + priceResponse.allowance.cost + "," + priceResponse.allowance.remaining);
    }

    private String determineURLCryptowatchMarketPrice(Exchange currentExchange, TradingPair currentTradingPair) {
        String url = "https://api.cryptowat.ch/markets/";

        switch (currentExchange) {
            case GDAX:
                url = url.concat("gdax/");
                break;
            case KRAKEN:
                url = url.concat("kraken/");
                break;
            case BITSTAMP:
                url = url.concat("bitstamp/");
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

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList source = (JList) e.getSource();

        if (source == exchangesList) {
            if (exchangesList.isSelectedIndex(0)) {
                currentExchange = Exchange.GDAX;
            } else if (exchangesList.isSelectedIndex(1)) {
                currentExchange = Exchange.KRAKEN;
            } else if (exchangesList.isSelectedIndex(2)) {
                currentExchange = Exchange.BITSTAMP;
            } else {
                logGui.log(Constants.EXCHANGE_ERROR);
                System.out.println(Constants.EXCHANGE_ERROR);
            }
        } else if (source == tradingPairList) {
            if (tradingPairList.isSelectedIndex(0)) {
                currentTradingPair = TradingPair.ETHUSD;
            } else if (tradingPairList.isSelectedIndex(1)) {
                currentTradingPair = TradingPair.ETHEUR;
            } else if (tradingPairList.isSelectedIndex(2)) {
                currentTradingPair = TradingPair.ETHBTC;
            } else {
                logGui.log(Constants.TRADINGPAIR_ERROR);
                System.out.println(Constants.TRADINGPAIR_ERROR);
            }

        }

        Thread thread = new Thread(() -> {
            try {
                update();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        thread.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == logButton) {
            if (!this.logGui.isVisible()) {
                alwaysOnTopCheckBox.setSelected(false);
                this.logGui.setVisible(true);
            }
        } else if (source == checkButton) {
            if (!isCoolingDown()) {
                try {
                    if (walletFormatIsOkay())
                        checkWallet();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println(Constants.COOLDOWN_ERROR);
                logGui.log(Constants.COOLDOWN_ERROR);
            }

        }
    }

    private boolean walletFormatIsOkay() {
        String walletAddress = walletTextField.getText();
        if (walletAddress.length() != 42 || !walletAddress.startsWith("0x")) {
            walletInfoLabel.setText(Constants.INVALID_ADDRESS);
            return false;
        } else {
            walletInfoLabel.setText(Constants.VALID_ADDRESS);
            return true;
        }
    }

    private boolean isCoolingDown() {
        return System.currentTimeMillis() - coolDown < 500;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            this.frame.setAlwaysOnTop(false);
        } else {
            this.frame.setAlwaysOnTop(true);
        }
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
        topLevelPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        topLevelPanel.setPreferredSize(new Dimension(600, 400));
        tabbedPane1 = new JTabbedPane();
        topLevelPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        priceTab = new JPanel();
        priceTab.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("ETH Price", priceTab);
        priceTab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        selectionInfoPanel = new JPanel();
        selectionInfoPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        priceTab.add(selectionInfoPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        priceTab.add(listPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        listPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        exchangesList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("GDAX");
        defaultListModel1.addElement("Kraken");
        defaultListModel1.addElement("Bitstamp");
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
        priceDisplayPanel = new JPanel();
        priceDisplayPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        priceTab.add(priceDisplayPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        generalInfoPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        priceTab.add(generalInfoPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        alwaysOnTopCheckBox.setSelected(false);
        alwaysOnTopCheckBox.setText("Always on top");
        generalInfoPanel.add(alwaysOnTopCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progressBar = new JProgressBar();
        generalInfoPanel.add(progressBar, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logButton = new JButton();
        logButton.setText("show log");
        generalInfoPanel.add(logButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        walletTab = new JPanel();
        walletTab.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Wallets", walletTab);
        walletTab.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        walletTab.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Enter Wallet Address");
        panel1.add(label4, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        walletTextField = new JTextField();
        panel1.add(walletTextField, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        walletInfoLabel = new JLabel();
        walletInfoLabel.setText("Please enter your wallet address above");
        panel1.add(walletInfoLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkButton = new JButton();
        checkButton.setText("Check");
        panel1.add(checkButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        walletTab.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Current ETH holdings:");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ethAmount = new JLabel();
        Font ethAmountFont = this.$$$getFont$$$(null, -1, 24, ethAmount.getFont());
        if (ethAmountFont != null) ethAmount.setFont(ethAmountFont);
        ethAmount.setText("");
        panel2.add(ethAmount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setEnabled(true);
        panel3.setVisible(false);
        walletTab.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("API Key:");
        panel3.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apiKeyTextField = new JTextField();
        panel3.add(apiKeyTextField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Please enter a valid API Key above. You can get one here: https://etherscan.io/myapikey");
        panel3.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        walletTab.add(spacer10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        walletTab.add(spacer11, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return topLevelPanel;
    }
}
