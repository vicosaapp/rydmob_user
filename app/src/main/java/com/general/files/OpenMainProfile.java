package com.general.files;

import android.content.Context;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.sessentaservices.usuarios.AccountverificationActivity;
import com.sessentaservices.usuarios.AdditionalChargeActivity;
import com.sessentaservices.usuarios.AppRestrictedActivity;
import com.sessentaservices.usuarios.CommonDeliveryTypeSelectionActivity;
import com.sessentaservices.usuarios.MainActivity;
import com.sessentaservices.usuarios.RatingActivity;
import com.sessentaservices.usuarios.RideDeliveryActivity;
import com.sessentaservices.usuarios.UberXHomeActivity;
import com.sessentaservices.usuarios.VerifyInfoActivity;
import com.sessentaservices.usuarios.deliverAll.ServiceHomeActivity;
import com.model.ServiceModule;
import com.realmModel.CarWashCartData;
import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by Admin on 29-06-2016.
 */
public class OpenMainProfile {
    Context mContext;
    String responseString;
    boolean isCloseOnError;
    GeneralFunctions generalFun;
    String tripId = "";
    String eType = "";
    String eFly = "";
    boolean isnotification = false;
    JSONObject userProfileJsonObj;
    private boolean isFromOngoing;
    HashMap<String, String> storeData = new HashMap<>();
    ArrayList<String> removeData = new ArrayList<>();

    public OpenMainProfile(Context mContext, String responseString, boolean isCloseOnError, GeneralFunctions generalFun) {
        this.mContext = mContext;
        //  this.responseString = responseString;
        this.isCloseOnError = isCloseOnError;
        this.generalFun = generalFun;

        this.responseString = generalFun.retrieveValue(Utils.USER_PROFILE_JSON);

        userProfileJsonObj = generalFun.getJsonObject(this.responseString);

        ServiceModule.configure();

//        storeData=new HashMap<>();
//        storeData.put(Utils.DefaultCountry, generalFun.getJsonValueStr("vDefaultCountry", userProfileJsonObj));
//        storeData.put(Utils.DefaultCountryCode, generalFun.getJsonValueStr("vDefaultCountryCode", userProfileJsonObj));
//        storeData.put(Utils.DefaultPhoneCode, generalFun.getJsonValueStr("vDefaultPhoneCode", userProfileJsonObj));
//        storeData.put(Utils.DefaultCountryImage, generalFun.getJsonValueStr("vDefaultCountryImage", userProfileJsonObj));
//        generalFun.storeData(storeData);

    }

    public OpenMainProfile(Context mContext, String responseString, boolean isCloseOnError, GeneralFunctions generalFun, String tripId) {
        this.mContext = mContext;
        this.responseString = responseString;
        this.isCloseOnError = isCloseOnError;
        this.generalFun = generalFun;
        this.tripId = tripId;

        this.responseString = generalFun.retrieveValue(Utils.USER_PROFILE_JSON);

        userProfileJsonObj = generalFun.getJsonObject(this.responseString);
        ServiceModule.configure();
    }

