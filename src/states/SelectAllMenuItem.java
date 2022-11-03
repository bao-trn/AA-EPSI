package states;

import drawing.DessinPanel2;
import states.interfaces.MenuState;

import javax.swing.*;

public class SelectAllMenuItem extends JMenuItem implements MenuState {

    @Override
    public JMenuItem setupKey(DessinPanel2 designPanel) {
        JMenuItem item = new JMenuItem("All");
        item.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
        item.addActionListener(e -> designPanel.selectAll());
        return item;
    }
}
