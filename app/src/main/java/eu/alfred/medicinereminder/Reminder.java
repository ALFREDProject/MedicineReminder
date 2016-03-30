package eu.alfred.medicinereminder;

import org.json.JSONObject;

import java.util.Calendar;

public class Reminder implements Comparable<Reminder> {
	public String title;
	public int hour;
	public int minute;
	public int weekdays;

	public static final int Monday = 1;
	public static final int Tuesday = 2;
	public static final int Wednesday = 4;
	public static final int Thursday = 8;
	public static final int Friday = 16;
	public static final int Saturday = 32;
	public static final int Sunday = 64;

	public Reminder(String title, int hour, int minute, int weekdays) {
		this.title = title;
		this.hour = hour;
		this.minute = minute;
		this.weekdays = weekdays;
	}

	private static int getDay(int day) {
		switch (day) {
			default:
			case Calendar.MONDAY: return Monday;
			case Calendar.TUESDAY: return Tuesday;
			case Calendar.WEDNESDAY: return Wednesday;
			case Calendar.THURSDAY: return Thursday;
			case Calendar.FRIDAY: return Friday;
			case Calendar.SATURDAY: return Saturday;
			case Calendar.SUNDAY: return Sunday;
		}
	}

	public int compareTo(Reminder reminder) {
		Calendar c1 = nextCalendar();
		Calendar c2 = nextCalendar();
		return c1.compareTo(c2);
	}

	public Calendar nextCalendar() {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		for (;;) {
			if ((weekdays & getDay(calendar.get(Calendar.DAY_OF_WEEK))) != 0) {
				if (calendar.compareTo(now) > 0) break;
			}
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
		}

		return calendar;
	}

	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", "Reminder");
			obj.put("title", title);
			obj.put("hour", hour);
			obj.put("minute", minute);
			obj.put("weekdays", weekdays);
		}
		catch (Exception ex) {
			System.err.println("Json Exception.");
			ex.printStackTrace();
		}
		return obj;
	}

	public static Reminder fromJson(JSONObject obj) {
		try {
			return new Reminder(obj.getString("title"), obj.getInt("hour"), obj.getInt("minute"), obj.getInt("weekdays"));
		}
		catch (Exception ex) {
			System.err.println("Json Exception.");
			ex.printStackTrace();
			return null;
		}
	}
}
