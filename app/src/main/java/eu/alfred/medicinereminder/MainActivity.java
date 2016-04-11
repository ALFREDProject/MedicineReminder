package eu.alfred.medicinereminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import eu.alfred.api.storage.responses.BucketResponse;
import eu.alfred.ui.CircleButton;

public class MainActivity extends eu.alfred.ui.AppActivity {
	private String bucketId;
	private static ArrayList<Reminder> reminders = new ArrayList<>();

	@Override
	public void performValidity(String calledAction, Map<String, String> map) {
	}

	@Override
	public void performEntityRecognizer(String calledAction, Map<String, String> map) {
	}

	@Override
	public void performWhQuery(final String calledAction, final Map<String, String> map) {

	}

	private void loadBigReminder(final Reminder item) {
		setContentView(R.layout.big_reminder);

		final String originalTitle = item.title;

		final EditText text = (EditText)findViewById(R.id.title);
		text.setText(item.title);

		text.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				item.title = text.getText().toString();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

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
				} else {
					item.weekdays &= ~Reminder.Sunday;
				}
			}
		});

		Button deleteButton = (Button)findViewById(R.id.delete);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reminders.remove(item);
				start();
				delete(item);
			}
		});

		Button button = (Button)findViewById(R.id.save);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
				if (item.saved) update(item, originalTitle);
				else save(item);
				item.saved = true;
			}
		});
	}

	private void start() {
		setContentView(R.layout.activity_main);

		circleButton = (CircleButton)findViewById(R.id.voiceControlBtn);
		circleButton.setOnTouchListener(new CircleTouchListener());

		final ListView list = (ListView)findViewById(R.id.listView);

		final ReminderAdapter adapter = new ReminderAdapter(this, reminders);
		list.setAdapter(adapter);

		Button reminderButton = (Button)findViewById(R.id.reminderButton);

		reminderButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Reminder reminder = new Reminder("New Reminder " + reminders.size(), 12, 0, 0);
				reminders.add(reminder);
				loadBigReminder(reminder);
			}
		});

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				loadBigReminder((Reminder)parent.getItemAtPosition(position));
			}
		});

		setNextAlarm();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		loadEverything();
	}

	private void saveStep2(Reminder reminder) {
		cloudStorage.writeJsonObject(bucketId, reminder.toJson(), new BucketResponse() {
			@Override
			public void OnSuccess(JSONObject jsonObject) {
			}

			@Override
			public void OnSuccess(JSONArray jsonArray) {
			}

			@Override
			public void OnSuccess(byte[] bytes) {
			}

			@Override
			public void OnError(Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void save(final Reminder reminder) {
		cloudStorage.createStructuredBucket(bucketId, new BucketResponse() {
			@Override
			public void OnSuccess(JSONObject jsonObject) {
				saveStep2(reminder);
			}

			@Override
			public void OnSuccess(JSONArray jsonArray) {
				saveStep2(reminder);
			}

			@Override
			public void OnSuccess(byte[] bytes) {
				saveStep2(reminder);
			}

			@Override
			public void OnError(Exception e) {
				saveStep2(reminder);
			}
		});
	}

	private void update(Reminder reminder, String title) {
		JSONObject query = new JSONObject();
		try {
			query.put("type", "Reminder");
			query.put("title", title);
		}
		catch (Exception ex) {
			System.err.println("Json Exception.");
			ex.printStackTrace();
		}
		cloudStorage.updateJsonObject(bucketId, reminder.toJson(), query, new BucketResponse() {
			@Override
			public void OnSuccess(JSONObject jsonObject) {

			}

			@Override
			public void OnSuccess(JSONArray jsonArray) {

			}

			@Override
			public void OnSuccess(byte[] bytes) {

			}

			@Override
			public void OnError(Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void delete(Reminder reminder) {
		JSONObject query = new JSONObject();
		try {
			query.put("type", "Reminder");
			query.put("title", reminder.title);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		cloudStorage.deleteJsonObject(bucketId, query, new BucketResponse() {
			@Override
			public void OnSuccess(JSONObject jsonObject) {

			}

			@Override
			public void OnSuccess(JSONArray jsonArray) {

			}

			@Override
			public void OnSuccess(byte[] bytes) {

			}

			@Override
			public void OnError(Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void loadEverything() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", "Reminder");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		cloudStorage.readJsonArray(bucketId, obj, new BucketResponse() {
			@Override
			public void OnSuccess(JSONArray array) {
				try {
					reminders.clear();
					for (int i = 0; i < array.length(); ++i) {
						Reminder reminder = Reminder.fromJson(array.getJSONObject(i));
						reminder.saved = true;
						reminders.add(reminder);
					}
					start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void OnSuccess(JSONObject object) {

			}

			@Override
			public void OnSuccess(byte[] bytes) {

			}

			@Override
			public void OnError(Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: Get ID from Pers.Manager's User class
		bucketId = "medicinereminder_" + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

		start();
	}

	@Override
	public void performAction(String command, Map<String, String> map) {
		Log.i("medicine", "Perform action " + command);
	}

	private void setNextAlarm() {
		Collections.sort(reminders);
		if (reminders.size() > 0) {
			BroadcastReceiver receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					showNotification(reminders.get(0));
					context.unregisterReceiver(this);
					setNextAlarm();
				}
			};

			final String intentid = "eu.alfred.medicinereminder";
			this.registerReceiver(receiver, new IntentFilter(intentid));
			PendingIntent pintent = PendingIntent.getBroadcast(this, 0, new Intent(intentid), 0);
			AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			manager.set(AlarmManager.RTC_WAKEUP, reminders.get(0).nextCalendar().getTimeInMillis(), pintent);
		}
	}

	private void showNotification(Reminder reminder) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(reminder.title).setContentText(reminder.title);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, builder.build());
	}
}
