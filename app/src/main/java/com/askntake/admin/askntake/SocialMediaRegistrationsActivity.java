package com.askntake.admin.askntake;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 4/1/2017.
 */

public class SocialMediaRegistrationsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    //For FB
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    ProgressDialog progressDialog;

    String facebook_id;


    //For Google Plus
    private static final String TAG = SocialMediaRegistrationsActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    Button button_signup, button_login, finish_button;
    TextView tv_main_heading;

    GoogleCloudMessaging gcm;
    String regid;
    String user_latitude_value, user_longitude_value;
    boolean gmail_clicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //For FB
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        setContentView(R.layout.activity_social_media);

        finish_button = (Button) findViewById(R.id.finish_button);


        button_signup = (Button) findViewById(R.id.button_signup);
        button_login = (Button) findViewById(R.id.button_login);
        tv_main_heading = (TextView) findViewById(R.id.tv_main_heading);

        TextView tv_terms_and_conditions = (TextView) findViewById(R.id.tv_terms_and_conditions);
        temsAndCondtionsTextView(tv_terms_and_conditions);

        Typeface typefont = Typeface.createFromAsset(getAssets(), "heading_font.ttf");
        tv_main_heading.setTypeface(typefont, Typeface.NORMAL);

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //google+

        btnSignIn = (SignInButton) findViewById(R.id.gmail_login_button);

        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());


        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {

                btnSignIn.setVisibility(View.GONE);

                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);


                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile

                            String photoUrl = profile2.getProfilePictureUri(500, 500).toString();
                            facebook_id = profile2.getId();
                            JSONObject requestJsonObject = prepareRequestJsonObject(profile2.getName(), profile2.getLastName(), "", photoUrl, facebook_id, true, "Male");
                            new LoginAsyncTask(requestJsonObject).execute();
                            Log.v("facebook - profile", profile2.getFirstName());
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                }

