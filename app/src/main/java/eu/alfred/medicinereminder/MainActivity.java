package eu.alfred.medicinereminder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import eu.alfred.ui.CircleButton;

public class MainActivity extends eu.alfred.ui.AppActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		circleButton = (CircleButton)findViewById(R.id.voiceControlBtn);
		circleButton.setOnTouchListener(new CircleTouchListener());

		ListView list = (ListView)findViewById(R.id.listView);

		Reminder[] reminders = {
			new Reminder("a", new Date(), Reminder.Monday | Reminder.Tuesday),
			new Reminder("b", new Date(), Reminder.Sunday),
			new Reminder("c", new Date(), Reminder.Friday)
		};

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
						item.title = "clicked";
						adapter.notifyDataSetChanged();
						view.setAlpha(1);
					}
				});
			}
		});
	}

	@Override
	public void performAction(String command, Map<String, String> map) {
		Log.i("medicine", "Perform action " + command);
	}
}
