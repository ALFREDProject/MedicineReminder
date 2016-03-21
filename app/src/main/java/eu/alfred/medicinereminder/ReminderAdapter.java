package eu.alfred.medicinereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
	private final Context context;
	private final ArrayList<Reminder> values;

	public ReminderAdapter(Context context, ArrayList<Reminder> values) {
		super(context, -1, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.small_reminder, parent, false);
		TextView text = (TextView)rowView.findViewById(R.id.textView);
		Reminder value = values.get(position);
		text.setText(value.title + " - " + time(value.hour) + ":" + time(value.minute));

		StringBuilder weekdays = new StringBuilder();
		if ((value.weekdays & Reminder.Monday) != 0) weekdays.append("Mo., ");
		if ((value.weekdays & Reminder.Tuesday) != 0) weekdays.append("Di., ");
		if ((value.weekdays & Reminder.Wednesday) != 0) weekdays.append("Mi., ");
		if ((value.weekdays & Reminder.Thursday) != 0) weekdays.append("Do., ");
		if ((value.weekdays & Reminder.Friday) != 0) weekdays.append("Fr., ");
		if ((value.weekdays & Reminder.Saturday) != 0) weekdays.append("Sa., ");
		if ((value.weekdays & Reminder.Sunday) != 0) weekdays.append("So., ");
		if (weekdays.length() > 1) weekdays.delete(weekdays.length() - 2, weekdays.length());

		TextView smallText = (TextView)rowView.findViewById(R.id.textView3);
		smallText.setText(weekdays.toString());

		return rowView;
	}

	private static String time(int num) {
		if (num < 10) return "0" + num;
		else return "" + num;
	}
}
