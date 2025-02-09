package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.sessentaservices.usuarios.CommonDeliveryTypeSelectionActivity;
import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.RideDeliveryActivity;
import com.sessentaservices.usuarios.deliverAll.FoodDeliveryHomeActivity;
import com.sessentaservices.usuarios.deliverAll.ServiceHomeActivity;
import com.model.ServiceModule;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;


public class AddBottomBar {


    public JSONObject userProfileJson;
    Context mContext;
    View view;


    public LinearLayout historyArea, walletArea, profileArea, homeArea;
    MTextView historyTxt, walletTxt, profileTxt, homeTxt;
    ImageView home_img, bookingImg, walletImg, profileImg;

    GeneralFunctions generalFunc;


    public AddBottomBar(Context mContext, JSONObject userProfileJson) {
        try {

            this.mContext = mContext;
            this.userProfileJson = userProfileJson;
            generalFunc = new GeneralFunctions(mContext);
            view = ((Activity) mContext).findViewById(android.R.id.content);


            profileTxt = view.findViewById(R.id.profileTxt);
            homeTxt = view.findViewById(R.id.homeTxt);
            historyTxt = view.findViewById(R.id.historyTxt);
            walletTxt = view.findViewById(R.id.walletTxt);
            home_img = view.findViewById(R.id.home_img);
            profileImg = view.findViewById(R.id.profileImg);
            bookingImg = view.findViewById(R.id.bookingImg);
            walletImg = view.findViewById(R.id.walletImg);


            profileArea = view.findViewById(R.id.profileArea);
            homeArea = view.findViewById(R.id.homeArea);
            historyArea = view.findViewById(R.id.historyArea);
            walletArea = view.findViewById(R.id.walletArea);

            profileArea.setOnClickListener(new setOnClickList());
            homeArea.setOnClickListener(new setOnClickList());
            historyArea.setOnClickListener(new setOnClickList());
            walletArea.setOnClickListener(new setOnClickList());
            manageBottomMenu(homeTxt);
            setLabel();
        } catch (Exception e) {
            Logger.d("AddBottomException", "::" + e.toString());

        }
    }

    public void setLabel() {
        profileTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_PROFILE"));

        homeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HOME"));
        walletTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_WALLET"));
        if (ServiceModule.isDeliverAllOnly()) {
            historyTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MY_ORDERS"));
        } else {
            historyTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_BOOKINGS"));
        }

    }

