package im.jahnke.partmanager.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class PartsPanel extends JPanel {

    public PartsPanel() {
	TablePanel tablePanel = new TablePanel();
	setLayout(new BorderLayout());

	JSplitPane splitPane = new JSplitPane();
	add(splitPane, BorderLayout.CENTER);

	splitPane.setLeftComponent(tablePanel);
	splitPane.setRightComponent(tablePanel.getTabPanel());

	splitPane.setDividerLocation(480);
	splitPane.setResizeWeight(0.5);
	splitPane.setEnabled(false);
    }

}
