package view;

import controller.PriceFinder;
import controller.ProductManager;
import model.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.SwingUtilities;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class Main extends JFrame {

    private int time = 5;

    private JLabel msgBar = new JLabel("");
    private JPanel control;
    private JPanel board;
    private JPanel panel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JList jList;
    private DefaultListModel<Product> defaultListModel;

    private final static String RESOURCE_DIR = "resources/";
    private final static Dimension DEFAULT_SIZE = new Dimension(600, 500);

    private Product product;
    private ProductManager productmanager;
    private ItemView itemView;

    private JPopupMenu popupmenu;
    private MouseListener mouselistener;
    private MouseEvent mouseevent;
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
        createProductManager();
        defaultListModel = createListModel();
        setLayout(new BorderLayout());
        setSize(dim);
        createUI();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        showMessage("Welcome!", time);
    }

    public void mouseListen(MouseEvent me) {
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    popupmenu.show(jList, mouseEvent.getX(), mouseEvent.getY());
                }
            }
        };
        jList.addMouseListener(mouseListener);
    }

    /**
     *
     * @param itemURL
     * @param itemName
     * @param itemPrice
     * @param itemDateAdded
     * @return
     */
    public Product createDefault(String itemURL, String itemName, double itemPrice, String itemDateAdded) {
        return new Product(itemURL, itemName, itemPrice, itemDateAdded);
    }

    /**
     *
     * @param event
     */
    private void refreshButtonClicked(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void singleRefreshButtonClicked(ActionEvent event) {

    }

    /**
     *
     * @param event
     */
    private void addButtonClicked(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void searchButtonClicked(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void forwardButtonClicked(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void backwardButtonClicked(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void delete(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void edit(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void openWeb(ActionEvent event) {
    }

    /**
     *
     * @param event
     */
    private void about(ActionEvent event) {
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
     * Callback to be invoked when the view-page icon is clicked. Launch a
     * (default) web browser by supplying the URL of the item.
     */
    private void viewPageClicked() {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(product.getProductURL()));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        showMessage("Opening Webpage", time);
    }

    /**
     * Configure UI.
     *
     */
    private void createUI() {
        setLayout(new BorderLayout());
        control = createControlPanel();
        control.setBorder(BorderFactory.createEmptyBorder(10, 16, 0, 16));
        add(control, BorderLayout.CENTER);
        board = new JPanel();
        jList = createJList();
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 16, 0, 16),
                BorderFactory.createLineBorder(Color.WHITE)));
        board.setLayout(new GridLayout(1, 1));
        mouseListen(mouseevent);
        itemView = new ItemView();
        itemView.setClickListener(this::viewPageClicked);
        board.add(new JScrollPane(jList));
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /**
     * Create a control panel consisting of a refresh button.
     */
    private JPanel createControlPanel() {
        panel = new JPanel();
        toolBar = new JToolBar("Toolbar");
        createJMenu();
        createJPopupMenu();
        createJToolBar();
        add(toolBar, BorderLayout.NORTH);
        return panel;
    }

    private void createJToolBar() {
        JButton checkmark = createButton("checkmark.png", false);
        checkmark.addActionListener(this::refreshButtonClicked);
        toolBar.add(checkmark);
        JButton add = createButton("plus.png", false);
        add.addActionListener(this::addButtonClicked);
        toolBar.add(add);
        JButton search = createButton("search.png", false);
        search.addActionListener(this::searchButtonClicked);
        toolBar.add(search);
        JButton forward = createButton("forward.png", false);
        forward.addActionListener(this::forwardButtonClicked);
        toolBar.add(forward);
        JButton backward = createButton("back.png", false);
        backward.addActionListener(this::backwardButtonClicked);
        toolBar.add(backward);
        toolBar.addSeparator();
        JButton singleRefresh = createButton("refresh.png", false);
        singleRefresh.addActionListener(this::singleRefreshButtonClicked);
        toolBar.add(singleRefresh);
        JButton openLink = createButton("link.png", false);
        openLink.addActionListener(this::openWeb);
        toolBar.add(openLink);
        JButton delete = createButton("delete.png", false);
        delete.addActionListener(this::delete);
        toolBar.add(delete);
        JButton edit = createButton("edit.png", false);
        edit.addActionListener(this::edit);
        toolBar.add(edit);
        toolBar.addSeparator();
        JButton about = createButton("about.png", false);
        about.addActionListener(this::about);
        toolBar.add(about);
    }

    //createDefault("https://www.amazon.com/Nintendo-Console-Resolution-Surround-Customize/dp/B07M5ZQSKV", "Nintendo Switch", 359.99, "1/30/2019");
    private void createProductManager() {
        productmanager = new ProductManager();
        productmanager = new ProductManager();
        String itemURL = "https://www.amazon.com/Nintendo-Console-Resolution-Surround-Customize/dp/B07M5ZQSKV";
        String itemName = "Nintendo Switch";
        double itemPrice = 359.99;
        String itemDateAdded = "1/30/2019";
        productmanager.add(new Product(itemURL, itemName, itemPrice, itemDateAdded));
    }

    private void createJPopupMenu() {
        this.popupmenu = new JPopupMenu();
        JMenuItem price = createMenutItem("Price", KeyEvent.VK_P, ActionEvent.ALT_MASK);
        price.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "checkmark.png")));
        JMenuItem view = createMenutItem("View", KeyEvent.VK_V, ActionEvent.ALT_MASK);
        view.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "webbrowser.png")));
        JMenuItem edit = createMenutItem("Edit", KeyEvent.VK_E, ActionEvent.ALT_MASK);
        edit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "edit.png")));
        JMenuItem remove = createMenutItem("Remove", KeyEvent.VK_R, ActionEvent.ALT_MASK);
        remove.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "delete.png")));
        popupmenu.add(price);
        popupmenu.add(view);
        popupmenu.add(edit);
        popupmenu.add(remove);
        popupmenu.addSeparator();
        JMenuItem cname = new JMenuItem("Copy Name");
        JMenuItem curl = new JMenuItem("Copy URL");
        JMenuItem citem = new JMenuItem("Copy Item");
        popupmenu.add(cname);
        popupmenu.add(curl);
        popupmenu.add(citem);
    }

    public DefaultListModel createListModel() {
        DefaultListModel generatedListModel = new DefaultListModel<>();
        productmanager.getProducts().forEach((iter) -> {
            generatedListModel.addElement(iter);
        });
        return generatedListModel;
    }

    private JList createJList() {
        JList generatedJList = new JList<>(defaultListModel);
        generatedJList.setCellRenderer(new ItemView());
        generatedJList.setBounds(100, 100, 75, 75);
        return generatedJList;
    }

    private void createJMenu() {
        JMenuBar fileMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("App");
        JMenu editMenu = new JMenu("Item");
        JMenu sortMenu = new JMenu("Sort");
        JMenuItem check = createMenutItem("Check Prices", KeyEvent.VK_E, ActionEvent.ALT_MASK);
        check.addActionListener(this::addButtonClicked);
        check.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "checkmark.png")));
        editMenu.add(check);

        JMenuItem add = createMenutItem("Add", KeyEvent.VK_E, ActionEvent.ALT_MASK);
        add.addActionListener(this::addButtonClicked);
        add.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "plus.png")));
        editMenu.add(add);

        JMenuItem edit = createMenutItem("Edit", KeyEvent.VK_E, ActionEvent.ALT_MASK);
        edit.addActionListener(this::addButtonClicked);
        edit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "edit.png")));
        editMenu.add(edit);

        editMenu.add(new JSeparator());

        JMenuItem search = createMenutItem("Search", KeyEvent.VK_S, ActionEvent.ALT_MASK);
        search.addActionListener(this::addButtonClicked);
        search.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "search.png")));
        editMenu.add(search);

        JMenuItem forward = createMenutItem("Forward", KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK);
        forward.addActionListener(this::addButtonClicked);
        forward.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "forward.png")));
        editMenu.add(forward);

        JMenuItem backward = createMenutItem("Backwards", KeyEvent.VK_LEFT, ActionEvent.ALT_MASK);
        backward.addActionListener(this::addButtonClicked);
        backward.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + "back.png")));
        editMenu.add(backward);
        JMenuItem oldest = new JRadioButtonMenuItem("Oldest");
        oldest.addActionListener(this::sortOld);
        sortMenu.add(oldest);
        JMenuItem newest = new JRadioButtonMenuItem("Newest");
        newest.addActionListener(this::sortNew);
        sortMenu.add(newest);
        sortMenu.addSeparator();
        JMenuItem ascend = new JRadioButtonMenuItem("Ascending Order");
        ascend.addActionListener(this::sortNameAscending);
        sortMenu.add(ascend);
        JMenuItem descend = new JRadioButtonMenuItem("Descending Order");
        descend.addActionListener(this::sortNameDescending);
        sortMenu.add(descend);
        sortMenu.addSeparator();
        JMenuItem low = new JRadioButtonMenuItem("Lowest Price ($)");
        low.addActionListener(this::sortLow);
        sortMenu.add(low);
        JMenuItem high = new JRadioButtonMenuItem("Highest Price ($)");
        high.addActionListener(this::sortHigh);
        sortMenu.add(high);
        JMenuItem priceChange = new JRadioButtonMenuItem("Price Change (%)");
        priceChange.addActionListener(this::sortChange);
        sortMenu.add(priceChange);

        fileMenuBar.add(fileMenu);
        fileMenuBar.add(editMenu);
        fileMenuBar.add(sortMenu);
        menuBar = fileMenuBar;
        setJMenuBar(menuBar);
    }

    private JMenuItem createMenutItem(String label, int key, int mask) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(key, mask));
        return menuItem;
    }

    /**
     * Create a button with the given label.
     *
     * @param label name of the resource (image)
     * @param enabled
     * @return a button
     */
    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getClassLoader().getResource(RESOURCE_DIR + label)));
        button.setFocusPainted(enabled);
        return button;
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
                Thread.sleep(time * 1000); // x seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (msg.equals(msgBar.getText())) {
                SwingUtilities.invokeLater(() -> msgBar.setText(" "));
            }
        }).start();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
    }

}
