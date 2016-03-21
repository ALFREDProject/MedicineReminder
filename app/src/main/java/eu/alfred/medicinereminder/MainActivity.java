package eu.alfred.medicinereminder;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import eu.alfred.ui.CircleButton;

public class MainActivity extends eu.alfred.ui.AppActivity {
	private static ArrayList<Reminder> reminders = new ArrayList<>();

	private void start() {
		setContentView(R.layout.activity_main);

		circleButton = (CircleButton)findViewById(R.id.voiceControlBtn);
		circleButton.setOnTouchListener(new CircleTouchListener());

		ListView list = (ListView)findViewById(R.id.listView);

		final ReminderAdapter adapter = new ReminderAdapter(this, reminders);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final Reminder item = (Reminder)parent.getItemAtPosition(position);
				view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
					@Override
					public void run() {
						//list.remove(item);
						//item.title = "clicked";
						adapter.notifyDataSetChanged();
						//view.setAlpha(1);

						setContentView(R.layout.big_reminder);

						TextView text = (TextView)findViewById(R.id.textView2);
						text.setText(item.title);

						TimePicker clock = (TimePicker)findViewById(R.id.timePicker);
						clock.setIs24HourView(true);
						clock.setCurrentHour(item.hour);
						clock.setCurrentMinute(item.minute);

						clock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
							@Override
							public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
								item.hour = hourOfDay;
								item.minute = minute;
							}
						});

						ToggleButton mo = (ToggleButton)findViewById(R.id.mo);
						ToggleButton di = (ToggleButton)findViewById(R.id.di);
						ToggleButton mi = (ToggleButton)findViewById(R.id.mi);
						ToggleButton don = (ToggleButton)findViewById(R.id.don);
						ToggleButton fr = (ToggleButton)findViewById(R.id.fr);
						ToggleButton sa = (ToggleButton)findViewById(R.id.sa);
						ToggleButton so = (ToggleButton)findViewById(R.id.so);

						if ((item.weekdays & Reminder.Monday) != 0) mo.setChecked(true);
						if ((item.weekdays & Reminder.Tuesday) != 0) di.setChecked(true);
						if ((item.weekdays & Reminder.Wednesday) != 0) mi.setChecked(true);
						if ((item.weekdays & Reminder.Thursday) != 0) don.setChecked(true);
						if ((item.weekdays & Reminder.Friday) != 0) fr.setChecked(true);
						if ((item.weekdays & Reminder.Saturday) != 0) sa.setChecked(true);
						if ((item.weekdays & Reminder.Sunday) != 0) so.setChecked(true);

						mo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Monday;
								}
								else {
									item.weekdays &= ~Reminder.Monday;
								}
							}
						});

						di.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Tuesday;
								}
								else {
									item.weekdays &= ~Reminder.Tuesday;
								}
							}
						});

						mi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Wednesday;
								} else {
									item.weekdays &= ~Reminder.Wednesday;
								}
							}
						});

						don.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Thursday;
								} else {
									item.weekdays &= ~Reminder.Thursday;
								}
							}
						});

						fr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Friday;
								}
								else {
									item.weekdays &= ~Reminder.Friday;
								}
							}
						});

						sa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Saturday;
								}
								else {
									item.weekdays &= ~Reminder.Saturday;
								}
							}
						});

						so.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									item.weekdays |= Reminder.Sunday;
								}
								else {
									item.weekdays &= ~Reminder.Sunday;
								}
							}
						});

						Button button = (Button)findViewById(R.id.button);
						button.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								start();
								return true;
							}
						});
					}
				});
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		reminders.add(new Reminder("Rote Pille", 12, 14, Reminder.Monday | Reminder.Tuesday));
		reminders.add(new Reminder("Grüne Pille", 15, 0, Reminder.Sunday));
		reminders.add(new Reminder("Blaue Pille", 11, 20, Reminder.Friday));

		start();
	}

	@Override
	public void performAction(String command, Map<String, String> map) {
		Log.i("medicine", "Perform action " + command);
	}
}