//fst

                // Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

                String user_id = loginResult.getAccessToken().getUserId();

                // Toast.makeText(getApplicationContext(), "User ID Is:" + user_id, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                btnSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(SocialMediaRegistrationsActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                btnSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(SocialMediaRegistrationsActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }

        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupActivity = new Intent(getApplicationContext(), LoginRegistrationActivity.class);
                signupActivity.putExtra("for_login", true);
                startActivity(signupActivity);
                finish();

            }
        });
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signupActivity = new Intent(getApplicationContext(), LoginRegistrationActivity.class);
                signupActivity.putExtra("for_login", false);
                startActivity(signupActivity);
                finish();

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //Google Signin

    private void signIn() {
        /*SocialMediaRegistrationsActivity myApp = (SocialMediaRegistrationsActivity )getApplicationContext();
        myApp.mGoogleApiClient = mGoogleApiClient;*/

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());
            String personPhotoUrl = null;

            String personName = acct.getDisplayName();
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }

            String email = acct.getEmail();


            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);


            updateUI(true, acct);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false, null);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.gmail_login_button:
                signIn();
                break;

        }
    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(SocialMediaRegistrationsActivity.this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(boolean isSignedIn, GoogleSignInAccount user_data) {
        if (isSignedIn) {

            String email = user_data.getEmail();
            String photoUrl = null;
            if (user_data.getPhotoUrl() != null) {
                photoUrl = user_data.getPhotoUrl().toString().replace("50", "500");
            }
            String displayName = user_data.getDisplayName();

            JSONObject requestJsonObject = prepareRequestJsonObject(displayName, "", email, photoUrl, "", false, "Male");

            new LoginAsyncTask(requestJsonObject).execute();

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            fbLoginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);

    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void nextActivity(Profile profile) {

        final SharedPreferences sharedPreferences = getSharedPreferences(DataKeyValues.USER_DATA_PREF, MODE_PRIVATE);


        if (profile != null && sharedPreferences.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

            String photoUrl = profile.getProfilePictureUri(500, 500).toString();
            String facebook_id = profile.getId();
            JSONObject requestJsonObject = prepareRequestJsonObject(profile.getName(), profile.getLastName(), "", photoUrl, facebook_id, true, "Male");
            new LoginAsyncTask(requestJsonObject).execute();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void temsAndCondtionsTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "By continuing, you agree to our ");
        spanTxt.append("Term of services");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent myintent = new Intent(SocialMediaRegistrationsActivity.this, HelpActivity.class);
                myintent.putExtra("showpage", "terms");
                startActivity(myintent);
            }
        }, spanTxt.length() - "Term of services".length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 32, spanTxt.length(), 0);
        spanTxt.append(" and ");
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent myintent = new Intent(SocialMediaRegistrationsActivity.this, HelpActivity.class);
                myintent.putExtra("showpage", "privacy");
                startActivity(myintent);
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 32, spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    private JSONObject prepareRequestJsonObject(String firstname,
                                                String lastname,
                                                String email,
                                                String image_url,
                                                String fbId,
                                                boolean isfb,
                                                String gender) {
        JSONObject jsonObj = null;
        try {


            jsonObj = new JSONObject();
            jsonObj.accumulate("firstname", firstname);
            jsonObj.accumulate("lastname", lastname);
            jsonObj.accumulate("email", email);
            jsonObj.accumulate("image", image_url);
            jsonObj.accumulate("password", "");
            jsonObj.accumulate("fbId", fbId);
            jsonObj.accumulate("fb", isfb);
            jsonObj.accumulate("gender", gender);
            jsonObj.accumulate("divicetype", "android");

            /*regid = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                regid = gcm.register(AppConstants.SENDER_ID);
                Log.d(TAG, "Device registered, registration id=" + regid);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            jsonObj.accumulate("gcmid", regid);*/
            if (isfb) {
                jsonObj.accumulate("regtype", "fb");
            } else {
                jsonObj.accumulate("regtype", "gmail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {

        JSONObject requestObject;

        public LoginAsyncTask(JSONObject requestObject) {

            this.requestObject = requestObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SocialMediaRegistrationsActivity.this, "", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            regid = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                regid = gcm.register(AppConstants.SENDER_ID);
                Log.d(TAG, "Device registered, registration id=" + regid);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            try {
                requestObject.accumulate("gcmid", regid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceHandler serviceHandler = new ServiceHandler();
            String result = serviceHandler.makeServiceCall(AppConstants.REG_URL, "POST", requestObject);
            return result;
        }

        @Override
        protected void onPostExecute(String service_result) {
            progressDialog.dismiss();
            super.onPostExecute(service_result);

            boolean status = false;
            try {
                JSONObject jsObj = new JSONObject(service_result);
                if (jsObj.has("status")) {
                    status = jsObj.getBoolean("status");
                    if (status) {

                        if (jsObj.has("lat")) {
                            user_latitude_value = jsObj.getString("lat");
                        }
                        if (jsObj.has("lang")) {
                            user_longitude_value = jsObj.getString("lang");
                        }
                        int unread_messages_count = 0;
                        if (jsObj.has("unreadmessagescount")) {
                            unread_messages_count = jsObj.getInt("unreadmessagescount");
                        }

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(DataKeyValues.USER_DATA_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor fb_editor = sharedPreferences.edit();


                        fb_editor.putString("fb_username", jsObj.getString(DataKeyValues.USER_FIRSTNAME));
                        fb_editor.putString("fb_email", jsObj.getString(DataKeyValues.USER_EMAIL));
                        fb_editor.putString(DataKeyValues.USER_IMAGE, jsObj.getString(DataKeyValues.USER_IMAGE));
                        //fb_editor.putString("fb_gender", fb_gender);
                        fb_editor.putString("fb_id", null);
                        fb_editor.putString("user_responce_lattitude", user_latitude_value);
                        fb_editor.putString("user_responce_langitude", user_longitude_value);
                        fb_editor.putString(DataKeyValues.MY_OWN_LATITUDE_VALUE, user_latitude_value);
                        fb_editor.putString(DataKeyValues.MY_OWN_LONGITUDE_VALUE, user_longitude_value);
                        fb_editor.putBoolean("login_status", status);
                        fb_editor.putString(DataKeyValues.USER_USERID, jsObj.getString(DataKeyValues.USER_USERID));
                        // fb_editor.putString("fb_gender", jsObj.getString(DataKeyValues.USER_GENDER));
                        fb_editor.putBoolean(DataKeyValues.GMAIL_LOGIN, false);
                        fb_editor.putString(DataKeyValues.OWNER_ZIPCODE, jsObj.getString(DataKeyValues.USER_ZIPCODE));
                        fb_editor.putString(DataKeyValues.OWNER_CITY, jsObj.getString(DataKeyValues.USER_CITY));
                        fb_editor.putString(DataKeyValues.OWNER_STATE, jsObj.getString(DataKeyValues.USER_STATE));
                        if (unread_messages_count != 0) {
                            fb_editor.putInt(DataKeyValues.UNREAD_MSG_COUNT, unread_messages_count);
                        }


                        if (gmail_clicked) {
                            fb_editor.putBoolean(DataKeyValues.GMAIL_LOGIN, true);
                        } else {
                            fb_editor.putBoolean(DataKeyValues.GMAIL_LOGIN, false);
                        }
                        fb_editor.putString("fb_id", facebook_id);
                        fb_editor.putBoolean(DataKeyValues.USER_LOGIN_STATUS, true);

                        fb_editor.commit();

                        if (user_latitude_value.equalsIgnoreCase("0") && user_longitude_value.equalsIgnoreCase("0")) {
                            Intent myIntent = new Intent(SocialMediaRegistrationsActivity.this, LocationSettingsActivity.class);
                            myIntent.putExtra("updatelocation", true);
                            myIntent.putExtra("requestFrom", "display_scr");
                            startActivity(myIntent);
                            finish();
                        } else {

                            Intent intent = new Intent(SocialMediaRegistrationsActivity.this, NavigationDrawerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            finish();
                        }
                        // Toast.makeText(getActivity(), "successfully Login", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Login completed successfully..", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed..", Toast.LENGTH_SHORT).show();

                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }

}