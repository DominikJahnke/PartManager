package im.jahnke.partmanager.view;

import im.jahnke.partmanager.model.DataManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class AddItem extends JDialog {
    JTextField order_id_field;
    JTextField title_field;
    JTextField pieces_field;
    JTextField price_field;
    JTextPane description_textArea;
    JScrollPane scrollPane;
    JComboBox<String> shopComboBox;
    JLabel image_label;
    Image dimg;
    TreeComboBox category_box;

    public AddItem() {

	setTitle("Neues Bauteil hinzuf\u00FCgen");
	setResizable(false);
	setSize(450, 460);
	setLocationRelativeTo(null);
	setModal(true);
	getContentPane().setLayout(null);

	JLabel order_id_label = new JLabel("Bestellnummer");
	order_id_label.setBounds(34, 28, 101, 14);
	getContentPane().add(order_id_label);

	order_id_field = new JTextField();
	order_id_field.setBounds(154, 25, 130, 20);
	getContentPane().add(order_id_field);
	order_id_field.setColumns(10);

	JButton dataGrabberButton = new JButton("Datengrabber");
	dataGrabberButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (shopComboBox.getSelectedItem().toString().equals("Conrad")) {
		    getProductNameConrad(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Reichelt")) {
		    getProductNameReichelt(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Pollin")) {
		    getProductNamePollin(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Digikey")) {
		    getProductNameDigikey(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Voelkner")) {
		    getProductNameVoelkner(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Mouser")) {
		    getProductNameMouser(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("ELV")) {
		    getProductNameElv(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Watterott")) {
		    getProductNameWatterott(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Farnell")) {
		    getProductNameFarnell(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Ebay")) {
		    getProductNameEbay(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Amazon")) {
		    getProductNameAmazon(order_id_field.getText());
		}
	    }
	});
	dataGrabberButton.setBounds(294, 24, 126, 23);
	getContentPane().add(dataGrabberButton);

	JLabel title_label = new JLabel("Titel");
	title_label.setBounds(34, 106, 68, 14);
	getContentPane().add(title_label);

	title_field = new JTextField();
	title_field.setBounds(154, 103, 266, 20);
	getContentPane().add(title_field);
	title_field.setColumns(10);

	JLabel description_label = new JLabel("Beschreibung");
	description_label.setBounds(34, 139, 93, 20);
	getContentPane().add(description_label);

	description_textArea = new JTextPane();
	description_textArea.setContentType("text/html");

	scrollPane = new JScrollPane(description_textArea);
	scrollPane.setBounds(154, 137, 266, 102);
	getContentPane().add(scrollPane);

	category_box = new TreeComboBox();
	category_box.setBounds(154, 250, 266, 20);
	getContentPane().add(category_box);

	JLabel shop_label = new JLabel("Shop");
	shop_label.setBounds(34, 59, 93, 14);
	getContentPane().add(shop_label);

	shopComboBox = new JComboBox<String>();
	shopComboBox.setBounds(154, 56, 266, 20);
	getContentPane().add(shopComboBox);
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

	JLabel category_label = new JLabel("Kategorie");
	category_label.setBounds(34, 253, 101, 14);
	getContentPane().add(category_label);

	pieces_field = new JTextField();
	pieces_field.setBounds(154, 281, 86, 20);
	getContentPane().add(pieces_field);
	pieces_field.setColumns(10);

	price_field = new JTextField();
	price_field.setBounds(154, 312, 86, 20);
	getContentPane().add(price_field);
	price_field.setColumns(10);

	JLabel pieces_label = new JLabel("Anzahl");
	pieces_label.setBounds(34, 281, 101, 20);
	getContentPane().add(pieces_label);

	JLabel price_label = new JLabel("Preis/St\u00FCck");
	price_label.setBounds(34, 312, 93, 20);
	getContentPane().add(price_label);

	image_label = new JLabel();
	image_label.setHorizontalAlignment(SwingConstants.CENTER);
	image_label.setBounds(280, 270, 110, 110);
	getContentPane().add(image_label);

	JButton save_button = new JButton("Speichern");
	save_button.setBounds(154, 355, 89, 23);
	save_button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		DataManager.addSingleItem(title_field.getText(),
			description_textArea.getText(), category_box
				.getSelectedIndex() + 1, dimg, pieces_field
				.getText(), shopComboBox.getSelectedItem()
				.toString(), order_id_field.getText(),
			price_field.getText());
		dispose();
	    }
	});
	getContentPane().add(save_button);

	setVisible(true);
    }
    
    public AddItem(String shop, String article_id, String pieces) {

	setTitle("Neues Bauteil hinzuf\u00FCgen");
	setResizable(false);
	setSize(450, 460);
	setLocationRelativeTo(null);
	setModal(true);
	getContentPane().setLayout(null);

	JLabel order_id_label = new JLabel("Bestellnummer");
	order_id_label.setBounds(34, 28, 101, 14);
	getContentPane().add(order_id_label);

	order_id_field = new JTextField();
	order_id_field.setBounds(154, 25, 130, 20);
	getContentPane().add(order_id_field);
	order_id_field.setColumns(10);
	order_id_field.setText(article_id);

	JButton dataGrabberButton = new JButton("Datengrabber");
	dataGrabberButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (shopComboBox.getSelectedItem().toString().equals("Conrad")) {
		    getProductNameConrad(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Reichelt")) {
		    getProductNameReichelt(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Pollin")) {
		    getProductNamePollin(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Digikey")) {
		    getProductNameDigikey(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Voelkner")) {
		    getProductNameVoelkner(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Mouser")) {
		    getProductNameMouser(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("ELV")) {
		    getProductNameElv(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Watterott")) {
		    getProductNameWatterott(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Farnell")) {
		    getProductNameFarnell(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Ebay")) {
		    getProductNameEbay(order_id_field.getText());
		} else if (shopComboBox.getSelectedItem().toString()
			.equals("Amazon")) {
		    getProductNameAmazon(order_id_field.getText());
		}
	    }
	});
	dataGrabberButton.setBounds(294, 24, 126, 23);
	getContentPane().add(dataGrabberButton);

	JLabel title_label = new JLabel("Titel");
	title_label.setBounds(34, 106, 68, 14);
	getContentPane().add(title_label);

	title_field = new JTextField();
	title_field.setBounds(154, 103, 266, 20);
	getContentPane().add(title_field);
	title_field.setColumns(10);

	JLabel description_label = new JLabel("Beschreibung");
	description_label.setBounds(34, 139, 93, 20);
	getContentPane().add(description_label);

	description_textArea = new JTextPane();
	description_textArea.setContentType("text/html");

	scrollPane = new JScrollPane(description_textArea);
	scrollPane.setBounds(154, 137, 266, 102);
	getContentPane().add(scrollPane);

	category_box = new TreeComboBox();
	category_box.setBounds(154, 250, 266, 20);
	getContentPane().add(category_box);

	JLabel shop_label = new JLabel("Shop");
	shop_label.setBounds(34, 59, 93, 14);
	getContentPane().add(shop_label);

	shopComboBox = new JComboBox<String>();
	shopComboBox.setBounds(154, 56, 266, 20);
	getContentPane().add(shopComboBox);
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
	
	shopComboBox.setSelectedItem(shop);

	JLabel category_label = new JLabel("Kategorie");
	category_label.setBounds(34, 253, 101, 14);
	getContentPane().add(category_label);

	pieces_field = new JTextField();
	pieces_field.setBounds(154, 281, 86, 20);
	getContentPane().add(pieces_field);
	pieces_field.setColumns(10);
	pieces_field.setText("0");

	price_field = new JTextField();
	price_field.setBounds(154, 312, 86, 20);
	getContentPane().add(price_field);
	price_field.setColumns(10);

	JLabel pieces_label = new JLabel("Anzahl");
	pieces_label.setBounds(34, 281, 101, 20);
	getContentPane().add(pieces_label);

	JLabel price_label = new JLabel("Preis/St\u00FCck");
	price_label.setBounds(34, 312, 93, 20);
	getContentPane().add(price_label);

	image_label = new JLabel();
	image_label.setHorizontalAlignment(SwingConstants.CENTER);
	image_label.setBounds(280, 270, 110, 110);
	getContentPane().add(image_label);

	JButton save_button = new JButton("Speichern");
	save_button.setBounds(154, 355, 89, 23);
	save_button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		DataManager.addSingleItem(title_field.getText(),
			description_textArea.getText(), category_box
				.getSelectedIndex() + 1, dimg, pieces_field
				.getText(), shopComboBox.getSelectedItem()
				.toString(), order_id_field.getText(),
			price_field.getText());
		dispose();
	    }
	});
	getContentPane().add(save_button);

	setVisible(true);
    }

    public void getProductNameConrad(String article_id) {
	String conradlink = "http://www.conrad.de/ce/de/product/" + article_id;
	URL link;
	StringBuilder a = null;
	try {
	    link = new URL(conradlink);
	    URLConnection yc = link.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(
		    yc.getInputStream(), "UTF-8"));
	    String inputLine;
	    a = new StringBuilder();
	    while ((inputLine = in.readLine()) != null)
		a.append(inputLine);
	    in.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	String re2 = "<meta name=\"WT.pn\" content=\"(.*?)\"";
	Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		| Pattern.DOTALL);
	Matcher m = p.matcher(a.toString());
	if (m.find()) {
	    title_field.setText(m.group(1));
	    title_field.setCaretPosition(0);
	}

	re2 = "_produktpreis\">€ (.*?)</span>";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    price_field.setText(m.group(1));
	}

	String description_temp = "";
	re2 = "_beschreibung\"> <p><strong>Beschreibung</strong></p> (.*?)</div>";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    description_temp = m.group(1);
	}

	re2 = "_technischedaten2\"> (.*?)</div>";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    description_temp += m.group(1);
	    description_textArea.setText(description_temp);
	    description_textArea.setCaretPosition(0);
	}

	re2 = "<strong>Datenblatt</strong></li> <li><a target=\"_blank\" href=\"(.*?)\"";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    System.out.println(m.group(1));
	}

	re2 = "class=\"MagicZoomPlus\" rel=\"zoom-distance:100px;\" href=\"(.*?)\"> <img";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    System.out.println(m.group(1));
	}

	Image image = null;
	dimg = null;
	try {
	    URL url = new URL("http://www.conrad.de" + m.group(1));
	    image = ImageIO.read(url);
	    dimg = image.getScaledInstance(image_label.getWidth(),
		    image_label.getHeight(), Image.SCALE_SMOOTH);
	} catch (IOException e) {
	}
	image_label.setIcon(new ImageIcon(dimg));

	if (dimg == null) {
	    try {
		URL url = new URL(
			"http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		image = ImageIO.read(url);
		System.out.println(image == null);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (Exception e) {
		// TODO: handle exception
	    }
	}
    }

    public void getProductNameReichelt(String article_id) {

	try {
	    String body = "SEARCH=" + URLEncoder.encode(article_id, "UTF-8");

	    URL url = new URL(
		    "http://www.reichelt.de/index.html?&ACTION=446&amp;LA=446");
	    HttpURLConnection connection = (HttpURLConnection) url
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

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    connection.getInputStream()));
	    String temp = "";
	    for (String line; (line = reader.readLine()) != null;) {
		temp += line;
	    }

	    writer.close();
	    reader.close();

	    String re2 = "ARTICLE=(.*?)&amp";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(temp.toString());
	    String id = "";
	    if (m.find()) {
		id = m.group(1);
	    }

	    String reicheltlink = "http://www.reichelt.de/?ARTICLE=" + id;
	    URL link;
	    StringBuilder a = null;
	    try {
		link = new URL(reicheltlink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    re2 = "<div id=\"av_articleherstbezeichnung\"><h1 class=\"av_fontnormal\">(.*?)</h1></div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    String desc = "";

	    re2 = "<div id=\"av_articletext\" itemprop=\"description\">(.*?)</div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += m.group(1);
	    }

	    re2 = "<div id=\"av_props\">(.*?)</div></div><div id=\"av_tabdata";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += m.group(1);
		description_textArea.setText(desc);
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<div itemprop=\"price\" id=\"av_price\">(.*?)&euro";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "class=\"av_datasheet_description\"><a href=\"(.*?)\"";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1).replaceAll("&#63;", "?")
			.replaceAll("&amp;", "&").replaceAll("&#37;252", "%2"));
	    }

	    re2 = "name=\"iframe\" src=\"(.*?)\"> </iframe>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNamePollin(String article_id) {

	try {

	    article_id = article_id.replaceAll(" ", "+");
	    String pollinlink = "http://www.pollin.de/shop/suchergebnis.html?S_TEXT="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(pollinlink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<div class=\"article odd\"><h3><a href=\"(.*?)\" title";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
		link = new URL(m.group(1));
	    }

	    try {
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    System.out.println(link);

	    re2 = "<h1>(.*?)</h1>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    re2 = "<h3>Produktbeschreibung</h3>(.*?)</div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		description_textArea.setText(m.group(1));
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<span class=\"price\"><strong>(.*?) &euro";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "gallery]\"><img src=\"(.*?)\" alt";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameDigikey(String article_id) {

	try {

	    article_id = article_id.replaceAll(" ", "+");
	    String digikeylink = "http://www.digikey.de/product-search/de?KeyWords="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(digikeylink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<td itemprop=\"description\">(.*?)</td>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    re2 = "<td align=center >1</td><td align=\"right\" >(.*?)</td>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "<table class=\"product-additional-info\" border=0><tr><td class=\"attributes-table-main\" valign=top>(.*?)<form style=";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		description_textArea.setText(m.group(1));
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "class=beablock><a href=\"(.*?)\"><img border=0";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameVoelkner(String article_id) {

	try {

	    article_id = article_id.replaceAll(" ", "+");
	    String voelknerlink = "http://www.voelkner.de/search/fact-search.html?keywords="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(voelknerlink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<div class=\"detail_headline\">    <h1>(.*?)</h1>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    String desc = "";
	    re2 = "<span itemprop=\"description\">(.*?)</span>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += ("<p>" + m.group(1) + "</p><br />");
	    }

	    re2 = "Technische Daten</h3>(.*?)<br/>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += m.group(1);
		description_textArea.setText(desc);
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<span class=\"detail_price_price\" itemprop=\"price\"                                  content=\"(.*?)\"";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "class=\"grouped_elements  zoom\">                        <img src=\"(.*?)\"";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameMouser(String article_id) {

	try {

	    String mouserlink = "http://www.mouser.de/Search/Refine.aspx?Keyword="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(mouserlink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<div id=\"divManufacturerPartNum\"><h1>(.*?)</h1>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    String desc = "";
	    re2 = "<span itemprop=\"description\">(.*?)</span>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += ("<p>" + m.group(1) + "</p><br />");
		title_field.setText(m.group(1) + " - " + title_field.getText());
	    }

	    re2 = "<div id=\"spec\">(.*?)<div style=\"text-align";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		desc += m.group(1);
		desc = desc.replaceAll("border=\"1\"", "border=\"0\"");
		desc = desc.replaceAll("<td class=\"find-similar\">(.*?)</td>",
			"");
		desc = desc
			.replaceAll(
				"<img src='../../../Images/Search/icon_rohs.gif' alt='RoHS-konform' border='0'>",
				"RoHS-konform");
		desc = desc.replaceAll("rel='nofollow'>Einzelheiten(.*?)</a>",
			"");
		System.out.println(desc);
		description_textArea.setText(desc);
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<span id=\"ctl00_ContentMain_ucP_rptrPriceBreaks_ctl01_lblPrice\" style=\"white-space: nowrap\">(.*?) €";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "itemprop=\"image\" src=\"../../../(.*?)\"";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL("http://www.mouser.de/" + m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameElv(String article_id) {

	JOptionPane.showMessageDialog(null,
		"ELV wird momentan nicht unterstützt");
	/*
	 * try {
	 * 
	 * String body = "search=" + URLEncoder.encode(article_id, "UTF-8");
	 * 
	 * URL url = new URL( "http://www.elv.de/Suche/x.aspx/cid_42" );
	 * HttpURLConnection connection = (HttpURLConnection)
	 * url.openConnection(); connection.setRequestMethod( "POST" );
	 * connection.setDoInput( true ); connection.setDoOutput( true );
	 * connection.setUseCaches( false ); connection.setRequestProperty(
	 * "Content-Type", "application/x-www-form-urlencoded" );
	 * connection.setRequestProperty( "Content-Length",
	 * String.valueOf(body.length()) );
	 * 
	 * OutputStreamWriter writer = new OutputStreamWriter(
	 * connection.getOutputStream() ); writer.write( body ); writer.flush();
	 * 
	 * 
	 * BufferedReader reader = new BufferedReader( new
	 * InputStreamReader(connection.getInputStream(), "UTF-8")); String temp
	 * = ""; for ( String line; (line = reader.readLine()) != null; ) { temp
	 * += line; }
	 * 
	 * writer.close(); reader.close();
	 * 
	 * String
	 * re2="<td valign=\"top\" style=\"padding-right: 5px;\"><h1>(.*?)</h1><h2"
	 * ; Pattern p = Pattern.compile(re2,Pattern.CASE_INSENSITIVE |
	 * Pattern.DOTALL); Matcher m = p.matcher(temp); if (m.find()) {
	 * title_field.setText(m.group(1)); title_field.setCaretPosition(0); }
	 * 
	 * re2=
	 * "<div id=\"tab_content2\" style=\"display: none;\">(.*?)</div><div id=\"tab_content3"
	 * ; p = Pattern.compile(re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	 * m = p.matcher(temp); if (m.find()) {
	 * description_textArea.setText(m.group(1));
	 * description_textArea.setCaretPosition(0); }
	 * 
	 * 
	 * re2="<div class=\"price\">EUR (.*?)\\*";
	 * 
	 * p = Pattern.compile(re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL); m
	 * = p.matcher(temp); if (m.find()) {
	 * price_field.setText(m.group(1).trim()); }
	 * 
	 * re2="<img width=\"238\" src=\"(.*?)\""; p =
	 * Pattern.compile(re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL); m =
	 * p.matcher(temp); if (m.find()) { System.out.println(m.group(1)); }
	 * 
	 * Image image = null; dimg = null; try { url = new URL(
	 * "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png"
	 * );
	 * 
	 * image = ImageIO.read(url); System.out.println(image==null); dimg =
	 * image.getScaledInstance(image_label.getWidth(),
	 * image_label.getHeight(), Image.SCALE_SMOOTH); } catch (IOException e)
	 * { } image_label.setIcon(new ImageIcon(dimg));
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 */
    }

    public void getProductNameWatterott(String article_id) {

	try {

	    article_id = article_id.trim();
	    String watterottlink = "http://www.watterott.com/index.php?page=search&page_action=query&keywords="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(watterottlink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<td class=\"productPreviewContent\">        <h2><a href=\"(.*?)\"";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
		link = new URL(m.group(1));
	    }

	    try {
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    System.out.println(link);

	    re2 = "<div id=\"contentbereich\"><h1>(.*?)</h1>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    re2 = "<h2>Produktbeschreibung</h2>(.*?)</div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		String temp = "";
		temp = m.group(1);
		temp = temp.replaceAll(" size=\"2\"", "");
		description_textArea.setText(temp);
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<span class=\"price\"> (.*?) EUR";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "<div id=\"productinfoimages\"><a rel=\"gallery\" href=\"(.*?)\"";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameFarnell(String article_id) {

	try {

	    article_id = article_id.trim();

	    String farnelllink = "http://de.farnell.com/jsp/search/browse.jsp?N=0&Ntk=gensearch&Ntt="
		    + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(farnelllink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<div id=\"headerContainer\"><h1>(.*?)</h1></div>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    String desc = "";

	    re2 = "<div class=\"prodAttrColumn\">(.*?)</div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {

		desc += m.group(1);
	    }

	    re2 = "<div class=\"features-and-benefits-container\">(.*?)</div></div>";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {

		desc += m.group(1);
		description_textArea.setText(desc);
		description_textArea.setCaretPosition(0);
	    }

	    re2 = "<span class=\"nowrap mfProductDescriptionAndPrice\"><!--item.LIST_POP_UP--><!--item.LIST_POP_UP-->(.*?) &euro";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "class=\"mfImageAlt productimg\" style=\"display:block;width:auto;height:auto;\" src=\"(.*?)\"";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL("http://de.farnell.com" + m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(dimg));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameEbay(String article_id) {

	try {

	    String ebaylink = "http://www.ebay.de/itm/" + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(ebaylink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<h1 class=\"it-ttl\" itemprop=\"name\" id=\"itemTitle\"><span class=\"g-hdn\">Details zu  &nbsp;</span>(.*?)</h1>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    re2 = "itemprop=\"image\" src=\"(.*?)\"";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
		image = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(image));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getProductNameAmazon(String article_id) {

	try {

	    String ebaylink = "http://www.amazon.de/dp/" + article_id;
	    URL link = null;
	    StringBuilder a = null;
	    try {
		link = new URL(ebaylink);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
			yc.getInputStream(), "UTF-8"));
		String inputLine;
		a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		    a.append(inputLine);
		in.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    String re2 = "<span id=\"productTitle\" class=\"a-size-large\">(.*?)</span>";
	    Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		    | Pattern.DOTALL);
	    Matcher m = p.matcher(a.toString());
	    if (m.find()) {
		title_field.setText(m.group(1));
		title_field.setCaretPosition(0);
	    }

	    re2 = "<span id=\"priceblock_ourprice\" class=\"a-size-medium a-color-price\">EUR (.*?)</span>";

	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		price_field.setText(m.group(1));
	    }

	    re2 = "data-a-dynamic-image=\"" + Pattern.quote("{")
		    + "&quot;(.*?)&";
	    p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    m = p.matcher(a.toString());
	    if (m.find()) {
		System.out.println(m.group(1));
	    }

	    Image image = null;
	    dimg = null;
	    try {
		URL url = new URL(m.group(1));
		image = ImageIO.read(url);
		dimg = image.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
		image = image.getScaledInstance(image_label.getWidth(),
			image_label.getHeight(), Image.SCALE_SMOOTH);
	    } catch (IOException e) {
	    }
	    if (dimg == null) {
		try {
		    URL url = new URL(
			    "http://www.euregio-aachen.de/wp-content/themes/mh-magazine-lite/images/noimage_174x131.png");

		    image = ImageIO.read(url);
		    System.out.println(image == null);
		    dimg = image.getScaledInstance(image_label.getWidth(),
			    image_label.getHeight(), Image.SCALE_SMOOTH);
		} catch (Exception e) {
		    // TODO: handle exception
		}
	    }
	    image_label.setIcon(new ImageIcon(image));

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
