package com.shpark.androidnativesample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String SENDER_ID = "452415134877";
    private GoogleCloudMessaging _gcm;
    private String _regid;

    private boolean _isNotificationOn;
    private int _notificationIndex;

    private SignInButton _signInButton;
    private Button _signOutButton;
    private ImageView _imageProfilePic;
    private TextView _textName, _textEmail;
    private LinearLayout _llProfileLayout;
    private Button _yesDialogButton;
    private Button _noYesDialogButton;
    private Button _systemBarHideButton;
    private Switch _notificationOnOffSwitch;
    private Button _normalNotificationButton;
    private Button _bigPictureNotificationButton;
    private Button _bigTextNotificationButton;
    private Button _addShortCutButton;
    private Button _checkRootingButton;
    private Button _checkNetworkStateButton;

    private GoogleApiClient _googleApiClient;
    private boolean _intentInProgress;
    private boolean _signInClicked;
    private ConnectionResult _connectionResult;

    private NotificationUtil _notificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        _signInButton.setOnClickListener(this);
        _signOutButton = (Button)findViewById(R.id.button_signOut);
        _signOutButton.setText("Sign Out");
        _signOutButton.setVisibility(View.GONE);
        _signOutButton.setOnClickListener(this);
        _imageProfilePic = (ImageView)findViewById(R.id.image_profilePic);
        _textName = (TextView)findViewById(R.id.textView_name);
        _textEmail = (TextView)findViewById(R.id.textView_email);
        _llProfileLayout = (LinearLayout)findViewById(R.id.layout_profile);
        _llProfileLayout.setVisibility(View.GONE);

        _yesDialogButton = (Button)findViewById(R.id.button_yesDialog);
        _yesDialogButton.setText("Yes Dialog");
        _yesDialogButton.setOnClickListener(this);

        _noYesDialogButton = (Button)findViewById(R.id.button_noYesDialog);
        _noYesDialogButton.setText("No Yes Dialog");
        _noYesDialogButton.setOnClickListener(this);

        _systemBarHideButton = (Button)findViewById(R.id.button_systemBarHide);
        _systemBarHideButton.setText("System Bar Hide");
        _systemBarHideButton.setOnClickListener(this);

        _notificationOnOffSwitch = (Switch)findViewById(R.id.switch_noti_onoff);
        _notificationOnOffSwitch.setChecked(true);
        _notificationOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Log.d(TAG, "Is checked");
                    _isNotificationOn = true;
                    if (checkPlayServices()) {
                        Log.i(TAG, "Check device for Play Services API");
                        getGcmTokenInBackground(getApplicationContext());
                    } else {
                        Log.i(TAG, "No valid Google Play Services APK found.");
                    }
                }else{
                    Log.d(TAG, "Is not checked");
                    _isNotificationOn = false;
                    deleteGcmTokeInBackground(getApplicationContext());
                }
            }
        });

        _normalNotificationButton = (Button)findViewById(R.id.button_noti_normal);
        _normalNotificationButton.setText("Normal Notification");
        _normalNotificationButton.setOnClickListener(this);

        _bigPictureNotificationButton = (Button)findViewById(R.id.button_noti_bigpicture);
        _bigPictureNotificationButton.setText("Bigpicture Notification");
        _bigPictureNotificationButton.setOnClickListener(this);

        _bigTextNotificationButton = (Button)findViewById(R.id.button_noti_bigtext);
        _bigTextNotificationButton.setText("Bigtext Notification");
        _bigTextNotificationButton.setOnClickListener(this);

        _addShortCutButton = (Button)findViewById(R.id.button_add_shortcut);
        _addShortCutButton.setText("Add ShortCut");
        _addShortCutButton.setOnClickListener(this);

        _checkRootingButton = (Button)findViewById(R.id.button_check_rooting);
        _checkRootingButton.setText("Check Rooting");
        _checkRootingButton.setOnClickListener(this);

        _checkNetworkStateButton = (Button)findViewById(R.id.button_check_networkstate);
        _checkNetworkStateButton.setText("Check Network State");
        _checkNetworkStateButton.setOnClickListener(this);

        _notificationUtil = new NotificationUtil();

        // Google login.
        if (_googleApiClient == null) {
            _googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                            // Optionally, add additional APIs and scopes if required.
                    .build();
        }

        // Create shortcut.
        if (PreferenceUtil.GetPreferenceString(this, "isFirst") == PreferenceUtil.DEFAULT_VALUE) {
            Log.d(TAG, "First execute.");
            ShortCutUtil.AddShortCut(this);
            PreferenceUtil.SavePreferenceString(this, "isFirst", "false");
        }

        _notificationIndex = 0;

        // GCM
        // Check device for Play Services APK.
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            //Intent intent = new Intent(this, RegistrationIntentService.class);
            //startService(intent);
        }

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            Log.i(TAG, "Check device for Play Services API");

            // Old version.
            //_gcm = GoogleCloudMessaging.getInstance(this);
            _regid = getStoredRegistrationId(getApplicationContext());

            //if (regid.isEmpty()) {
            if (_regid.equals("")) {
                getGcmTokenInBackground(getApplicationContext());
            } else {
                Log.d(TAG, "Registered regid = " + _regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        // Keep the screen on.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (_googleApiClient != null) {
            _googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
            {
                signInWithGplus();
                break;
            }
            case R.id.button_signOut:
            {
                signOutFromGplus();
                break;
            }
            case R.id.button_yesDialog:
            {
                DialogUtil.BuildYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_noYesDialog:
            {
                DialogUtil.BuildNoYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_systemBarHide:
            {
                SystemBarUtil.HideSystemBar(this);
                break;
            }
            case R.id.button_noti_normal:
            {
                //_notificationUtil.BuildOldNotification(this, "Ticker text", "Title", "Message");
                _notificationUtil.BuildNotification(this, _notificationIndex, "Normal ticker text", "Normal title", "Normal message");
                _notificationIndex++;
                break;
            }
            case R.id.button_noti_bigpicture:
            {
                _notificationUtil.BuildPicpictureNotification(this, _notificationIndex, "Bigpicture ticker text", "Bigpicture title", "Bigpicture message", "Bigpicture Expanded Title", "Bigpicture expanded message");
                _notificationIndex++;
                break;
            }
            case R.id.button_noti_bigtext:
            {
                _notificationUtil.BuildBigTextNotification(this, _notificationIndex, "Bigtext ticker text", "Bigtext title", "Bigtext message", "Bigtext Expanded Title",
                        "A long time ago, in a galaxy far,\n" +
                                "far away....\n" +
                                "\n" +
                                "It is a period of civil war.\n" +
                                "Rebel spaceships, striking\n" +
                                "from a hidden base, have won\n" +
                                "their first victory against\n" +
                                "the evil Galactic Empire.");
                _notificationIndex++;
                break;
            }
            case R.id.button_add_shortcut:
            {
                ShortCutUtil.AddShortCut(this);
                break;
            }
            case R.id.button_check_rooting:
            {
                HackDetectUtil.CheckRooting(this);
            }
            case R.id.button_check_networkstate:
            {
                NetworkStateUtil.CheckNetworkState(this);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "GoogleApiClient connected");
        // TODO: Start making API requests.
        _signInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleApiClient connection suspended");
        _googleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
        }

        if (!_intentInProgress) {
            _connectionResult = result;

            if (_signInClicked) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                _signInClicked = false;
            }

            _intentInProgress = false;

            if (!_googleApiClient.isConnecting()) {
                _googleApiClient.connect();
            }
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            _signInButton.setVisibility(View.GONE);
            _signOutButton.setVisibility(View.VISIBLE);
            _llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            _signInButton.setVisibility(View.VISIBLE);
            _signOutButton.setVisibility(View.GONE);
            _llProfileLayout.setVisibility(View.GONE);
        }
    }

    private void signInWithGplus() {
        if (!_googleApiClient.isConnecting()) {
            _signInClicked = true;
            resolveSignInError();
        }
    }

    private void signOutFromGplus() {
        if (_googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(_googleApiClient);
            _googleApiClient.disconnect();
            _googleApiClient.connect();
            updateUI(false);
        }
    }

    private void resolveSignInError() {
        if (_connectionResult.hasResolution()) {
            try {
                _intentInProgress = true;
                _connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                _intentInProgress = false;
                _googleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(_googleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(_googleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(_googleApiClient);

                Log.d(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl);

                _textName.setText(personName);
                _textEmail.setText(email);

                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + 400;

                new LoadProfileImage(_imageProfilePic).execute(personPhotoUrl);
            } else {
                Toast.makeText(getApplicationContext(), "Person imformation is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GCM
    private String getStoredRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        //if (registrationId.isEmpty()) {
        if (registrationId.equals("")) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void getGcmTokenInBackground(final Context context) {
        Log.d(TAG, "Register in background.");
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    _regid = InstanceID.getInstance(context).getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    /*String token = instanceID.getToken(getString(R.string.gcm_senderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(TAG, "GCM Registration Token: " + token);*/

                    // Old version.
                    /*if (_gcm == null) {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    _regid = _gcm.register(SENDER_ID);*/

                    msg = "Device registered, registration ID = " + _regid;
                    Log.d(TAG, msg);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), _regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.

    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public void deleteGcmTokeInBackground(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InstanceID.getInstance(context).deleteToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    Log.d(TAG, "delete token succeeded." + "\nsenderId: " + SENDER_ID);
                } catch (final IOException e) {
                    Log.d(TAG, "remove token failed." + "\nsenderId: " + SENDER_ID + "\nerror: " + e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    // Image download.
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public LoadProfileImage(ImageView bitmapImage) {
            this.bitmapImage = bitmapImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlDisplay = params[0];
            Bitmap icon11 = null;
            try {
                InputStream is = new java.net.URL(urlDisplay).openStream();
                icon11 = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon11;
        }

        protected void onPostExecute(Bitmap result) {
            bitmapImage.setImageBitmap(result);
        }
    }
}