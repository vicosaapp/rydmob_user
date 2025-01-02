package com.sessentaservices.usuarios;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.ParentActivity;
import com.adapter.files.GalleryImagesRecyclerAdapter;
import com.general.files.ActUtils;
import com.general.files.GeneralFunctions;
import com.service.handler.ApiHandler;
import com.utils.LoadImage;
import com.utils.MyUtils;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.carouselview.CarouselView;
import com.view.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PrescriptionHistoryImagesActivity extends ParentActivity implements GalleryImagesRecyclerAdapter.OnItemClickListener {

    ImageView backImgView;
    private MTextView noteTxt, noDescTxt, closeCarouselTxtView;
    ArrayList<HashMap<String, String>> listData = new ArrayList<>();
    AppCompatImageView noImgView;
    ProgressBar loading_images;

    RecyclerView imageListRecyclerView;
    GalleryImagesRecyclerAdapter adapter;
    LinearLayout confirmBtnArea;
    MButton btn_type2_confirm;

    View carouselContainerView;
    CarouselView carouselView;
    ArrayList<String> imageIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_history_images);
        backImgView = findViewById(R.id.backImgView);
        MTextView titleTxt = findViewById(R.id.titleTxt);
        confirmBtnArea = findViewById(R.id.confirmBtnArea);
        btn_type2_confirm = ((MaterialRippleLayout) findViewById(R.id.btn_type2_confirm)).getChildView();
        addToClickHandler(btn_type2_confirm);

        noteTxt = findViewById(R.id.noteTxt);
        noDescTxt = findViewById(R.id.noDescTxt);
        noImgView = findViewById(R.id.noImgView);
        imageListRecyclerView = findViewById(R.id.imageListRecyclerView);
        loading_images = findViewById(R.id.loading_images);
        carouselContainerView = findViewById(R.id.carouselContainerView);
        carouselView = findViewById(R.id.carouselView);
        closeCarouselTxtView = findViewById(R.id.closeCarouselTxtView);
        addToClickHandler(closeCarouselTxtView);

        adapter = new GalleryImagesRecyclerAdapter(getActContext(), listData, generalFunc, false, false, true);

        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY"));
        noDescTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY_NOREPORT"));
        noteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRESCRIPTION_HISTORY_NOTE"));
        btn_type2_confirm.setText(generalFunc.retrieveLangLBl("", "LBL_CONFIRM_TXT"));
        closeCarouselTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT"));


        GridLayoutManager gridLay = new GridLayoutManager(getActContext(), adapter.getNumOfColumns());

        imageListRecyclerView.setLayoutManager(gridLay);
        adapter.setOnItemClickListener(this);
        imageListRecyclerView.setAdapter(adapter);
        addToClickHandler(backImgView);
        getImages();

    }

    private Context getActContext() {
        return PrescriptionHistoryImagesActivity.this;
    }

    @Override
    public void onItemClickList(View v, int position) {
        carouselContainerView.setVisibility(View.VISIBLE);
        carouselView.setViewListener(viewListener);
        carouselView.setPageCount(listData.size());
        carouselView.setCurrentItem(position);
    }

    ViewListener viewListener = position -> {
        ImageView customView = new ImageView(getActContext());

        CarouselView.LayoutParams layParams = new CarouselView.LayoutParams(CarouselView.LayoutParams.MATCH_PARENT, CarouselView.LayoutParams.MATCH_PARENT);
        customView.setLayoutParams(layParams);

        customView.setPadding(Utils.dipToPixels(getActContext(), 15), 0, Utils.dipToPixels(getActContext(), 15), 0);
        customView.setImageResource(R.mipmap.ic_no_icon);

        final HashMap<String, String> item = listData.get(position);

        new LoadImage.builder(LoadImage.bind(Utils.getResizeImgURL(getActContext(), Objects.requireNonNull(item.get("vImage")), ((int) Utils.getScreenPixelWidth(getActContext())) - Utils.dipToPixels(getActContext(), 30), 0, Utils.getScreenPixelHeight(getActContext()) - Utils.dipToPixels(getActContext(), 30))), customView).setErrorImagePath(R.mipmap.ic_no_icon).setPlaceholderImagePath(R.mipmap.ic_no_icon).build();

        return customView;
    };

    @Override
    public void onLongItemClickList(View v, int position) {
        HashMap<String, String> map = listData.get(position);
        map.put("isSel", "Yes");
        listData.set(position, map);
        imageIdList.add(listData.get(position).get("iImageId"));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClick(View v, int position) {
        HashMap<String, String> map = listData.get(position);
        map.put("isSel", "No");
        listData.set(position, map);
        adapter.notifyDataSetChanged();
        while (imageIdList.remove(listData.get(position).get("iImageId"))) {
        }
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.backImgView) {
            PrescriptionHistoryImagesActivity.super.onBackPressed();
        } else if (i == btn_type2_confirm.getId()) {

            if (imageIdList.size() == 0) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_SELECT_IMAGE_ERROR"));
                return;
            }
            Bundle bn = new Bundle();
            bn.putString("iImageId", android.text.TextUtils.join(",", imageIdList));
            (new ActUtils(getActContext())).setOkResult(bn);
            finish();

        } else if (i == closeCarouselTxtView.getId()) {
            if (carouselContainerView.getVisibility() == View.VISIBLE) {
                carouselContainerView.setVisibility(View.GONE);
            }
        }
    }


    private void getImages() {
        loading_images.setVisibility(View.VISIBLE);
        noImgView.setVisibility(View.GONE);
        noDescTxt.setVisibility(View.GONE);
        noteTxt.setVisibility(View.VISIBLE);
        confirmBtnArea.setVisibility(View.GONE);

        listData.clear();
        adapter.notifyDataSetChanged();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getPrescriptionImages");
        parameters.put("UserType", Utils.app_type);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("iServiceId", generalFunc.getServiceId());
        parameters.put("PreviouslyUploaded", "1");
        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, responseString -> {
            if (responseString != null && !responseString.equalsIgnoreCase("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                confirmBtnArea.setVisibility(View.VISIBLE);
                if (isDataAvail) {
                    listData.clear();

                    JSONArray arr_data = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_data != null) {
                        for (int i = 0; i < arr_data.length(); i++) {
                            JSONObject obj_tmp = generalFunc.getJsonObject(arr_data, i);

                            HashMap<String, String> mapData = new HashMap<>();
                            MyUtils.createHashMap(generalFunc, mapData, obj_tmp);
                            mapData.put("isSel", "No");
                            listData.add(mapData);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (listData.size() == 0) {
                        noDescTxt.setVisibility(View.VISIBLE);
                        noImgView.setVisibility(View.VISIBLE);
                        noteTxt.setVisibility(View.VISIBLE);
                        confirmBtnArea.setVisibility(View.GONE);
                    }
                } else {
                    noDescTxt.setVisibility(View.VISIBLE);
                    noImgView.setVisibility(View.VISIBLE);
                    noteTxt.setVisibility(View.VISIBLE);
                    confirmBtnArea.setVisibility(View.GONE);
                }
            } else {
                generalFunc.showError(true);
            }
            loading_images.setVisibility(View.GONE);
        });

    }
}