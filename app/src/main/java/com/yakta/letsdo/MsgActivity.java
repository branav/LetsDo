package com.yakta.letsdo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yakta.letsdo.*;

public class MsgActivity extends Activity {

	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);

		Button del_button = (Button)findViewById(R.id.del_but);

		String msg = null;
		String date = null;
		String time = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			msg = extras.getString("Msg");
			date = extras.getString("Date");
			time = extras.getString("Time");
		}
		TextView msgtv = (TextView) findViewById(R.id.msgtv);
		TextView datetv = (TextView) findViewById(R.id.datetv);
		TextView timetv = (TextView) findViewById(R.id.timetv);
		msgtv.setText(msg);
		datetv.setText(date);
		timetv.setText(time);


		final String finalMsg = msg;
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				db = openOrCreateDatabase("Tododb", MODE_PRIVATE, null);
				db.execSQL("delete from todotable where act = '"+ finalMsg + "';");

				finish();
			}
		};

		del_button.setOnClickListener(listener);
	}
}

/* To Do list
1. Screen first launch with empty
2. create new activity to edit to do list
3. insertion into db from multiple activity
4. Design work out
 */