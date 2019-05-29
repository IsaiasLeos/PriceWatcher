package view;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import model.Product;

/**
 *
 * @author Isaias Leos
 */
public class Renderer extends ItemView implements ListCellRenderer<Product> {

    /**
     * Renders what the selected index looks like.
     *
     * @param list list of products
     * @param value product to be changed
     * @param index position of selected text
     * @param isSelected is any cell is selected
     * @param cellHasFocus if cell has focus
     * @return cell renderer
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list,
            Product value, int index, boolean isSelected, boolean cellHasFocus) {
        setProduct(value);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
