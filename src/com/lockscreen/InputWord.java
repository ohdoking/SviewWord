package com.lockscreen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputWord extends Activity {

	LockScreenAppActivity nActivity = new LockScreenAppActivity();

	private ArrayAdapter<String> m_adapter;
	private ArrayList<String> m_list;

	private Button listadd;
	private Button wordgo;
	private Button chacolor;
	private Button backgroundcolor;
	private EditText listtext;
	private EditText listtext2;
	private ListView lsView;

	TextView tView;
	private int color = 0;

	private static int RESULT_LOAD_IMAGE = 1;

	public Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_word);

		final DatabaseHandler db = new DatabaseHandler(this);

		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		
		
		intent = getIntent();
		
		Toast.makeText(this,  mPhoneNumber+" 님 영단어 화이팅!",
				Toast.LENGTH_LONG).show();

/*		
		 * Notification noti = new NotificationCompat.Builder(this)
		 * .setContentTitle("wish List") .setTicker("로그인됨니다")
		 * .setContentText("로긴댐") .setSmallIcon(R.drawable.ic_launcher)
		 * .build();
		 * 
		 * NotificationManager ngrManager =
		 * (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		 * 
		 * ngrManager.notify(123123,noti);
		 */


		listadd = (Button) findViewById(R.id.btn1);
		wordgo = (Button) findViewById(R.id.btn2);
		chacolor = (Button) findViewById(R.id.btn3);
		backgroundcolor = (Button) findViewById(R.id.btn4);
		listtext = (EditText) findViewById(R.id.editText1);
		listtext2 = (EditText) findViewById(R.id.editText2);
		lsView = (ListView) findViewById(R.id.listView1);

		m_list = new ArrayList<String>();

		m_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, m_list);
		lsView.setAdapter(m_adapter);

		// final String input = listtext.getText().toString();

		Log.d("Reading: ", "Reading all contacts..");
		List<Word> words = db.getAllWords();
		for (Word cn : words) {
			// Writing Contacts to log
			String stx = cn.get_word();
			String st2x = cn.get_mean();
			String addStx = stx +"/"+ st2x;

			m_list.add(addStx);
			
			m_adapter.notifyDataSetChanged();
		}
		// 모델 객체에 데이터 추가하기
		// lsView.add(input);
		// 아답터에 모델이 바뀌었다고 알린다.
		// m_adapter.notifyDataSetChanged();
		// editText초기호
		// .setText("");
		// 키보드 감추기(util파일 필요 * 첨부해놈)

		/*
		Intent in = new Intent();
		in.setType("image/*");
		in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(in, "Select Picture"),0);*/
		
		
		listadd.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String st = listtext.getText().toString();
				String st2 = listtext2.getText().toString();
				String addSt = st +"/"+ st2;
				if(st.equals("") || st2.equals("")){
					Toast.makeText(InputWord.this, "단어를 입력해주세요", Toast.LENGTH_LONG).show();
					
				}
				else{
					m_list.add(addSt);
					//m_list.add(1,listtext2.getText().toString());
					
					Log.d("Insert: ", "Inserting ..");
					db.addContact(new Word(listtext.getText().toString(), listtext2
							.getText().toString()));
					listtext.setText("");
					listtext2.setText("");
					m_adapter.notifyDataSetChanged();
				}
			}
		});

		wordgo.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = getIntent();
				intent = getIntent();
				// getIntExtra("받는변수명", 기본값)
				int i = intent.getIntExtra("yes", 1000);
				System.out.println(i);
				Intent intent1= new Intent(InputWord.this, LockScreenAppActivity.class);
				// putExtra("넘길변수명", 넘기는 int값)
				intent1.putExtra("yes", 1);

				// Activity 시작
				startActivity(intent1);

			}
		});
		
	    // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personalresults);
	    lsView.setAdapter(m_adapter);
	     registerForContextMenu(lsView);

	     lsView.setOnItemLongClickListener(new OnItemLongClickListener() {

	            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
	                    final int pos, final long id) {
	                // TODO Auto-generated method stub

	            	
	            	Builder dlg= new AlertDialog.Builder(InputWord.this);
				
	                DialogInterface.OnClickListener removeButtonClickListener = new DialogInterface.OnClickListener() {
		         		   
	            	    @Override
	            	    public void onClick(DialogInterface dialog, int which) {
	            	    	 Log.v("long clicked","id: " + id);
	                         
	                         String val =(String) arg0.getItemAtPosition(pos);
	                         String[] data = val.split("/");
	                         String val1 = data[0];
	                         System.out.println(val);
	                         System.out.println(val1);
	                         db.deleteWord(val1);
	                         m_list.remove(val);
	                         m_adapter.notifyDataSetChanged();
	            	    }

	            	};

	            	DialogInterface.OnClickListener gourlButtonClickListener = new DialogInterface.OnClickListener() {
	            	   
	            	    @Override
	            	    public void onClick(DialogInterface dialog, int which) {
	            	    	 String val =(String) arg0.getItemAtPosition(pos);
	                         String[] data = val.split("/");
	                         String val1 = data[0];
	            	    	Intent i= new Intent(InputWord.this, DetailWord.class);
	        				i.putExtra("word",val1 );

	        				// Activity 시작
	        				startActivity(i);
	            	    	
	            	    	Toast.makeText(getApplicationContext(), "I can't believe it!", Toast.LENGTH_SHORT).show();
	            	    }

	            	};
	            	
					dlg.setTitle("What are you want?")
	                .setMessage("무엇을 원하세요?")
	                .setIcon(R.drawable.ic_launcher)
	                .setPositiveButton("Naver 검색", gourlButtonClickListener)
	                .setNegativeButton("삭제",removeButtonClickListener)
	                .show();
	                /*
	                Log.v("long clicked","id: " + id);
	                
	                String val =(String) arg0.getItemAtPosition(pos);
	                String[] data = val.split("/");
	                String val1 = data[0];
	                System.out.println(val);
	                System.out.println(val1);
	                db.deleteWord(val1);
	                m_list.remove(val);
	                m_adapter.notifyDataSetChanged();
	                
	              */  
	            	return true;
	                
	            }
	        }); 
	     
	     chacolor.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String Scolor;
				tView = (TextView)findViewById(R.id.tV);
				intent = getIntent();

				if(color==0){
					chacolor.setText("글자색 바꾸기(White)");
				//	nActivity.droid.setTextColor(Color.RED);
				//  tView.setTextColor(Color.parseColor("#FFFFFF"));
				//  tView.text.setTextColor(Color.RED);
					Scolor = "Black";
					intent.putExtra("Black", Scolor);
					setResult(RESULT_OK,intent);
					color++;
					finish();
					System.out.println("글자 black");
					System.out.println("black_num : "+color);
				}
				else if(color==1){
					chacolor.setText("글자색 바꾸기(Black)");
					//nActivity.droid.setTextColor(Color.WHITE);
					Scolor = "White";
					intent.putExtra("White", Scolor);
					setResult(RESULT_OK,intent);
					color--;
					finish();

					System.out.println("글자 White");
				}
			}
		});
		// registerForContextMenu();
	     
	     backgroundcolor.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// startActivityForResult(i, RESULT_LOAD_IMAGE);

				 intent = getIntent();
				 intent.putExtra("Back", true);
				 setResult(RESULT_OK,intent);
				 finish();
				 //startActivityForResult(intent, RESULT_LOAD_IMAGE);
				 
			}
		});
	}

	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
          
         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            /* Uri selectedImage = data.getData();
             String[] filePathColumn = { MediaStore.Images.Media.DATA };
  
             Cursor cursor = getContentResolver().query(selectedImage,
                     filePathColumn, null, null, null);
             cursor.moveToFirst();
  
             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String picturePath = cursor.getString(columnIndex);
             cursor.close();*/

             Uri imageUri = data.getData();
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
          
             LinearLayout rLayout = (LinearLayout) findViewById (R.id.back);
             Drawable dr = new BitmapDrawable(bitmap);
             rLayout.setBackgroundDrawable(dr);
             /*
             RelativeLayout relative = (RelativeLayout)findViewById(R.id.back1);
             relative.setBackgroundDrawable(dr);
             */
             //Intent IntentParent = getIntent();
             //setResult (RESULT_OK, IntentParent);
             /*ImageView imageView = (ImageV iew) findViewById(R.id.imageView);
             imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

             Resources res = getResources(); //resource handle
             Drawable drawable = res.getDrawable(imageView); //new Image that was added to the res folder

              rLayout.setBackgroundDrawable(drawable);*/
          
         }
     }
	 
	   @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        Log.d("OptionMenu", "onPrepareOptionsMenu()");
	        Toast.makeText(this,  "No Action",
					Toast.LENGTH_LONG).show();
	        return super.onPrepareOptionsMenu(menu);
	    }
	     
	    /**
	     * OptionMenu 아이템이 선택될 때 호출 된다.
	     */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        Log.d("OptionMenu", "onOptionsItemSelected()");
	        return super.onOptionsItemSelected(item);
	    }
	     
	    /**
	     * OptionMenu가 종료될 때 호출 된다.
	     */
	    @Override
	    public void onOptionsMenuClosed(Menu menu) {
	        Log.d("OptionMenu", "onOptionsMenuClosed()");
	        super.onOptionsMenuClosed(menu);
	    }
}
