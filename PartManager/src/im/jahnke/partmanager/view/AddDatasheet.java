package im.jahnke.partmanager.view;

import im.jahnke.partmanager.model.DataManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class AddDatasheet extends JDialog {
    private JTextField url_field;
    private JTextField file_field;
    private int item_id;
    JButton btnDownload;
    JButton btnSave;
    boolean waitingFlag = true;

    public AddDatasheet(int item_id) {
	this.item_id = item_id;

	setSize(450, 160);
	setLocationRelativeTo(null);
	setModal(true);
	setTitle("Datenblatt hinzuf\u00FCgen");
	getContentPane().setLayout(null);

	JLabel lblUrl = new JLabel("Link");
	lblUrl.setBounds(20, 29, 43, 14);
	getContentPane().add(lblUrl);

	JLabel lblDatei = new JLabel("Datei");
	lblDatei.setBounds(20, 54, 46, 14);
	getContentPane().add(lblDatei);

	url_field = new JTextField();
	url_field.setBounds(73, 26, 228, 20);
	getContentPane().add(url_field);
	url_field.setColumns(10);

	file_field = new JTextField();
	file_field.setBounds(73, 51, 228, 20);
	getContentPane().add(file_field);
	file_field.setColumns(10);

	btnDownload = new JButton("Download");
	btnDownload.setBounds(311, 26, 101, 20);
	getContentPane().add(btnDownload);
	btnDownload.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		downloadDatasheet();
	    }
	});

	JButton btnBrowse = new JButton("Durchsuchen");
	btnBrowse.setBounds(311, 51, 101, 20);
	getContentPane().add(btnBrowse);
	btnBrowse.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File datasheet = chooser.getSelectedFile();
		datasheet.renameTo(new File("C:\\Users\\Dominik\\datasheets\\"
			+ getItemId() + ".pdf"));
		DataManager.addDatasheetToItem(getItemId());
		btnSave.setEnabled(true);
	    }
	});

	btnSave = new JButton("Speichern");
	btnSave.setBounds(144, 89, 146, 20);
	getContentPane().add(btnSave);
	btnSave.setEnabled(false);
	btnSave.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		dispose();
	    }
	});

	setVisible(true);

    }

    public int getItemId() {
	return item_id;
    }

    public void downloadDatasheet() {
	Thread downloadThread = new Thread() {
	    @Override
	    public void run() {
		try {
		    waitingFlag = true;
		    URL file1 = new URL(url_field.getText());
		    ReadableByteChannel rbc1 = Channels.newChannel(file1
			    .openStream());
		    FileOutputStream fos1 = new FileOutputStream(
			    "C:\\Users\\Dominik\\datasheets\\" + getItemId()
				    + ".pdf");
		    fos1.getChannel().transferFrom(rbc1, 0, 1 << 24);
		    fos1.close();
		    url_field.setEnabled(false);
		    DataManager.addDatasheetToItem(getItemId());
		    System.out.println("Download");
		    waitingFlag = false;
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
	    }

	};
	downloadThread.start();
	Thread waitingThread = new Thread() {
	    @Override
	    public void run() {
		while (waitingFlag) {
		    btnDownload.setEnabled(false);
		    btnDownload.setText("");
		    for (int i = 0; i < 20; i++) {
			btnDownload.setText(btnDownload.getText() + ".");
			repaint();
			try {
			    sleep(500);
			} catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if (!waitingFlag) {
			    break;
			}
		    }

		}
		btnDownload.setText("Fertig");
		btnSave.setEnabled(true);
	    }
	};
	waitingThread.start();
    }
}
