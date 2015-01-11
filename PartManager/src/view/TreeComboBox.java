package view;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import model.DataManager;

@SuppressWarnings({ "serial" })
public class TreeComboBox extends JComboBox<Object> {

    String[][] data;
    long time = System.currentTimeMillis();
    boolean keyTyped = false;
    String keyword = "";

    @SuppressWarnings("unchecked")
    public TreeComboBox() {
	super();
	getCategoriesFromDatabase();
	addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (System.currentTimeMillis() > time + 1000) {
		    keyword = "";
		}
		time = System.currentTimeMillis();
		keyword += e.getKeyChar();
		for (int i = 0; i < data.length; i++) {
		    if (data[i][2].toLowerCase().startsWith(keyword)) {
			setSelection(i);
			break;
		    }
		}
	    }
	});
	setRenderer(new ItemRenderer());
    }

    @SuppressWarnings("unchecked")
    public TreeComboBox(String topLine) {
	super();
	getCategoriesFromDatabase(topLine);
	addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (System.currentTimeMillis() > time + 1000) {
		    keyword = "";
		}
		time = System.currentTimeMillis();
		keyword += e.getKeyChar();
		for (int i = 0; i < data.length; i++) {
		    if (data[i][2].toLowerCase().startsWith(keyword)) {
			setSelection(i + 1);
			break;
		    }
		}
	    }
	});
	setRenderer(new ItemRenderer());
    }

    public String getFormattedItem() {
	String temp = getSelectedItem().toString();
	return temp;
    }

    private void getCategoriesFromDatabase() {
	data = DataManager.getCategoriesArray();
	final String OFFSET = "     ";

	for (int i = 0; i < data.length; i++) {
	    String[] s = data[i];
	    String item = s[2];
	    for (int j = 0; j < pathLength(Integer.toString(i + 1), 0); j++) {
		item = OFFSET + item;
	    }
	    Item test = new Item(item);
	    addItem(test);
	}
    }

    private void getCategoriesFromDatabase(String topLine) {
	addItem(new Item(topLine));
	data = DataManager.getCategoriesArray();
	final String OFFSET = "     ";

	for (int i = 0; i < data.length; i++) {
	    String[] s = data[i];
	    // System.out.println("ID: " + s[0] + " | Parent: " + s[1] +
	    // " | Titel: " + s[2]);
	    String item = s[2];
	    for (int j = 0; j < pathLength(Integer.toString(i + 1), 0); j++) {
		item = OFFSET + item;
	    }
	    Item test = new Item(item);
	    addItem(test);
	}
    }

    public int pathLength(String id, int i) {
	if (!data[Integer.parseInt(id) - 1][1].equals("0")) {
	    i = 1 + pathLength(
		    data[Integer.parseInt(data[Integer.parseInt(id) - 1][1])][1],
		    i);
	}
	return i;
    }

    private static class Item {
	private String value;

	public Item(String value) {
	    this.value = value;
	}

	@SuppressWarnings("unused")
	public String getValue() {
	    return value;
	}

	@Override
	public String toString() {
	    return value; // this is what display in the JComboBox
	}
    }

    private void setSelection(int index) {
	System.out.println("Debug: Index " + index + " selected");
	this.setSelectedIndex(index);
	this.repaint();
    }

    class ItemRenderer extends BasicComboBoxRenderer {

	public Component getListCellRendererComponent(
		@SuppressWarnings("rawtypes") JList list, Object value,
		int index, boolean isSelected, boolean cellHasFocus) {
	    super.getListCellRendererComponent(list, value, index, isSelected,
		    cellHasFocus);

	    Item item = (Item) value;

	    if (index == -1) {
		setText(item.value.trim());
	    } else {
		setText(item.value);
	    }
	    return this;
	}
    }
}
