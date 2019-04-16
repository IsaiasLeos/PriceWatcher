package view;

import controller.PriceFinder;
import controller.ProductManager;
import model.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    private int time = 4;

    private JLabel msgBar = new JLabel("");
    private JPanel controlPanel;
    private JPanel drawingBoard;
    private JPanel panel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JList jListRenderer;
    private JScrollPane jScrollPane;
    private JPopupMenu popupMenu;
    private MouseEvent mouseEvent;
    private JMenu nestedMenu;

    private DefaultListModel<Product> defaultListModel;

    private final static String RESOURCE_DIR = "resources/";
    private final static Dimension DEFAULT_SIZE = new Dimension(600, 400);

    private Product product;
    private ProductManager originalProductManager;
    private ProductManager backUpProductManager;
    private ItemView itemView;
    private PriceFinder webPrice;

    /**
     * Create a new dialog.
     */
    public Main() {
        this(DEFAULT_SIZE);
    }

    /**
     * Create a new dialog of the given screen dimension.
     *
     * @param dim
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Main(Dimension dim) {
        super("Price Watcher");
        createDefaultProduct();
        setLayout(new BorderLayout());
        setSize(dim);
        createUI();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        showMessage("Welcome!", time);
        pack();
    }

    /**
     * Configure UI.
     *
     */
    private void createUI() {
        controlPanel = createControlPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        add(controlPanel, BorderLayout.CENTER);
        drawingBoard = new JPanel();
        jListRenderer = createJList();
        mouseListener(mouseEvent);
        mouseMotionListener(mouseEvent);
        jListRenderer.setVisibleRowCount(3);
        jScrollPane = new JScrollPane(jListRenderer);
        drawingBoard.add(jScrollPane);
        drawingBoard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 16, 0, 16),
                BorderFactory.createLineBorder(Color.WHITE)));
        drawingBoard.setLayout(new GridLayout(1, 1));
        add(drawingBoard, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /**
     * Create a control panel consisting of a refresh button.
     */
    private JPanel createControlPanel() {
        panel = new JPanel();
        toolBar = new JToolBar("Toolbar");
        createJPopupMenu();
        createJMenu();
        createJToolBar();
        add(toolBar, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Show briefly the given string in the message bar.
     *
     * @param msg
     */
    private void showMessage(String msg, int time) {
        msgBar.setText(msg);
        new Thread(() -> {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (msg.equals(msgBar.getText())) {
                SwingUtilities.invokeLater(() -> msgBar.setText(" "));
            }
        }).start();
    }

    private void mouseListener(MouseEvent mouseEvent) {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    popupMenu.show(jListRenderer, mouseEvent.getX(), mouseEvent.getY());
                }
                if (SwingUtilities.isLeftMouseButton(mouseEvent) && itemView.imageClicked(mouseEvent.getX(), mouseEvent.getY())) {
                    openWeb(mouseEvent);
                }
            }
        };
        jListRenderer.addMouseListener(mouseListener);
    }

    private void mouseMotionListener(MouseEvent mouseEvent) {
        MouseMotionListener mouseMotion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (itemView.imageClicked(e.getX(), e.getY())) {
                    itemView.setItemImage(itemView.getImage("webbrowserbig.png"));
                    repaint();
                } else {
                    itemView.setItemImage(itemView.getImage("webbrowser.png"));
                    repaint();
                }
            }
        };
        jListRenderer.addMouseMotionListener(mouseMotion);
    }

    /**
     *
     * @param itemURL
     * @param itemName
     * @param itemPrice
     * @param itemDateAdded
     * @return
     */
    private Product createProduct(String itemURL, String itemName, double itemPrice, String itemDateAdded) {
        return new Product(itemURL, itemName, itemPrice, itemDateAdded);
    }

    /**
     *
     */
    private void createDefaultProduct() {
        originalProductManager = new ProductManager();
        String productURL = "https://www.amazon.com/Nintendo-Console-Resolution-Surround-Customize/dp/B07M5ZQSKV";
        String productName = "Nintendo Switch";
        double productInitialPrice = 359.99;
        String productDateAdded = "1/30/2019";
        originalProductManager.add(createProduct(productURL, productName, productInitialPrice, productDateAdded));
        originalProductManager.add(createProduct(productURL, productName, productInitialPrice, productDateAdded));
        originalProductManager.add(createProduct("https://www.google.com", "Google", 69999.99, "9/30/2019"));
        originalProductManager.add(createProduct("https://www.google.com", "Google", 69999.99, "9/30/2019"));
        defaultListModel = createListModel(originalProductManager);
        webPrice = new PriceFinder();
    }

    /**
     *
     * @param event
     */
    private void refreshButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() != 0) {
            for (int i = 0; i < defaultListModel.getSize(); i++) {
                defaultListModel.get(i).checkPrice(webPrice.getSimulatedPrice(defaultListModel.get(i).getInitialPrice()));
            }
            repaint();
            showMessage("Refreshing...", time);
        } else {
            showMessage("Product List is Empty", time);
        }

    }

    /**
     *
     * @param event
     */
    private void singleRefreshButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            defaultListModel.get(jListRenderer.getSelectedIndex()).checkPrice(webPrice.getSimulatedPrice(defaultListModel.get(jListRenderer.getSelectedIndex()).getInitialPrice()));
            repaint();
            showMessage("Refreshing...", time);
        } else {
            showMessage("Not Selecting an Item", time);
        }

    }

    /**
     *
     * @param event
     */
    private void addButtonClicked(ActionEvent event) {
        JTextField name = new JTextField();
        JTextField url = new JTextField();
        JTextField price = new JTextField();
        Object[] message = {
            "Product Name:", name,
            "Product URL:", url,
            "Product Price:", price
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        //OK
        if (option == 0) {
            Product generatedProduct = createProduct(url.getText(), name.getText(), Double.parseDouble(price.getText()), getCurrentDate());
            defaultListModel.addElement(generatedProduct);
            showMessage("Product Successfully Added", time);
        }
        //Cancel 
        if (option == 2) {

        }
        //Closed
        if (option == -1) {

        }
    }

    /**
     * Gets the current date.
     *
     * @return current date in MM/d/yyyy format
     *
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/d/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /**
     *
     * @param event
     */
    private void searchButtonClicked(ActionEvent event) {
        JTextField search = new JTextField();
        Object[] message = {
            "Search:", search
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        if (option == 0) {
            backUpProductManager = originalProductManager;
            for (int i = 0; i < defaultListModel.getSize(); i++) {
                if (defaultListModel.get(i).getProductName().toLowerCase().contains(search.getText().toLowerCase())) {
                    
                }
            }
            repaint();
        }
    }

    /**
     *
     * @param event
     */
    private void moveUpButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            jListRenderer.setSelectedIndex(0);
        }
    }

    /**
     *
     * @param event
     */
    private void moveDownButtonClicked(ActionEvent event) {
        if (defaultListModel.getSize() > -1) {
            jListRenderer.setSelectedIndex(defaultListModel.getSize() - 1);

        }
    }

    /**
     *
     * @param event
     */
    private void deleteButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            defaultListModel.remove(jListRenderer.getSelectedIndex());
            repaint();
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     *
     * @param event
     */
    private void editButtonClicked(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            Product generatedProduct = defaultListModel.get(jListRenderer.getSelectedIndex());
            JTextField name = new JTextField(generatedProduct.getProductName());
            JTextField url = new JTextField(generatedProduct.getProductURL(), 5);
            JTextField price = new JTextField("" + (generatedProduct.getInitialPrice()));
            Object[] message = {
                "Product Name:", name,
                "Product URL:", url,
                "Product Price:", price
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Edit", JOptionPane.PLAIN_MESSAGE, 0, new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
            if (option == 0) {
                originalProductManager.delete(product);
                defaultListModel.get(jListRenderer.getSelectedIndex()).setProductName(name.getText());
                defaultListModel.get(jListRenderer.getSelectedIndex()).setCurrentURL(url.getText());
                defaultListModel.get(jListRenderer.getSelectedIndex()).setInitialPrice(Double.parseDouble(price.getText()));
                defaultListModel.get(jListRenderer.getSelectedIndex()).setProductPrice(Double.parseDouble(price.getText()));
                this.originalProductManager.add(product);
                repaint();
                showMessage("Succesfully Edited a Product", time);
            }
            //Close Button
            if (option == -1) {

            }
            //Cancel
            if (option == 2) {

            }
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     *
     * @param event
     */
    private void openWeb(ActionEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL()));

                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(Main.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            showMessage("Opening Webpage", time);
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     *
     * @param event
     */
    private void openWeb(MouseEvent event) {
        if (jListRenderer.getSelectedIndex() > -1) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL()));

                } catch (URISyntaxException | IOException ex) {
                    Logger.getLogger(Main.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            showMessage("Opening Webpage", time);
        }
    }

    /**
     *
     * @param event
     */
    private void aboutButtonClicked(ActionEvent event) {
        JLabel label = new JLabel("<html>"
                + "<center>"
                + "<strong>PriceWatcher v3.6</strong>"
                + "<br>Creators:<br><br><i><sup>Isaias Leos<br>Leslie Gomez</sup>"
                + "</i><br><strong><a href=\"https://github.com/IsaiasLeos/PriceWatcher\">Github</a><strong></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/IsaiasLeos/PriceWatcher"));
                } catch (URISyntaxException | IOException error) {
                    error.printStackTrace();
                }
            }
        });
        JOptionPane.showMessageDialog(null, label, "About", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     *
     * @param event
     */
    private void exitButtonClicked(ActionEvent event) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     *
     * @param event
     */
    private void sortOld(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNew(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNameAscending(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortNameDescending(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortHigh(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortLow(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void sortChange(ActionEvent event) {
    }

    /**
     *
     */
    private void createJToolBar() {
        JButton checkmark = createButton("checkmark.png", "Check Item Prices", false);
        checkmark.addActionListener((event) -> this.refreshButtonClicked(event));
        toolBar.add(checkmark);
        JButton add = createButton("plus.png", "Add Product", false);
        add.addActionListener((event) -> this.addButtonClicked(event));
        toolBar.add(add);
        JButton search = createButton("search.png", "Search for a Item", false);
        search.addActionListener((event) -> this.searchButtonClicked(event));
        toolBar.add(search);
        JButton upButton = createButton("up.png", "Move to First Item", false);
        upButton.addActionListener((event) -> this.moveUpButtonClicked(event));
        toolBar.add(upButton);
        JButton downButton = createButton("down.png", "Move to Last Item", false);
        downButton.addActionListener((event) -> this.moveDownButtonClicked(event));
        toolBar.add(downButton);
        toolBar.addSeparator();
        JButton singleRefresh = createButton("refresh.png", "Refresh Selected Item", false);
        singleRefresh.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        toolBar.add(singleRefresh);
        JButton openLink = createButton("webbrowser.png", "Open in Browser", false);
        openLink.addActionListener((event) -> this.openWeb(event));
        toolBar.add(openLink);
        JButton delete = createButton("delete.png", "Delete Selected Item", false);
        delete.addActionListener((event) -> this.deleteButtonClicked(event));
        toolBar.add(delete);
        JButton edit = createButton("edit.png", "Edit Selected Item", false);
        edit.addActionListener((event) -> this.editButtonClicked(event));
        toolBar.add(edit);
        toolBar.addSeparator();
        JButton about = createButton("about.png", "App Information", false);
        about.addActionListener((event) -> this.aboutButtonClicked(event));
        toolBar.add(about);
    }

    /**
     *
     */
    private void createJPopupMenu() {
        this.popupMenu = new JPopupMenu();
        JMenuItem price = createMenutItem("Price");
        price.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "checkmark.png")));
        price.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        JMenuItem view = createMenutItem("View");
        view.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "webbrowser.png")));
        view.addActionListener((event) -> this.openWeb(event));
        JMenuItem edit = createMenutItem("Edit");
        edit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "edit.png")));
        edit.addActionListener((event) -> this.editButtonClicked(event));
        JMenuItem remove = createMenutItem("Remove");
        remove.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "delete.png")));
        remove.addActionListener((event) -> this.deleteButtonClicked(event));
        JMenuItem cname = new JMenuItem("Copy Name");
        cname.addActionListener((event) -> this.copyToClipboard("name"));
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.copyToClipboard("url"));
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.copyToClipboard("item"));
        popupMenu.add(price);
        popupMenu.add(view);
        popupMenu.add(edit);
        popupMenu.add(remove);
        popupMenu.addSeparator();
        popupMenu.add(cname);
        popupMenu.add(curl);
        popupMenu.add(citem);
    }

    /**
     *
     * @param originalProductManager
     * @return
     */
    public DefaultListModel createListModel(ProductManager originalProductManager) {
        DefaultListModel generatedListModel = new DefaultListModel<>();
        originalProductManager.getProducts().forEach((iter) -> {
            generatedListModel.addElement(iter);
        });
        return generatedListModel;
    }

    /**
     *
     * @return
     */
    private JList createJList() {
        JList generatedJList = new JList<>(defaultListModel);
        itemView = new ItemView();
        generatedJList.setCellRenderer(itemView);
        generatedJList.setBounds(100, 100, 75, 75);
        return generatedJList;
    }

    /**
     *
     */
    private void createJMenu() {
        JMenuBar fileMenuBar = new JMenuBar();
        JMenu appMenu = new JMenu("App");
        JMenu editMenu = new JMenu("Item");
        JMenu sortMenu = new JMenu("Sort");
        JMenuItem about = createMenutItem("About", KeyEvent.VK_A, ActionEvent.CTRL_MASK);
        about.addActionListener((event) -> this.aboutButtonClicked(event));
        about.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "about.png")));
        appMenu.add(about);
        JMenuItem exit = createMenutItem("Exit", KeyEvent.VK_X, ActionEvent.ALT_MASK);
        exit.addActionListener((event) -> this.exitButtonClicked(event));
        exit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        appMenu.add(exit);
        JMenuItem check = createMenutItem("Check Prices", KeyEvent.VK_C, ActionEvent.ALT_MASK);
        check.addActionListener((event) -> this.refreshButtonClicked(event));
        check.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "checkmark.png")));
        editMenu.add(check);
        JMenuItem add = createMenutItem("Add", KeyEvent.VK_A, ActionEvent.ALT_MASK);
        add.addActionListener((event) -> this.addButtonClicked(event));
        add.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        editMenu.add(add);
        JMenuItem edit = createMenutItem("Edit", KeyEvent.VK_E, ActionEvent.ALT_MASK);
        edit.addActionListener((event) -> this.editButtonClicked(event));
        edit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "edit.png")));
        editMenu.add(edit);
        editMenu.add(new JSeparator());
        JMenuItem search = createMenutItem("Search", KeyEvent.VK_S, ActionEvent.ALT_MASK);
        search.addActionListener((event) -> this.searchButtonClicked(event));
        search.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "search.png")));
        editMenu.add(search);
        JMenuItem forward = createMenutItem("Move Up", KeyEvent.VK_UP, ActionEvent.ALT_MASK);
        forward.addActionListener((event) -> this.moveUpButtonClicked(event));
        forward.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "up.png")));
        editMenu.add(forward);
        JMenuItem backward = createMenutItem("Move Down", KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
        backward.addActionListener((event) -> this.moveDownButtonClicked(event));
        backward.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "down.png")));
        editMenu.add(backward);
        JMenuItem oldest = new JRadioButtonMenuItem("Oldest");
        oldest.addActionListener((event) -> this.sortOld(event));
        sortMenu.add(oldest);
        JMenuItem newest = new JRadioButtonMenuItem("Newest");
        newest.addActionListener((event) -> this.sortNew(event));
        sortMenu.add(newest);
        sortMenu.addSeparator();
        JMenuItem ascend = new JRadioButtonMenuItem("Ascending Order");
        ascend.addActionListener((event) -> this.sortNameAscending(event));
        sortMenu.add(ascend);
        JMenuItem descend = new JRadioButtonMenuItem("Descending Order");
        descend.addActionListener((event) -> this.sortNameDescending(event));
        sortMenu.add(descend);
        sortMenu.addSeparator();
        JMenuItem low = new JRadioButtonMenuItem("Lowest Price ($)");
        low.addActionListener((event) -> this.sortLow(event));
        sortMenu.add(low);
        JMenuItem high = new JRadioButtonMenuItem("Highest Price ($)");
        high.addActionListener((event) -> this.sortHigh(event));
        sortMenu.add(high);
        JMenuItem priceChange = new JRadioButtonMenuItem("Price Change (%)");
        priceChange.addActionListener((event) -> this.sortChange(event));
        createNestedJMenu();
        editMenu.add(nestedMenu);
        sortMenu.add(priceChange);
        fileMenuBar.add(appMenu);
        fileMenuBar.add(editMenu);
        fileMenuBar.add(sortMenu);
        menuBar = fileMenuBar;
        setJMenuBar(menuBar);
    }

    private void createNestedJMenu() {
        JMenu generatedNestedMenu = new JMenu("Selected");
        JMenuItem price = createMenutItem("Price");
        price.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "checkmark.png")));
        price.addActionListener((event) -> this.singleRefreshButtonClicked(event));
        JMenuItem view = createMenutItem("View");
        view.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "webbrowser.png")));
        view.addActionListener((event) -> this.openWeb(event));
        JMenuItem edit = createMenutItem("Edit");
        edit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "edit.png")));
        edit.addActionListener((event) -> this.editButtonClicked(event));
        JMenuItem remove = createMenutItem("Remove");
        remove.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "delete.png")));
        remove.addActionListener((event) -> this.deleteButtonClicked(event));
        JMenuItem cname = new JMenuItem("Copy Name");
        cname.addActionListener((event) -> this.copyToClipboard("name"));
        JMenuItem curl = new JMenuItem("Copy URL");
        curl.addActionListener((event) -> this.copyToClipboard("url"));
        JMenuItem citem = new JMenuItem("Copy Item");
        citem.addActionListener((event) -> this.copyToClipboard("item"));
        generatedNestedMenu.add(price);
        generatedNestedMenu.add(view);
        generatedNestedMenu.add(edit);
        generatedNestedMenu.add(remove);
        generatedNestedMenu.addSeparator();
        generatedNestedMenu.add(cname);
        generatedNestedMenu.add(curl);
        generatedNestedMenu.add(citem);
        this.nestedMenu = generatedNestedMenu;
    }

    /**
     *
     * @param copyTo
     */
    private void copyToClipboard(String copyTo) {
        if (jListRenderer.getSelectedIndex() > -1) {
            StringSelection selection = new StringSelection("");
            Clipboard clipboard;
            if (copyTo.equalsIgnoreCase("name")) {
                selection = new StringSelection(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductName());
            } else if (copyTo.equalsIgnoreCase("url")) {
                selection = new StringSelection(defaultListModel.get(jListRenderer.getSelectedIndex()).getProductURL());
            } else if (copyTo.equalsIgnoreCase("item")) {
                Product toClipboard = defaultListModel.get(jListRenderer.getSelectedIndex());
                selection = new StringSelection(
                        "Name:  " + toClipboard.getProductName() + "\n"
                        + "URL:  " + toClipboard.getProductURL() + "\n"
                        + "Price:  " + toClipboard.getProductPrice() + "\n"
                        + "Change:  " + toClipboard.getChange() + "\n"
                        + "Date Added:  " + toClipboard.getAddedDate() + "\n"
                );
            }
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        } else {
            showMessage("Not Selecting an Item", time);
        }
    }

    /**
     *
     * @param label
     * @param key
     * @param mask
     * @return
     */
    private JMenuItem createMenutItem(String label, int key, int mask) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(key, mask));
        menuItem.setMnemonic(mask);
        menuItem.setRolloverEnabled(true);
        return menuItem;
    }

    /**
     *
     * @param label
     * @return
     */
    private JMenuItem createMenutItem(String label) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setRolloverEnabled(true);
        return menuItem;

    }

    /**
     * Create a button with the given label.
     *
     * @param label name of the resource (image)
     * @param enabled
     * @return a button
     */
    private JButton createButton(String label, String tooltip, boolean enabled) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + label)));
        button.setFocusPainted(enabled);
        button.setRolloverEnabled(true);
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
    }

}
