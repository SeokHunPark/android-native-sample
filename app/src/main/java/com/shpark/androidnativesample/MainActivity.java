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

    private SignInButton signInButton_;
    private Button signOutButton_;
    private ImageView imageProfilePic_;
    private TextView textName_, textEmail_;
    private LinearLayout llProfileLayout_;
    private Button yesDialogButton_;
    private Button noYesDialogButton_;
    private Button systemBarHideButton_;

    private GoogleApiClient googleApiClient_;
    private boolean intentInProgress_;
    private boolean signInClicked_;
    private ConnectionResult connectionResult_;

    private com.shpark.androidnativesample.DialogBuilder dialogBuilder_;
    private com.shpark.androidnativesample.SystemBarUtil systemBarUtil_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton_ = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton_.setOnClickListener(this);
        signOutButton_ = (Button)findViewById(R.id.button_signOut);
        signOutButton_.setText("Sign Out");
        signOutButton_.setVisibility(View.GONE);
        signOutButton_.setOnClickListener(this);
        imageProfilePic_ = (ImageView)findViewById(R.id.image_profilePic);
        textName_ = (TextView)findViewById(R.id.textView_name);
        textEmail_ = (TextView)findViewById(R.id.textView_email);
        llProfileLayout_ = (LinearLayout)findViewById(R.id.layout_profile);
        llProfileLayout_.setVisibility(View.GONE);

        yesDialogButton_ = (Button)findViewById(R.id.button_yesDialog);
        yesDialogButton_.setText("Yes Dialog");
        yesDialogButton_.setOnClickListener(this);

        noYesDialogButton_ = (Button)findViewById(R.id.button_noYesDialog);
        noYesDialogButton_.setText("No Yes Dialog");
        noYesDialogButton_.setOnClickListener(this);

        systemBarHideButton_ = (Button)findViewById(R.id.button_systemBarHide);
        systemBarHideButton_.setText("System Bar Hide");
        systemBarHideButton_.setOnClickListener(this);

        dialogBuilder_ = new com.shpark.androidnativesample.DialogBuilder();
        systemBarUtil_ = new com.shpark.androidnativesample.SystemBarUtil();

        if (googleApiClient_ == null) {
            googleApiClient_ = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                            // Optionally, add additional APIs and scopes if required.
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient_.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient_ != null) {
            googleApiClient_.disconnect();
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
                dialogBuilder_.BuildYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_noYesDialog:
            {
                dialogBuilder_.BuildNoYesDialog(this, "Title", "Message.");
                break;
            }
            case R.id.button_systemBarHide:
            {
                systemBarUtil_.hideSystemBar(this);
                break;
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        // TODO: Start making API requests.
        signInClicked_ = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        getProfileInformation();
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        googleApiClient_.connect();
        updateUI(false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
        }

        if (!intentInProgress_) {
            connectionResult_ = result;

            if (signInClicked_) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                signInClicked_ = false;
            }

            intentInProgress_ = false;

            if (!googleApiClient_.isConnecting()) {
                googleApiClient_.connect();
            }
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            signInButton_.setVisibility(View.GONE);
            signOutButton_.setVisibility(View.VISIBLE);
            llProfileLayout_.setVisibility(View.VISIBLE);
        } else {
            signInButton_.setVisibility(View.VISIBLE);
            signOutButton_.setVisibility(View.GONE);
            llProfileLayout_.setVisibility(View.GONE);
        }
    }

    private void signInWithGplus() {
        if (!googleApiClient_.isConnecting()) {
            signInClicked_ = true;
            resolveSignInError();
        }
    }

    private void signOutFromGplus() {
        if (googleApiClient_.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient_);
            googleApiClient_.disconnect();
            googleApiClient_.connect();
            updateUI(false);
        }
    }

    private void resolveSignInError() {
        if (connectionResult_.hasResolution()) {
            try {
                intentInProgress_ = true;
                connectionResult_.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                intentInProgress_ = false;
                googleApiClient_.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(googleApiClient_) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient_);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(googleApiClient_);

                Log.e(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl);

                textName_.setText(personName);
                textEmail_.setText(email);

                personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + 400;

                new LoadProfileImage(imageProfilePic_).execute(personPhotoUrl);
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
            String urldisplay = params[0];
            Bitmap icon11 = null;
            try {
                InputStream is = new java.net.URL(urldisplay).openStream();
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