package com.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.countryview.view.CountryPicker;
import com.dialogs.OpenListView;
import com.general.files.ConfigureMemberData;
import com.general.files.GeneralFunctions;
import com.general.files.GetUserData;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sessentaservices.usuarios.MyProfileActivity;
import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.VerifyInfoActivity;
import com.model.ContactModel;
import com.realmModel.Cart;
import com.realmModel.Options;
import com.realmModel.Topping;
import com.service.handler.ApiHandler;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment {

    MyProfileActivity myProfileAct;
    View view;

    GeneralFunctions generalFunc;

    String userProfileJson = "";

    MaterialEditText countryBox;
    MaterialEditText mobileBox;

    ArrayList<HashMap<String, String>> languageDataList = new ArrayList<>();
    ArrayList<HashMap<String, String>> currencyDataList = new ArrayList<>();


    String selected_currency = "";
    String default_selected_currency = "";

    String selected_language_code = "";
    String default_selected_language_code = "";
    androidx.appcompat.app.AlertDialog list_language;


    String selected_currency_symbol = "";
    androidx.appcompat.app.AlertDialog list_currency;

    MButton btn_type2;
    int submitBtnId;

    String required_str = "";
    String error_email_str = "";

    String vCountryCode = "";
    String vPhoneCode = "";
    String mobileBoxVal = "";
    boolean isCountrySelected = false;

    FrameLayout langSelectArea, currencySelectArea;
    RealmResults<Cart> realmCartList;
    ImageView countryimage;
    String vSImage = "";

    CountryPicker countryPicker;
    Locale locale;
    InternetConnection intCheck;

    MTextView fNameTextH, lNameTextH, emailTextH, langTextH, curTextH,mobileBoxHTxt;
    MaterialEditText txtfNametxt, txtlNametxt, txtemailtxt, txtlangtxt, txtcurtxt;
    ImageView langDropDownArrow,curDropDownArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        myProfileAct = (MyProfileActivity) getActivity();

        generalFunc = myProfileAct.generalFunc;
        locale = new Locale(generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        intCheck = new InternetConnection(getActContext());
        countryimage = (ImageView) view.findViewById(R.id.countryimage);

        View fNameBox = view.findViewById(R.id.fNameBox);
        fNameTextH = fNameBox.findViewById(R.id.mTextH);
        txtfNametxt = fNameBox.findViewById(R.id.mEditText);


        View lNameBox = view.findViewById(R.id.lNameBox);
        lNameTextH = lNameBox.findViewById(R.id.mTextH);
        txtlNametxt = lNameBox.findViewById(R.id.mEditText);


        View emailBox = view.findViewById(R.id.emailBox);
        emailTextH = emailBox.findViewById(R.id.mTextH);
        txtemailtxt = emailBox.findViewById(R.id.mEditText);

        countryBox = (MaterialEditText) view.findViewById(R.id.countryBox);
        mobileBox = (MaterialEditText) view.findViewById(R.id.mobileBox);

        View langBox = view.findViewById(R.id.langBox);
        langTextH = langBox.findViewById(R.id.mTextH);
        txtlangtxt = langBox.findViewById(R.id.mEditText);
        langDropDownArrow = langBox.findViewById(R.id.mDropDownArrow);
        langDropDownArrow.setVisibility(View.VISIBLE);

        View currencyBox = view.findViewById(R.id.currencyBox);
        curTextH = currencyBox.findViewById(R.id.mTextH);
        txtcurtxt = currencyBox.findViewById(R.id.mEditText);
        curDropDownArrow = currencyBox.findViewById(R.id.mDropDownArrow);
        curDropDownArrow.setVisibility(View.VISIBLE);

        mobileBoxHTxt =(MTextView) view.findViewById(R.id.mobileBoxHTxt);

        btn_type2 = ((MaterialRippleLayout) view.findViewById(R.id.btn_type2)).getChildView();
        vSImage = generalFunc.retrieveValue(Utils.DefaultCountryImage);
        int imagewidth = (int) getResources().getDimension(R.dimen._30sdp);
        int imageheight = (int) getResources().getDimension(R.dimen._20sdp);
        new LoadImage.builder(LoadImage.bind(Utils.getResizeImgURL(getActContext(), vSImage, imagewidth, imageheight)), countryimage).build();


        currencySelectArea = (FrameLayout) view.findViewById(R.id.currencySelectArea);
        langSelectArea = (FrameLayout) view.findViewById(R.id.langSelectArea);

        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);

        addToClickHandler(btn_type2);
        mobileBox.setInputType(InputType.TYPE_CLASS_NUMBER);

        txtemailtxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT);

        mobileBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

        setLabels();

        userProfileJson = myProfileAct.obj_userProfile.toString();

        removeInput();

        setData();
        buildLanguageList();


        realmCartList = getCartData();
        myProfileAct.changePageTitle(generalFunc.retrieveLangLBl("", "LBL_EDIT_PROFILE_TXT"));

        if (myProfileAct.isEmail) {
            emailBox.requestFocus();
        }

        if (myProfileAct.isMobile) {
            mobileBox.requestFocus();
        }
        return view;
    }

    public void setLabels() {
        fNameTextH.setText(generalFunc.retrieveLangLBl("", "LBL_FIRST_NAME_HEADER_TXT"));
        lNameTextH.setText(generalFunc.retrieveLangLBl("", "LBL_LAST_NAME_HEADER_TXT"));
        emailTextH.setText(generalFunc.retrieveLangLBl("", "LBL_EMAIL_LBL_TXT"));
        mobileBox.setBothText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_NUMBER_HEADER_TXT"));
        langTextH.setText(generalFunc.retrieveLangLBl("", "LBL_LANGUAGE_TXT"));
        curTextH.setText(generalFunc.retrieveLangLBl("", "LBL_CURRENCY_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_UPDATE"));
        txtemailtxt.setHint(generalFunc.retrieveLangLBl("", "LBL_ENTER_EMAIL_HINT"));
        txtfNametxt.setHint(generalFunc.retrieveLangLBl("", "LBL_FIRST_NAME_HEADER_TXT"));
        txtlNametxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LAST_NAME_HEADER_TXT"));
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR_TXT");
        mobileBoxHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MOBILE_NUMBER_HINT_TXT"));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void removeInput() {
        Utils.removeInput(countryBox);
        Utils.removeInput(txtlangtxt);
        Utils.removeInput(txtcurtxt);

        if (generalFunc.retrieveValue("showCountryList").equalsIgnoreCase("Yes")) {
            view.findViewById(R.id.countrydropimage).setVisibility(View.VISIBLE);
            addToClickHandler(countryBox);
            countryBox.setOnTouchListener(new setOnTouchList());
        }

        txtlangtxt.setOnTouchListener(new setOnTouchList());
        txtcurtxt.setOnTouchListener(new setOnTouchList());
        txtlangtxt.setOnClickListener(view -> {
            //
            showLanguageList();
        });
        txtcurtxt.setOnClickListener(view -> {
            //
            showCurrencyList();
        });
    }

    public void setData() {
        txtfNametxt.setText(generalFunc.getJsonValue("vName", userProfileJson));
        txtlNametxt.setText(generalFunc.getJsonValue("vLastName", userProfileJson));
        txtemailtxt.setText(generalFunc.getJsonValue("vEmail", userProfileJson));
        countryBox.setText(generalFunc.convertNumberWithRTL("+" + generalFunc.getJsonValue("vPhoneCode", userProfileJson)));
        mobileBoxVal = generalFunc.getJsonValue("vPhone", userProfileJson);
        mobileBox.setText(mobileBoxVal);
        txtcurtxt.setText(generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson));

        if (!generalFunc.getJsonValue("vPhoneCode", userProfileJson).equals("")) {
            isCountrySelected = true;
            vPhoneCode = generalFunc.getJsonValue("vPhoneCode", userProfileJson);
            vCountryCode = generalFunc.getJsonValue("vCountry", userProfileJson);
        }
        if (generalFunc.getJsonValue("vSCountryImage", userProfileJson) != null && !generalFunc.getJsonValue("vSCountryImage", userProfileJson).equalsIgnoreCase("")) {
            vSImage = generalFunc.getJsonValue("vSCountryImage", userProfileJson);
            int imagewidth = (int) getResources().getDimension(R.dimen._30sdp);
            int imageheight = (int) getResources().getDimension(R.dimen._20sdp);

            new LoadImage.builder(LoadImage.bind(Utils.getResizeImgURL(getActContext(), vSImage, imagewidth, imageheight)), countryimage).build();

        }

        selected_currency = generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson);
        default_selected_currency = selected_currency;
    }

    public void buildLanguageList() {

        JSONArray languageList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.LANGUAGE_LIST_KEY));
        languageDataList.clear();


        HashMap<String, String> data = new HashMap<>();
        data.put(Utils.LANGUAGE_LIST_KEY, "");
        data.put(Utils.LANGUAGE_CODE_KEY, "");
        data = generalFunc.retrieveValue(data);

        for (int i = 0; i < languageList_arr.length(); i++) {
            JSONObject obj_temp = generalFunc.getJsonObject(languageList_arr, i);

            HashMap<String, String> mapData = new HashMap<>();
            mapData.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
            mapData.put("vCode", generalFunc.getJsonValueStr("vCode", obj_temp));
            mapData.put("vService_TEXT_color", generalFunc.getJsonValueStr("vService_TEXT_color", obj_temp));
            mapData.put("vService_BG_color", generalFunc.getJsonValueStr("vService_BG_color", obj_temp));

            if (Utils.getText(txtlangtxt).equalsIgnoreCase(generalFunc.getJsonValueStr("vTitle", obj_temp))) {
                selLanguagePosition = i;
            }

            if ((data.get(Utils.LANGUAGE_CODE_KEY)).equalsIgnoreCase(generalFunc.getJsonValueStr("vCode", obj_temp))) {
                selLanguagePosition = i;

                txtlangtxt.setText(generalFunc.getJsonValueStr("vTitle", obj_temp));
            }

            languageDataList.add(mapData);

            if ((generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY)).equals(generalFunc.getJsonValue("vCode", obj_temp))) {
                selected_language_code = generalFunc.getJsonValueStr("vCode", obj_temp);

            }
        }


        if (languageDataList.size() < 2) {
            langSelectArea.setVisibility(View.GONE);
        }

        buildCurrencyList();

    }

    public void buildCurrencyList() {


        JSONArray currencyList_arr = generalFunc.getJsonArray(generalFunc.retrieveValue(Utils.CURRENCY_LIST_KEY));
        currencyDataList.clear();
        if (currencyList_arr != null) {
            for (int i = 0; i < currencyList_arr.length(); i++) {
                JSONObject obj_temp = generalFunc.getJsonObject(currencyList_arr, i);

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("vName", generalFunc.getJsonValueStr("vName", obj_temp));
                mapData.put("vCode", generalFunc.getJsonValueStr("vSymbol", obj_temp));
                mapData.put("vSymbol", generalFunc.getJsonValueStr("vSymbol", obj_temp));
                mapData.put("vService_BG_color", generalFunc.getJsonValueStr("vService_BG_color", obj_temp));
                mapData.put("vService_TEXT_color", generalFunc.getJsonValueStr("vService_TEXT_color", obj_temp));

                if (Utils.getText(txtcurtxt).equalsIgnoreCase(generalFunc.getJsonValueStr("vName", obj_temp))) {
                    selCurrancyPosition = i;
                }

                currencyDataList.add(mapData);

            }
            if (generalFunc.getJsonValue("ENABLE_OPTION_UPDATE_CURRENCY", userProfileJson).equalsIgnoreCase("No")) {
                txtcurtxt.setVisibility(View.VISIBLE);
                txtcurtxt.setEnabled(false);
                txtcurtxt.setClickable(false);
            } else {

                if (currencyDataList.size() < 2) {
                    currencySelectArea.setVisibility(View.GONE);
                }

            }
        } else {
            currencySelectArea.setVisibility(View.GONE);
        }

        if (languageDataList.size() < 2) {
            langSelectArea.setVisibility(View.GONE);
        }


    }

    public void showCurrencyList() {
        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SELECT_CURRENCY"), currencyDataList, OpenListView.OpenDirection.CENTER, true, position -> {

            selCurrancyPosition = position;
            HashMap<String, String> mapData = currencyDataList.get(position);
            selected_currency = mapData.get("vName");
            txtcurtxt.setText(mapData.get("vName"));

        }, true, generalFunc.retrieveLangLBl("", "LBL_CURRENCY_PREFER"), false).show(selCurrancyPosition, "vName");
    }

    int selCurrancyPosition = -1;
    int selLanguagePosition = -1;

    public void showLanguageList() {


        OpenListView.getInstance(getActContext(), getSelectLangText(), languageDataList, OpenListView.OpenDirection.CENTER, true, position -> {

            selLanguagePosition = position;
            HashMap<String, String> mapData = languageDataList.get(position);

            selected_language_code = mapData.get("vCode");

            if (!intCheck.isNetworkConnected() && !intCheck.check_int()) {
                generalFunc.showGeneralMessage("",
                        generalFunc.retrieveLangLBl("No Internet Connection", "LBL_NO_INTERNET_TXT"));
            } else {
                if (!generalFunc.retrieveValue(Utils.DEFAULT_LANGUAGE_VALUE).equals(mapData.get("vTitle"))) {
                    txtlangtxt.setText(mapData.get("vTitle"));
                    generalFunc.storeData(Utils.DEFAULT_LANGUAGE_VALUE, mapData.get("vTitle"));

                }
            }
        }, true, generalFunc.retrieveLangLBl("", "LBL_LANG_PREFER"), false).show(selLanguagePosition, "vTitle");


    }

    public String getSelectLangText() {
        return ("" + generalFunc.retrieveLangLBl("Select", "LBL_SELECT_LANGUAGE_HINT_TXT"));
    }

    public void checkValues() {


        boolean fNameEntered = Utils.checkText(txtfNametxt) ? true : Utils.setErrorFields(txtfNametxt, required_str);
        boolean lNameEntered = Utils.checkText(txtlNametxt) ? true : Utils.setErrorFields(txtlNametxt, required_str);
        boolean isEmailBlankAndOptional = generalFunc.isEmailBlankAndOptional(generalFunc, Utils.getText(txtemailtxt));
        boolean emailEntered = isEmailBlankAndOptional ? true : (Utils.checkText(txtemailtxt) ?
                (generalFunc.isEmailValid(Utils.getText(txtemailtxt)) ? true : Utils.setErrorFields(txtemailtxt, error_email_str))
                : Utils.setErrorFields(txtemailtxt, required_str));
        boolean mobileEntered = Utils.checkText(mobileBox) ? true : Utils.setErrorFields(mobileBox, required_str);
        boolean countryEntered = isCountrySelected ? true : false;
        boolean currencyEntered = !selected_currency.equals("") ? true : Utils.setErrorFields(txtcurtxt, required_str);


        if (mobileEntered) {
            mobileEntered = mobileBox.length() >= 3 ? true : Utils.setErrorFields(mobileBox, generalFunc.retrieveLangLBl("", "LBL_INVALID_MOBILE_NO"));
        }
        if (fNameEntered == false || lNameEntered == false || emailEntered == false || mobileEntered == false
                || countryEntered == false || currencyEntered == false) {
            return;
        }

        String currentMobileNum = generalFunc.getJsonValue("vPhone", userProfileJson);
        String currentPhoneCode = generalFunc.getJsonValue("vPhoneCode", userProfileJson);

        if (!currentPhoneCode.equals(vPhoneCode) || !currentMobileNum.equals(mobileBoxVal)) {
            if (generalFunc.retrieveValue(Utils.MOBILE_VERIFICATION_ENABLE_KEY).equals("Yes")) {
                notifyVerifyMobile();
                return;
            }
        }

        /** Below Code block Used when DeliverAll Enable **/
        if (realmCartList != null && realmCartList.size() > 0 && (!default_selected_currency.equalsIgnoreCase(selected_currency) || !default_selected_language_code.equalsIgnoreCase(selected_language_code)) && generalFunc.getJsonValue("DELIVERALL", userProfileJson) != null && generalFunc.getJsonValue("DELIVERALL", userProfileJson).equalsIgnoreCase("Yes")) {

            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
            generateAlert.setCancelable(false);
            generateAlert.setBtnClickList(btn_id -> {
                if (btn_id == 0) {
                    generateAlert.closeAlertBox();
                } else {
                    updateProfile();
                }

            });
            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("your cart is clear", "LBL_CART_REMOVE_NOTE"));
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
            generateAlert.showAlertBox();

            return;
        }
        /** Above Code block Used when DeliverAll Enable **/

        updateProfile();

    }

    public void notifyVerifyMobile() {
        Bundle bn = new Bundle();
        bn.putString("MOBILE", vPhoneCode + mobileBoxVal);
        bn.putString("msg", "DO_PHONE_VERIFY");
        generalFunc.verifyMobile(bn, myProfileAct.getEditProfileFrag(), VerifyInfoActivity.class);

    }

    public void updateProfile() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "updateUserProfileDetail");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("vName", Utils.getText(txtfNametxt));
        parameters.put("vLastName", Utils.getText(txtlNametxt));
        parameters.put("vPhone", Utils.getText(mobileBox));
        parameters.put("vPhoneCode", vPhoneCode);
        parameters.put("vCountry", vCountryCode);
        parameters.put("vEmail", Utils.getText(txtemailtxt));
        parameters.put("CurrencyCode", selected_currency);
        parameters.put("LanguageCode", selected_language_code);
        parameters.put("UserType", Utils.app_type);

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    String currentLangCode = generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY);
                    String vCurrencyPassenger = generalFunc.getJsonValue("vCurrencyPassenger", userProfileJson);

                    String messgeJson = generalFunc.getJsonValue(Utils.message_str, responseString);
                    generalFunc.storeData(Utils.USER_PROFILE_JSON, messgeJson);
                    responseString = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);


                    new ConfigureMemberData(responseString, generalFunc, getActContext(), false);


                    if (!currentLangCode.equals(selected_language_code) || !selected_currency.equals(vCurrencyPassenger)) {

                        changeLanguagedata(selected_language_code);

                    } else {
                        myProfileAct.changeUserProfileJson(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                    }

                } else {
                    generalFunc.showGeneralMessage("",
                            generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
        });

    }

    public RealmResults<Cart> getCartData() {
        Realm realm = MyApp.getRealmInstance();
        return realm.where(Cart.class).findAll();
    }

    public void changeLanguagedata(String langcode) {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", langcode);
        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    Realm realm = MyApp.getRealmInstance();
                    realm.beginTransaction();
                    realm.delete(Cart.class);
                    realm.delete(Topping.class);
                    realm.delete(Options.class);
                    realm.commitTransaction();

                    generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));
                    generalFunc.storeData(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());


                    Gson gson = new Gson();
                    String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                            }.getType()
                    );
                    if (contactdetails != null && contactdetails.shouldremove) {
                        generalFunc.removeValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                    }

                    generalFunc = MyApp.getInstance().getGeneralFun(getActContext());

                    GenerateAlertBox alertBox = generalFunc.notifyRestartApp();
                    alertBox.setCancelable(false);
                    alertBox.setBtnClickList(btn_id -> {

                        if (btn_id == 1) {
                            //  generalFunc.restartApp();
                            generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, selected_language_code);
                            generalFunc.storeData(Utils.DEFAULT_CURRENCY_VALUE, selected_currency);
                            GetUserData getUserData = new GetUserData(generalFunc, MyApp.getInstance().getApplicationContext());
                            getUserData.GetConfigDataForLocalStorage();
                            new Handler().postDelayed(() -> generalFunc.restartApp(), 100);

                        }
                    });
                }
            }
        });

    }

    public Context getActContext() {
        return myProfileAct.getActContext();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.SELECT_COUNTRY_REQ_CODE && resultCode == myProfileAct.RESULT_OK && data != null) {
            vCountryCode = data.getStringExtra("vCountryCode");
            vPhoneCode = data.getStringExtra("vPhoneCode");
            isCountrySelected = true;
            vSImage = data.getStringExtra("vSImage");

            countryBox.setText(generalFunc.convertNumberWithRTL("+" + vPhoneCode));
        } else if (requestCode == Utils.VERIFY_MOBILE_REQ_CODE && resultCode == myProfileAct.RESULT_OK) {

            if (realmCartList != null && realmCartList.size() > 0 && (!default_selected_currency.equalsIgnoreCase(selected_currency) || !default_selected_language_code.equalsIgnoreCase(selected_language_code))) {

                final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                generateAlert.setCancelable(false);
                generateAlert.setBtnClickList(btn_id -> {
                    if (btn_id == 0) {
                        generateAlert.closeAlertBox();
                    } else {
                        updateProfile();
                    }

                });
                generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("your cart is clear", "LBL_CART_REMOVE_NOTE"));
                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_YES"));
                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_NO"));
                generateAlert.showAlertBox();
            } else {
                updateProfile();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getActivity());
    }

    public class setOnTouchList implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP && !view.hasFocus()) {
                view.performClick();
            }
            return true;
        }
    }


    public void onClickView(View view) {
        Utils.hideKeyboard(getActivity());
        int i = view.getId();

        if (i == submitBtnId) {

            checkValues();
        } else if (i == R.id.countryBox) {
            if (countryPicker == null) {
                countryPicker = new CountryPicker.Builder(getActContext()).showingDialCode(true)
                        .setLocale(locale).showingFlag(true)
                        .enablingSearch(true)
                        .setCountrySelectionListener(country -> setData(country.getCode(), country.getDialCode(), country.getFlagName()))
                        .build();
            }
            countryPicker.show(getActContext());
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
