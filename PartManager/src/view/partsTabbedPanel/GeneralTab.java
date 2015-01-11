package view.partsTabbedPanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

import view.AddDatasheet;
import view.TreeComboBox;
import model.DataManager;

import javax.swing.JButton;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class GeneralTab extends JPanel {
    private JTextField title_field;
    private JSpinner pieces_spinner;
    JTextPane description_field;
    private JLabel image_label;
    JButton btnOpenDatasheet;
    JButton btnEditDatasheet;
    TreeComboBox category_box;
    JComboBox<String> comboBox_storage;
    JComboBox<String> comboBox_box;
    ArrayList<Integer> storage_list = new ArrayList<Integer>();

    int currentItem = 0;

    public GeneralTab() {
	setLayout(null);
	// setSize(300, 300);
	JLabel lblTitel = new JLabel("Titel");
	lblTitel.setBounds(10, 11, 46, 14);
	add(lblTitel);

	JLabel lblBeschreibbung = new JLabel("Beschreibung");
	lblBeschreibbung.setBounds(10, 44, 97, 14);
	add(lblBeschreibbung);

	title_field = new JTextField();
	title_field.setBounds(123, 8, 320, 20);
	add(title_field);
	title_field.setColumns(10);

	description_field = new JTextPane();
	description_field.setContentType("text/html");
	// textArea.setBounds(99, 31, 235, 106);
	JScrollPane scrollPane = new JScrollPane(description_field);
	scrollPane.setBounds(123, 39, 320, 106);
	add(scrollPane);

	JLabel lblKategorie = new JLabel("Kategorie");
	lblKategorie.setBounds(10, 159, 74, 14);
	add(lblKategorie);

	category_box = new TreeComboBox();
	category_box.setBounds(123, 156, 320, 20);
	add(category_box);
	category_box.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {

	    }
	});

	JSeparator separator = new JSeparator();
	separator.setBounds(10, 230, 430, 2);
	add(separator);

	JLabel lblAnzahl = new JLabel("Anzahl");
	lblAnzahl.setBounds(10, 190, 46, 14);
	add(lblAnzahl);

	pieces_spinner = new JSpinner();
	pieces_spinner.setBounds(144, 187, 64, 22);
	add(pieces_spinner);

	JLabel lblStorage = new JLabel("Kiste");
	lblStorage.setBounds(10, 251, 46, 14);
	add(lblStorage);

	image_label = new JLabel();

	image_label.setBounds(300, 250, 140, 140);
	image_label.setBackground(Color.white);
	add(image_label);

	JButton btnSave = new JButton("Speichern");
	btnSave.setBounds(178, 396, 89, 23);
	add(btnSave);
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (currentItem != 0) {
		    DataManager.saveSingleData(currentItem,
			    title_field.getText(), description_field.getText(),
			    category_box.getSelectedIndex() + 1,
			    (int) pieces_spinner.getValue());
		}
	    }
	});

	JLabel lblBox = new JLabel("Fach");
	lblBox.setBounds(10, 276, 64, 14);
	add(lblBox);

	comboBox_storage = new JComboBox<String>();
	comboBox_storage.setBounds(123, 248, 167, 20);
	add(comboBox_storage);
	comboBox_storage.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (currentItem != 0) {
		    updateList();
		}
	    }

	});
	comboBox_box = new JComboBox<String>();
	comboBox_box.setBounds(123, 273, 167, 20);
	add(comboBox_box);

	// updateList();

	JLabel lblDatasheet = new JLabel("Datenblatt");
	lblDatasheet.setBounds(10, 301, 74, 14);
	add(lblDatasheet);

	btnOpenDatasheet = new JButton("\u00D6ffnen");
	btnOpenDatasheet.setBounds(123, 298, 74, 20);
	add(btnOpenDatasheet);
	btnOpenDatasheet.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		DataManager.openDatasheetFromItem(currentItem);
	    }
	});

	btnEditDatasheet = new JButton("Bearbeiten");
	btnEditDatasheet.setBounds(197, 298, 93, 20);
	add(btnEditDatasheet);
	btnEditDatasheet.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		new AddDatasheet(currentItem);
	    }
	});

	if (!DataManager.hasItemDatasheet(currentItem)) {
	    btnOpenDatasheet.setEnabled(false);
	    btnEditDatasheet.setEnabled(false);
	}

    }

    public void update(int i) {
	this.currentItem = i;

	Object[] temp = DataManager.getDataFromSingleItem(i);
	title_field.setText(temp[0].toString());
	title_field.setCaretPosition(0);
	description_field.setText(temp[1].toString());
	description_field.setCaretPosition(0);
	category_box.setSelectedIndex(Integer.parseInt(temp[2].toString()) - 1);
	pieces_spinner.setValue(Integer.parseInt(temp[3].toString()));
	if (temp[4] != null) {
	    Image img = (Image) temp[4];
	    image_label.setIcon(new ImageIcon(img));
	}

	if (!DataManager.hasItemDatasheet(currentItem)) {
	    btnOpenDatasheet.setEnabled(false);
	    btnEditDatasheet.setEnabled(false);
	} else {
	    btnOpenDatasheet.setEnabled(true);
	    btnEditDatasheet.setEnabled(true);
	}

	// combobox updates
	// check if item is in storage
	comboBox_storage.removeAllItems();
	comboBox_box.removeAllItems();
	if (DataManager.isItemInABox(currentItem)) {
	    // Then load all storage boxes
	    String[][] boxList = DataManager.getBoxList();
	    storage_list.clear();
	    for (int j = 0; j < boxList.length; j++) {
		storage_list.add(Integer.parseInt(boxList[j][0]));
		comboBox_storage.addItem(boxList[j][1]);
	    }
	    int[] boxData = DataManager.getBoxIdOfItem(currentItem);
	    int currentStorageBox = boxData[0];
	    int currentBox = boxData[1];
	    int boxCount = Integer.parseInt(boxList[storage_list
		    .indexOf(currentStorageBox)][2]);
	    for (int j = 0; j < boxCount; j++) {
		comboBox_box.addItem("Fach " + (j + 1));
	    }
	    // select the items in comboboxes
	    comboBox_storage.setSelectedIndex(storage_list
		    .indexOf(currentStorageBox));
	    comboBox_box.setSelectedIndex(currentBox - 1);
	}

    }

    private void updateList() {
	comboBox_box.removeAllItems();
	int currentStorageindex = comboBox_storage.getSelectedIndex();
	if (currentStorageindex == -1) {
	    currentStorageindex = 0;
	}
	int boxes = Integer
		.parseInt(DataManager.getBoxList()[currentStorageindex][2]);
	for (int i = 0; i < boxes; i++) {
	    comboBox_box.addItem("Fach " + (i + 1));
	}

    }

}
