package com.sessentaservices.usuarios;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.activity.ParentActivity;
import com.adapter.files.DonationBannerAdapter;
import com.adapter.files.DonationBannerAdapter.OnBannerItemClickList;
import com.general.files.GeneralFunctions;
import com.general.files.ActUtils;
import com.service.handler.ApiHandler;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DonationActivity extends ParentActivity implements OnBannerItemClickList {

    MTextView titleTxt;
    ImageView backImgView;
    RecyclerView donateListRecyclerView;
    DonationBannerAdapter donationBannerAdapter;
    ArrayList<HashMap<String, String>> donationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        donateListRecyclerView = (RecyclerView) findViewById(R.id.donateListRecyclerView);
        addToClickHandler(backImgView);
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DONATE"));

        if (generalFunc.isRTLmode()) {
            backImgView.setRotation(180);
        }

        donationBannerAdapter = new DonationBannerAdapter(getActContext(), donationList);
        donationBannerAdapter.setOnItemClickList(this);
        donateListRecyclerView.setAdapter(donationBannerAdapter);
        getDonationDetails();
    }

    public void getDonationDetails() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getDonation");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);


        ApiHandler.execute(getActContext(), parameters, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    JSONArray arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(arr, i);


                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("tTitle", generalFunc.getJsonValue("tTitle", obj_temp.toString()));
                        hashMap.put("tDescription", generalFunc.getJsonValue("tDescription", obj_temp.toString()));
                        hashMap.put("tDescription", generalFunc.getJsonValue("tDescription", obj_temp.toString()));
                        hashMap.put("vImage", generalFunc.getJsonValue("vImage", obj_temp.toString()));
                        hashMap.put("tLink", generalFunc.getJsonValue("tLink", obj_temp.toString()));
                        donationList.add(hashMap);
                    }
                    donationBannerAdapter.notifyDataSetChanged();
                } else {

                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), buttonId -> onBackPressed());
                }
            }
        });
    }

    public Context getActContext() {
        return DonationActivity.this;
    }

    @Override
    public void onBannerItemClick(int position) {
        Logger.d("URL", "::" + donationList.get(position).get("tLink"));
        (new ActUtils(getActContext())).openURL(donationList.get(position).get("tLink"), BuildConfig.APPLICATION_ID, this.getLocalClassName());

    }


    public void onClick(View view) {
        Utils.hideKeyboard(getActContext());
        switch (view.getId()) {
            case R.id.backImgView:
                onBackPressed();
                break;
        }
    }

}
