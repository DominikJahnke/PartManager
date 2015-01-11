package im.jahnke.partmanager.view;

import im.jahnke.partmanager.model.DataManager;
import im.jahnke.partmanager.model.ShippingGrabbManager;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class OrderPanel extends JPanel {

    FrameView view;
    private JTable table;
    DefaultTableModel model;
    OrderDetailPanel pan;
    String[] columnNames = { "ID", "Datum", "Shop", "Rechnung", "Betrag" };

    public OrderPanel() {
	setLayout(null);
	setPreferredSize(new Dimension(944, 465));

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(24, 38, 362, 263);
	add(scrollPane);

	model = new DefaultTableModel(DataManager.getOrderList(), columnNames) {

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
	table.getColumnModel().getColumn(0).setMinWidth(0);
	table.getColumnModel().getColumn(0).setMaxWidth(0);
	table.getColumnModel().getColumn(0).setWidth(0);
	table.getColumnModel().getColumn(1).setPreferredWidth(160);
	table.getColumnModel().getColumn(2).setPreferredWidth(80);
	table.getColumnModel().getColumn(3).setPreferredWidth(120);
	table.getColumnModel().getColumn(4).setPreferredWidth(70);

	scrollPane.setViewportView(table);

	table.addMouseListener(new MouseAdapter() {

	    public void mouseClicked(MouseEvent e) {
		int id = Integer.parseInt(model.getValueAt(
			table.getSelectedRow(), 0).toString());
		if (pan != null) {
		    get().remove(pan);
		}
		pan = new OrderDetailPanel(id);
		pan.setBounds(450, 38, 450, 580);
		get().add(pan);
		get().repaint();
	    }
	});

	JButton btnAddOrder = new JButton("Hinzuf\u00FCgen");
	btnAddOrder.setBounds(24, 324, 89, 23);
	add(btnAddOrder);
	btnAddOrder.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		new OrderDialog("Bestellung hinzufügen");
	    }
	});
	
	JButton btnEditBox = new JButton("Bearbeiten");
	btnEditBox.setBounds(157, 324, 89, 23);
	add(btnEditBox);

	JButton btnDeleteBox = new JButton("L\u00F6schen");
	btnDeleteBox.setBounds(297, 324, 89, 23);
	add(btnDeleteBox);

	pan = new OrderDetailPanel();
	pan.setBounds(450, 38, 450, 380);
	add(pan);
    }

    public OrderPanel get() {
	return this;
    }

    class OrderDetailPanel extends JPanel {

	int order_id;

	String[] columnNames = { "ID", "Titel", "Stück", "Betrag" };

	JButton btnShippingStatus = new JButton("Sendungsverlauf");
	JTextField fieldShippingStatus = new JTextField("");

	public OrderDetailPanel() {

	}

	public OrderDetailPanel(int order_id) {
	    this.order_id = order_id;
	    setLayout(null);
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(24, 0, 362, 263);
	    add(scrollPane);

	    DefaultTableModel model = new DefaultTableModel(
		    DataManager.getOrderedPositionsList(order_id), columnNames) {

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

	    JTable table = new JTable(model);
	    table.getColumnModel().getColumn(0).setMinWidth(0);
	    table.getColumnModel().getColumn(0).setMaxWidth(0);
	    table.getColumnModel().getColumn(0).setWidth(0);
	    table.getColumnModel().getColumn(1).setPreferredWidth(235);
	    table.getColumnModel().getColumn(2).setPreferredWidth(45);
	    table.getColumnModel().getColumn(3).setPreferredWidth(50);

	    scrollPane.setViewportView(table);

	    btnShippingStatus.setBounds(24, 285, 130, 20);
	    btnShippingStatus.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    getShippingDetails();
		}
	    });
	    add(btnShippingStatus);

	    fieldShippingStatus.setBounds(170, 285, 216, 20);
	    fieldShippingStatus.setEditable(false);
	    add(fieldShippingStatus);

	    //getShippingDetails();

	}

	private void getShippingDetails() {
	    String trackingID = DataManager
		    .getShippingDetailsOfOrder(this.order_id)[1];
	    fieldShippingStatus.setText(ShippingGrabbManager
		    .getDHLDeliveryDate(trackingID));
	}

    }
}