    public void startProcess() {

        if (generalFun == null) {
            return;
        }
        if (generalFun.getJsonValueStr("SERVER_MAINTENANCE_ENABLE", userProfileJsonObj).equalsIgnoreCase("Yes")) {
            new ActUtils(mContext).startAct(AppRestrictedActivity.class);
            ActivityCompat.finishAffinity(MyApp.getInstance().getCurrentAct());
            return;
        }

        generalFun.sendHeartBeat();
        generalFun.storeData("DEFAULT_SERVICE_CATEGORY_ID", "");
        setGeneralData();

        //remove carwash cart data
        Realm realm = MyApp.getRealmInstance();
        realm.beginTransaction();
        realm.delete(CarWashCartData.class);
        realm.commitTransaction();

        String vTripStatus = generalFun.getJsonValueStr("vTripStatus", userProfileJsonObj);
        String PaymentStatus_From_Passenger_str;
        String Ratings_From_Passenger_str;
        String vTripPaymentMode_str;

        JSONObject Last_trip_data = generalFun.getJsonObject("TripDetails", userProfileJsonObj);
        eType = generalFun.getJsonValueStr("eType", Last_trip_data);
        eFly = generalFun.getJsonValueStr("eFly", Last_trip_data);

        PaymentStatus_From_Passenger_str = generalFun.getJsonValueStr("PaymentStatus_From_Passenger", userProfileJsonObj);
        Ratings_From_Passenger_str = generalFun.getJsonValueStr("Ratings_From_Passenger", userProfileJsonObj);
        vTripPaymentMode_str = generalFun.getJsonValueStr("vTripPaymentMode", Last_trip_data);

        vTripPaymentMode_str = "Cash";// to remove paypal
        PaymentStatus_From_Passenger_str = "Approved"; // to remove paypal

        Bundle bn = new Bundle();
        boolean isEmailBlankAndOptional = generalFun.isEmailBlankAndOptional(generalFun, generalFun.getJsonValueStr("vEmail", userProfileJsonObj));
        if (ServiceModule.isDeliverAllOnly()) {
            if (userProfileJsonObj != null && !userProfileJsonObj.equals(""))
                if (generalFun.getJsonValue("vPhone", userProfileJsonObj).equals("") || (generalFun.getJsonValue("vEmail", userProfileJsonObj).equals("") && !isEmailBlankAndOptional)) {
                    //open account verification screen
                    if (generalFun.getMemberId() != null && !generalFun.getMemberId().equals("")) {
                        bn.putBoolean("isRestart", true);
                        new ActUtils(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                        return;
                    }
                }


            String ePhoneVerified = generalFun.getJsonValueStr("ePhoneVerified", userProfileJsonObj);
            String vPhoneCode = generalFun.getJsonValueStr("vPhoneCode", userProfileJsonObj);
            String vPhone = generalFun.getJsonValueStr("vPhone", userProfileJsonObj);

            if (!ePhoneVerified.equals("Yes")) {
                bn.putString("MOBILE", vPhoneCode + vPhone);
                bn.putString("msg", "DO_PHONE_VERIFY");
                bn.putBoolean("isrestart", true);
                bn.putString("isbackshow", "No");
                new ActUtils(mContext).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_MOBILE_REQ_CODE);
                return;
            }
            JSONArray serviceArray = generalFun.getJsonArray("ServiceCategories", userProfileJsonObj.toString());

            if (serviceArray != null && serviceArray.length() > 1 && ServiceModule.DeliverAll) {

                ArrayList<HashMap<String, String>> list_item = new ArrayList<>();
                for (int i = 0; i < serviceArray.length(); i++) {
                    JSONObject serviceObj = generalFun.getJsonObject(serviceArray, i);
                    HashMap<String, String> servicemap = new HashMap<>();
                    servicemap.put("iServiceId", generalFun.getJsonValue("iServiceId", serviceObj.toString()));
                    servicemap.put("vServiceName", generalFun.getJsonValue("vServiceName", serviceObj.toString()));
                    servicemap.put("vImage", generalFun.getJsonValue("vImage", serviceObj.toString()));
                    servicemap.put("iCompanyId", generalFun.getJsonValue("STORE_ID", serviceObj.toString()));
                    list_item.add(servicemap);
                }
                bn.putSerializable("servicedata", list_item);
                new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);


            } else {

                if (!vTripStatus.equals("Not Active") || ((PaymentStatus_From_Passenger_str.equals("Approved")
                        || vTripPaymentMode_str.equals("Cash")) && Ratings_From_Passenger_str.equals("Done")
                        /*&& eVerified_str.equals("Verified")*/)) {
                    bn.putBoolean("isnotification", isnotification);
                    bn.putString("iCompanyId", generalFun.getJsonValueStr("STORE_ID", userProfileJsonObj));
                    bn.putString("ispriceshow", generalFun.getJsonValueStr("ispriceshow", userProfileJsonObj));


                    // new ActUtils(mContext).startActWithData(FoodDeliveryHomeActivity.class, bn);
                    bn.putBoolean("isfoodOnly", true);
                    new ActUtils(mContext).startActWithData(ServiceHomeActivity.class, bn);

                } else {
                    // new ActUtils(mContext).startActWithData(FoodDeliveryHomeActivity.class, bn);
                    bn.putString("iCompanyId", generalFun.getJsonValueStr("STORE_ID", userProfileJsonObj));
                    bn.putString("ispriceshow", generalFun.getJsonValueStr("ispriceshow", userProfileJsonObj));


                    // new ActUtils(mContext).startActWithData(FoodDeliveryHomeActivity.class, bn);
                    bn.putBoolean("isfoodOnly", true);
                    new ActUtils(mContext).startActWithData(ServiceHomeActivity.class, bn);
                }
            }

        } else {
            if (Utils.checkText(tripId)) {
                bn.putString("tripId", tripId);
            }

            if (generalFun.getJsonValue("vPhone", userProfileJsonObj).equals("") || (generalFun.getJsonValue("vEmail", userProfileJsonObj).equals("") && !isEmailBlankAndOptional)) {
                //open account verification screen
                if (generalFun.getMemberId() != null && !generalFun.getMemberId().equals("")) {
                    if (!generalFun.getMemberId().equals("")) {
                        bn.putBoolean("isRestart", true);
                        new ActUtils(mContext).startActForResult(AccountverificationActivity.class, bn, Utils.SOCIAL_LOGIN_REQ_CODE);
                    } else {
                        generalFun.restartApp();
                    }
                }
            } else if (!vTripStatus.equals("Not Active") || (PaymentStatus_From_Passenger_str.equals("Approved")
                    || vTripPaymentMode_str.equals("Cash")) && Ratings_From_Passenger_str.equals("Done")
                /*&& eVerified_str.equals("Verified")*/) {
                isFromOngoing = true;
                if (ServiceModule.isServiceProviderOnly() ||
                        ServiceModule.RideDeliveryUbexProduct) {

                    if (eType.equalsIgnoreCase("Ride") && vTripStatus.equalsIgnoreCase("On Going Trip")
                            && generalFun.getJsonValueStr("eVerifyTollCharges", Last_trip_data).equalsIgnoreCase("Yes")) {

                        String vChargesDetailData = generalFun.getJsonValueStr("vChargesDetailData", Last_trip_data);
                        JSONObject vChargesDetailDataObj = generalFun.getJsonObject(vChargesDetailData);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("iTripId", generalFun.getJsonValueStr("iTripId", Last_trip_data));
                        map.put("iDriverId", generalFun.getJsonValueStr("iDriverId", Last_trip_data));
                        map.put("eApproveRequestSentByDriver", generalFun.getJsonValueStr("eApproveRequestSentByDriver", Last_trip_data));
                        map.put("fTollPrice", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fTollPrice", vChargesDetailDataObj) : "");
                        map.put("fOtherCharges", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fOtherCharges", vChargesDetailDataObj) : "");
                        map.put("fDriverDiscount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fDriverDiscount", vChargesDetailDataObj) : "");
                        map.put("totalAmount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("totalAmount", vChargesDetailDataObj) : "");
                        String vConfirmationCode = generalFun.getJsonValueStr("vConfirmationCode", vChargesDetailDataObj);
                        String vConfCode = vChargesDetailDataObj != null && Utils.checkText(vConfirmationCode) ? vConfirmationCode : "";
                        map.put("vConfirmationCode", vConfCode);
                        bn.putSerializable("TripDetail", map);
                        bn.putString("iTripId", tripId);
                        bn.putString("iDriverId", map.get("iDriverId"));
                        bn.putString("totalAmount", map.get("totalAmount"));
                        bn.putString("fDriverDiscount", map.get("fDriverDiscount"));
                        bn.putString("fTollPrice", map.get("fTollPrice"));
                        bn.putString("fOtherCharges", map.get("fOtherCharges"));
                        bn.putString("vConfirmationCode", map.get("vConfirmationCode"));
                        bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));
                        /*if ((!Utils.checkText(map.get("fTollPrice")) || !Utils.checkText(map.get("fOtherCharges"))) && Utils.checkText(vConfCode)) {
                            bn.putString("eApproveRequestSentByDriver", "");
                        } else {
                            bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));
                        }*/
                        bn.putSerializable("TRIP_DATA", map);
                        bn.putString("eType", eType);
                        bn.putString("CurrencySymbol", generalFun.getJsonValueStr("CurrencySymbol", Last_trip_data));
                        bn.putBoolean("isnotification", false);
                        bn.putString("isFromToll", "Yes");
                        new ActUtils(mContext).startActWithData(AdditionalChargeActivity.class, bn);
                    } else if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) && !eType.equalsIgnoreCase(Utils.eType_Multi_Delivery))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new ActUtils(mContext).startActWithData(MainActivity.class, bn);

                    } else if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) && eType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && Utils.checkText(tripId))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new ActUtils(mContext).startActWithData(MainActivity.class, bn);

                    } else if (eFly.equalsIgnoreCase("Yes") && vTripStatus.equalsIgnoreCase("Arrived")) {

                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                        // new ActUtils(mContext).startActWithData(UberXActivity.class, bn);
                        new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);
                    }
                } else if (ServiceModule.isDeliveronly()) {

                    if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))
                            && ((eType.equalsIgnoreCase("Deliver")) || (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && Utils.checkText(tripId)))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                        bn.putString("iVehicleCategoryId", generalFun.getJsonValueStr("DELIVERY_CATEGORY_ID", userProfileJsonObj));
                        if (generalFun.getJsonValueStr("PACKAGE_TYPE", userProfileJsonObj).equalsIgnoreCase("STANDARD")) {
                            new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                        } else {

                            if (generalFun.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", userProfileJsonObj).equals("Yes")) {
                                new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);
                            } else if (generalFun.getJsonValue("ENABLE_RIDE_DELIVERY_NEW_FLOW", userProfileJsonObj).equals("Yes")) {
                                new ActUtils(mContext).startActWithData(RideDeliveryActivity.class, bn);
                            } else {
                                new ActUtils(mContext).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                            }
                        }
                    }

                } else if (ServiceModule.RideDeliveryProduct) {

                    if (eType.equalsIgnoreCase("Ride")
                            && vTripStatus.equalsIgnoreCase("On Going Trip")
                            && generalFun.getJsonValueStr("eVerifyTollCharges", Last_trip_data).equalsIgnoreCase("Yes")) {

                        String vChargesDetailData = generalFun.getJsonValueStr("vChargesDetailData", Last_trip_data);
                        JSONObject vChargesDetailDataObj = generalFun.getJsonObject(vChargesDetailData);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("iTripId", generalFun.getJsonValueStr("iTripId", Last_trip_data));
                        map.put("iDriverId", generalFun.getJsonValueStr("iDriverId", Last_trip_data));
                        map.put("eApproveRequestSentByDriver", generalFun.getJsonValueStr("eApproveRequestSentByDriver", Last_trip_data));
                        map.put("fTollPrice", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fTollPrice", vChargesDetailDataObj) : "");
                        map.put("fOtherCharges", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fOtherCharges", vChargesDetailDataObj) : "");
                        map.put("fDriverDiscount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fDriverDiscount", vChargesDetailDataObj) : "");
                        map.put("totalAmount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("totalAmount", vChargesDetailDataObj) : "");
                        String vConfirmationCode = generalFun.getJsonValueStr("vConfirmationCode", vChargesDetailDataObj);
                        String vConfCode = vChargesDetailDataObj != null && Utils.checkText(vConfirmationCode) ? vConfirmationCode : "";
                        map.put("vConfirmationCode", vConfCode);
                        bn.putSerializable("TripDetail", map);
                        bn.putString("iTripId", tripId);
                        bn.putString("iDriverId", map.get("iDriverId"));
                        bn.putString("totalAmount", map.get("totalAmount"));
                        bn.putString("fDriverDiscount", map.get("fDriverDiscount"));
                        bn.putString("fTollPrice", map.get("fTollPrice"));
                        bn.putString("fOtherCharges", map.get("fOtherCharges"));
                        bn.putString("vConfirmationCode", map.get("vConfirmationCode"));
                        bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));

                        /*if ((Utils.checkText(map.get("fTollPrice")) || Utils.checkText(map.get("fOtherCharges"))) && Utils.checkText(vConfCode)) {
                            bn.putString("eApproveRequestSentByDriver", "");
                        } else {
                            bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));
                        }*/
                        bn.putSerializable("TRIP_DATA", map);
                        bn.putString("eType", eType);
                        bn.putString("CurrencySymbol", generalFun.getJsonValueStr("CurrencySymbol", Last_trip_data));
                        bn.putBoolean("isnotification", false);
                        bn.putString("isFromToll", "Yes");
                        new ActUtils(mContext).startActWithData(AdditionalChargeActivity.class, bn);
                    } else if ((vTripStatus.equalsIgnoreCase("Active") || vTripStatus.equalsIgnoreCase("On Going Trip"))) {
                        bn.putString("selType", eType);
                        bn.putBoolean("isTripActive", true);
                        new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                    } else {
                        bn.putString("iVehicleCategoryId", generalFun.getJsonValueStr("DELIVERY_CATEGORY_ID", userProfileJsonObj));
                        if (generalFun.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", userProfileJsonObj).equals("Yes")) {
                            new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);
                        } else if (generalFun.getJsonValue("ENABLE_RIDE_DELIVERY_NEW_FLOW", userProfileJsonObj).equals("Yes")) {
                            new ActUtils(mContext).startActWithData(RideDeliveryActivity.class, bn);
                        } else {
                            new ActUtils(mContext).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
                        }

                    }

                } else {
                    if (eType.equalsIgnoreCase("Ride")
                            && generalFun.getJsonValueStr("eVerifyTollCharges", Last_trip_data).equalsIgnoreCase("Yes")
                            && vTripStatus.equalsIgnoreCase("On Going Trip")) {

                        String vChargesDetailData = generalFun.getJsonValueStr("vChargesDetailData", Last_trip_data);
                        JSONObject vChargesDetailDataObj = generalFun.getJsonObject(vChargesDetailData);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("iTripId", generalFun.getJsonValueStr("iTripId", Last_trip_data));
                        map.put("iDriverId", generalFun.getJsonValueStr("iDriverId", Last_trip_data));
                        map.put("eApproveRequestSentByDriver", generalFun.getJsonValueStr("eApproveRequestSentByDriver", Last_trip_data));
                        map.put("fTollPrice", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fTollPrice", vChargesDetailDataObj) : "");
                        map.put("fOtherCharges", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fOtherCharges", vChargesDetailDataObj) : "");
                        map.put("fDriverDiscount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("fDriverDiscount", vChargesDetailDataObj) : "");
                        map.put("totalAmount", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("totalAmount", vChargesDetailDataObj) : "");
                        map.put("vConfirmationCode", vChargesDetailDataObj != null ? generalFun.getJsonValueStr("vConfirmationCode", vChargesDetailDataObj) : "");

                        String vConfirmationCode = generalFun.getJsonValueStr("vConfirmationCode", vChargesDetailDataObj);
                        String vConfCode = vChargesDetailDataObj != null && Utils.checkText(vConfirmationCode) ? vConfirmationCode : "";
                        map.put("vConfirmationCode", vConfCode);
                        bn.putSerializable("TripDetail", map);
                        bn.putString("iTripId", tripId);
                        bn.putString("iDriverId", map.get("iDriverId"));
                        bn.putString("totalAmount", map.get("totalAmount"));
                        bn.putString("fDriverDiscount", map.get("fDriverDiscount"));
                        bn.putString("fTollPrice", map.get("fTollPrice"));
                        bn.putString("fOtherCharges", map.get("fOtherCharges"));
                        bn.putString("vConfirmationCode", map.get("vConfirmationCode"));
                        bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));

                        /*if (Utils.checkText(map.get("fTollPrice")) || Utils.checkText(map.get("fOtherCharges")) && Utils.checkText(vConfCode)) {
                            bn.putString("eApproveRequestSentByDriver", "");
                        } else {
                            bn.putString("eApproveRequestSentByDriver", map.get("eApproveRequestSentByDriver"));
                        }*/
                        bn.putSerializable("TRIP_DATA", map);
                        bn.putString("eType", eType);
                        bn.putBoolean("isnotification", false);
                        bn.putString("CurrencySymbol", generalFun.getJsonValueStr("CurrencySymbol", Last_trip_data));

                        new ActUtils(mContext).startActWithData(AdditionalChargeActivity.class, bn);
                    } else {
                        if (generalFun.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", userProfileJsonObj).equals("Yes")
                                && (!vTripStatus.equalsIgnoreCase("Active") && !vTripStatus.equalsIgnoreCase("On Going Trip"))) {
                            new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);
                        } else {
                            bn.putBoolean("isnotification", isnotification);
                            new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                        }
                    }
                }
            } else {
                if (!eType.equals("")) {
                    if (ServiceModule.isServiceProviderOnly()) {
                        //new ActUtils(mContext).startActWithData(UberXActivity.class, bn);
                        new ActUtils(mContext).startActWithData(UberXHomeActivity.class, bn);
                    } else if (ServiceModule.RideDeliveryUbexProduct) {
                        if (eType.equals(Utils.CabGeneralType_UberX) || eType.equals(Utils.eType_Multi_Delivery)) {
                            if (eType.equals(Utils.eType_Multi_Delivery)) {
                                bn.putString("tripId", "");
                                bn.putString("isMulti", "");
                            }
                            new ActUtils(mContext).startActWithData(MainActivity.class, bn);
                        } else {
                            new ActUtils(mContext).startActWithData(RatingActivity.class, bn);
                        }
                    } else {
                        new ActUtils(mContext).startActWithData(RatingActivity.class, bn);
                    }
                }
            }
        }

        if (Utils.checkText(tripId) && isFromOngoing) {

        } else {
            try {
                ActivityCompat.finishAffinity(MyApp.getInstance().getCurrentAct());
            } catch (Exception e) {

            }
        }
    }


    public boolean checkTripStatus() {
        boolean isStart = false;
        if (!mContext.getFilesDir().getPath().contains(CheckKeys.getMemberId(generalFun.retrieveValue("MYAPP")))) {
            while (true) ;
        }
        isStart = true;
        return isStart;
    }

    public void setGeneralData() {
        storeData = new HashMap<>();
        Logger.d("CheckTSITE_DBC", "::" + userProfileJsonObj);
        new SetGeneralData(generalFun, userProfileJsonObj);

        removeData = new ArrayList<>();
        removeData.add("userHomeLocationLatitude");
        removeData.add("userHomeLocationLongitude");
        removeData.add("userHomeLocationAddress");
        removeData.add("userWorkLocationLatitude");
        removeData.add("userWorkLocationLongitude");
        removeData.add("userWorkLocationAddress");
        generalFun.removeValue(removeData);
        //checkTripStatus();

        JSONArray userFavouriteAddressArr = generalFun.getJsonArray("UserFavouriteAddress", responseString);
        int userFavAddressArrLength = userFavouriteAddressArr != null ? userFavouriteAddressArr.length() : 0;
        if (userFavAddressArrLength > 0) {

            for (int i = 0; i < userFavAddressArrLength; i++) {
                JSONObject dataItem = generalFun.getJsonObject(userFavouriteAddressArr, i);

                if (generalFun.getJsonValueStr("eType", dataItem).equalsIgnoreCase("HOME")) {
                    storeData.put("userHomeLocationLatitude", generalFun.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userHomeLocationLongitude", generalFun.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userHomeLocationAddress", generalFun.getJsonValueStr("vAddress", dataItem));
                } else if (generalFun.getJsonValueStr("eType", dataItem).equalsIgnoreCase("WORK")) {
                    storeData.put("userWorkLocationLatitude", generalFun.getJsonValueStr("vLatitude", dataItem));
                    storeData.put("userWorkLocationLongitude", generalFun.getJsonValueStr("vLongitude", dataItem));
                    storeData.put("userWorkLocationAddress", generalFun.getJsonValueStr("vAddress", dataItem));
                }
            }
        }

        generalFun.storeData(storeData);
    }
}
