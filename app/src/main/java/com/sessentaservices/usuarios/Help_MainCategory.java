package com.sessentaservices.usuarios;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.activity.ParentActivity;
import com.adapter.files.HelpCategoryRecycleAdapter;
import com.general.files.ActUtils;
import com.service.handler.ApiHandler;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 08-03-18.
 */

public class Help_MainCategory extends ParentActivity implements HelpCategoryRecycleAdapter.OnItemClickList {

    MTextView titleTxt;
    ImageView backImgView;
    ProgressBar loading;
    MTextView noHelpTxt;

    RecyclerView helpCategoryRecyclerView;
    HelpCategoryRecycleAdapter adapter;
    ErrorView errorView;

    ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_maincategory);


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);

        loading = (ProgressBar) findViewById(R.id.loading);
        noHelpTxt = (MTextView) findViewById(R.id.noHelpTxt);
        helpCategoryRecyclerView = (RecyclerView) findViewById(R.id.helpCategoryRecyclerView);
        errorView = (ErrorView) findViewById(R.id.errorView);

        list = new ArrayList<>();
        adapter = new HelpCategoryRecycleAdapter(getActContext(), list, generalFunc);
        helpCategoryRecyclerView.setAdapter(adapter);

        getHelpCategory();
        setLabels();
        addToClickHandler(backImgView);

        adapter.setOnItemClickList(this);
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Help?", "LBL_HEADER_HELP_TXT"));
    }

    @Override
    public void onItemClick(int position, String question, String answer) {


        HashMap<String, String> indexPos = list.get(position);
        Bundle bn = new Bundle();
        bn.putString("iHelpDetailId", indexPos.get("iHelpDetailId"));
        bn.putString("vTitle", indexPos.get("vSubTitle"));
        bn.putString("tAnswer", indexPos.get("tAnswer"));
        bn.putString("eShowFrom", indexPos.get("eShowFrom"));
        bn.putString("iUniqueId", list.get(position).get("iUniqueId"));
        if (getIntent().hasExtra("iOrderId")) {
            bn.putString("iOrderId", getIntent().getStringExtra("iOrderId"));
        }
        else {
            bn.putString("iTripId", getIntent().getStringExtra("iTripId"));
        }
        new ActUtils(getActContext()).startActWithData(Help_DetailsActivity.class, bn);
    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void getHelpCategory() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        if (list.size() > 0) {
            list.clear();
        }

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getHelpDetailCategoty");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("appType", Utils.app_type);

        if (getIntent().hasExtra("iOrderId")) {
            parameters.put("iOrderId", getIntent().getStringExtra("iOrderId"));
            parameters.put("eSystem", Utils.eSystem_Type);
        }
        else if(getIntent().hasExtra("iBiddingPostId"))
        {
            parameters.put("iBiddingPostId", getIntent().getStringExtra("iBiddingPostId"));

        }
        else {
            parameters.put("iTripId", getIntent().getStringExtra("iTripId"));
        }


        noHelpTxt.setVisibility(View.GONE);

        ApiHandler.execute(getActContext(), parameters, responseString -> {

            noHelpTxt.setVisibility(View.GONE);

            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {

                    JSONArray obj_arr = generalFunc.getJsonArray(Utils.message_str, responseString);

                    for (int i = 0; i < obj_arr.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(obj_arr, i);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("iHelpDetailCategoryId", generalFunc.getJsonValueStr("iHelpDetailCategoryId", obj_temp));
                        map.put("vTitle", generalFunc.getJsonValueStr("vTitle", obj_temp));
                        map.put("iUniqueId", generalFunc.getJsonValueStr("iUniqueId", obj_temp));

                        list.add(map);

                        JSONArray obj_ques = generalFunc.getJsonArray("subData", obj_temp.toString());
                        for (int i1 = 0; i1 < obj_ques.length(); i1++) {
                            JSONObject obj_temp1 = generalFunc.getJsonObject(obj_ques, i1);
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("iHelpDetailId", generalFunc.getJsonValueStr("iHelpDetailId", obj_temp1));
                            map1.put("vSubTitle", generalFunc.getJsonValueStr("vTitle", obj_temp1));
                            map1.put("tAnswer", generalFunc.getJsonValueStr("tAnswer", obj_temp1));
                            map1.put("eShowFrom", generalFunc.getJsonValueStr("eShowFrom", obj_temp1));
                            map1.put("iUniqueId", generalFunc.getJsonValueStr("iUniqueId", obj_temp));

                            list.add(map1);
                        }


                    }

                    adapter.notifyDataSetChanged();

                } else {
                    noHelpTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    noHelpTxt.setVisibility(View.VISIBLE);
                }
            } else {
                generateErrorView();
            }
        });

    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getHelpCategory());
    }

    public Context getActContext() {
        return Help_MainCategory.this;
    }


    public void onClick(View view) {
        Utils.hideKeyboard(getActContext());
        switch (view.getId()) {
            case R.id.backImgView:
                Help_MainCategory.super.onBackPressed();
                break;

        }
    }


}
