package states;

import drawing.DessinPanel2;
import states.interfaces.MenuState;

import javax.swing.*;

public class NoneMenuItem extends JMenu implements MenuState {

    @Override
    public JMenuItem setupKey(DessinPanel2 designPanel) {
        JMenuItem item = new JMenuItem("None");
        item.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
        item.addActionListener(e -> designPanel.clearAll());
        return item;
    }
}

