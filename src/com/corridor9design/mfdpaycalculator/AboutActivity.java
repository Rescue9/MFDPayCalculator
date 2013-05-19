package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AboutActivity extends Activity {

	public AboutActivity() {
		// TODO Auto-generated constructor stub
	}

	AdRequest ad_request = new AdRequest();
	private AdView ad_view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);

		// create the adview instance
		ad_view = new AdView(this, AdSize.SMART_BANNER, "a15175ee2ecb52c");

		// look up linear layout
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAbout);

		// add adview to it
		layout.addView(ad_view, 0);

		// initiate a generic request to load
		ad_request.addTestDevice(AdRequest.TEST_EMULATOR); // Emulator
		ad_request.addTestDevice("EFCCE6E324855D892FC4772BE5938406"); // Galaxy S3 Test ID
		ad_request.addTestDevice("E952DED8DFB1CA8350FD5D82F409703A"); // Note 10.1 Test ID

		ad_view.loadAd(ad_request);

		getVersionNumber();
	}

	// get version number from AndroidManifest
	public void getVersionNumber() {
		String versionName = "";

		try {
			final PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		TextView version = (TextView) findViewById(R.id.about_version_info);
		version.setText("Version: " + versionName);

	}

	@Override
	public void onDestroy() {
		if (ad_view != null) {
			ad_view.destroy();
		}
		super.onDestroy();
	}
}
