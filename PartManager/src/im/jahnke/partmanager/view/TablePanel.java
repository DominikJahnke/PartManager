package im.jahnke.partmanager.view;

import im.jahnke.partmanager.model.DataManager;
import im.jahnke.partmanager.view.partsTabbedPanel.TabPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TablePanel extends JPanel {

    JTable table;
    TabPanel tabPanel;
    DefaultTableModel model;
    String[] columnNames = { "ID", "Titel" };
    JTextField search;
    TreeComboBox category_box;

    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem menuItemAdd = new JMenuItem("Add Item");
    JMenuItem menuItemRemove = new JMenuItem("Remove Item");

    public TablePanel() {

	create();

    }

    public TabPanel getTabPanel() {
	return tabPanel;
    }

    public void create() {

	tabPanel = new TabPanel();
	model = new DefaultTableModel(DataManager.getData(), columnNames) {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	    }

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};

	table = new JTable(DataManager.getData(), columnNames);
	table.setModel(model);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.getColumnModel().getColumn(0).setPreferredWidth(30);
	table.getColumnModel().getColumn(1).setPreferredWidth(421);
	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.setPreferredSize(new Dimension(472, 420));
	scrollPane.setFocusable(false);
	popupMenu.add(menuItemAdd);
	popupMenu.add(menuItemRemove);
	table.setComponentPopupMenu(popupMenu);

	menuItemAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		new AddItem();
		updateTable();
	    }
	});
	
	menuItemRemove.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int id = Integer.parseInt(table.getValueAt(
			table.getSelectedRow(), 0).toString());
		DataManager.deleteSingleItem(id);
		updateTable();
	    }
	});

	category_box = new TreeComboBox("Alle Kategorien");
	category_box.setPreferredSize(new Dimension(267, 20));
	category_box.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (category_box.getSelectedIndex() != 0) {
		    updateTable(DataManager.getDataByCategory(
			    category_box.getSelectedIndex(), category_box.data));
		} else {
		    updateTable();
		}
	    }
	});

	search = new JTextField();
	search.setPreferredSize(new Dimension(200, 20));
	search.addKeyListener(new KeyAdapter() {
	    public void keyTyped(KeyEvent e) {
		updateTable(DataManager.searchData(search.getText()));

	    }
	});

	JPanel subPanel = new JPanel();

	subPanel.add(category_box);
	subPanel.add(search);

	// Now we simply add it to your main panel.
	add(subPanel, BorderLayout.NORTH);
	add(scrollPane, BorderLayout.SOUTH);
	setVisible(true);
	table.addKeyListener(new KeyAdapter() {
	    public void keyReleased(final java.awt.event.KeyEvent evt) {
		tabPanel.update(Integer.parseInt((String) table.getValueAt(
			table.getSelectedRow(), 0)));
	    }
	});

	table.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		
		tabPanel.update(Integer.parseInt((String) table.getValueAt(
			table.getSelectedRow(), 0)));
	    }
	    public void mousePressed(MouseEvent e){
		Point point = e.getPoint();
	        int currentRow = table.rowAtPoint(point);
	        table.setRowSelectionInterval(currentRow, currentRow);
	        tabPanel.update(Integer.parseInt((String) table.getValueAt(
			table.getSelectedRow(), 0)));
	    }
	});

    }

    public void updateTable() {
	model = new DefaultTableModel(DataManager.getData(), columnNames) {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	    }

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	table.setModel(model);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.getColumnModel().getColumn(0).setPreferredWidth(30);
	table.getColumnModel().getColumn(1).setPreferredWidth(421);
	table.revalidate();
	table.repaint();
    }

    public void updateTable(String[][] data) {
	model = new DefaultTableModel(data, columnNames) {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public Class<?> getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	    }

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	table.setModel(model);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.getColumnModel().getColumn(0).setPreferredWidth(30);
	table.getColumnModel().getColumn(1).setPreferredWidth(421);
	table.revalidate();
	table.repaint();
    }

}
