package states;

import drawing.DessinPanel2;
import states.interfaces.MenuState;

import javax.swing.*;

public class ColorMenuItem extends JMenu implements MenuState {

    @Override
    public JMenuItem setupKey(DessinPanel2 designPanel) {
        JMenuItem item = new JMenuItem("Color");
        item.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        item.addActionListener(e -> designPanel.coloreSelected());
        return item;
    }
}
