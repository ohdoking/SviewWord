package com.lockscreen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class LockScreenAppActivity extends Activity implements
		SensorEventListener {

	/** Called when the activity is first created. */
	KeyguardManager.KeyguardLock k1;
	TelephonyManager telephonyManager;

	boolean inDragMode;
	int selectedImageViewX;
	int selectedImageViewY;
	int windowwidth;
	int windowheight;
	ImageView phone, home;
	// int phone_x,phone_y;
	int home_x, home_y;
	int[] droidpos;

	public ArrayList<String> m_list = new ArrayList<String>();
	TextView droid;
	ImageButton setbtn;
	int yes = 0;

	TextView time;

	Time go;
	Thread t1;

	private LayoutParams layoutParams;

	SensorManager m_sensor_manager;
	Sensor m_sensor;

	int m_check_count = 0;

	static final int REQUEST_CODE = 1234;

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(
				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onAttachedToWindow();
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DatabaseHandler db = new DatabaseHandler(this);
		setContentView(R.layout.main);
		droid = (TextView) findViewById(R.id.tV);

		time = (TextView) findViewById(R.id.tm);

		// 출력용 텍스트뷰를 얻는다.

		// 시스템서비스로부터 SensorManager 객체를 얻는다.
		m_sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// SensorManager 를 이용해서 근접 센서 객체를 얻는다.
		m_sensor = m_sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		/*
		 * go = new Time(); t1 = new Thread(go); t1.start();
		 */

		setbtn = (ImageButton) findViewById(R.id.setbtn);
		setbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LockScreenAppActivity.this,
						InputWord.class);
				// i.putExtra("yes", 1);
				// yes = 1;
				startActivity(i);

				startActivityForResult(i, 0);

			}
		});

		Log.d("Reading: ", "Reading all contacts..");
		List<Word> wordss = db.getAllWords();
		System.out.println("1:" + yes);
		Intent in = getIntent();
		yes = in.getIntExtra("yes", 0);
		System.out.println("2:" + yes);

		if (wordss.isEmpty()) {
			droid.setText("setting에서 단어를\n추가해 주세요");
		} else {
			int num = wordss.size();
			int ran = (int) (Math.random() * num);
			droid.setText(wordss.get(ran).get_word() + "\n\n"
					+ wordss.get(ran).get_mean());
			System.out.println(num + "/" + ran);
		}

		System.out.println("measured width" + droid.getMeasuredWidth());
		System.out.println(" width" + droid.getWidth());

		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			// Toast.makeText(this, "" + "kill activityy",
			// Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		droid = (TextView) findViewById(R.id.tV);

		time = (TextView) findViewById(R.id.tm);
		String c;
		if (intent.getExtras().containsKey("Black")) {
			//droid.setText(intent.getStringExtra("Black"));
			droid.setTextColor(Color.BLACK);
		}
		else if (intent.getExtras().containsKey("White")) {
			//c = intent.getStringExtra("White");
			droid.setTextColor(Color.WHITE);
			
		}
		else if (intent.getExtras().containsKey("Back")) {

	             Uri imageUri = intent.getData();
	             Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	          
	             Drawable dr = new BitmapDrawable(bitmap);
	             
	             RelativeLayout relative = (RelativeLayout)findViewById(R.id.back1);
	             relative.setBackgroundDrawable(dr);
	             
	          
	         }

	}

	class StateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("call Activity off hook");
				finish();

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			}
		}
	}

	public void onSlideTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int x_cord = (int) event.getRawX();
			int y_cord = (int) event.getRawY();

			if (x_cord > windowwidth) {
				x_cord = windowwidth;
			}
			if (y_cord > windowheight) {
				y_cord = windowheight;
			}

			layoutParams.leftMargin = x_cord - 25;
			layoutParams.topMargin = y_cord - 75;

			view.setLayoutParams(layoutParams);
			break;
		default:
			break;

		}

	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss.
		return;
	}

	// only used in lockdown mode
	@Override
	protected void onPause() {
		super.onPause();

		// Don't hang around.
		// finish();
	}

	// 해당 액티비티가 시작되면 근접 데이터를 얻을 수 있도록 리스너를 등록한다.
	protected void onStart() {
		super.onStart();

		m_check_count = 0;
		// 센서 값을 이 컨텍스트에서 받아볼 수 있도록 리스너를 등록한다.
		m_sensor_manager.registerListener(this, m_sensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	// 해당 액티비티가 멈추면 근접 데이터를 얻어도 소용이 없으므로 리스너를 해제한다.
	protected void onStop() {
		super.onStop();
		// 센서 값이 필요하지 않는 시점에 리스너를 해제해준다.
		m_sensor_manager.unregisterListener(this);
	}

	// 정확도 변경시 호출되는 메소드. 센서의 경우 거의 호출되지 않는다.
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// 측정한 값을 전달해주는 메소드.
	public void onSensorChanged(SensorEvent event) {
		// 센서값을 측정한 센서의 종류가 근접 센서인 경우
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

			time = (TextView) findViewById(R.id.tm);
			// -----------------------------------닫히면
			SimpleDateFormat format = new SimpleDateFormat("MM-dd hh : mm");
			String data = format.format(new Date());
			try {
				Runtime.getRuntime().exec("date -s " + data);
				time.setText("  " + data);
			} catch (IOException e) {
			}

			if (event.values[0] == 0) {
				try {
					// initialize receiver

					startService(new Intent(this, MyService.class));

					StateListener phoneStateListener = new StateListener();
					telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					telephonyManager.listen(phoneStateListener,
							PhoneStateListener.LISTEN_CALL_STATE);

					windowwidth = getWindowManager().getDefaultDisplay()
							.getWidth();
					System.out.println("windowwidth" + windowwidth);
					windowheight = getWindowManager().getDefaultDisplay()
							.getHeight();
					System.out.println("windowheight" + windowheight);

					MarginLayoutParams marginParams2 = new MarginLayoutParams(
							droid.getLayoutParams());

					marginParams2.setMargins((windowwidth / 24) * 2,
							(windowheight / 32) * 3, 0,
							(windowheight / 32) * 20);
					/*
					 * setMargins((windowwidth / 24) * 8, ((windowheight / 32) *
					 * 8), 0, 0);
					 */
					// marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
					RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
							marginParams2);

					droid.setLayoutParams(layoutdroid);
					droid.setTextSize(30);

					LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
					homelinear.setPadding(0, 0, 0, (windowheight / 32) * 5);
					home = (ImageView) findViewById(R.id.home);

					MarginLayoutParams marginParams1 = new MarginLayoutParams(
							home.getLayoutParams());

					marginParams1.setMargins((windowwidth / 24) * 16,
							(windowheight / 8) * 1, 0, (windowheight / 8) * 5);
					// marginParams1.setMargins(((windowwidth-home.getWidth())/2),0,(windowheight/32)*10,0);
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
							marginParams1);

					home.setLayoutParams(layout);

					droid.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							layoutParams = (LayoutParams) v.getLayoutParams();

							switch (event.getAction()) {

							case MotionEvent.ACTION_DOWN:
								int[] hompos = new int[2];
								// int[] phonepos=new int[2];
								droidpos = new int[2];
								// phone.getLocationOnScreen(phonepos);
								home.getLocationOnScreen(hompos);
								home_x = hompos[0];
								home_y = hompos[1];
								// phone_x=phonepos[0];
								// phone_y=phonepos[1];

								break;
							case MotionEvent.ACTION_MOVE:
								int x_cord = (int) event.getRawX();
								int y_cord = (int) event.getRawY();

								if (x_cord > windowwidth - (windowwidth / 24)) {
									x_cord = windowwidth - (windowwidth / 24)
											* 2;
								}
								if (y_cord > windowheight - (windowheight / 32)) {
									y_cord = windowheight - (windowheight / 32)
											* 2;
								}

								layoutParams.leftMargin = x_cord;
								layoutParams.topMargin = y_cord;

								droid.getLocationOnScreen(droidpos);
								v.setLayoutParams(layoutParams);

								if (((x_cord - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord) <= (windowwidth / 24) * 4)
										&& ((home_y - y_cord) <= (windowheight / 32) * 5)) {
									System.out.println("home overlapps");
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord + " " + droidpos[1]);

									v.setVisibility(View.GONE);

									// startActivity(new
									// Intent(Intent.ACTION_VIEW,
									// Uri.parse("content://contacts/people/")));
									finish();
								} else {
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord + " " + droidpos[1]);

									System.out.println("home notttt overlapps");
								}

								break;
							case MotionEvent.ACTION_UP:

								int x_cord1 = (int) event.getRawX();
								int y_cord2 = (int) event.getRawY();

								if (((x_cord1 - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord1) <= (windowwidth / 24) * 4)
										&& ((home_y - y_cord2) <= (windowheight / 32) * 5)) {
									System.out.println("home overlapps");
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord1 + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord2 + " " + droidpos[1]);

								} else {

									layoutParams.leftMargin = (windowwidth / 24) * 10;
									layoutParams.topMargin = (windowheight / 32) * 8;
									v.setLayoutParams(layoutParams);

								}

							}

							return true;
						}
					});

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			// ----------------------------- 열리면
			else {
				try {
					// initialize receiver

					startService(new Intent(this, MyService.class));

					StateListener phoneStateListener = new StateListener();
					telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					telephonyManager.listen(phoneStateListener,
							PhoneStateListener.LISTEN_CALL_STATE);

					windowwidth = getWindowManager().getDefaultDisplay()
							.getWidth();
					System.out.println("windowwidth" + windowwidth);
					windowheight = getWindowManager().getDefaultDisplay()
							.getHeight();
					System.out.println("windowheight" + windowheight);

					MarginLayoutParams marginParams2 = new MarginLayoutParams(
							droid.getLayoutParams());

					marginParams2.setMargins((windowwidth / 24) * 8,
							((windowheight / 32) * 8), 0, 0);

					// marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
					RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(
							marginParams2);

					droid.setLayoutParams(layoutdroid);
					droid.setTextSize(50);

					LinearLayout homelinear = (LinearLayout) findViewById(R.id.homelinearlayout);
					homelinear.setPadding(0, 0, 0, (windowheight / 32) * 5);
					home = (ImageView) findViewById(R.id.home);

					MarginLayoutParams marginParams1 = new MarginLayoutParams(
							home.getLayoutParams());

					marginParams1.setMargins((windowwidth / 24) * 8, 0,
							(windowheight / 32) * 8, 0);
					// marginParams1.setMargins(((windowwidth-home.getWidth())/2),0,(windowheight/32)*10,0);
					LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
							marginParams1);

					home.setLayoutParams(layout);

					droid.setOnTouchListener(new View.OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							layoutParams = (LayoutParams) v.getLayoutParams();

							switch (event.getAction()) {

							case MotionEvent.ACTION_DOWN:
								int[] hompos = new int[2];
								// int[] phonepos=new int[2];
								droidpos = new int[2];
								// phone.getLocationOnScreen(phonepos);
								home.getLocationOnScreen(hompos);
								home_x = hompos[0];
								home_y = hompos[1];
								// phone_x=phonepos[0];
								// phone_y=phonepos[1];

								break;
							case MotionEvent.ACTION_MOVE:
								int x_cord = (int) event.getRawX();
								int y_cord = (int) event.getRawY();

								if (x_cord > windowwidth - (windowwidth / 24)) {
									x_cord = windowwidth - (windowwidth / 24)
											* 2;
								}
								if (y_cord > windowheight - (windowheight / 32)) {
									y_cord = windowheight - (windowheight / 32)
											* 2;
								}

								layoutParams.leftMargin = x_cord;
								layoutParams.topMargin = y_cord;

								droid.getLocationOnScreen(droidpos);
								v.setLayoutParams(layoutParams);

								if (((x_cord - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord) <= (windowwidth / 24) * 4)
										&& ((home_y - y_cord) <= (windowheight / 32) * 5)) {
									System.out.println("home overlapps");
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord + " " + droidpos[1]);

									v.setVisibility(View.GONE);

									// startActivity(new
									// Intent(Intent.ACTION_VIEW,
									// Uri.parse("content://contacts/people/")));
									finish();
								} else {
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord + " " + droidpos[1]);

									System.out.println("home notttt overlapps");
								}

								break;
							case MotionEvent.ACTION_UP:

								int x_cord1 = (int) event.getRawX();
								int y_cord2 = (int) event.getRawY();

								if (((x_cord1 - home_x) <= (windowwidth / 24) * 5 && (home_x - x_cord1) <= (windowwidth / 24) * 4)
										&& ((home_y - y_cord2) <= (windowheight / 32) * 5)) {
									System.out.println("home overlapps");
									System.out.println("homeee" + home_x + "  "
											+ (int) event.getRawX() + "  "
											+ x_cord1 + " " + droidpos[0]);

									System.out.println("homeee" + home_y + "  "
											+ (int) event.getRawY() + "  "
											+ y_cord2 + " " + droidpos[1]);

								} else {

									layoutParams.leftMargin = (windowwidth / 24) * 10;
									layoutParams.topMargin = (windowheight / 32) * 8;
									v.setLayoutParams(layoutParams);

								}

							}

							return true;
						}
					});

				} catch (Exception e) {
					// TODO: handle exception
				}
				// time.setText("");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA)) {
			// this is where I can do my stuff
			return true; // because I handled the event
		}
		if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		}

		return false;

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
			// Intent i = new Intent(this, NewActivity.class);
			// startActivity(i);
			return false;
		}
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

			System.out.println("alokkkkkkkkkkkkkkkkk");
			return true;
		}
		return false;
	}

	public void onDestroy() {
		// k1.reenableKeyguard();

		super.onDestroy();
	}

}