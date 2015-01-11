package im.jahnke.partmanager.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShippingGrabbManager {

    public static String getDHLDeliveryDate(String trackingID) {
	String status = "";
	String dhllink = "http://nolp.dhl.de/nextt-online-public/report_popup.jsp?lang=de&idc="
		+ trackingID;
	URL link;
	StringBuilder a = null;
	try {
	    link = new URL(dhllink);
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

	String re2 = "<td class=\"mm_bold text-right mm_yellow\">Voraussichtliche Zustellung</td>											<td class=\"mm_yellow\" colspan=\"2\">(.*?)</br>";
	Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		| Pattern.DOTALL);
	Matcher m = p.matcher(a.toString());
	if (m.find()) {
	    status = "Lieferung: " + m.group(1);
	}
	re2 = "<td class=\"mm_bold text-right\">Status vom (.*?)</td>										<td colspan=\"2\" class=\"mm_delivered\">";
	p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		| Pattern.DOTALL);
	m = p.matcher(a.toString());
	if (m.find()) {
	    status = "Erfolgreich zugestellt am " + m.group(1) ;
	} else {
	    status = "Sendung nicht gefunden";
	}
	return status;
    }

}
