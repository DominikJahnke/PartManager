package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class DataManager {

    public static String[][] getData() {
	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM parts;");

	    while (rs.next()) {
		temp = new String[2];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("title");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][2];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static List<String> getChildren(List<String> list,
	    String[][] hierarchy, String node) {
	boolean nochildren = true;

	for (int i = 0; i < hierarchy.length; i++) {
	    if (hierarchy[i][1].equals(node)) {
		nochildren = false;
		getChildren(list, hierarchy, hierarchy[i][0]);
	    }
	}
	if (nochildren) {
	    System.out.println(node);
	    list.add(node);
	}
	return list;
    }

    public static String[][] getDataByCategory(int category,
	    String[][] hierarchy) {
	List<String> list = getChildren(new ArrayList<String>(), hierarchy, ""
		+ (category));

	String sql = "";

	for (int i = 0; i < list.size(); i++) {
	    sql += (" OR category=" + list.get(i));
	}

	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM parts WHERE category="
			    + category + sql + " ;");

	    while (rs.next()) {
		temp = new String[2];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("title");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][2];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static String[][] searchData(String keyword) {
	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT shop_positions.part_id, parts.id,"
			    + " title, description, shop_positions.order_id FROM"
			    + " parts, shop_positions WHERE shop_positions.part_id"
			    + " = parts.id AND (title LIKE '%" + keyword
			    + "%' OR" + " description LIKE '%" + keyword
			    + "%' OR" + " shop_positions.order_id LIKE '%"
			    + keyword + "%');");

	    while (rs.next()) {
		temp = new String[2];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("title");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][2];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static Object[] getDataFromSingleItem(int itemID) {
	Object[] temp = null;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM parts WHERE id="
		    + Integer.toString(itemID) + " LIMIT 1;");

	    while (rs.next()) {
		temp = new Object[5];
		temp[0] = rs.getString("title");
		temp[1] = rs.getString("description");
		temp[2] = rs.getString("category");
		temp[3] = rs.getString("pieces");

		if (rs.getObject("image") != null) {
		    InputStream is = rs.getBinaryStream("image");
		    Image image = ImageIO.read(is);
		    temp[4] = image;
		} else {
		    temp[4] = null;
		}

	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return temp;
    }

    public static void addSingleItem(String title, String description,
	    int category, Image image, String pieces, String shop,
	    String order_id, String price) {
	int id = getNextNewPartID();

	Connection c = null;
	PreparedStatement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    String sql = "INSERT INTO parts(id,title,description,category,image,pieces) VALUES (?, ?, ?, ?, ?, ?);";
	    stmt = c.prepareStatement(sql);
	    stmt.setInt(1, id);
	    System.out.println("ID: " + id);
	    stmt.setString(2, title);
	    stmt.setString(3, description);
	    stmt.setInt(4, category);
	    // construct the buffered image
	    BufferedImage bImage = new BufferedImage(image.getWidth(null),
		    image.getHeight(null), BufferedImage.TYPE_INT_RGB);

	    // obtain it's graphics
	    Graphics2D bImageGraphics = bImage.createGraphics();

	    // draw the Image (image) into the BufferedImage (bImage)
	    bImageGraphics.drawImage(image, null, null);

	    // cast it to rendered image
	    RenderedImage rImage = (RenderedImage) bImage;
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ImageIO.write((RenderedImage) rImage, "jpg", os);
	    InputStream fis = new ByteArrayInputStream(os.toByteArray());
	    stmt.setBinaryStream(5, fis, (int) os.size());
	    stmt.setInt(6, Integer.parseInt(pieces));
	    c.setAutoCommit(false);
	    stmt.execute();
	    c.commit();
	    fis.close();
	    c.close();
	    int shop_id = getNextNewShopID();
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    sql = "INSERT INTO shop_positions(id,part_id,shop,order_id,price) VALUES (?, ?, ?, ?, ?);";
	    stmt = c.prepareStatement(sql);
	    stmt.setInt(1, shop_id);
	    stmt.setInt(2, id);
	    stmt.setString(3, shop);
	    stmt.setString(4, order_id);
	    stmt.setString(5, price);

	    c.setAutoCommit(false);
	    stmt.execute();

	    c.commit();
	    fis.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}

    }

    public static int getNextNewPartID() {
	Connection c = null;
	Statement stmt = null;
	int id = 0;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT MAX(id) AS new_id FROM parts;");

	    while (rs.next()) {
		id = rs.getInt("new_id");

	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
	return (id + 1);
    }

    public static int getNextNewShopID() {
	Connection c = null;
	Statement stmt = null;
	int id = 0;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT MAX(id) AS new_id FROM shop_positions;");

	    while (rs.next()) {
		id = rs.getInt("new_id");

	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
	return (id + 1);
    }

    public static int getNextNewBoxID() {
	Connection c = null;
	Statement stmt = null;
	int id = 0;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT MAX(id) AS new_id FROM boxes;");

	    while (rs.next()) {
		id = rs.getInt("new_id");

	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
	return (id + 1);
    }

    public static void deleteSingleItem(int id) {
	Connection c2 = null;
	Statement stmt2 = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c2 = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt2 = c2.createStatement();
	    String query = "DELETE FROM parts WHERE id=" + id + "";
	    System.out.println(query);
	    stmt2.execute(query);
	    c2.setAutoCommit(false);
	    c2.commit();
	    stmt2.close();
	    c2.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}

	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    String query = "DELETE FROM shop_positions WHERE part_id=" + id
		    + ";";
	    System.out.println(query);
	    stmt.execute(query);
	    c.setAutoCommit(false);
	    c.commit();

	    stmt = c.createStatement();
	    query = "DELETE FROM datasheets WHERE part_id=" + id + ";";
	    System.out.println(query);
	    stmt.execute(query);
	    c.setAutoCommit(false);
	    c.commit();

	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}

    }

    public static Object[][] getShopDataFromItem(int id) {
	Object[][] result = null;
	List<Object[]> data = new ArrayList<>();
	Object[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM shop_positions WHERE part_id="
			    + id + ";");

	    while (rs.next()) {
		temp = new Object[3];
		temp[0] = rs.getString("shop");
		temp[1] = rs.getString("order_id");
		temp[2] = rs.getString("price");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][3];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static void addShopPositionToItem(int part_id, Object shop,
	    String order_id, String price) {
	String count = "";
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT COUNT(*) AS counting FROM shop_positions WHERE shop=\""
			    + shop
			    + "\" AND part_id=\""
			    + part_id
			    + "\" AND price=\"" + price + "\";");

	    while (rs.next()) {
		count = rs.getString("counting");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	if (Integer.parseInt(count) > 0) {
	    JOptionPane.showMessageDialog(null,
		    "Fehler. Eintrag bereits vorhanden.");
	    return;
	}

	Connection c2 = null;
	PreparedStatement stmt2 = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c2 = DriverManager.getConnection("jdbc:sqlite:database.db");
	    String sql = "INSERT INTO shop_positions(id,part_id,shop,order_id,price) VALUES (?, ?, ?, ?, ?);";
	    stmt2 = c2.prepareStatement(sql);
	    stmt2.setInt(1, getNextNewShopID());
	    stmt2.setString(2, Integer.toString(part_id));
	    stmt2.setString(3, shop.toString());
	    stmt2.setString(4, order_id);
	    stmt2.setString(5, price);
	    c2.setAutoCommit(false);
	    stmt2.execute();
	    c2.commit();
	    stmt2.close();
	    c2.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}

    }

    public static void deleteShopPositionFromItem(String shop, String order_id,
	    String price) {
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    String query = "DELETE FROM shop_positions WHERE shop=\"" + shop
		    + "\" AND order_id=\"" + order_id + "\" AND price=\""
		    + price + "\";";
	    System.out.println(query);
	    c.setAutoCommit(false);
	    stmt.execute(query);
	    c.commit();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
    }

    public static String[][] getCategoriesArray() {
	ArrayList<String[]> list = new ArrayList<String[]>();
	String result[][] = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    Connection c = DriverManager
		    .getConnection("jdbc:sqlite:database.db");
	    Statement stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM categories;");

	    String[] temp;
	    while (rs.next()) {
		temp = new String[3];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("parent");
		temp[2] = rs.getString("title");
		list.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();
	    result = new String[list.size()][3];
	    for (int i = 0; i < list.size(); i++) {
		result[i] = list.get(i);
	    }

	} catch (Exception e) {

	}
	return result;
    }

    public static void saveSingleData(int currentItem, String title,
	    String description, int category, int pieces) {
	try {
	    Class.forName("org.sqlite.JDBC");
	    Connection c = DriverManager
		    .getConnection("jdbc:sqlite:database.db");
	    c.setAutoCommit(false);

	    String query = "UPDATE parts SET title=?, description=?, category=?, pieces=? WHERE id=?";
	    PreparedStatement stmt = c.prepareStatement(query);

	    stmt.setString(1, title);
	    stmt.setString(2, description);
	    stmt.setInt(3, category);
	    stmt.setInt(4, pieces);
	    stmt.setInt(5, currentItem);
	    stmt.execute();
	    c.commit();
	    stmt.close();

	    /*
	     * query =
	     * "UPDATE box_positions SET box_id=?, box=? WHERE part_id=?"; stmt
	     * = c.prepareStatement(query);
	     * 
	     * stmt.setString(1, Integer.toString(storage_box));
	     * stmt.setString(2, Integer.toString(box)); stmt.setInt(3,
	     * currentItem); stmt.execute(); c.commit(); stmt.close();
	     */

	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
    }

    public static void saveSingleShopPosition(String shop, String order_id,
	    String price) {
	try {
	    Class.forName("org.sqlite.JDBC");
	    Connection c = DriverManager
		    .getConnection("jdbc:sqlite:database.db");
	    c.setAutoCommit(false);

	    String query = "UPDATE shop_positions SET shop=\""
		    + shop
		    + "\", order_id=\""
		    + order_id
		    + "\", price=\""
		    + price
		    + "\" WHERE id=(SELECT id FROM shop_positions WHERE shop=\""
		    + shop + "\" AND order_id=\"" + order_id
		    + "\" AND price=\"" + price + "\")";
	    Statement stmt = c.createStatement();

	    stmt.execute(query);
	    System.out.println(query);
	    c.commit();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
    }

    public static String[][] getBoxList() {
	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM boxes;");

	    while (rs.next()) {
		temp = new String[3];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("name");
		temp[2] = rs.getString("boxes");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][3];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static String[][] getOrderList() {
	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM orders;");
	    Date date;
	    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
	    String formattedDate;
	    while (rs.next()) {
		temp = new String[6];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("date_ordered");
		date = new Date(Long.parseLong(temp[1]) * 1000L);
		formattedDate = sdf.format(date);
		temp[1] = formattedDate;
		temp[2] = rs.getString("shop");
		temp[3] = rs.getString("order_number");
		temp[4] = rs.getString("invoice_amount");
		temp[5] = rs.getString("conveyance");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][6];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static String[][] getOrderedPositionsList(int order_id) {
	String[][] result = null;
	List<String[]> data = new ArrayList<>();
	String[] temp;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT  id, title, order_positions.pieces, "
			    + "total_cost FROM order_positions, parts WHERE"
			    + " order_positions.part_id=parts.id AND "
			    + "order_positions.order_id=" + order_id + ";");

	    while (rs.next()) {
		temp = new String[4];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("title");
		temp[2] = rs.getString("pieces");
		temp[3] = rs.getString("total_cost");
		data.add(temp);
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    result = new String[data.size()][4];

	    for (int i = 0; i < data.size(); i++) {
		temp = data.get(i);
		for (int j = 0; j < temp.length; j++) {
		    result[i][j] = temp[j];
		}
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static String[] getShippingDetailsOfOrder(int order_id) {
	String[] data = new String[2];
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM orders WHERE id="
		    + order_id + ";");

	    while (rs.next()) {
		data[0] = rs.getString("conveyance");
		data[1] = rs.getString("tracking_id");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return data;
    }

    /**
     * 
     * @param shop
     * @param article_id
     * @return If item is in database, return the part id. Else return -1
     */
    public static int isItemAlreadyInDatabase(String shop, String article_id) {
	int result = 0;
	System.out.println(shop + " " + article_id);
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT COUNT(*) AS count FROM shop_positions WHERE order_id='"
			    + article_id + "' AND shop='" + shop + "'");

	    while (rs.next()) {
		System.out.println(rs.getInt("count"));
		if (rs.getInt("count") < 1) {
		    result = -1;
		}
	    }

	    stmt = c.createStatement();
	    rs = stmt
		    .executeQuery("SELECT * FROM shop_positions WHERE order_id='"
			    + article_id + "' AND shop='" + shop + "'");
	    while (rs.next()) {
		result = rs.getInt("part_id");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return result;
    }

    public static boolean isItemInABox(int item_id) {
	boolean isInABox = false;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT COUNT(*) as count FROM box_positions WHERE part_id="
			    + item_id + ";");

	    while (rs.next()) {
		if (rs.getInt("count") > 0) {
		    isInABox = true;
		}
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return isInABox;
    }

    public static int[] getBoxIdOfItem(int item_id) {// TODO
	int[] boxData = new int[2];
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM box_positions WHERE part_id="
			    + item_id + " LIMIT 1;");// TODO:

	    while (rs.next()) {
		boxData[0] = rs.getInt("box_id");
		boxData[1] = rs.getInt("box");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return boxData;
    }

    public static String[] getDataFromBox(int id) {
	String[] temp = null;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM boxes WHERE id="
		    + id + " LIMIT 1;");

	    while (rs.next()) {
		temp = new String[5];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("name");
		temp[2] = rs.getString("boxes");
		temp[3] = rs.getString("columns");
		temp[4] = rs.getString("rows");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return temp;
    }

    public static String[] getDataFromBox(String name) {
	String[] temp = null;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM boxes WHERE name=\"" + name
			    + "\" LIMIT 1;");

	    while (rs.next()) {
		temp = new String[5];
		temp[0] = rs.getString("id");
		temp[1] = rs.getString("name");
		temp[2] = rs.getString("boxes");
		temp[3] = rs.getString("columns");
		temp[4] = rs.getString("rows");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
	return temp;
    }

    public static String getItemDetailsInStorageBox(int storage, int box) {
	int id = 0;
	String title = "";
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM box_positions WHERE box_id="
			    + storage + " AND box=" + box + " LIMIT 1;");

	    while (rs.next()) {
		id = rs.getInt("part_id");
	    }
	    rs.close();

	    rs = stmt.executeQuery("SELECT title FROM parts WHERE id=" + id
		    + " LIMIT 1;");

	    while (rs.next()) {
		title = rs.getString("title");
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}

	return title;
    }

    public static Image getItemImage(int storage, int box) {
	int id = 0;
	Image img = null;
	Connection c = null;
	Statement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT * FROM box_positions WHERE box_id="
			    + storage + " AND box=" + box + " LIMIT 1;");

	    while (rs.next()) {
		id = rs.getInt("part_id");
	    }
	    rs.close();

	    rs = stmt.executeQuery("SELECT image FROM parts WHERE id=" + id
		    + " LIMIT 1;");

	    while (rs.next()) {
		if (rs.getObject("image") != null) {
		    InputStream is = rs.getBinaryStream("image");
		    img = ImageIO.read(is);
		} else {
		    img = null;
		}
	    }
	    rs.close();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}

	return img;
    }

    public static void addDatasheetToItem(int part_id) {
	Connection c2 = null;
	PreparedStatement stmt2 = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c2 = DriverManager.getConnection("jdbc:sqlite:database.db");
	    String sql = "INSERT INTO datasheets(part_id, filename) VALUES (?, ?);";
	    stmt2 = c2.prepareStatement(sql);
	    stmt2.setInt(1, part_id);
	    stmt2.setString(2, part_id + ".pdf");
	    c2.setAutoCommit(false);
	    stmt2.execute();
	    c2.commit();
	    stmt2.close();
	    c2.close();
	    System.out.println("Added");

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
    }

    public static void openDatasheetFromItem(int item_id) {
	try {
	    File datasheetFile = new File("C://Users/Dominik/datasheets/"
		    + item_id + ".pdf");

	    Runtime.getRuntime().exec(
		    "rundll32 url.dll,FileProtocolHandler "
			    + datasheetFile.toURI());
	} catch (Exception e) {
	    new Exception("ERR: Datenblatt nicht gefunden!");

	}
    }

    public static boolean hasItemDatasheet(int item_id) {
	Connection c = null;
	Statement stmt = null;
	int count = 0;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    stmt = c.createStatement();
	    ResultSet rs = stmt
		    .executeQuery("SELECT COUNT() as count FROM datasheets WHERE part_id="
			    + item_id + ";");

	    while (rs.next()) {
		count = rs.getInt("count");

	    }
	    rs.close();
	    stmt.close();
	    c.close();

	    if (count == 1) {
		return true;
	    }

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}

	return false;
    }

    public static void addBoxToDatabase(String name, int columns, int rows) {

	Connection c = null;
	PreparedStatement stmt = null;
	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:database.db");
	    String sql = "INSERT INTO boxes(id,name,boxes,columns,rows) VALUES (?, ?, ?, ?, ?);";
	    stmt = c.prepareStatement(sql);
	    stmt.setInt(1, getNextNewBoxID());
	    stmt.setString(2, name);
	    stmt.setInt(3, columns * rows);
	    stmt.setInt(4, columns);
	    stmt.setInt(5, rows);
	    c.setAutoCommit(false);
	    stmt.execute();
	    c.commit();
	    stmt.close();
	    c.close();

	} catch (Exception e) {
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    System.exit(0);
	}
    }
}