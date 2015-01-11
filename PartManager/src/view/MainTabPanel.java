package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class MainTabPanel extends JPanel {

    JTabbedPane tabbedPane;

    public MainTabPanel() {
	setLayout(null);
	tabbedPane = new JTabbedPane(JTabbedPane.TOP,
		JTabbedPane.SCROLL_TAB_LAYOUT);
	tabbedPane.addTab("Bauteile (F1)", new PartsPanel());
	tabbedPane.addTab("Lager (F2)", new StoragePanel());
	tabbedPane.addTab("Bestellungen (F3)", new OrderPanel());
	tabbedPane.addTab("Einstellungen (F4)", new OrderPanel());
	tabbedPane.setBounds(-3, -3, 955, 500);
	add(tabbedPane);
	tabbedPane.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F1) {
		    tabbedPane.setSelectedIndex(0);
		} else if (e.getKeyCode() == KeyEvent.VK_F2) {
		    tabbedPane.setSelectedIndex(1);
		} else if (e.getKeyCode() == KeyEvent.VK_F3) {
		    tabbedPane.setSelectedIndex(2);
		} else if (e.getKeyCode() == KeyEvent.VK_F4) {
		    tabbedPane.setSelectedIndex(2);
		}
	    }
	});

    }

    public void selectFirst() {
	tabbedPane.setSelectedIndex(0);
    }

}
