package com.yakta.letsdo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yakta.letsdo.adapters.BaseInflaterAdapter;
import com.yakta.letsdo.adapters.CardItemData;
import com.yakta.letsdo.adapters.inflaters.CardInflater;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToDoActivity extends Activity {

	SQLiteDatabase db;
	String todomsg, date, time;
	final BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listactivity);
		
		Button add_button = (Button)findViewById(R.id.add_but);
		Button do_button = (Button)findViewById(R.id.do_button);

		final ListView list = (ListView) findViewById(R.id.list_view);
        final EditText todomsget = (EditText) findViewById(R.id.todomsget);
		
		final Context context = this;		

		db = openOrCreateDatabase("Tododb", MODE_PRIVATE, null);
        db.execSQL("create table if not exists todotable(act varchar, date varchar, time varchar)");
		
		 View.OnClickListener listener = new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                todomsg = todomsget.getText().toString();
				Calendar c = Calendar.getInstance();
				//String date = String.valueOf(c.get(Calendar.DATE));
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
				String time = sdf.format(c.getTime());
				String date = sdf1.format(c.DATE);
                todomsget.setText("");
                db.execSQL("insert into todotable values('" + todomsg + "','" + date + "','"+ time +"')");
                
                CardItemData data = new CardItemData(todomsg, date, time);
    			adapter.addItem(data, false);
                
                Toast.makeText(context,"Let's do it..!!!",Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
			}
		};

		View.OnClickListener listener1 = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ToDoActivity.this, DoActivity.class);
				startActivity(intent);
			}
		};
		
		OnItemClickListener l_listener = new OnItemClickListener(){

			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String msg =((TextView) view.findViewById(R.id.text1)).getText().toString();
				String date =((TextView) view.findViewById(R.id.text2)).getText().toString();
				String time =((TextView) view.findViewById(R.id.text3)).getText().toString();
				Intent intent = new Intent(ToDoActivity.this, MsgActivity.class);
				Bundle extras = new Bundle();
				extras.putString("Msg", msg);
				extras.putString("Date", date);
				extras.putString("Time",time);
				intent.putExtras(extras);
				startActivityForResult(intent,1);
				Toast.makeText(context, ""+position, Toast.LENGTH_LONG).show();
			}
			
		};

		list.setOnItemClickListener(l_listener);		
		add_button.setOnClickListener(listener);
		do_button.setOnClickListener(listener1);
	}

	protected void onResume(){

		super.onResume();

		final ListView list = (ListView) findViewById(R.id.list_view);

		adapter.clear(false);


		list.addHeaderView(new View(this));
		list.addFooterView(new View(this));

		Cursor cursor = db.rawQuery("SELECT * FROM todotable", null);
		if (cursor.moveToFirst()) {
			do {

				todomsg = cursor.getString(cursor.getColumnIndex("act"));
				date = cursor.getString(1);
				time = cursor.getString(2);

				CardItemData data = new CardItemData(todomsg, date, time);
				adapter.addItem(data, true);

			} while (cursor.moveToNext());
			cursor.close();
			list.setAdapter(adapter);
		}
		/* else{
			CardItemData data = new CardItemData("Let", "do", "it");
			adapter.addItem(data, true);
		} */
		adapter.notifyDataSetChanged();
	}
}
