package com.sessentaservices.usuarios;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activity.ParentActivity;
import com.general.files.OnScrollTouchDelegate;
import com.general.files.ActUtils;
import com.service.handler.ApiHandler;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.MTextView;
import com.view.anim.loader.AVLoadingIndicatorView;
import com.view.pinnedListView.CountryListItem;
import com.adapter.files.PinnedSectionListAdapter;
import com.view.pinnedListView.PinnedSectionListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SelectCountryActivity extends ParentActivity implements PinnedSectionListAdapter.CountryClick, OnScrollTouchDelegate {

    ArrayList<CountryListItem> items_list;
    MTextView titleTxt;
    ImageView backImgView;

    AVLoadingIndicatorView loading;
    ErrorView errorView;
    MTextView noResTxt;
    PinnedSectionListView country_list;
    PinnedSectionListAdapter pinnedSectionListAdapter;
    private CountryListItem[] sections;
    ImageView searchImgView;
    LinearLayout searcharea;
    View toolbarArea;
    MTextView cancelTxt;
    EditText searchTxt;
    ImageView imageCancel;

    JSONArray countryArr;
    MTextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        noResTxt = (MTextView) findViewById(R.id.noResTxt);
        noData = (MTextView) findViewById(R.id.noData);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        errorView = (ErrorView) findViewById(R.id.errorView);
        country_list = (PinnedSectionListView) findViewById(R.id.country_list);
        country_list.onTouchDegateListener(this);
        searchImgView = (ImageView) findViewById(R.id.searchImgView);
        searcharea = (LinearLayout) findViewById(R.id.searcharea);
        toolbarArea = (View) findViewById(R.id.toolbarArea);
        cancelTxt = (MTextView) findViewById(R.id.cancelTxt);
        searchTxt = (EditText) findViewById(R.id.searchTxt);
        imageCancel = (ImageView) findViewById(R.id.imageCancel);
        searchImgView.setVisibility(View.GONE);
        addToClickHandler(searchImgView);
        addToClickHandler(cancelTxt);
        addToClickHandler(imageCancel);


        country_list.setShadowVisible(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            country_list.setFastScrollEnabled(true);
            country_list.setFastScrollAlwaysVisible(true);
        }
        items_list = new ArrayList<>();
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filterCountries(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (searchTxt.length() == 0) {
                    imageCancel.setVisibility(View.INVISIBLE);
                } else {
                    imageCancel.setVisibility(View.VISIBLE);
                }
            }
        });


        setLabels();

        addToClickHandler(backImgView);
        getCountryList();
        imageCancel.setVisibility(View.INVISIBLE);
        searcharea.setVisibility(View.VISIBLE);
        toolbarArea.setVisibility(View.GONE);


    }

    @Override
    public void countryClickList(CountryListItem countryListItem) {

        Utils.hideKeyboard(getActContext());

        Bundle bn = new Bundle();
        bn.putString("vCountry", countryListItem.text);
        bn.putString("vCountryCode", countryListItem.getvCountryCode());
        bn.putString("vPhoneCode", countryListItem.getvPhoneCode());
        bn.putString("vSImage", countryListItem.getvSImage());
        new ActUtils(getActContext()).setOkResult(bn);

        backImgView.performClick();
    }

    public Context getActContext() {
        return SelectCountryActivity.this;
    }

    public void setLabels() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_CONTRY"));
        searchTxt.setHint(generalFunc.retrieveLangLBl("Search Country", "LBL_SEARCH_COUNTRY"));
        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
    }

    public void getCountryList() {
        if (errorView.getVisibility() == View.VISIBLE) {
            errorView.setVisibility(View.GONE);
        }
        if (loading.getVisibility() != View.VISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "countryList");

        noResTxt.setVisibility(View.GONE);

        ApiHandler.execute(getActContext(), parameters, responseString -> {

            noResTxt.setVisibility(View.GONE);

            if (responseString != null && !responseString.equals("")) {

                closeLoader();

                if (generalFunc.checkDataAvail(Utils.action_str, responseString) == true) {


                    JSONArray countryArr = generalFunc.getJsonArray("CountryList", responseString);

                    this.countryArr = countryArr;

                    items_list.clear();

                    sections = new CountryListItem[generalFunc.parseIntegerValue(0, generalFunc.getJsonValue("totalValues", responseString))];
                    pinnedSectionListAdapter = new PinnedSectionListAdapter(getActContext(), items_list, sections);
                    country_list.setAdapter(pinnedSectionListAdapter);

                    pinnedSectionListAdapter.setCountryClickListener(SelectCountryActivity.this);
                    items_list.clear();
                    pinnedSectionListAdapter.notifyDataSetChanged();

                    int sectionPosition = 0, listPosition = 0;
                    int imagewidth = (int) getActContext().getResources().getDimension(R.dimen._30sdp);

                    for (int i = 0; i < countryArr.length(); i++) {
                        JSONObject tempJson = generalFunc.getJsonObject(countryArr, i);

                        String key_str = generalFunc.getJsonValueStr("key", tempJson);
                        String count_str = generalFunc.getJsonValueStr("TotalCount", tempJson);


                        CountryListItem section = new CountryListItem(CountryListItem.SECTION, key_str);
                        section.sectionPosition = sectionPosition;
                        section.listPosition = listPosition++;
                        section.CountSubItems = generalFunc.parseIntegerValue(0, count_str);
                        onSectionAdded(section, sectionPosition);
                        items_list.add(section);

                        JSONArray subListArr = generalFunc.getJsonArray("List", tempJson);

                        for (int j = 0; j < subListArr.length(); j++) {
                            JSONObject subTempJson = generalFunc.getJsonObject(subListArr, j);
                            String vCountryCode = generalFunc.getJsonValueStr("vCountryCode", subTempJson);
                            String vPhoneCode = generalFunc.getJsonValueStr("vPhoneCode", subTempJson);
                            String iCountryId = generalFunc.getJsonValueStr("iCountryId", subTempJson);
                            String vCountry = generalFunc.getJsonValueStr("vCountry", subTempJson);
                            String vRImage = generalFunc.getJsonValueStr("vRImage", subTempJson);
                            String vSImage = generalFunc.getJsonValueStr("vSImage", subTempJson);

                            CountryListItem countryListItem = new CountryListItem(CountryListItem.ITEM, vCountry);
                            countryListItem.sectionPosition = sectionPosition;
                            countryListItem.listPosition = listPosition++;
                            countryListItem.setvCountryCode(vCountryCode);
                            countryListItem.setvPhoneCode(vPhoneCode);
                            countryListItem.setiCountryId(iCountryId);
                            countryListItem.setvRImage(Utils.getResizeImgURL(getActContext(), vRImage, imagewidth, imagewidth));
                            countryListItem.setvSImage(vSImage);
                            items_list.add(countryListItem);
                        }

                        sectionPosition++;
                    }
                    searchImgView.setEnabled(true);
                    if (countryArr == null || countryArr.length() == 0) {
                        searchImgView.setEnabled(false);
                        noData.setText(generalFunc.retrieveLangLBl("", "LBL_NO_COUNTRY_AVAIL"));
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                    }
                    pinnedSectionListAdapter.notifyDataSetChanged();
                } else {
                    noResTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ERROR_TXT"));
                    noResTxt.setVisibility(View.VISIBLE);
                }
            } else {
                generateErrorView();
            }
        });

    }

    private void filterCountries(String searchText) {
        int totalSection = 0;
        ArrayList<CountryListItem> items_list_tmp = new ArrayList<>();


        int sectionPosition = 0, listPosition = 0;

        HashMap<Integer, CountryListItem> sectionMapData = new HashMap<>();
        int imagewidth = (int) getActContext().getResources().getDimension(R.dimen._30sdp);

        if (countryArr == null) {
            return;
        }

        for (int i = 0; i < countryArr.length(); i++) {
            JSONObject tempJson = generalFunc.getJsonObject(countryArr, i);
            String key_str = generalFunc.getJsonValueStr("key", tempJson);
            JSONArray subListArr = generalFunc.getJsonArray("List", tempJson);
            ArrayList<CountryListItem> items_list_sub_tmp = new ArrayList<>();

            for (int j = 0; j < subListArr.length(); j++) {
                JSONObject subTempJson = generalFunc.getJsonObject(subListArr, j);
                String vCountryCode = generalFunc.getJsonValueStr("vCountryCode", subTempJson);
                String vPhoneCode = generalFunc.getJsonValueStr("vPhoneCode", subTempJson);
                String iCountryId = generalFunc.getJsonValueStr("iCountryId", subTempJson);
                String vCountry = generalFunc.getJsonValueStr("vCountry", subTempJson);
                String vRImage = generalFunc.getJsonValueStr("vRImage", subTempJson);
                String vSImage = generalFunc.getJsonValueStr("vSImage", subTempJson);

                String searchTxt = searchText.trim().toLowerCase(Locale.US);
                String countryTxt = vCountry.toLowerCase(Locale.US);

                if (searchText.trim().equals("") || countryTxt.startsWith(searchTxt)) {
                    CountryListItem countryListItem = new CountryListItem(CountryListItem.ITEM, vCountry);
                    countryListItem.sectionPosition = sectionPosition;
                    countryListItem.listPosition = listPosition++;
                    countryListItem.setvCountryCode(vCountryCode);
                    countryListItem.setvPhoneCode(vPhoneCode);
                    countryListItem.setiCountryId(iCountryId);
                    countryListItem.setvRImage(Utils.getResizeImgURL(getActContext(), vRImage, imagewidth, imagewidth));
                    countryListItem.setvSImage(vSImage);
                    items_list_sub_tmp.add(countryListItem);
                }


            }

            if (items_list_sub_tmp.size() > 0) {
                CountryListItem section = new CountryListItem(CountryListItem.SECTION, key_str);
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                section.CountSubItems = items_list_sub_tmp.size();
                sectionMapData.put(sectionPosition, section);

                items_list_tmp.add(section);

                totalSection = totalSection + 1;

                items_list_tmp.addAll(items_list_sub_tmp);


                sectionPosition++;
            }
        }

        sections = new CountryListItem[totalSection];

        for (Integer currentKey : sectionMapData.keySet()) {
            onSectionAdded(sectionMapData.get(currentKey), currentKey);
        }

        items_list.clear();

        items_list.addAll(items_list_tmp);

        pinnedSectionListAdapter = new PinnedSectionListAdapter(getActContext(), items_list, sections);
        country_list.setAdapter(pinnedSectionListAdapter);

        pinnedSectionListAdapter.setCountryClickListener(SelectCountryActivity.this);

        pinnedSectionListAdapter.notifyDataSetChanged();
    }

    public void closeLoader() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.GONE);
        }
    }

    public void generateErrorView() {

        closeLoader();

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (errorView.getVisibility() != View.VISIBLE) {
            errorView.setVisibility(View.VISIBLE);
        }
        errorView.setOnRetryListener(() -> getCountryList());
    }

    protected void onSectionAdded(CountryListItem section, int sectionPosition) {
        sections[sectionPosition] = section;
    }

    @Override
    public void onTouchDelegate() {
        Utils.hideKeyboard(getActContext());
    }


    public void onClick(View view) {
        Utils.hideKeyboard(getActContext());
        switch (view.getId()) {
            case R.id.backImgView:
                SelectCountryActivity.super.onBackPressed();
                break;
            case R.id.cancelTxt:
                toolbarArea.setVisibility(View.VISIBLE);
                searcharea.setVisibility(View.GONE);
                searchTxt.setText("");
                Utils.hideKeyboard(getActContext());
                break;
            case R.id.searchImgView:
                if (countryArr != null) {
                    imageCancel.setVisibility(View.INVISIBLE);
                    searcharea.setVisibility(View.VISIBLE);
                    toolbarArea.setVisibility(View.GONE);

                    searchTxt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(searchTxt, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                break;
            case R.id.imageCancel:
                searchTxt.setText("");
                break;
        }
    }


}

