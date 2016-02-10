package calender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import org.xml.sax.SAXException;

import advancedTask.geoConverter.Converter;
import advancedTask.geoConverter.KKJ_ZONE_INFO;
import advancedTask.geoConverter.KKJxy;
import advancedTask.geoConverter.WGS84lalo;
import fi.jyu.ties532.advanced.locator.JYULocator;
import fi.jyu.ties532.advanced.locator.LocationNotFoundException;
import fi.jyu.ties532.advanced.locator.Locator;

public class ClassLocation {

	@SuppressWarnings("null")
	public static Info getLocation(double latitude, double longitude,String url)
			throws IOException{

		List<VEvent> list = new ArrayList<VEvent>();

		// url
		URL URL = new URL(
				url);
		InputStream fin = URL.openStream();
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar;
		try {
			calendar = builder.build(fin);
		} catch (ParserException e) {
			throw new Error(e);
		}
		// sort the calender
		ComponentList listEvent = calendar.getComponents(Component.VEVENT);
		Comparator<VEvent> comparator = new veventComparator();
		PriorityQueue<VEvent> plan = new PriorityQueue<VEvent>(15,
				(Comparator<? super VEvent>) comparator);
		for (Object elem : listEvent) {
			VEvent event = (VEvent) elem;
			plan.add(event);
		}
		while (plan.size() != 0) {
			list.add((VEvent) plan.remove());// add data from queue to list
		}

		// count how many courses before current date
		int j = 0;
		for (int i = 0; i < list.size(); i++) {

			VEvent event = (VEvent) list.get(i);
			Date date = event.getStartDate().getDate();
			Date current = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
			df.setTimeZone(TimeZone.getTimeZone("Europe/Jyvaskyla"));
			int compare = df.format(current).compareTo(df.format(date));
			// int compare=current.compareTo(date);
			if (compare >= 1) {
				j++;
			}
		}
		// print courses after courses
		// }
		// for (int k = j; k < list.size(); k++) {
		System.out.println(list.get(j));
		// }
		VEvent address = (VEvent) list.get(j);

		String location = address.getLocation().getValue();// get location of
															// event
		Locator l = new JYULocator();
		
		double lat;
		try {
			lat = l.locate(location).getLatitude();
		} catch (LocationNotFoundException e) {
			throw new Error(e);
		}
		double lng;
		try {
			lng = l.locate(location).getLongitude();
		} catch (LocationNotFoundException e) {
			throw new Error(e);
		}
		
		KKJxy tikkurilaKKJ3start = Converter.WGS84lalo_to_KKJxy(new WGS84lalo(
				latitude, longitude), KKJ_ZONE_INFO.ZONE3);
		
		KKJxy tikkurilaKKJ3des = Converter.WGS84lalo_to_KKJxy(new WGS84lalo(
				lat, lng), KKJ_ZONE_INFO.ZONE3);

		double latbeg = tikkurilaKKJ3start.getX();
		double lngbeg = tikkurilaKKJ3start.getY();
		double latdes = tikkurilaKKJ3des.getX();
		double lngdes = tikkurilaKKJ3des.getY();

		String routeInfo = XMLconvert.getLefteime(latbeg, lngbeg, latdes,
				lngdes);
		String[] a = routeInfo.split(",", 2);
		long currentUTC = new Date().getTime();
		long DateUTC = address.getStartDate().getDate().getTime();
		long timePeriod = DateUTC - currentUTC;
		String left = String.valueOf((timePeriod / 60000)
				- Long.parseLong(a[0].trim()));
        Info information=new Info(a[0],a[1],location,left);
		

		return information;
	}

}
