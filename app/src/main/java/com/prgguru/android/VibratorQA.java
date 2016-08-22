package com.prgguru.android;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.os.Handler;
import android.widget.Toast;

public class VibratorQA extends Activity {
	Button bStart,bReset;
	Vibrator mVibrator;
	Chronometer chronometer;
	EditText editText;
	Handler mHandler;
	int shortVib= 2500;
	int longVib = 500;
	int gap = 2500; // Length of Gap Between
	long[] pattern = {0,
			shortVib, gap, shortVib, gap, shortVib, gap, shortVib, gap, shortVib, gap,shortVib, gap,shortVib, gap, //7x2.5sn
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap,
			longVib, gap, longVib, gap, longVib, gap, longVib, gap, longVib, gap//135x 0.5sn
	};
	long cPassed;
	long mainDelay,intDelay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		bStart = (Button) findViewById(R.id.btn1);
		bReset = (Button) findViewById(R.id.btn2);
		editText = (EditText) findViewById(R.id.zaman);
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		mHandler=new Handler();

		bStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Button s = (Button) v;
				if (s.getText().toString().equalsIgnoreCase("Start")) {
					String check=editText.getText().toString();
					if(TextUtils.isEmpty(check))girdiUyarı();
					else{
						s.setText("Pause");
						int hour = Integer.parseInt(editText.getText().toString());//Get the period as an hour
						mainDelay=hour*3600*1000+1000;
						chronometer.setBase(SystemClock.elapsedRealtime());//Set chronometer 00:00
						chronometer.start();
						mVibrator.vibrate(pattern, 0);
						mHandler.postDelayed(mRunnable, mainDelay);
						baslangıc();
					}
				}
				else if (s.getText().toString().equalsIgnoreCase("Pause")){
					pause();
				}
				else if(s.getText().toString().equalsIgnoreCase("Go on")){
					s.setText("Pause");
					mVibrator.vibrate(pattern, 0);
					chronometer.setBase(cPassed + SystemClock.elapsedRealtime());
					chronometer.start();
					intDelay=mainDelay+cPassed;
					mHandler.postDelayed(mRunnable, intDelay);
				}else{
					tamamUyarı();
				}
			}
		});

		// Click Listener for button Start
		bReset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mVibrator != null)
					mVibrator.cancel();
				chronometer.stop();
				chronometer.setBase(SystemClock.elapsedRealtime());//Set chronometer 00:00
				bStart.setText("Start");
			}
		});
	}

	public void onPause(){
		if(bStart.getText().toString().equalsIgnoreCase("Pause"))
		pause();
		super.onPause();
	}

	private void pause() {
		bStart.setText("Go on");
		mVibrator.cancel();
		cPassed = chronometer.getBase() - SystemClock.elapsedRealtime();
		chronometer.stop();
		mHandler.removeCallbacks(mRunnable);
	}

	@Override
	public void onDestroy() {
		//Cancel vibration when the application is about to close
		if (mVibrator != null)
			mVibrator.cancel();
			super.onDestroy();
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			Log.e("Timer", "Calls");
			mVibrator.cancel();
			chronometer.stop();
			mHandler.removeCallbacks(mRunnable);
			bStart.setText("Test tamamlandı");
			}
	};

	private void baslangıc() {Toast.makeText(this,"Test başladı!",Toast.LENGTH_SHORT).show();}
	private void tamamUyarı(){Toast.makeText(this,"Test tamamlandı,testi resetleyin!",Toast.LENGTH_LONG).show();}
	private void girdiUyarı() {Toast.makeText(this,"Bir değer giriniz!",Toast.LENGTH_LONG).show();}
}