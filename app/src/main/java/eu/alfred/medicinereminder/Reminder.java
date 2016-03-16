package eu.alfred.medicinereminder;

import java.util.Date;

public class Reminder {
	public String title;
	public Date date;
	public int weekdays;

	public static final int Monday = 1;
	public static final int Tuesday = 2;
	public static final int Wednesday = 4;
	public static final int Thursday = 8;
	public static final int Friday = 16;
	public static final int Saturday = 32;
	public static final int Sunday = 64;

	public Reminder(String title, Date date, int weekdays) {
		this.title = title;
		this.date = date;
		this.weekdays = weekdays;
	}
}
