package view;

import controller.ProductManager;
import model.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static java.nio.file.Files.list;
import static java.util.Collections.list;
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
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
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
    private final static Dimension DEFAULT_SIZE = new Dimension(600, 400);

    private Product product;
    private ProductManager productmanager;
    private ItemView itemView;

    private JPopupMenu popupmenu;
    private MouseListener mouselistener;
    private MouseEvent mouseevent;

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
                    System.out.println("right clicked");
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
        add(control, BorderLayout.NORTH);
        board = new JPanel();
        jList = createJList();
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 16, 0, 16),
                BorderFactory.createLineBorder(Color.WHITE)));
        board.setLayout(new GridLayout(1, 1));
        itemView = new ItemView();
        itemView.setClickListener(this::viewPageClicked);
        
        mouseListen(mouseevent);
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
        setJMenuBar(createJMenu());
        createJPopupMenu();
        createJToolBar();
        menuBar.add(toolBar);
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
        this.popupmenu = new JPopupMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        popupmenu.add(cut);
        popupmenu.add(copy);
        popupmenu.add(paste);
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

    private JMenuBar createJMenu() {
        JMenuBar fileMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("App");
        JMenu editMenu = new JMenu("Item");
        final JMenu aboutMenu = new JMenu("Sort");
        setJMenuBar(fileMenuBar);
        fileMenuBar.add(fileMenu);
        fileMenuBar.add(editMenu);
        fileMenuBar.add(aboutMenu);
        menuBar = fileMenuBar;
        return menuBar;
    }

    /**
     * Create a button with the given label.
     *
     * @param label name of the resource (image)
     * @param enabled
     * @return a button
     */
    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton(new ImageIcon(getClass().getClassLoader()
                .getResource(RESOURCE_DIR + label)));
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
