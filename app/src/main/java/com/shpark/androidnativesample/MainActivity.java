package com.shpark.androidnativesample;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 0;

    private SignInButton _signInButton;
    private Button _signOutButton;
    private ImageView _imageProfilePic;
    private TextView _textName, _textEmail;
    private LinearLayout _llProfileLayout;
    private Button _yesDialogButton;
    private Button _noYesDialogButton;
    private Button _systemBarHideButton;
    private Button _normalNotificationButton;
    private Button _bigPictureNotificationButton;
    private Button _bigTextNotificationButton;
    private Button _addShortCutButton;

    private GoogleApiClient _googleApiClient;
    private boolean _intentInProgress;
    private boolean _signInClicked;
    private ConnectionResult _connectionResult;

    private DialogBuilder _dialogBuilder;
    //private SystemBarTool _systemBarUtil;
    private NotificationBuilder _notificationBuilder;

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

        _normalNotificationButton = (Button)findViewById(R.id.button_noti_normal);
        _normalNotificationButton.setText("Normal Notification");
        _normalNotificationButton.setOnClickListener(this);

        _bigPictureNotificationButton = (Button)findViewById(R.id.button_noti_bicpicture);
        _bigPictureNotificationButton.setText("Bigpicture Notification");
        _bigPictureNotificationButton.setOnClickListener(this);

        _bigTextNotificationButton = (Button)findViewById(R.id.button_noti_bictext);
        _bigTextNotificationButton.setText("Bigtext Notification");
        _bigTextNotificationButton.setOnClickListener(this);

        _addShortCutButton = (Button)findViewById(R.id.button_add_shortcut);
        _addShortCutButton.setText("Add ShortCut");
        _addShortCutButton.setOnClickListener(this);

        _dialogBuilder = new DialogBuilder();
        //_systemBarUtil = new SystemBarUtil();
        _notificationBuilder = new NotificationBuilder();

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
                _dialogBuilder.BuildYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_noYesDialog:
            {
                _dialogBuilder.BuildNoYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_systemBarHide:
            {
                SystemBarUtil.HideSystemBar(this);
                break;
            }
            case R.id.button_noti_normal:
            {
                //_notificationBuilder.BuildOldNotification(this, "Ticker text", "Title", "Message");
                _notificationBuilder.BuildNotification(this, "Ticker text", "Title", "Message");
                break;
            }
            case R.id.button_noti_bicpicture:
            {
                _notificationBuilder.BuildPicpictureNotification(this, "Ticker text", "Title", "Message");
                break;
            }
            case R.id.button_add_shortcut:
            {
                ShortCutUtil.AddShortCut(this);
                break;
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