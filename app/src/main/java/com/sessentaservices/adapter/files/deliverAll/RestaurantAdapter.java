package com.adapter.files.deliverAll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sessentaservices.usuarios.R;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.general.files.SafetyDialog;
import com.general.files.ActUtils;
import com.like.LikeButton;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HashMap<String, String>> resArrList;
    Context mContext;
    public RestaurantOnClickListener restaurantOnClickListener;
    boolean isFooterEnabled = false;
    FooterViewHolder footerHolder;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    View footerView;
    GeneralFunctions generalFunctions;
    String ENABLE_FAVORITE_STORE_MODULE = "";
    String userProfileJson = "";

    int enabledColor, disabledColor, appCompactColor;
    PorterDuff.Mode porterDuffMode;
    int backColor;
    int strokeColor;
    int appThemeColor;
    String LBL_DELIVERY_TIME, LBL_MIN_ORDER_TXT;
    String ENABLE_STORE_WISE_BANNER;
    JSONObject userProfileJsonObj;

    public RestaurantAdapter(Context context, ArrayList<HashMap<String, String>> mapArrayList, boolean isFooterEnabled) {
        this.mContext = context;
        this.resArrList = mapArrayList;
        this.isFooterEnabled = isFooterEnabled;
        generalFunctions = MyApp.getInstance().getGeneralFun(context);
        userProfileJson = generalFunctions.retrieveValue(Utils.USER_PROFILE_JSON);
        userProfileJsonObj = generalFunctions.getJsonObject(generalFunctions.retrieveValue(Utils.USER_PROFILE_JSON));
        ENABLE_FAVORITE_STORE_MODULE = generalFunctions.getJsonValue("ENABLE_FAVORITE_STORE_MODULE", userProfileJson);

        enabledColor = mContext.getResources().getColor(R.color.black);
        disabledColor = mContext.getResources().getColor(R.color.gray);
        appCompactColor = ContextCompat.getColor(mContext, R.color.gray);
        porterDuffMode = PorterDuff.Mode.SRC_IN;
        backColor = mContext.getResources().getColor(R.color.appThemeColor_1);
        appThemeColor = mContext.getResources().getColor(R.color.appThemeColor_1);
        strokeColor = Color.parseColor("#ffffff");

        LBL_DELIVERY_TIME = generalFunctions.retrieveLangLBl("", "LBL_DELIVERY_TIME");
        LBL_MIN_ORDER_TXT = generalFunctions.retrieveLangLBl("", "LBL_MIN_ORDER_TXT");
        ENABLE_STORE_WISE_BANNER = generalFunctions.getJsonValueStr("ENABLE_STORE_WISE_BANNER", userProfileJsonObj);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_list_design, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RestaurantAdapter.ViewHolder) {

            RestaurantAdapter.ViewHolder resholder = (RestaurantAdapter.ViewHolder) holder;
            HashMap<String, String> data = resArrList.get(position);
            resholder.restaurantNameTxt.setText(data.get("vCompany"));
            String vAvgRating = data.get("vAvgRating");
            if (vAvgRating != null && !vAvgRating.equalsIgnoreCase("") && !vAvgRating.equalsIgnoreCase("0")) {
                resholder.restaurantRateTxt.setText(data.get("vAvgRatingConverted"));
                resholder.restaurantRateTxt.setVisibility(View.VISIBLE);
                resholder.ratingImg.setVisibility(View.VISIBLE);
            } else {
                resholder.restaurantRateTxt.setVisibility(View.GONE);
                resholder.ratingImg.setVisibility(View.GONE);
            }
            resholder.RestCuisineTXT.setText(data.get("Restaurant_Cuisine"));
            resholder.pricePerPersonTxt.setText(data.get("Restaurant_PricePerPersonConverted"));
            resholder.deliveryTimeTxt.setText(data.get("Restaurant_OrderPrepareTimeConverted"));

            resholder.deliveryLBLTimeTxt.setText(LBL_DELIVERY_TIME);


            if (Utils.checkText(data.get("Restaurant_OfferMessage_shortConverted"))) {
                resholder.offerArea.setVisibility(View.VISIBLE);
                resholder.offerTxt.setText(data.get("Restaurant_OfferMessage_shortConverted"));
                resholder.offerTxt.setSelected(true);
            } else {
                resholder.offerArea.setVisibility(View.GONE);
            }

            String image = data.get("vImage");
            String vImage = Utils.checkText(image) ? image : "http";
            if (Utils.checkText(vImage)) {
                new LoadImage.builder(LoadImage.bind(vImage), resholder.vImageImgView).setErrorImagePath(R.color.imageBg).setPlaceholderImagePath(R.color.imageBg).build();
            }


            String fav = data.get("eFavStore");
            //Fav Store Features
            if (ENABLE_FAVORITE_STORE_MODULE.equalsIgnoreCase("Yes") && Utils.checkText(fav) && fav.equalsIgnoreCase("Yes")) {
                resholder.favImage.setVisibility(View.VISIBLE);

            } else {
                resholder.favImage.setVisibility(View.GONE);
            }


            if (data.get("Restaurant_Status").equalsIgnoreCase("Closed")) {

                resholder.restaurantNameTxt.setTextColor(disabledColor);
                // resholder.offerTxt.setTextColor(disabledColor);
                resholder.RestCuisineTXT.setTextColor(disabledColor);
                resholder.pricePerPersonTxt.setTextColor(disabledColor);
                resholder.deliveryTimeTxt.setTextColor(disabledColor);
                resholder.minOrderTxt.setTextColor(disabledColor);
                // resholder.offerImage.setColorFilter(appCompactColor, porterDuffMode);
                resholder.timerImage.setColorFilter(appCompactColor, porterDuffMode);
                resholder.resStatusTxt.setVisibility(View.VISIBLE);
                if (data.get("timeslotavailable").equalsIgnoreCase("Yes")) {
                    if (data.get("eAvailable").equalsIgnoreCase("No")) {
                        resholder.resStatusTxt.setText(data.get("LBL_NOT_ACCEPT_ORDERS_TXT"));
                    }
                } else {
                    if (!data.get("Restaurant_Opentime").equalsIgnoreCase("")) {
                        resholder.resStatusTxt.setText(data.get("LBL_CLOSED_TXT") + ": " + data.get("Restaurant_OpentimeConverted"));
                    } else {
                        resholder.resStatusTxt.setText(data.get("LBL_CLOSED_TXT"));
                    }
                }
                resholder.resStatusTxt.setTextColor(mContext.getResources().getColor(R.color.redlight));
            } else {
                resholder.resStatusTxt.setVisibility(View.GONE);

                PorterDuff.Mode porterDuffMode = PorterDuff.Mode.SRC_IN;

                resholder.restaurantNameTxt.setTextColor(enabledColor);
                int color = mContext.getResources().getColor(R.color.itemgray);
                resholder.RestCuisineTXT.setTextColor(color);
                resholder.pricePerPersonTxt.setTextColor(color);
                resholder.deliveryTimeTxt.setTextColor(enabledColor);
                resholder.minOrderTxt.setTextColor(appThemeColor);
                resholder.timerImage.setColorFilter(ContextCompat.getColor(mContext, R.color.appThemeColor_1), porterDuffMode);
            }

            if (generalFunctions.getServiceId().equals("1") || generalFunctions.getServiceId().equalsIgnoreCase("")) {
                resholder.pricePerPersonTxt.setVisibility(View.VISIBLE);
            } else {
                resholder.pricePerPersonTxt.setVisibility(View.GONE);
            }


            resholder.minOrderTxt.setText(data.get("Restaurant_MinOrderValueConverted"));
            resholder.minOrderLBLTxt.setText(LBL_MIN_ORDER_TXT);
            if (Utils.getText(resholder.minOrderTxt).equals("")) {
                resholder.minOrderLBLTxt.setVisibility(View.GONE);
            } else {
                resholder.minOrderLBLTxt.setVisibility(View.GONE);
            }
            String url = data.get("Restaurant_Safety_URL");

            if ((data.get("Restaurant_Safety_Status") != null && data.get("Restaurant_Safety_Status").equalsIgnoreCase("Yes"))) {

                if (generalFunctions.isRTLmode()) {
                    resholder.safetyArea.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drawable_rounded_left_curve));
                }
                resholder.safetyArea.setVisibility(View.VISIBLE);
                resholder.safetylayout.setVisibility(View.GONE);
                resholder.resSsafetyTxt.setText(generalFunctions.retrieveLangLBl("", "LBL_SAFETY_NOTE_TITLE_LIST"));


                int grid = mContext.getResources().getDimensionPixelSize(R.dimen.fab_margin);
                String imageURL = Utils.getResizeImgURL(mContext, data.get("Restaurant_Safety_Icon"), grid, grid);

                new LoadImage.builder(LoadImage.bind(imageURL), resholder.safetyImage).setErrorImagePath(R.drawable.ic_safety).setPlaceholderImagePath(R.drawable.ic_safety).build();


                resholder.safetyArea.setOnClickListener(v -> {

                    Intent intent = new Intent(mContext, SafetyDialog.class);
                    intent.putExtra("URL", url);
                    new ActUtils(mContext).startAct(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);

                });

            } else {
                resholder.safetyArea.setVisibility(View.GONE);
                resholder.safetylayout.setVisibility(View.GONE);
            }


            resholder.restaurantAdptrLayout.setOnClickListener(v -> {
                Log.e("resSize", "click");
                if (restaurantOnClickListener != null) {
                    Log.e("resSize", "clickNotnull");
                    restaurantOnClickListener.setOnRestaurantclick(position);
                }
            });
        } else {
            RestaurantAdapter.FooterViewHolder footerHolder = (FooterViewHolder) holder;
            this.footerHolder = footerHolder;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled == true) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }


    private boolean isPositionFooter(int position) {
        return position == resArrList.size();
    }


    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return resArrList.size() + 1;
        } else {
            return resArrList.size();
        }
    }

    public void addFooterView() {
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ratingImg;
        MTextView restaurantRateTxt;
        LinearLayout restaurantAdptrLayout;
        MTextView restaurantNameTxt, deliveryTimeTxt, minOrderTxt, RestCuisineTXT, pricePerPersonTxt, offerTxt, deliveryLBLTimeTxt, minOrderLBLTxt;
        MTextView resStatusTxt;
        SelectableRoundedImageView imgView;
        ImageView offerImage, timerImage, vImageImgView;
        //Fav Store Features
        LikeButton likeButton;
        ImageView favImage;

        MTextView resSsafetyTxt;
        ImageView safetyImage;
        LinearLayout offerArea, safetylayout;
        RelativeLayout safetyArea;
        View viewArea;

        public ViewHolder(View itemView) {
            super(itemView);

            imgView = (SelectableRoundedImageView) itemView.findViewById(R.id.imgView);
            restaurantNameTxt = itemView.findViewById(R.id.restaurantNameTxt);
            resStatusTxt = itemView.findViewById(R.id.resStatusTxt);
            restaurantRateTxt = itemView.findViewById(R.id.restaurantRateTxt);
            ratingImg = itemView.findViewById(R.id.ratingImg);
            RestCuisineTXT = itemView.findViewById(R.id.RestCuisineTXT);
            offerTxt = itemView.findViewById(R.id.offerTxt);
            pricePerPersonTxt = itemView.findViewById(R.id.pricePerPersonTxt);
            deliveryTimeTxt = itemView.findViewById(R.id.deliveryTimeTxt);
            deliveryLBLTimeTxt = itemView.findViewById(R.id.deliveryLBLTimeTxt);
            minOrderTxt = itemView.findViewById(R.id.minOrderTxt);
            minOrderLBLTxt = itemView.findViewById(R.id.minOrderLBLTxt);
            restaurantAdptrLayout = itemView.findViewById(R.id.restaurantAdptrLayout);
            //  viewArea = itemView.findViewById(R.id.viewArea);
            offerArea = itemView.findViewById(R.id.offerArea);
            offerImage = itemView.findViewById(R.id.offerImage);
            timerImage = itemView.findViewById(R.id.timerImage);
            vImageImgView = itemView.findViewById(R.id.imgView);
            favImage = itemView.findViewById(R.id.favImage);
            likeButton = (LikeButton) itemView.findViewById(R.id.likeButton);

            resSsafetyTxt = itemView.findViewById(R.id.resSsafetyTxt);
            safetyImage = itemView.findViewById(R.id.safetyImage);
            safetylayout = itemView.findViewById(R.id.safetylayout);
            safetyArea = itemView.findViewById(R.id.safetyArea);

        }
    }

    public interface RestaurantOnClickListener {
        void setOnRestaurantclick(int position);

        //Fav Store Features
        void setOnRestaurantclick(int position, boolean liked);
    }

    public void setOnRestaurantItemClick(RestaurantOnClickListener onRestaurantItemClick) {
        this.restaurantOnClickListener = onRestaurantItemClick;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }
}
