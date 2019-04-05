package view;

import model.Product;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/**
 * A special panel to display the detail of an item.
 *
 * @author Isaias Leos, Leslie Gomez
 */
@SuppressWarnings("serial")
public class ItemView extends JPanel {

    private Image itemIcon;
    private Product product;

    /**
     * Interface to notify a click on the view page icon.
     */
    public interface ClickListener {

        /**
         * Callback to be invoked when the view page icon is clicked.
         */
        void clicked();
    }

    /**
     * Directory for image files: src/image.
     */
    private final static String RESOURCE_DIR = "/resources/";

    /**
     * View-page clicking listener.
     */
    private ClickListener listener;

    /**
     * Create a new instance.
     *
     * @param itemList a list contain the current amount of Products
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ItemView(Product product) {
        this.product = product;
        this.itemIcon = getImage("itemIcon.png");
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1280, 720));//Learn what this does.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null && isViewPageClicked(e.getX(), e.getY())) {
                    listener.clicked();
                }
            }
        });
    }

    /**
     * Set the view-page click listener.
     *
     * @param listener
     */
    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /**
     * Display the details of an item list within the ItemView Panel. Overridden
     * here to display the details of the item.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        Dimension dim = getSize();
        System.out.println("Height: " + dim.height + "\nWidth:" + dim.width);
        int x = 20, y = 0;
        g.drawImage(itemIcon, x, y, this);
        y += itemIcon.getHeight(this) + 20;

        g.drawString(textAttrManipulation("Name:      ", product.getProductName(), Font.BOLD, Color.BLACK), x, y);
        y += 20;
        g.drawString("URL:         " + product.getProductURL(), x, y);
        y += 20;
        g.drawString(textAttrManipulation("Price:       ", product.getProductPrice() + "$", Font.PLAIN, Color.BLUE), x, y);//Green or Red
        y += 20;
        float f = (float) product.getChange();
        Color change = f == 0.0 ? Color.BLACK : f > 0.0 ? Color.GREEN : Color.RED;
        if (f > 0.0) {
            priceDropSound("play.wav");
        }
        g.drawString(textAttrManipulation("Change:   ", Math.abs(product.getChange()) + "%", Font.PLAIN, change), x, y);//Green or Red
        y += 20;
        g.drawString("Added:    " + product.getAddedDate() + " (" + product.getInitialPrice() + "$)", x, y);
        y += 80;

        g.dispose();
    }

    /**
     * Manipulates the given two strings. Changes the first string to make the
     * same font style, but ignores color and fond type. Changes the color and
     * font type of the second string given the font style and color.
     *
     * @param productPrefix first part of the string
     * @param productPostfix second part of the string
     * @param font the font type e.g. bold or plain
     * @param color the color of the font
     * @return the re-formated string
     */
    private AttributedCharacterIterator textAttrManipulation(String productPrefix, String productPostfix, int font, Color color) {
        AttributedString text = new AttributedString(productPrefix + productPostfix);
        text.addAttribute(TextAttribute.FONT, new Font("Arial", font, 16),
                0, productPrefix.length() + productPostfix.length());
        text.addAttribute(TextAttribute.FONT, new Font("Arial", font, 16),
                productPrefix.length(),
                productPrefix.length() + productPostfix.length());
        text.addAttribute(TextAttribute.FOREGROUND, color,
                productPrefix.length(),
                productPrefix.length() + productPostfix.length());
        return text.getIterator();
    }

    /**
     *
     * @param x x-coordinate of the mouse pointer
     * @param y y-coordinate of the mouse pointer
     * @return true if the given screen coordinate is inside the viewPage icon.
     */
    private boolean isViewPageClicked(int x, int y) {
        return new Rectangle(20, 0, itemIcon.getHeight(this),
                itemIcon.getWidth(this)).contains(x, y);
    }

    /**
     * Return the image stored in the given file.
     *
     * @param file
     * @return
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public Image getImage(String file) {
        try {
            URL url = new URL(getClass().getResource(RESOURCE_DIR), file);
            System.out.println(url.toString());
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Play the audio clip (wav) specified by a URL. This method has no effect
     * if the audio clip cannot be found.
     *
     * @param file
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private void priceDropSound(String filename) {
        try {
            System.out.println(getClass().getResource(RESOURCE_DIR) + filename);
            URL url = new URL(getClass().getResource(RESOURCE_DIR), filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
