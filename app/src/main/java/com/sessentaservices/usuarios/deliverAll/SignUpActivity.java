package com.sessentaservices.usuarios.deliverAll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activity.ParentActivity;
import com.countryview.view.CountryPicker;
import com.fontanalyzer.SystemFont;
import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.SupportActivity;
import com.sessentaservices.usuarios.VerifyInfoActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.PasswordViewHideManager;
import com.general.files.RegisterFbLoginResCallBack;
import com.general.files.RegisterGoogleLoginResCallBack;
import com.general.files.RegisterLinkedinLoginResCallBack;
import com.general.files.SetGeneralData;
import com.general.files.SetOnTouchList;
import com.general.files.ConfigureMemberData;
import com.general.files.SmartLogin;
import com.general.files.ActUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.service.handler.ApiHandler;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import com.facebook.WebDialog;

public class SignUpActivity extends ParentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 001;
    public MTextView titleTxt;

    ImageView imagefacebook, imagetwitter, imageGoogle, imagelinkedin;
    CallbackManager callbackManager;
    LoginButton loginButton;
    GoogleApiClient mGoogleApiClient;
    boolean isGoogleLogin = true;
    boolean isFacebookLogin = true;
    boolean isTwitterLogin = true;
    boolean isLinkdinLogin = true;
    LinearLayout socialarea;


    MaterialEditText countryBox;
    String vCountryCode = "";
    String vPhoneCode = "";
    boolean isCountrySelected = false;

    GenerateAlertBox generateAlert;

    MaterialEditText fNameBox;
    MaterialEditText lNameBox;
    MaterialEditText emailBox;
    MaterialEditText passwordBox;
    MaterialEditText invitecodeBox;
    MaterialEditText mobileBox;

    // SignUpFragment signUpFrag;
    ImageView inviteQueryImg, imgSmartLoginQuery;
    LinearLayout inviteCodeArea, llSmartLoginEnable, llLoaderView;
    ;
    private SmartLogin mSmartLogin;
    String required_str = "";
    String error_email_str = "";
    static String vSImage = "";

    MTextView signbootomHint;

    ImageView countrydropimage, countrydropimagerror;

    CheckBox checkboxTermsCond, checkboxSmartLogin;
    MTextView txtTermsCond;
    public MTextView signheaderHint, orTxt;


    MTextView btnTxt;
    LinearLayout btnArea;
    ImageView btnImg;
    LinearLayout imgClose;
    static ImageView countryimage;
    JSONObject userProfileJsonObj;
    CountryPicker countryPicker;
    Locale locale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* FacebookSdk.sdkInitialize(getActContext());
        FacebookSdk.setWebDialogTheme(R.style.FBDialogtheme);*/
        WebDialog.setWebDialogTheme(R.style.FBDialogtheme);
        setContentView(R.layout.activity_sign_up);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initview();


        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void initview() {

        if (generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY) != null && !generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY).isEmpty()) {
            FacebookSdk.setApplicationId(generalFunc.retrieveValue(Utils.FACEBOOK_APPID_KEY));
        }
        titleTxt = (MTextView) findViewById(R.id.titleTxt);


        socialarea = (LinearLayout) findViewById(R.id.socialarea);

        signheaderHint = (MTextView) findViewById(R.id.signheaderHint);
        orTxt = (MTextView) findViewById(R.id.orTxt);
        countryimage = (ImageView) findViewById(R.id.countryimage);

        btnArea = (LinearLayout) findViewById(R.id.btnArea);
        btnTxt = (MTextView) findViewById(R.id.btnTxt);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        imgClose = (LinearLayout) findViewById(R.id.imgClose);
        addToClickHandler(imgClose);

        btnImg = (ImageView) findViewById(R.id.btnImg);
        if (generalFunc.isRTLmode()) {
            btnImg.setRotation(180);
            btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
        }
        addToClickHandler(btnArea);


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

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));

        loginButton.registerCallback(callbackManager, new RegisterFbLoginResCallBack(getActContext(), callbackManager, getIntent().getBooleanExtra("isRestart", true)));


        generateAlert = new GenerateAlertBox(getActContext());


        fNameBox = (MaterialEditText) findViewById(R.id.fNameBox);
        lNameBox = (MaterialEditText) findViewById(R.id.lNameBox);
        emailBox = (MaterialEditText) findViewById(R.id.emailBox);
        countryBox = (MaterialEditText) findViewById(R.id.countryBox);
        mobileBox = (MaterialEditText) findViewById(R.id.mobileBox);
        passwordBox = (MaterialEditText) findViewById(R.id.passwordBox);
        invitecodeBox = (MaterialEditText) findViewById(R.id.invitecodeBox);
        signbootomHint = (MTextView) findViewById(R.id.signbootomHint);

        countrydropimage = (ImageView) findViewById(R.id.countrydropimage);
        countrydropimagerror = (ImageView) findViewById(R.id.countrydropimagerror);
        checkboxTermsCond = (CheckBox) findViewById(R.id.checkboxTermsCond);
        txtTermsCond = (MTextView) findViewById(R.id.txtTermsCond);

        addToClickHandler(txtTermsCond);

        mSmartLogin = new SmartLogin(getActContext(), generalFunc);

        llLoaderView = (LinearLayout) findViewById(R.id.llLoaderView);
        llLoaderView.setVisibility(View.GONE);
        llSmartLoginEnable = (LinearLayout) findViewById(R.id.llSmartLoginEnable);
        checkboxSmartLogin = (CheckBox) findViewById(R.id.checkboxSmartLogin);
        checkboxSmartLogin.setText(" " + generalFunc.retrieveLangLBl("", "LBL_QUICK_LOGIN"));
        imgSmartLoginQuery = (ImageView) findViewById(R.id.imgSmartLoginQuery);
        imgSmartLoginQuery.setOnClickListener(v -> generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_QUICK_LOGIN"), generalFunc.retrieveLangLBl("", "LBL_QUICK_LOGIN_INFO")));


        vCountryCode = generalFunc.retrieveValue(Utils.DefaultCountryCode);
        vPhoneCode = generalFunc.retrieveValue(Utils.DefaultPhoneCode);
        vSImage = generalFunc.retrieveValue(Utils.DefaultCountryImage);
        new LoadImage.builder(LoadImage.bind(vSImage), countryimage).build();
        if (!vPhoneCode.equalsIgnoreCase("")) {
            countryBox.setText("+" + generalFunc.convertNumberWithRTL(vPhoneCode));
            isCountrySelected = true;
        }

        int paddingValStart = (int) getResources().getDimension(R.dimen._35sdp);
        int paddingValEnd = (int) getResources().getDimension(R.dimen._12sdp);
        if (generalFunc.isRTLmode()) {
            countryBox.setPaddings(paddingValEnd, 0, paddingValStart, 0);
        } else {
            countryBox.setPaddings(paddingValStart, 0, paddingValEnd, 0);
        }


        inviteQueryImg = (ImageView) findViewById(R.id.inviteQueryImg);

        inviteCodeArea = (LinearLayout) findViewById(R.id.inviteCodeArea);

        inviteQueryImg.setColorFilter(Color.parseColor("#CECECE"));

        addToClickHandler(inviteQueryImg);

        inviteCodeArea.setVisibility(View.GONE);

        if (generalFunc.isReferralSchemeEnable()) {
            inviteCodeArea.setVisibility(View.VISIBLE);
        }

        removeInput();
        setLabels();



        passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordBox.setTypeface(SystemFont.FontStyle.DEFAULT.font);

        new PasswordViewHideManager(getActContext(), passwordBox, generalFunc);

        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);
        fNameBox.setInputType(InputType.TYPE_CLASS_TEXT);
        lNameBox.setInputType(InputType.TYPE_CLASS_TEXT);

        fNameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        lNameBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        emailBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        passwordBox.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mobileBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        countryBox.setShowClearButton(false);

        locale = new Locale(generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSmartLogin.isSmartLoginEnable(llSmartLoginEnable, checkboxSmartLogin);
    }

    public void removeInput() {
        Utils.removeInput(countryBox);

        countryBox.setOnTouchListener(new SetOnTouchList());

        addToClickHandler(countryBox);
    }

    public void setLabels() {


        btnTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNUP_SIGNUP"));
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SIGNUP"));

        fNameBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_FIRST_NAME_HEADER_TXT"));
        lNameBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_LAST_NAME_HEADER_TXT"));
        emailBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_LBL_TXT"));
        countryBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_COUNTRY_TXT"));
        mobileBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_NUMBER_HEADER_TXT"));
        passwordBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_PASSWORD_LBL_TXT"));

        signbootomHint.setText(generalFunc.retrieveLangLBl("", "LBL_ALREADY_HAVE_ACC"));
        signheaderHint.setText(generalFunc.retrieveLangLBl("", "LBL_SIGN_UP_WITH_SOC_ACC_HINT"));

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR");

        invitecodeBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_REFERRAL_CODE_HINT"), generalFunc.retrieveLangLBl("", "LBL_REFERRAL_CODE_HINT"));


        String termsfirstVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_PREFIX_TXT");
        String termsSecondVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION");
        String termsThirdVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_POSTFIX_TXT");
        String termsVal = termsfirstVal + " " + termsSecondVal + " " + termsThirdVal;
        manageSpanView(termsVal, termsSecondVal, txtTermsCond);

        String signupFirstVal = generalFunc.retrieveLangLBl("", "LBL_ALREADY_HAVE_ACC");
        String signupSecondVal = generalFunc.retrieveLangLBl("", "LBL_SIGN_IN");
        String signupThirdVal = generalFunc.retrieveLangLBl("", "LBL_NOW");
        String signupfinalVal = signupFirstVal + " " + signupSecondVal + " " + signupThirdVal;

        manageSpanView(signupfinalVal, signupSecondVal, signbootomHint);


    }

    android.text.style.ClickableSpan ClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {


            if (view == txtTermsCond) {
                Bundle bn = new Bundle();
                bn.putBoolean("islogin", true);
                new ActUtils(getActContext()).startActWithData(SupportActivity.class, bn);
            } else if (view == signbootomHint) {
                onBackPressed();

            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.appThemeColor_1));    // you can use custom color
            ds.setUnderlineText(false);    // this remove the underline
        }
    };

    public void manageSpanView(String finalString, String clickAbleString, MTextView txtView) {
        String firstVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_PREFIX_TXT");
        String secondVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION");
        String thirdVal = generalFunc.retrieveLangLBl("", "LBL_TERMS_CONDITION_POSTFIX_TXT");
        String finalVal = firstVal + " " + secondVal + " " + thirdVal;
        SpannableString termsSpan = new SpannableString(finalString);


        termsSpan.setSpan(
                ClickableSpan, // Span to add
                finalString.indexOf(clickAbleString), // Start of the span (inclusive)
                finalString.indexOf(clickAbleString) + String.valueOf(clickAbleString).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        );
        txtView.setText(termsSpan);
        txtView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void checkData() {
        Utils.hideKeyboard(getActContext());

        String noWhiteSpace = generalFunc.retrieveLangLBl("Password should not contain whitespace.", "LBL_ERROR_NO_SPACE_IN_PASS");
        String pass_length = generalFunc.retrieveLangLBl("Password must be", "LBL_ERROR_PASS_LENGTH_PREFIX")
                + " " + Utils.minPasswordLength + " " + generalFunc.retrieveLangLBl("or more character long.", "LBL_ERROR_PASS_LENGTH_SUFFIX");

        boolean fNameEntered = Utils.checkText(fNameBox) ? true : Utils.setErrorFields(fNameBox, required_str);
        boolean lNameEntered = Utils.checkText(lNameBox) ? true : Utils.setErrorFields(lNameBox, required_str);
        boolean isEmailBlankAndOptional = generalFunc.isEmailBlankAndOptional(generalFunc, Utils.getText(emailBox));

        boolean emailEntered = isEmailBlankAndOptional ? true : (Utils.checkText(emailBox) ?
                (generalFunc.isEmailValid(Utils.getText(emailBox)) ? true : Utils.setErrorFields(emailBox, error_email_str))
                : Utils.setErrorFields(emailBox, required_str));
        boolean mobileEntered = Utils.checkText(mobileBox) ? true : Utils.setErrorFields(mobileBox, required_str);
        boolean countryEntered = isCountrySelected ? true : false;
        boolean passwordEntered = Utils.checkText(passwordBox) ?
                (Utils.getText(passwordBox).contains(" ") ? Utils.setErrorFields(passwordBox, noWhiteSpace)
                        : (Utils.getText(passwordBox).length() >= Utils.minPasswordLength ? true : Utils.setErrorFields(passwordBox, pass_length)))
                : Utils.setErrorFields(passwordBox, required_str);

        if (countryBox.getText().length() == 0) {
            countryEntered = false;
        }

        if (!countryEntered) {

            Utils.setErrorFields(countryBox, required_str);
            countrydropimagerror.setVisibility(View.VISIBLE);
            countrydropimage.setVisibility(View.GONE);
        } else {
            countrydropimage.setVisibility(View.VISIBLE);
            countrydropimagerror.setVisibility(View.GONE);

        }
        if (mobileEntered) {
            mobileEntered = mobileBox.length() >= 3 ? true : Utils.setErrorFields(mobileBox, generalFunc.retrieveLangLBl("", "LBL_INVALID_MOBILE_NO"));
        }

        if (fNameEntered == false || lNameEntered == false || emailEntered == false || mobileEntered == false
                || countryEntered == false || passwordEntered == false) {
            return;
        }

      /*  if (!checkboxTermsCond.isChecked()) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_ACCEPT_TERMS_PRIVACY_ALERT"));
            return;
        }*/

        btnArea.setEnabled(false);
        registerUser();

    }

    public void registerUser() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "signup");
        parameters.put("vFirstName", Utils.getText(fNameBox));
        parameters.put("vLastName", Utils.getText(lNameBox));
        parameters.put("vEmail", Utils.getText(emailBox));
        parameters.put("vPhone", Utils.getText(mobileBox));
        parameters.put("vPassword", Utils.getText(passwordBox));
        parameters.put("PhoneCode", vPhoneCode);
        parameters.put("CountryCode", vCountryCode);
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("vInviteCode", Utils.getText(invitecodeBox));
        parameters.put("UserType", Utils.userType);
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSystem", Utils.eSystem_Type);


        llLoaderView.setVisibility(View.VISIBLE);
        ApiHandler.execute(getActContext(), parameters, false, true, generalFunc, responseString -> {

            llLoaderView.setVisibility(View.GONE);
            btnArea.setEnabled(true);
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    new ConfigureMemberData(responseString, generalFunc, getActContext(), true);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    setGeneralData();

                    String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);

                    String ePhoneVerified = generalFunc.getJsonValue("ePhoneVerified", userProfileJson);

//                        if (!ePhoneVerified.equals("Yes")) {
//                            notifyVerifyMobile();
//                            return;
//                        }
//
//                        Intent returnIntent = new Intent();
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();

                    MyApp.getInstance().restartWithGetDataApp();
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });

    }

    public void checkUserExist() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "isUserExist");
        parameters.put("Email", Utils.getText(emailBox));
        parameters.put("Phone", Utils.getText(mobileBox));
        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail == true) {
                    notifyVerifyMobile();
                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });
    }

    public void notifyVerifyMobile() {
        Bundle bn = new Bundle();
        bn.putString("MOBILE", vPhoneCode + Utils.getText(mobileBox));
        bn.putString("msg", "DO_PHONE_VERIFY");
        bn.putBoolean("isrestart", false);
        bn.putString("isbackshow", "No");
        new ActUtils(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);

            new RegisterGoogleLoginResCallBack(getActContext(), getIntent().getBooleanExtra("isRestart", true)).handleSignInResult(result);
        } else if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE) {
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE && resultCode == RESULT_OK && data != null) {

            vCountryCode = data.getStringExtra("vCountryCode");
            vPhoneCode = data.getStringExtra("vPhoneCode");
            vSImage = data.getStringExtra("vSImage");
            isCountrySelected = true;
            countryBox.setTextColor(getResources().getColor(R.color.black));
            countryBox.setText(vPhoneCode);
            new LoadImage.builder(LoadImage.bind(vSImage), countryimage).build();

            countryBox.setText("+" + vPhoneCode);
        } else if (requestCode == Utils.VERIFY_MOBILE_REQ_CODE && resultCode == RESULT_OK) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

    }

    public void setGeneralData() {
        String responseString = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        HashMap<String, String> storeData = new HashMap<>();
        ArrayList<String> removeData = new ArrayList<>();
        userProfileJsonObj = generalFunc.getJsonObject(responseString);
        new SetGeneralData(generalFunc, userProfileJsonObj);

        removeData.add("userHomeLocationLatitude");
        removeData.add("userHomeLocationLongitude");
        removeData.add("userHomeLocationAddress");
        removeData.add("userWorkLocationLatitude");
        removeData.add("userWorkLocationLongitude");
        removeData.add("userWorkLocationAddress");
        generalFunc.removeValue(removeData);

        if (generalFunc.getJsonArray("UserFavouriteAddress", responseString) == null) {
            return;
        }

        JSONArray userFavouriteAddressArr = generalFunc.getJsonArray("UserFavouriteAddress", responseString);
        if (userFavouriteAddressArr.length() > 0) {

            for (int i = 0; i < userFavouriteAddressArr.length(); i++) {
                JSONObject dataItem = generalFunc.getJsonObject(userFavouriteAddressArr, i);

                if (generalFunc.getJsonValueStr("eType", dataItem).equalsIgnoreCase("HOME")) {

                    storeData.put("userHomeLocationLatitude", generalFunc.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userHomeLocationLongitude", generalFunc.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userHomeLocationAddress", generalFunc.getJsonValueStr("vAddress", dataItem));

                } else if (generalFunc.getJsonValueStr("eType", dataItem).equalsIgnoreCase("WORK")) {
                    storeData.put("userWorkLocationLatitude", generalFunc.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userWorkLocationLongitude", generalFunc.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userWorkLocationAddress", generalFunc.getJsonValueStr("vAddress", dataItem));

                }

            }
        }

        generalFunc.storeData(storeData);


    }

    public Context getActContext() {
        return SignUpActivity.this;
    }


    public void onClick(View view) {

        int i = view.getId();
        Utils.hideKeyboard(getActContext());
        if (i == imagefacebook.getId()) {
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
                RegisterLinkedinLoginResCallBack registerLinkedinLoginResCallBack = new RegisterLinkedinLoginResCallBack(getActContext(), getIntent().getBooleanExtra("isRestart", true));
                registerLinkedinLoginResCallBack.continueLogin();
            }
        }

        if (i == btnArea.getId()) {
            checkData();
        } else if (i == R.id.countryBox) {
            // new ActUtils(getActContext()).startActForResult(SelectCountryActivity.class, Utils.SELECT_COUNTRY_REQ_CODE);
            if (countryPicker == null) {
                countryPicker = new CountryPicker.Builder(getActContext()).showingDialCode(true)
                        .setLocale(locale).showingFlag(true)
                        .enablingSearch(true)
                        //.setCountries(items_list)
                        .setCountrySelectionListener(country -> setData(country.getCode(), country.getDialCode(), country.getFlagName()))
                        .build();
            }
            countryPicker.show(getActContext());
        } else if (i == inviteQueryImg.getId()) {
            generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl(" What is Referral / Invite Code ?", "LBL_REFERAL_SCHEME_TXT"),
                    generalFunc.retrieveLangLBl("", "LBL_REFERAL_SCHEME"));

        } else if (i == txtTermsCond.getId()) {
            Bundle bn = new Bundle();
            bn.putBoolean("islogin", true);
            new ActUtils(getActContext()).startActWithData(SupportActivity.class, bn);

        } else if (i == imgClose.getId()) {
            finish();
        }

    }


    public void setData(String vCountryCode, String vPhoneCode, String vSImage) {
        this.vCountryCode = vCountryCode;
        this.vPhoneCode = vPhoneCode;
        isCountrySelected = true;
        this.vSImage = vSImage;
        new LoadImage.builder(LoadImage.bind(vSImage), countryimage).build();

        GeneralFunctions generalFunctions = new GeneralFunctions(MyApp.getInstance().getCurrentAct());
        countryBox.setText("+" + generalFunctions.convertNumberWithRTL(vPhoneCode));
    }

}
