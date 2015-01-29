package com.flybean.study;

import com.flybean.study.view.CircleProgress;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class MainActivity extends Activity {
	CircleProgress progress = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress = (CircleProgress) findViewById(R.id.circle_progress);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			int p = progress.getProgress();
			if (p>0) {
				progress.setProgress(--p);
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			int p = progress.getProgress();
			if (p < 100) {
				progress.setProgress(++p);
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			progress.setMute(!progress.getMute());
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
