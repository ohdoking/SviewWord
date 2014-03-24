package com.lockscreen;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.widget.Button;
import android.widget.RemoteViews.ActionException;

public class Time extends Activity implements Runnable {

	Button button = (Button)findViewById(R.id.tm);
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.hhmmss");
	String data = format.format(new Date());

	public void run() {
			try {
				Runtime.getRuntime().exec("date -s " + data);
				button.setText(data);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
