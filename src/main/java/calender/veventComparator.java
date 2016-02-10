package calender;

import java.util.Comparator;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;

public class veventComparator implements Comparator<VEvent> {
	public int compare(VEvent e1, VEvent e2) {
		Date d1 = e1.getStartDate().getDate();
		Date d2 = e2.getStartDate().getDate();
		return d1.compareTo(d2);
	}

}
