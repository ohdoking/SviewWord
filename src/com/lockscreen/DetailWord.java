package com.lockscreen;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class DetailWord extends Activity {

	TextView tView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_word);
		
		tView = (TextView)findViewById(R.id.WordText);
		
		Intent intent = getIntent();
		String i = intent.getStringExtra("word");
		tView.setText(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_word, menu);
		return true;
	}

}
