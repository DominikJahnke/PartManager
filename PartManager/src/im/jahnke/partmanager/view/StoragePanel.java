package im.jahnke.partmanager.view;

import im.jahnke.partmanager.model.DataManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class StoragePanel extends JPanel {

    FrameView view;
    private JTable table;
    private JTextField box_field;
    private JTextField countofBoxesInBox;
    private JTextField box_rows;
    private JTextField box_columns;
    DefaultTableModel model;
    BoxImagePanel pan;
    String[] columnNames = { "ID", "Box", "Fächer" };

    public StoragePanel() {
	setLayout(null);
	setPreferredSize(new Dimension(944, 465));

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(24, 38, 362, 263);
	add(scrollPane);

	model = new DefaultTableModel(DataManager.getBoxList(), columnNames) {

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
	scrollPane.setViewportView(table);

	table.addMouseListener(new MouseAdapter() {

	    public void mouseClicked(MouseEvent e) {
		int id = Integer.parseInt(model.getValueAt(
			table.getSelectedRow(), 0).toString());
		if (pan != null) {
		    get().remove(pan);
		}
		pan = new BoxImagePanel(id);
		pan.setBounds(450, 38, 450, 380);
		get().add(pan);
		
		String[] temp = DataManager.getDataFromBox(id);
		if (!(temp[3] == null || temp[4] == null)) {
		    box_field.setText(temp[1]);
		    countofBoxesInBox.setText(temp[2]);
		    box_columns.setText(temp[3]);
		    box_rows.setText(temp[4]);
		}
		
		get().repaint();
	    }
	});

	JLabel lblstoragebox = new JLabel("Lager/Kiste");
	lblstoragebox.setBounds(24, 329, 88, 14);
	add(lblstoragebox);

	box_field = new JTextField();
	box_field.setBounds(107, 323, 279, 20);
	add(box_field);
	box_field.setColumns(10);

	countofBoxesInBox = new JTextField();
	countofBoxesInBox.setBounds(107, 351, 40, 20);
	add(countofBoxesInBox);
	countofBoxesInBox.setColumns(10);

	JLabel lblNewLabel = new JLabel("Anzahl F\u00E4cher");
	lblNewLabel.setBounds(24, 354, 98, 14);
	add(lblNewLabel);

	JButton btnAddBox = new JButton("Hinzuf\u00FCgen");
	btnAddBox.setBounds(24, 395, 89, 23);
	add(btnAddBox);
	btnAddBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		DataManager.addBoxToDatabase(box_field.getText(),
			Integer.parseInt(box_columns.getText()),
			Integer.parseInt(box_rows.getText()));

	    }
	});

	JButton btnEditBox = new JButton("Bearbeiten");
	btnEditBox.setBounds(160, 395, 89, 23);
	add(btnEditBox);

	JButton btnDeleteBox = new JButton("L\u00F6schen");
	btnDeleteBox.setBounds(297, 395, 89, 23);
	add(btnDeleteBox);

	pan = new BoxImagePanel();
	pan.setBounds(450, 38, 450, 380);
	add(pan);

	JLabel lblNewLabel_1 = new JLabel("Anordnung");
	lblNewLabel_1.setBounds(200, 354, 72, 14);
	add(lblNewLabel_1);

	box_rows = new JTextField();
	box_rows.setBounds(273, 351, 40, 20);
	add(box_rows);
	box_rows.setColumns(10);

	JLabel lblNewLabel_2 = new JLabel("x");
	lblNewLabel_2.setBounds(327, 354, 13, 14);
	add(lblNewLabel_2);

	box_columns = new JTextField();
	box_columns.setBounds(346, 351, 40, 20);
	add(box_columns);
	box_columns.setColumns(10);
    }

    public StoragePanel get() {
	return this;
    }

    public void update() {
	model = new DefaultTableModel(DataManager.getBoxList(), columnNames) {

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
	table.revalidate();
	table.repaint();
    }

    class BoxImagePanel extends JPanel {

	int columns = 0;
	int rows = 0;
	int boxes = 0;
	int box;
	Rectangle[] array;

	String[] titles;
	Image[] images;

	public BoxImagePanel(int box) {
	    this.box = box;
	    String[] temp = DataManager.getDataFromBox(box);
	    if (!(temp[3] == null || temp[4] == null)) {
		columns = Integer.parseInt(temp[3].toString());
		rows = Integer.parseInt(temp[4].toString());
	    }
	    this.boxes = columns * rows;
	    titles = new String[boxes];
	    for (int i = 0; i < titles.length; i++) {
		titles[i] = "";
		titles[i] = splitString(DataManager.getItemDetailsInStorageBox(
			box, i + 1));
		if (titles[i].isEmpty()) {
		    titles[i] = "(leer)";
		}
	    }
	    images = new Image[boxes];
	    for (int i = 0; i < titles.length; i++) {
		images[i] = DataManager.getItemImage(box, i + 1);
	    }

	    setSize(450, 380);

	    array = new Rectangle[boxes];
	    box = 0;
	    for (int j = 0; j < rows; j++) {//
		for (int k = 0; k < columns; k++) {
		    array[box] = new Rectangle(new Point(k * getWidth()
			    / columns, j * getHeight() / rows), new Dimension(
			    this.getWidth() / columns, getHeight() / rows));
		    box++;
		}
	    }

	    setToolTipText("");
	}

	public BoxImagePanel() {

	    setSize(450, 380);
	    setToolTipText("");
	}

	public void paintComponent(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;

	    // draw images
	    if (images != null) {
		for (int i = 0; i < images.length; i++) {
		    if (images[i] != null) {
			g2d.drawImage(images[i], array[i].x, array[i].y,
				array[i].width, array[i].height, null);
		    }
		}
	    }

	    // draw box numbers
	    for (int i = 0; i < boxes; i++) {
		String text = "Fach " + (i + 1);
		g2d.drawString(text, array[i].x + array[i].width
			/ 4, array[i].y + array[i].height - 2);
	    }

	    // draw grid
	    g2d.setColor(Color.BLACK);
	    g2d.drawLine(0, 0, getWidth(), 0);
	    g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
	    g2d.drawLine(getWidth() - 1, getHeight() - 1, 0, getHeight() - 1);
	    g2d.drawLine(0, getHeight() - 1, 0, 0);
	    for (int i = 1; i < columns; i++) {
		g2d.drawLine(getWidth() / columns * i, 0, getWidth() / columns
			* i, getHeight() - 1);
	    }
	    for (int i = 1; i < rows; i++) {
		g2d.drawLine(0, getHeight() / rows * i, getWidth() - 1,
			getHeight() / rows * i);
	    }

	}

	public String getToolTipText(MouseEvent e) {
	    for (int i = 0; i < boxes; i++) {
		if (array[i].contains(e.getPoint())) {
		    return "<html>" + titles[i] + "</html>";
		}
	    }
	    return "no";
	}

	public Point getToolTipLocation(MouseEvent e) {
	    Point p = e.getPoint();

	    String textFromBox = "";

	    for (int i = 0; i < boxes; i++) {
		if (array[i].contains(e.getPoint())) {
		    textFromBox = titles[i];
		}
	    }

	    String[] text = textFromBox.split("<br />");
	    // get longest line
	    UIDefaults uidefs = UIManager.getLookAndFeelDefaults();
	    Font font = uidefs.getFont("ToolTip.font");
	    FontMetrics metrics = getGraphics().getFontMetrics(font);
	    int maxTextWidth = 0;
	    for (int i = 0; i < text.length; i++) {
		if (metrics.stringWidth(text[i]) > maxTextWidth) {
		    maxTextWidth = metrics.stringWidth(text[i]);
		}
	    }

	    if (p.x > this.getWidth() / 2) {
		p.x -= maxTextWidth;
	    }
	    p.y += 15;
	    return p;
	}

	public String splitString(String string) {
	    if (string.isEmpty()) {
		return "";
	    }
	    StringBuffer buf = new StringBuffer();
	    String tempString = string;
	    if (string != null) {

		while (tempString.length() > 40) {
		    String block = tempString.substring(0, 40);
		    int index = block.lastIndexOf(' ');
		    if (index < 0) {
			index = tempString.indexOf(' ');
		    }
		    if (index >= 0) {
			buf.append(tempString.substring(0, index) + "<br />");
		    }
		    tempString = tempString.substring(index + 1);
		}
	    }
	    buf.append(tempString);
	    return buf.toString();

	}

    }
}