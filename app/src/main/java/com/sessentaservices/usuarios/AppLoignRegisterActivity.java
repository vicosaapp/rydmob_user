package com.sessentaservices.usuarios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activity.ParentActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.WebDialog;
import com.facebook.login.widget.LoginButton;
import com.fragments.SignInFragment;
import com.fragments.SignUpFragment;
import com.general.files.InternetConnection;
import com.general.files.RegisterFbLoginResCallBack;
import com.general.files.RegisterGoogleLoginResCallBack;
import com.general.files.RegisterLinkedinLoginResCallBack;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import java.util.Arrays;

public class AppLoignRegisterActivity extends ParentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 001;
    public MTextView titleTxt;
    public MTextView signheaderHint, orTxt;
    ImageView backImgView;
    FrameLayout container;
    String type = "";
    ImageView imagefacebook, imagetwitter, imageGoogle, imagelinkedin;
    CallbackManager callbackManager;
    LoginButton loginButton;
    GoogleApiClient mGoogleApiClient;
    public SignUpFragment signUpFragment;
    boolean isGoogleLogin = true;
    boolean isFacebookLogin = true;
    boolean isTwitterLogin = true;
    boolean isLinkdinLogin = true;
    public LinearLayout socialarea, llLoaderView;
    boolean isBack = false;

    private final static String LOGIN_TAG_FRAGMENT = "LOGIN_TAG_FRAGMENT";
    private final static String SIGNUP_TAG_FRAGMENT = "SIGNUP_TAG_FRAGMENT";
    public boolean isSignInFirst = false;
    public boolean isSignUpFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            WebDialog.setWebDialogTheme(R.style.FBDialogtheme);
            setContentView(R.layout.activity_app_loign_register);

            type = getIntent().getStringExtra("type");
            intCheck = new InternetConnection(this);


            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActContext())
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


            callbackManager = CallbackManager.Factory.create();
            initview();
            setLabel();


        } catch (Exception e) {

            Logger.d("exception oncreate()", e.toString());
        }

    }


    private void initview() {

        if (generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY) != null && !generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY).isEmpty()) {
            FacebookSdk.setApplicationId(generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY));
        }
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        addToClickHandler(backImgView);
        container = (FrameLayout) findViewById(R.id.container);
        socialarea = (LinearLayout) findViewById(R.id.socialarea);

        llLoaderView = (LinearLayout) findViewById(R.id.llLoaderView);
        llLoaderView.setVisibility(View.GONE);

        signheaderHint = (MTextView) findViewById(R.id.signheaderHint);
        orTxt = (MTextView) findViewById(R.id.orTxt);


        imagefacebook = (ImageView) findViewById(R.id.imagefacebook);
        imagetwitter = (ImageView) findViewById(R.id.imagetwitter);
        imageGoogle = (ImageView) findViewById(R.id.imageGoogle);
        imagelinkedin = (ImageView) findViewById(R.id.imagelinkedin);


        addToClickHandler(imagefacebook);
        addToClickHandler(imagetwitter);
        addToClickHandler(imageGoogle);
        addToClickHandler(imagelinkedin);

        if (generalFunc.retrieveValue(Utils.FACEBOOK_LOGIN).equalsIgnoreCase("NO")) {
            isFacebookLogin = false;
            imagefacebook.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.GOOGLE_LOGIN).equalsIgnoreCase("NO")) {
            isGoogleLogin = false;
            imageGoogle.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.TWITTER_LOGIN).equalsIgnoreCase("NO")) {
            isTwitterLogin = false;
            imagetwitter.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.LINKDIN_LOGIN).equalsIgnoreCase("NO")) {
            isLinkdinLogin = false;
            imagelinkedin.setVisibility(View.GONE);
        }

        if (!isTwitterLogin && !isGoogleLogin & !isFacebookLogin && !isLinkdinLogin) {
            socialarea.setVisibility(View.GONE);
            signheaderHint.setVisibility(View.GONE);

        }
        loginButton = new LoginButton(getActContext());


        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(getActContext(), callbackManager));


    }

    public Context getActContext() {
        return AppLoignRegisterActivity.this;
    }


    private void setLabel() {

        if (type != null) {
            if (type.equals("login")) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_TXT"));
                signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_IN_WITH_SOC_ACC_HINT"));
                signUpFragment = null;
                hadnleFragment(new SignInFragment(), false, true);
                isSignInFirst = true;
            } else {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_UP"));
                signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_UP_WITH_SOC_ACC_HINT"));
                signUpFragment = new SignUpFragment();
                hadnleFragment(signUpFragment, false, false);
                isSignUpFirst = true;
            }
        }
        orTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OR_TXT"));
    }

    public void hadnleFragment(Fragment fragment, boolean isback, boolean isLogin) {
        this.isBack = isback;
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, isLogin ? LOGIN_TAG_FRAGMENT : SIGNUP_TAG_FRAGMENT);
        if (isback) {
            transaction.addToBackStack(backStateName);
        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            new RegisterGoogleLoginResCallBack(getActContext()).handleSignInResult(result);
        } else if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE) {
            if (signUpFragment != null) {
                SignUpFragment.setdata(requestCode, resultCode, data);
            } else {
                SignInFragment.setdata(requestCode, resultCode, data);
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {

        if (isBack) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            super.onBackPressed();
            isBack = false;
            return;
        }
        finish();
    }

    public void onClick(View view) {

        int i = view.getId();
        Utils.hideKeyboard(getActContext());
        if (i == backImgView.getId()) {
            Utils.hideKeyboard(AppLoignRegisterActivity.this);
            finish();

        } else if (i == imagefacebook.getId()) {
            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                generalFunc.showError();
            } else {
                loginButton.performClick();
            }

        } else if (i == imageGoogle.getId()) {
            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {

                generalFunc.showError();
            } else {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        } else if (i == imagelinkedin.getId()) {
            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
                generalFunc.showError();
            } else {
                RegisterLinkedinLoginResCallBack registerLinkedinLoginResCallBack = new RegisterLinkedinLoginResCallBack(getActContext());
                registerLinkedinLoginResCallBack.continueLogin();


            }

        }
    }

}
