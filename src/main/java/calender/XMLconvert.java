package calender;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.client.utils.URIBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLconvert {

	public static String getLefteime(double latbeg, double lngbeg,
			double latdes, double lngdes) throws IOException
			 {
		int i = 1;
		URIBuilder builder;
		URL url;
		InputStream stream;
		String begAdress;
		String Latbeg = String.valueOf(latbeg).substring(0, 7);
		String Lngbeg = String.valueOf(lngbeg).substring(0, 7);
		String Latdes = String.valueOf(latdes).substring(0, 7);
		String Lngdes = String.valueOf(lngdes).substring(0, 7);
		try {
			builder = new URIBuilder("http://api.matka.fi/");
		} catch (URISyntaxException e1) {
			throw new Error(e1);
		}
				
				builder.addParameter("a",  Latbeg + "," + Lngbeg);
				builder.addParameter("b", Latdes + "," + Lngdes);
				builder.addParameter("time", "1030");
				builder.addParameter("show", "1");
				builder.addParameter("timemode", "2");
				builder.addParameter("user", "TIES532JHC");
				builder.addParameter("pass", "123456");
				
				try {
					url = builder.build().toURL();
				} catch (MalformedURLException e) {
					throw new Error(e);
				} catch (URISyntaxException e) {
					throw new Error(e);
				}
				try {
					 stream = url.openStream();
				} catch (IOException e) {
					throw new Error(e);
				}

//		URL url = new URL("http://api.matka.fi/?a=" + Latbeg + "," + Lngbeg
//				+ "&b=" + Latdes + "," + Lngdes
//				+ "&time=1030&show=1&timemode=2&user=TIES532JHC&pass=123456");
//		InputStream stream = url.openStream();
		// read the parameter several times
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new Error(e);
		}
		Document document;
		try {
			document = db.parse(stream);
		} catch (SAXException e) {
			throw new Error(e);
		}
		XPathFactory getCC = XPathFactory.newInstance();
		XPath ccPath = getCC.newXPath();
		String a;
		try {
			a = ccPath.evaluate(
					"MTRXML/ROUTE/POINT[@uid='start']/ARRIVAL/@time", document);
		} catch (XPathExpressionException e) {
			throw new Error(e);
		}
		String b;
		try {
			b = ccPath.evaluate(
					"MTRXML/ROUTE/POINT[@uid='dest']/ARRIVAL/@time", document);
		} catch (XPathExpressionException e) {
			throw new Error(e);
		}
		String sxpr = "count(MTRXML/ROUTE/LINE)";
		Double ncount;
		try {
			ncount = (Double) ccPath.evaluate(sxpr, document,
					XPathConstants.NUMBER);
		} catch (XPathExpressionException e) {
			throw new Error(e);
		}
		boolean Exists = (ncount.intValue() > 0) ? true : false;
		
		if (Exists == true) {
			try {
				begAdress = ccPath.evaluate("MTRXML/ROUTE/WALK[1]/STOP/NAME/@val",
						document);
			} catch (XPathExpressionException e) {
				throw new Error(e);
			}
		} else {
			try {
				begAdress = ccPath.evaluate(
						"MTRXML/ROUTE/WALK[1]/MAPLOC[1]/NAME/@val", document);
			} catch (XPathExpressionException e) {
				throw new Error(e);
			}
		}

		int time = Integer.parseInt(b.substring(0, 2))
				* 60
				+ Integer.parseInt(b.substring(2, 4))
				- (Integer.parseInt(a.substring(0, 2)) * 60 + Integer
						.parseInt(a.substring(2, 4)));
		String wayTime = String.valueOf(time);
		String info = wayTime + "," + begAdress;
		
		return info;

	}
}
