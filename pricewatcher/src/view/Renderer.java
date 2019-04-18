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

    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list, Product value, int index, boolean isSelected, boolean cellHasFocus) {
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
