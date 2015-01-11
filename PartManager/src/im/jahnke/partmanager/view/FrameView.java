package im.jahnke.partmanager.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameView {

    JFrame frame = new JFrame();
    MainTabPanel mainTabPanel = new MainTabPanel();
    public FrameView() {

	frame.setSize(950, 550);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setJMenuBar(createMenu());
	frame.add(mainTabPanel);
	frame.setVisible(true);

    }

    private JMenuBar createMenu() {
	JMenuBar menuBar = new JMenuBar();

	// Build the file menu.
	JMenu file_menu = new JMenu("Datei");
	menuBar.add(file_menu);

	JMenuItem exit_item = new JMenuItem("Beenden");
	exit_item.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		System.exit(0);
	    }
	});
	file_menu.add(exit_item);

	JMenu help_menu = new JMenu("Hilfe");
	menuBar.add(help_menu);
	JMenuItem about_item = new JMenuItem("Über");
	about_item.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JOptionPane
			.showMessageDialog(
				null,
				"PartManager 0.1 \u00A9 2014\n\n"
					+ "PartManager ist ein Programm zur Lagerverwaltung \n"
					+ "von elektronischen Bauteilen.\n\n"
					+ "Programmiert von:\n  Dominik Jahnke (dominik@jahnke.im)\n",
				"Über", JOptionPane.PLAIN_MESSAGE);
	    }
	});
	help_menu.add(about_item);

	return menuBar;

    }

    public FrameView get() {
	return this;
    }

}