    public void manageBottomMenu(MTextView selTextView) {
        //manage Select deselect Bottom Menu
        if (selTextView.getId() == homeTxt.getId()) {
            homeTxt.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_1));
            home_img.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), android.graphics.PorterDuff.Mode.SRC_IN);
            home_img.setImageResource(R.drawable.ic_home_fill);
        } else {
            homeTxt.setTextColor(mContext.getResources().getColor(R.color.homedeSelectColor));
            home_img.setColorFilter(ContextCompat.getColor(mContext, R.color.homedeSelectColor), android.graphics.PorterDuff.Mode.SRC_IN);
            home_img.setImageResource(R.drawable.ic_home);
        }

        if (selTextView.getId() == historyTxt.getId()) {
            historyTxt.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_1));
            bookingImg.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), android.graphics.PorterDuff.Mode.SRC_IN);
            bookingImg.setImageResource(R.drawable.ic_booking_fill);
        } else {
            historyTxt.setTextColor(mContext.getResources().getColor(R.color.homedeSelectColor));
            bookingImg.setColorFilter(ContextCompat.getColor(mContext, R.color.homedeSelectColor), android.graphics.PorterDuff.Mode.SRC_IN);
            bookingImg.setImageResource(R.drawable.ic_booking);
        }
        if (selTextView.getId() == walletTxt.getId()) {
            walletTxt.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_1));
            walletImg.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), android.graphics.PorterDuff.Mode.SRC_IN);
            walletImg.setImageResource(R.drawable.ic_wallet_fill);
        } else {
            walletTxt.setTextColor(mContext.getResources().getColor(R.color.homedeSelectColor));
            walletImg.setColorFilter(ContextCompat.getColor(mContext, R.color.homedeSelectColor), android.graphics.PorterDuff.Mode.SRC_IN);
            walletImg.setImageResource(R.drawable.ic_wallet);
        }
        if (selTextView.getId() == profileTxt.getId()) {
            profileTxt.setTextColor(mContext.getResources().getColor(R.color.appThemeColor_1));
            profileImg.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), android.graphics.PorterDuff.Mode.SRC_IN);
            profileImg.setImageResource(R.drawable.ic_profile_fill);
        } else {
            profileTxt.setTextColor(mContext.getResources().getColor(R.color.homedeSelectColor));
            profileImg.setColorFilter(ContextCompat.getColor(mContext, R.color.homedeSelectColor), android.graphics.PorterDuff.Mode.SRC_IN);
            profileImg.setImageResource(R.drawable.ic_profile);
        }
    }


    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view.getId() == profileArea.getId()) {
                manageBottomMenu(profileTxt);
                if (mContext instanceof FoodDeliveryHomeActivity) {
                    FoodDeliveryHomeActivity foodDeliveryHomeActivity = (FoodDeliveryHomeActivity) mContext;
                    foodDeliveryHomeActivity.openProfileFragment();
                } else if (mContext instanceof CommonDeliveryTypeSelectionActivity) {
                    CommonDeliveryTypeSelectionActivity commObj = (CommonDeliveryTypeSelectionActivity) mContext;
                    commObj.openProfileFragment();
                } else if (mContext instanceof ServiceHomeActivity) {
                    ServiceHomeActivity serviceHomeActivity = (ServiceHomeActivity) mContext;
                    serviceHomeActivity.openProfileFragment();
                } else if (mContext instanceof RideDeliveryActivity) {
                    RideDeliveryActivity rideDeliveryActivity = (RideDeliveryActivity) mContext;
                    rideDeliveryActivity.openProfileFragment();
                }
            } else if (view.getId() == homeArea.getId()) {
                manageBottomMenu(homeTxt);

                if (generalFunc.prefHasKey(Utils.isMultiTrackRunning) && generalFunc.retrieveValue(Utils.isMultiTrackRunning).equalsIgnoreCase("Yes")) {
                    MyApp.getInstance().restartWithGetDataApp();
                } else {
                    if (mContext instanceof FoodDeliveryHomeActivity) {
                        FoodDeliveryHomeActivity foodDeliveryHomeActivity = (FoodDeliveryHomeActivity) mContext;
                        foodDeliveryHomeActivity.manageHome();
                    } else if (mContext instanceof CommonDeliveryTypeSelectionActivity) {
                        CommonDeliveryTypeSelectionActivity commObj = (CommonDeliveryTypeSelectionActivity) mContext;
                        commObj.manageHome();
                    } else if (mContext instanceof ServiceHomeActivity) {
                        ServiceHomeActivity serviceHomeActivity = (ServiceHomeActivity) mContext;
                        serviceHomeActivity.manageHome();
                    } else if (mContext instanceof RideDeliveryActivity) {
                        RideDeliveryActivity rideDeliveryActivity = (RideDeliveryActivity) mContext;
                        rideDeliveryActivity.manageHome();
                    }
                }

            } else if (view.getId() == historyArea.getId()) {
                if (Utils.checkText(generalFunc.getMemberId())) {
                    manageBottomMenu(historyTxt);
                    if (mContext instanceof FoodDeliveryHomeActivity) {
                        FoodDeliveryHomeActivity foodDeliveryHomeActivity = (FoodDeliveryHomeActivity) mContext;
                        foodDeliveryHomeActivity.openHistoryFragment();

                    } else if (mContext instanceof CommonDeliveryTypeSelectionActivity) {
                        CommonDeliveryTypeSelectionActivity commObj = (CommonDeliveryTypeSelectionActivity) mContext;
                        commObj.openHistoryFragment();
                    } else if (mContext instanceof ServiceHomeActivity) {
                        ServiceHomeActivity serviceHomeActivity = (ServiceHomeActivity) mContext;
                        serviceHomeActivity.openHistoryFragment();
                    } else if (mContext instanceof RideDeliveryActivity) {
                        RideDeliveryActivity rideDeliveryActivity = (RideDeliveryActivity) mContext;
                        rideDeliveryActivity.openHistoryFragment();
                    }
                } else {
                    manageBottomMenu(historyTxt);
                    manageWithoutSignIn();
                }


            } else if (view.getId() == walletArea.getId()) {
                if (Utils.checkText(generalFunc.getMemberId())) {
                    manageBottomMenu(walletTxt);
                    if (mContext instanceof FoodDeliveryHomeActivity) {
                        FoodDeliveryHomeActivity foodDeliveryHomeActivity = (FoodDeliveryHomeActivity) mContext;
                        foodDeliveryHomeActivity.openWalletFragment();

                    } else if (mContext instanceof CommonDeliveryTypeSelectionActivity) {
                        CommonDeliveryTypeSelectionActivity commObj = (CommonDeliveryTypeSelectionActivity) mContext;
                        commObj.openWalletFragment();
                    } else if (mContext instanceof ServiceHomeActivity) {
                        ServiceHomeActivity serviceHomeActivity = (ServiceHomeActivity) mContext;
                        serviceHomeActivity.openWalletFragment();
                    } else if (mContext instanceof RideDeliveryActivity) {
                        RideDeliveryActivity rideDeliveryActivity = (RideDeliveryActivity) mContext;
                        rideDeliveryActivity.openWalletFragment();
                    }

                } else {
                    manageBottomMenu(walletTxt);
                    manageWithoutSignIn();
                }
            }


        }
    }

    public void manageWithoutSignIn() {
        if (mContext instanceof FoodDeliveryHomeActivity) {
            FoodDeliveryHomeActivity foodDeliveryHomeActivity = (FoodDeliveryHomeActivity) mContext;
            foodDeliveryHomeActivity.openProfileFragment();

        } else if (mContext instanceof CommonDeliveryTypeSelectionActivity) {
            CommonDeliveryTypeSelectionActivity commObj = (CommonDeliveryTypeSelectionActivity) mContext;
            commObj.openProfileFragment();
        } else if (mContext instanceof ServiceHomeActivity) {
            ServiceHomeActivity serviceHomeActivity = (ServiceHomeActivity) mContext;
            serviceHomeActivity.openProfileFragment();
        }

    }


}
