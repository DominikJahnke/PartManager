package im.jahnke.partmanager.view.partsTabbedPanel;

import im.jahnke.partmanager.model.DataManager;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ShopTab extends JPanel {
    private JTable table;
    private String[] columns = { "Shop", "Artikelnummer", "Preis/Stück" };
    private TableModel model;
    private JTextField order_id_field;
    private JTextField price_field;
    private int part_id;
    JComboBox<String> shopComboBox;

    public ShopTab() {
	setLayout(null);
	model = new DefaultTableModel() {

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
	JScrollPane scrollPane = new JScrollPane(table);
	scrollPane.setBounds(10, 10, 435, 249);
	add(scrollPane);
	table.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		shopComboBox.setSelectedItem(table.getModel()
			.getValueAt(row, 0));
		order_id_field.setText(table.getModel().getValueAt(row, 1)
			.toString());
		price_field.setText(table.getModel().getValueAt(row, 2)
			.toString());
	    }
	});

	shopComboBox = new JComboBox<String>();
	shopComboBox.setBounds(81, 270, 105, 20);
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
	add(shopComboBox);

	JLabel lblShop = new JLabel("Shop");
	lblShop.setBounds(10, 276, 34, 14);
	add(lblShop);

	JLabel lblOrderId = new JLabel("Bestellnummer");
	lblOrderId.setBounds(203, 273, 88, 14);
	add(lblOrderId);

	order_id_field = new JTextField();
	order_id_field.setBounds(289, 270, 156, 20);
	add(order_id_field);
	order_id_field.setColumns(10);

	JLabel lblPrice = new JLabel("Preis/St\u00FCck");
	lblPrice.setBounds(10, 300, 64, 20);
	add(lblPrice);

	price_field = new JTextField();
	price_field.setBounds(81, 300, 105, 20);
	add(price_field);
	price_field.setColumns(10);

	JButton btnAdd = new JButton("Hinzuf\u00FCgen");
	btnAdd.setBounds(39, 351, 89, 23);
	add(btnAdd);
	btnAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		DataManager.addShopPositionToItem(part_id,
			shopComboBox.getSelectedItem(),
			order_id_field.getText(), price_field.getText());
		update(part_id);
	    }
	});

	JButton btnSave = new JButton("Speichern");
	btnSave.setBounds(138, 351, 89, 23);
	add(btnSave);
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		/*
		 * int row = table.getSelectedRow(); String shop =
		 * table.getModel().getValueAt(row, 0).toString(); String
		 * order_id = table.getModel().getValueAt(row, 1).toString();
		 * String price = table.getModel().getValueAt(row,
		 * 2).toString(); DataManager.saveSingleShopPosition(shop,
		 * order_id, price); update(part_id);
		 */
		JOptionPane.showMessageDialog(null, "Noch nicht verfügbar");
	    }
	});

	JButton btnDelete = new JButton("L\u00F6schen");
	btnDelete.setBounds(237, 351, 89, 23);
	add(btnDelete);
	btnDelete.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		String shop = table.getModel().getValueAt(row, 0).toString();
		String order_id = table.getModel().getValueAt(row, 1)
			.toString();
		String price = table.getModel().getValueAt(row, 2).toString();
		DataManager.deleteShopPositionFromItem(shop, order_id, price);
		update(part_id);
	    }
	});

	JButton btnOrder = new JButton("Bestellen");
	btnOrder.setBounds(336, 351, 89, 23);
	add(btnOrder);
	btnOrder.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		String shop = table.getValueAt(row, 0).toString();
		String article_id = table.getValueAt(row, 1).toString();
		String url = "";
		switch (shop) {
		case "Conrad":
		    url = "http://www.conrad.de/ce/de/product/" + article_id;
		    break;
		case "Reichelt":
		    try {
			String body = "SEARCH="
				+ URLEncoder.encode(article_id, "UTF-8");

			URL link = new URL(
				"http://www.reichelt.de/index.html?&ACTION=446&amp;LA=446");
			HttpURLConnection connection = (HttpURLConnection) link
				.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
				String.valueOf(body.length()));

			OutputStreamWriter writer = new OutputStreamWriter(
				connection.getOutputStream());
			writer.write(body);
			writer.flush();

			BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection
					.getInputStream()));
			String temp = "";
			for (String line; (line = reader.readLine()) != null;) {
			    temp += line;
			}

			writer.close();
			reader.close();

			String re2 = "ARTICLE=(.*?)&amp";
			Pattern p = Pattern.compile(re2,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(temp.toString());
			String id = "";
			if (m.find()) {
			    id = m.group(1);
			}

			url = "http://www.reichelt.de/?ARTICLE=" + id;
		    } catch (Exception e2) {

		    }
		    break;
		case "Pollin":
		    try {
			String pollinlink = "http://www.pollin.de/shop/suchergebnis.html?S_TEXT="
				+ article_id;
			URL link = null;
			StringBuilder a = null;
			try {
			    link = new URL(pollinlink);
			    URLConnection yc = link.openConnection();
			    BufferedReader in = new BufferedReader(
				    new InputStreamReader(yc.getInputStream(),
					    "UTF-8"));
			    String inputLine;
			    a = new StringBuilder();
			    while ((inputLine = in.readLine()) != null)
				a.append(inputLine);
			    in.close();
			} catch (Exception e1) {
			    e1.printStackTrace();
			}

			String re2 = "<div class=\"article odd\"><h3><a href=\"(.*?)\" title";
			Pattern p = Pattern.compile(re2,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(a.toString());
			if (m.find()) {
			    url = m.group(1);
			}
		    } catch (Exception e2) {

		    }
		    break;
		case "Digikey":
		    url = "http://www.digikey.de/product-search/de?KeyWords="
			    + article_id;
		    break;
		case "Voelkner":
		    url = "http://www.voelkner.de/search/fact-search.html?keywords="
			    + article_id;
		    break;
		case "Mouser":
		    url = "http://www.mouser.de/Search/Refine.aspx?Keyword="
			    + article_id;
		    break;
		case "ELV":
		    JOptionPane.showMessageDialog(null,
			    "ELV wird momentan nicht unterstützt");
		    break;
		case "Watterott":
		    String watterottlink = "http://www.watterott.com/index.php?page=search&page_action=query&keywords="
			    + article_id;
		    URL link = null;
		    StringBuilder a = null;
		    try {
			link = new URL(watterottlink);
			URLConnection yc = link.openConnection();
			BufferedReader in = new BufferedReader(
				new InputStreamReader(yc.getInputStream(),
					"UTF-8"));
			String inputLine;
			a = new StringBuilder();
			while ((inputLine = in.readLine()) != null)
			    a.append(inputLine);
			in.close();
		    } catch (Exception e2) {
			e2.printStackTrace();
		    }

		    String re2 = "<td class=\"productPreviewContent\">        <h2><a href=\"(.*?)\"";
		    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
			    | Pattern.DOTALL);
		    Matcher m = p.matcher(a.toString());
		    if (m.find()) {
			System.out.println(m.group(1));
			url = m.group(1);
		    }
		    break;
		case "Farnell":
		    url = "http://de.farnell.com/jsp/search/browse.jsp?N=0&Ntk=gensearch&Ntt="
			    + article_id;
		    break;
		case "Ebay":
		    url = "http://www.ebay.de/itm/" + article_id;
		    break;
		case "Amazon":
		    url = "http://www.amazon.de/dp/" + article_id;
		default:
		    break;
		}

		try {
		    System.out.println(url);
		    if (!url.equals("")) {
			Desktop.getDesktop().browse(new URI(url));
			Thread.sleep(1000);
		    }
		} catch (IOException | URISyntaxException
			| InterruptedException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	});

    }

    public void update(int part_id) {

	this.part_id = part_id;

	model = new DefaultTableModel(DataManager.getShopDataFromItem(part_id),
		columns) {

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
	table.getColumnModel().getColumn(0).setPreferredWidth(120);
	table.getColumnModel().getColumn(1).setPreferredWidth(150);
	table.getColumnModel().getColumn(2).setPreferredWidth(90);
	// table.setCellSelectionEnabled(false);
	table.revalidate();
	table.repaint();

	table.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {

	    }

	});
	order_id_field.setText("");
	price_field.setText("");
	shopComboBox.setSelectedIndex(0);
    }
}
