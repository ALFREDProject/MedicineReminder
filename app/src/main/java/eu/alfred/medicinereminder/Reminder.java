package eu.alfred.medicinereminder;

import org.json.JSONObject;

public class Reminder {
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

	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
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
