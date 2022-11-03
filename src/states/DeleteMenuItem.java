package states;

import drawing.DessinPanel2;
import states.interfaces.MenuState;

import javax.swing.*;

public class DeleteMenuItem extends JMenu implements MenuState {

    @Override
    public JMenuItem setupKey(DessinPanel2 designPanel) {
        JMenuItem item = new JMenuItem("Delete");
        item.setAccelerator(KeyStroke.getKeyStroke("BACK_SPACE"));
        item.addActionListener(e -> designPanel.deleteSelected());
        return item;
    }
}
