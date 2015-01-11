package view.partsTabbedPanel;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class TabPanel extends JPanel {

	GeneralTab generalTab = new GeneralTab();
	ShopTab shopTab = new ShopTab();
	
	public TabPanel() {
		setLayout(null);
		setSize(300, 300);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addTab("Allgemein", generalTab);
		tabbedPane.addTab("Händler", shopTab);
		tabbedPane.setBounds(0, 0, 460, 475);
		add(tabbedPane);
	}

	public void update(int index) {
		generalTab.update(index);
		shopTab.update(index);
	}

}
