package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.DataManager;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;

public class OrderDialog extends JDialog {

    JComboBox<String> shopComboBox;
    JComboBox<String> comboBox_delivery;
    DefaultTableModel model;
    JTable table;
    String[] columnNames = { "Artikelnr.", "Titel", "Menge", "Gesamtpreis" };
    ArrayList<String[]> tempdata = new ArrayList<String[]>();
    String[][] data = new String[0][4];
    private JTextField textField_articleNumber;
    private JTextField textField_pieces;
    private JTextField textField_articleSum;
    private JTextField textField_deliveryCosts;
    private JTextField textField_totalSum;
    private JTextField textField_deliveryID;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;

    public OrderDialog(String windowTitle) {

	setTitle(windowTitle);
	setResizable(false);
	setSize(520, 520);
	setLocationRelativeTo(null);
	setModal(true);
	getContentPane().setLayout(null);

	for (int i = 0; i < data.length; i++) {
	    for (int j = 0; j < data[0].length; j++) {
		data[i][j] = "";
	    }
	}

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(24, 58, 462, 170);
	getContentPane().add(scrollPane);

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

	table = new JTable(model);
	table.getColumnModel().getColumn(0).setPreferredWidth(80);
	table.getColumnModel().getColumn(1).setPreferredWidth(220);
	table.getColumnModel().getColumn(2).setPreferredWidth(60);
	table.getColumnModel().getColumn(3).setPreferredWidth(100);

	scrollPane.setViewportView(table);

	JLabel shop_label = new JLabel("Shop");
	shop_label.setBounds(34, 59, 93, 14);
	getContentPane().add(shop_label);

	shopComboBox = new JComboBox<String>();
	shopComboBox.setBounds(24, 28, 462, 20);
	getContentPane().add(shopComboBox);
	
	JLabel lblArtikelnr = new JLabel("Artikelnr.");
	lblArtikelnr.setBounds(24, 239, 57, 14);
	getContentPane().add(lblArtikelnr);
	
	textField_articleNumber = new JTextField();
	textField_articleNumber.setBounds(93, 236, 93, 20);
	getContentPane().add(textField_articleNumber);
	textField_articleNumber.setColumns(10);
	
	JLabel lblMenge = new JLabel("Menge");
	lblMenge.setBounds(206, 239, 46, 14);
	getContentPane().add(lblMenge);
	
	textField_pieces = new JTextField();
	textField_pieces.setBounds(262, 236, 36, 20);
	getContentPane().add(textField_pieces);
	textField_pieces.setColumns(10);
	
	JLabel lblGesamtpreis = new JLabel("Gesamtpreis");
	lblGesamtpreis.setBounds(318, 239, 68, 14);
	getContentPane().add(lblGesamtpreis);
	
	textField_articleSum = new JTextField();
	textField_articleSum.setBounds(393, 236, 93, 20);
	getContentPane().add(textField_articleSum);
	textField_articleSum.setColumns(10);
	
	JButton btnAdd = new JButton("Hinzuf\u00FCgen");
	btnAdd.setBounds(24, 273, 136, 20);
	getContentPane().add(btnAdd);
	btnAdd.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int article_id;
		if ((article_id = DataManager.isItemAlreadyInDatabase(shopComboBox.getSelectedItem().toString(), textField_articleNumber.getText()))>0) {
		    //available, add to table
		    String[] dataForTable = new String[4];
		    dataForTable[0] = textField_articleNumber.getText();
		    dataForTable[1] = DataManager.getDataFromSingleItem(article_id)[0].toString();
		    dataForTable[2] = textField_pieces.getText();
		    dataForTable[3] = textField_articleSum.getText();
		    tempdata.add(dataForTable);
		    data = new String[tempdata.size()][4];
		    for (int i = 0; i < data.length; i++) {
			data[i] = tempdata.get(i);
		    }
		    updateTable();
		} else {
		    //not in db, open add dialog
		    new AddItem(shopComboBox.getSelectedItem().toString(), textField_articleNumber.getText(), textField_pieces.getText());
		}
		System.out.println(article_id);
	    }
	});
	
	JButton btnEdit = new JButton("Bearbeiten");
	btnEdit.setBounds(186, 273, 136, 20);
	getContentPane().add(btnEdit);
	
	JButton btnDelete = new JButton("L\u00F6schen");
	btnDelete.setBounds(350, 273, 136, 20);
	getContentPane().add(btnDelete);
	
	JSeparator separator = new JSeparator();
	separator.setBounds(24, 311, 462, 2);
	getContentPane().add(separator);
	
	JLabel lblVersandkosten = new JLabel("Versandkosten");
	lblVersandkosten.setBounds(24, 324, 84, 14);
	getContentPane().add(lblVersandkosten);
	
	textField_deliveryCosts = new JTextField();
	textField_deliveryCosts.setBounds(118, 321, 86, 20);
	getContentPane().add(textField_deliveryCosts);
	textField_deliveryCosts.setColumns(10);
	
	JLabel lblKostenGesamt = new JLabel("Kosten Gesamt");
	lblKostenGesamt.setBounds(24, 347, 84, 14);
	getContentPane().add(lblKostenGesamt);
	
	textField_totalSum = new JTextField();
	textField_totalSum.setBounds(118, 344, 86, 20);
	getContentPane().add(textField_totalSum);
	textField_totalSum.setColumns(10);
	
	JButton btnSpeichern = new JButton("Speichern");
	btnSpeichern.setBounds(118, 446, 114, 20);
	getContentPane().add(btnSpeichern);
	
	JButton btnAbbrechen = new JButton("Abbrechen");
	btnAbbrechen.setBounds(272, 446, 114, 20);
	getContentPane().add(btnAbbrechen);
	
	JLabel lblVersandPer = new JLabel("Versand per");
	lblVersandPer.setBounds(233, 324, 74, 14);
	getContentPane().add(lblVersandPer);
	
	comboBox_delivery = new JComboBox<String>();
	comboBox_delivery.setBounds(350, 321, 136, 20);
	getContentPane().add(comboBox_delivery);
	
	JLabel lblSendungsnummer = new JLabel("Sendungsnummer");
	lblSendungsnummer.setBounds(233, 347, 102, 14);
	getContentPane().add(lblSendungsnummer);
	
	textField_deliveryID = new JTextField();
	textField_deliveryID.setBounds(350, 344, 136, 20);
	getContentPane().add(textField_deliveryID);
	textField_deliveryID.setColumns(10);
	
	JLabel lblAuftragsnummer = new JLabel("Auftragsnummer");
	lblAuftragsnummer.setBounds(24, 382, 93, 14);
	getContentPane().add(lblAuftragsnummer);
	
	textField = new JTextField();
	textField.setBounds(118, 379, 86, 20);
	getContentPane().add(textField);
	textField.setColumns(10);
	
	JLabel lblBestelltAm = new JLabel("Bestellt am");
	lblBestelltAm.setBounds(24, 407, 68, 14);
	getContentPane().add(lblBestelltAm);
	
	textField_1 = new JTextField();
	textField_1.setBounds(118, 404, 86, 20);
	getContentPane().add(textField_1);
	textField_1.setColumns(10);
	
	textField_2 = new JTextField();
	textField_2.setBounds(400, 404, 86, 20);
	getContentPane().add(textField_2);
	textField_2.setColumns(10);
	
	JLabel lblEingegangenAm = new JLabel("Lieferung eingegangen am");
	lblEingegangenAm.setBounds(233, 407, 153, 14);
	getContentPane().add(lblEingegangenAm);
	comboBox_delivery.addItem("DHL");
	comboBox_delivery.addItem("Deutsche Post");
	comboBox_delivery.addItem("DPD");
	comboBox_delivery.addItem("Hermes");
	comboBox_delivery.addItem("UPS");
	comboBox_delivery.addItem("Sonstige");
	
	shopComboBox.addItem("Conrad");
	shopComboBox.addItem("Reichelt");
	shopComboBox.addItem("Pollin");
	shopComboBox.addItem("Digikey");
	shopComboBox.addItem("Voelkner");
	shopComboBox.addItem("Mouser");
	shopComboBox.addItem("ELV");
	shopComboBox.addItem("Watterott");
	shopComboBox.addItem("Farnell");
	shopComboBox.addItem("Ebay");
	shopComboBox.addItem("Amazon");

	setVisible(true);

    }

    public void updateTable() {
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
	table.getColumnModel().getColumn(0).setPreferredWidth(80);
	table.getColumnModel().getColumn(1).setPreferredWidth(220);
	table.getColumnModel().getColumn(2).setPreferredWidth(60);
	table.getColumnModel().getColumn(3).setPreferredWidth(100);
	table.revalidate();
	table.repaint();
    }
}
