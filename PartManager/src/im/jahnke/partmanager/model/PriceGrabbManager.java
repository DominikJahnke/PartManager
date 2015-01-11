package im.jahnke.partmanager.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceGrabbManager {

    public static String getPriceFromConrad(String article_id) {
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

	String re2 = "_produktpreis\">€ (.*?)</span>";
	Pattern p = Pattern.compile(re2, Pattern.CASE_INSENSITIVE
		| Pattern.DOTALL);
	Matcher m = p.matcher(a.toString());
	if (m.find()) {
	    return m.group(1);
	}
	return "";
    }

    public static String getPriceFromReichelt(String article_id) {
	try {
	    String body = "SEARCH=" + URLEncoder.encode(article_id, "UTF-8");

	    URL url = new URL(
	    	"http://www.reichelt.de/index.html?&ACTION=446&amp;LA=446");
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
	        return m.group(1);
	    }
	    return "";
	} catch (IOException e) {
	}
	return "";
    }

}
