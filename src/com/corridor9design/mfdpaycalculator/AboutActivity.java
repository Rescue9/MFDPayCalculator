package com.corridor9design.mfdpaycalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corridor9design.mfdpaycalculator.billing.IabHelper;
import com.corridor9design.mfdpaycalculator.billing.IabResult;
import com.corridor9design.mfdpaycalculator.billing.Inventory;
import com.corridor9design.mfdpaycalculator.billing.Purchase;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AboutActivity extends Activity {

	AdRequest ad_request = new AdRequest();
	private AdView ad_view;

	// objects
	Button purchase_button;
	TextView premium_versin_text;

	// debug tag for logging
	static final String TAG = "MFDPayCalc";

	// SKU for our products:
	static final String SKU_PREMIUM = "mfd_pay_calculator_remove_ads";

	// does user have premium key?
	boolean isPremium = false;

	// (arbitrary) tequest code for the purchase flow
	static final int RC_REQUEST = 12131;

	// create an IAB helper object
	IabHelper mHelper;

	public AboutActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);

		purchase_button = (Button) findViewById(R.id.about_premium_purchase_button);
		premium_versin_text = (TextView) findViewById(R.id.about_premium_version);

		// setup in-app billing
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApMZgB6WstG7wxH5/595qHdO5uo7vQu2wDcwNs4DGgzJVKIbHxafw3lg1RswIMECems9sqoj6bI6aISqLhASx2lX/fCDXI5PSxPymlkgNUTMlS4yd7ZBkQF1UMwbwMSJWJ84FYOjRB26ctXTkhOa9A9oL73OgMvolTNbpuCG7YB9UnoJuhlmu182537Qh5sZAMi6fAM4JfaVKicFczIuIhHXTXSGWUX0d2xPiRGXNDHzTB8D2MT1LmfTrjjC8gsEBg/+Frjk5U6I3gUH9aH2fxKeVvy+KblrZsMVNrv7APPqSJ3CSkBlI3+4FiSBjnyLGfUYdmNRBhPeXi55PJN3A9QIDAQAB"; 

		mHelper = new IabHelper(this, base64EncodedPublicKey);

		Log.d(TAG, "Starting IAB setup");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

			@Override
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished");

				if (!result.isSuccess()) {
					// oh noes, there was a problem.
					Log.d(TAG, "Problem setting up in-app billing: " + result);
				}

				// hooray, iab is fully set up!
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		getVersionNumber();
		purchasePro();
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
			ad_view.removeAllViews();
			ad_view.destroy();
		}
		// destroy billing helper
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;

		super.onDestroy();
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished");
			if (result.isFailure()) {
				Log.d(TAG, "Failed to query inventory: " + result);
				return;
			} else {
				Log.d(TAG, "Query inventory was successful.");

				// does the user have the premium upgrade?
				isPremium = inventory.hasPurchase(SKU_PREMIUM);

				// update UI here
				refreshUi();
				
				// tag log with purchase status
				Log.d(TAG, "User is " + (isPremium ? "PREMIUM" : "NOT PREMIUM"));
			}

			Log.d(TAG, "Initial inventory query finished; enabling main UI");
		}
	};

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				Log.d(TAG, "Error purchasing: " + result);
				return;
			} else if (purchase.getSku().equals(SKU_PREMIUM)) {
				// give user access to premium content & update the UI
				purchase_button.setVisibility(View.GONE);
				premium_versin_text.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

		// pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil");
		}
	}

	protected void purchasePro() {
		purchase_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHelper.launchPurchaseFlow(AboutActivity.this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener);
			}

		});
	}
	
	protected void premiumIsPurchased(){
		// lets see if we have a preference for a permium item being purchased
		PreferencesHandler ph = new PreferencesHandler();
		ph.setPreferences("is_premium_purchased", "true", this);
	}
	
	protected void refreshUi(){

		if (!isPremium) {
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
			
			// display button for purchase
			purchase_button.setVisibility(View.VISIBLE); //TODO Change to VISIBLE on production
			premium_versin_text.setVisibility(View.GONE);

		} else {
			//remove purchase button
			purchase_button.setVisibility(View.GONE);
			premium_versin_text.setVisibility(View.VISIBLE); //TODO Change to VISIBLE on production
			
			// set premium purchased preference
			premiumIsPurchased();
		}
	}
}
