package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

import com.sessentaservices.usuarios.AppLoginActivity;
import com.sessentaservices.usuarios.BuildConfig;
import com.service.handler.ApiHandler;
import com.service.handler.AppService;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Admin on 19-06-2017.
 */

public class GetUserData {

    GeneralFunctions generalFunc;
    Context mContext;
    /*Multi*/
    String tripId = "";
    boolean releaseCurrActInstance = true;

    public GetUserData(GeneralFunctions generalFunc, Context mContext) {
        this.generalFunc = generalFunc;
        this.mContext = mContext;
        this.releaseCurrActInstance = true;
    }

    /*Track Particular Trip Data*/
    public GetUserData(GeneralFunctions generalFunc, Context mContext, String tripID) {
        this.generalFunc = generalFunc;
        this.mContext = mContext;
        this.tripId = tripID;

        if (Utils.checkText(tripID)) {
            this.releaseCurrActInstance = false;
        }
    }

    public void getLatestDataOnly() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDetail");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        if (Utils.checkText(tripId)) {
            generalFunc.storeData(Utils.isMultiTrackRunning, "Yes");
            parameters.put("LiveTripId", tripId);
        } else {
            generalFunc.storeData(Utils.isMultiTrackRunning, "No");
        }

        if (!generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY).equalsIgnoreCase("")) {
            parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        }


        ApiHandler.execute(mContext, parameters, true, true, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {


                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));

                }
            }
        });

    }

    public void getData() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDetail");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vDeviceType", Utils.deviceType);
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        if (Utils.checkText(tripId)) {
            generalFunc.storeData(Utils.isMultiTrackRunning, "Yes");
            parameters.put("LiveTripId", tripId);
        } else {
            generalFunc.storeData(Utils.isMultiTrackRunning, "No");
        }

        if (!generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY).equalsIgnoreCase("")) {
            parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        }


        Logger.e("GetUserData", "::Being Called::" + releaseCurrActInstance + "::" + ((Activity) mContext).isFinishing());

        ApiHandler.execute(mContext, parameters, true, true, generalFunc, responseString -> {

            Logger.e("GetUserData", "::Called::");

            if (responseString != null && !responseString.equals("")) {


                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                if (Utils.checkText(responseString) && message.equals("SESSION_OUT")) {
                    AppService.destroy();
                    MyApp.getInstance().notifySessionTimeOut();
                    Utils.runGC();
                    return;
                }

                if (isDataAvail) {

                    generalFunc.storeData(Utils.USER_PROFILE_JSON, generalFunc.getJsonValue(Utils.message_str, responseString));
                    new OpenMainProfile(mContext,
                            generalFunc.getJsonValue(Utils.message_str, responseString), true, generalFunc, tripId).startProcess();

                    if (releaseCurrActInstance) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            try {

                                ActivityCompat.finishAffinity((Activity) mContext);

                            } catch (Exception e) {
                            }
                            Utils.runGC();
                        }, 300);

                    }


                } else {
                    if (!generalFunc.getJsonValue("isAppUpdate", responseString).trim().equals("")
                            && generalFunc.getJsonValue("isAppUpdate", responseString).equals("true")) {

                    } else {

                        if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_CONTACT_US_STATUS_NOTACTIVE_COMPANY") ||
                                generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_ACC_DELETE_TXT") ||
                                generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LBL_CONTACT_US_STATUS_NOTACTIVE_DRIVER")) {

                            GenerateAlertBox alertBox = generalFunc.notifyRestartApp("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                            alertBox.setCancelable(false);
                            alertBox.setBtnClickList(btn_id -> {

                                if (btn_id == 1) {
                                    MyApp.getInstance().logOutFromDevice(true);
                                }
                            });
                            return;
                        }
                    }

                    showError(false);
                }
            } else {
                showError(false);
            }
        });

    }


    public void GetConfigData() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "generalConfigData");
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

        ApiHandler.execute(mContext, parameters, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.toString().equals("")) {
                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {
                    new SetGeneralData(generalFunc, responseObj);
                    MyApp.getInstance().updateLangForAllServiceType(generalFunc, responseString);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        try {
                            new ActUtils(mContext).startAct(AppLoginActivity.class);
                            ActivityCompat.finishAffinity((Activity) mContext);
                        } catch (Exception e) {
                            Logger.d("GetConfigData", "::ex" + e.getMessage());
                        }
                        Utils.runGC();
                    }, 300);
                }
            }
        });
    }

    public void GetConfigDataForLocalStorageWithRestart() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "generalConfigData");
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

        ApiHandler.execute(mContext, parameters, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.toString().equals("")) {
                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {
                    MyApp.getInstance().writeToFile(responseString, mContext);
                    generalFunc.restartApp();
                }
            }
        });
    }

    public void GetConfigDataForLocalStorage() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "generalConfigData");
        parameters.put("UserType", Utils.app_type);
        parameters.put("AppVersion", BuildConfig.VERSION_NAME);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("vCurrency", generalFunc.retrieveValue(Utils.DEFAULT_CURRENCY_VALUE));

        ApiHandler.execute(mContext, parameters, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.toString().equals("")) {
                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {
                    MyApp.getInstance().writeToFile(responseString, mContext);
                }
            } else {
                showError(true);
            }
        });
    }

    private void showError(boolean isBtnCancelShow) {
        MyApp.getInstance().getGeneralFun(MyApp.getInstance().getCurrentAct()).showGeneralMessage("",
                generalFunc.retrieveLangLBl("", "LBL_PLEASE_TRY_AGAIN_TXT"),
                isBtnCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "",
                generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> {
                    if (buttonId == 0) {
                        MyApp.getInstance().getCurrentAct().finish();
                    } else {
                        generalFunc.restartApp();
                    }
                });
    }
}