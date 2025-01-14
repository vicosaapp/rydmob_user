package com.adapter.files;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.general.files.showTermsDialog;
import com.sessentaservices.usuarios.R;
import com.utils.CommonUtilities;
import com.utils.LoadImage;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class UberXCategoryInnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int TYPE_LIST_HORIZONTAL_INNER = 1;
    private final int NO_TYPE = -1;

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;

    String CAT_TYPE_MODE = "0";

    boolean isForceToGrid = false;
    boolean ismultiSelect = false;
    String userProfileJson;

    int dimension;
    int bannerHeight;
    int bannerWidth;
    int width;
    int margin;
    int grid;
    int parentPos;
    boolean click = false;
    //added for manage Listing
    String APP_HOME_PAGE_LIST_VIEW_ENABLED;
    String LBL_DELIVERY_SERVICES;
    boolean isFirst = true;
    int isFirstPos = 0;
    int daynamic_height, daynamic_width;
    boolean isDaynamic = false;
    boolean isList = false;
    String bgColor, eShowType;

    double itemWidth = 0;
    boolean ENABLE_NEW_HOME_SCREEN_LAYOUT_APP;

    public UberXCategoryInnerAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, int parentPos, GeneralFunctions generalFunc, String bgColor, String eShowType) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.parentPos = parentPos;
        this.generalFunc = generalFunc;
        this.bgColor = bgColor;
        this.eShowType = eShowType;
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        APP_HOME_PAGE_LIST_VIEW_ENABLED = generalFunc.getJsonValue("APP_HOME_PAGE_LIST_VIEW_ENABLED", userProfileJson);
        LBL_DELIVERY_SERVICES = generalFunc.retrieveLangLBl("", "LBL_DELIVERY_SERVICES");
        if (generalFunc.getJsonValue("SERVICE_PROVIDER_FLOW", userProfileJson).equalsIgnoreCase("PROVIDER")) {
            ismultiSelect = true;
        }
        dimension = mContext.getResources().getDimensionPixelSize(R.dimen.category_grid_size);
        margin = mContext.getResources().getDimensionPixelSize(R.dimen.category_banner_left_right_margin);
        grid = mContext.getResources().getDimensionPixelSize(R.dimen.category_grid_size);
        width = margin * 2;
        bannerWidth = Utils.getWidthOfBanner(mContext, width);
        bannerHeight = Utils.getHeightOfBanner(mContext, width * 4, "16:9");
        daynamic_height = mContext.getResources().getDimensionPixelSize(R.dimen._60sdp);
        daynamic_width = mContext.getResources().getDimensionPixelSize(R.dimen._60sdp);

        ENABLE_NEW_HOME_SCREEN_LAYOUT_APP = generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP", userProfileJson) != null && generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP", userProfileJson).equalsIgnoreCase("Yes");

        if (ENABLE_NEW_HOME_SCREEN_LAYOUT_APP) {
            itemWidth = (Utils.getScreenPixelWidth(mContext) - mContext.getResources().getDimensionPixelSize(R.dimen._90sdp) - mContext.getResources().getDimensionPixelSize(R.dimen._32sdp)) * 0.33;
            itemWidth = (itemWidth * 0.65) + itemWidth;

        }
        if (eShowType.equalsIgnoreCase("CubeXList")) {
            itemWidth = (Utils.getScreenPixelWidth(mContext) - mContext.getResources().getDimensionPixelSize(R.dimen._90sdp) - mContext.getResources().getDimensionPixelSize(R.dimen._32sdp)) * 0.33;
            itemWidth = (itemWidth * 0.75) + itemWidth;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        isList = true;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uberx_list_design, parent, false);

        if (ENABLE_NEW_HOME_SCREEN_LAYOUT_APP || eShowType.equalsIgnoreCase("CubeXList")) {
            isDaynamic = true;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uberx_daynamiclist_design_new, parent, false);

            view.setLayoutParams(new ViewGroup.LayoutParams((int) itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new ListViewHolderHorizontalInner(view);

    }

    public void setCategoryMode(String CAT_TYPE_MODE) {
        this.CAT_TYPE_MODE = CAT_TYPE_MODE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentViewHolder, final int position) {


        if (parentViewHolder instanceof ListViewHolderHorizontalInner) {
            HashMap<String, String> item = list_item.get(position);
            String vCategory = item.get("vCategory");
            String eShowTermsStr = item.get("eShowTerms");
            String vListLogo = item.get("vListLogo");
            //String vCategoryName = item.get("vCategoryName");
            boolean eShowTerms = Utils.checkText(eShowTermsStr) && eShowTermsStr.equalsIgnoreCase("yes");

            ListViewHolderHorizontalInner viewHolder = (ListViewHolderHorizontalInner) parentViewHolder;

            if (isFirstPos == position) {
                isFirst = true;
            }
            if (isFirst) {
                isFirst = false;
                isFirstPos = position;
            }


            viewHolder.catNameTxt.setText(vCategory);

            String imageURL;
            assert vListLogo != null;
            if (vListLogo != null) {
                if (isDaynamic) {
                    imageURL = Utils.getResizeImgURL(mContext, vListLogo, daynamic_width, daynamic_height);
                } else {
                    imageURL = Utils.getResizeImgURL(mContext, vListLogo, grid, grid);
                }
                new LoadImage.builder(LoadImage.bind(imageURL), viewHolder.catImgView).setErrorImagePath(R.mipmap.ic_no_icon).setPlaceholderImagePath(R.mipmap.ic_no_icon).build();
            }


            if (bgColor != null && !bgColor.equalsIgnoreCase("")) {
                viewHolder.contentArea.setBackgroundColor(Color.parseColor(bgColor));
            }

            viewHolder.contentArea.setOnClickListener(view -> {
                if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
                    if (onItemClickList != null) {
                        if (eShowTerms && !CommonUtilities.ageRestrictServices.contains("1")) {
                            new showTermsDialog(getActContext(), generalFunc, position, vCategory, click, () -> onItemClickList.onItemClick(position, parentPos));
                        } else {
                            onItemClickList.onItemClick(position, parentPos);
                        }
                    }
                } else {
                    if (onItemClickList != null) {
                        onItemClickList.onItemClick(position, parentPos);
                    }
                }
            });
        }
    }

    private Context getActContext() {
        return mContext;
    }

    private class SetOnTouchList implements View.OnTouchListener {
        int viewType;
        int item_position;
        boolean isBlockDownEvent;

        public SetOnTouchList(int viewType, int item_position, boolean isBlockDownEvent) {
            this.item_position = item_position;
            this.viewType = viewType;
            this.isBlockDownEvent = isBlockDownEvent;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    /*if (!isBlockDownEvent) {
                        scaleView(view, viewType == TYPE_BANNER ? 0.97f : 0.85f, viewType == TYPE_BANNER ? 0.97f : 0.85f, motionEvent.getAction(), viewType, item_position);
                    }*/
                    break;
                case MotionEvent.ACTION_UP:
                    if (list_item.size() > item_position && (list_item.get(item_position).get("LAST_CLICK_TIME") == null || (System.currentTimeMillis() - GeneralFunctions.parseLongValue(0, list_item.get(item_position).get("LAST_CLICK_TIME"))) > 1000)) {
                        scaleView(view, (float) 1.0, (float) 1.0, motionEvent.getAction(), viewType, item_position);
                    } else {
                        scaleView(view, (float) 1.0, (float) 1.0, MotionEvent.ACTION_CANCEL, viewType, item_position);
                    }

                    break;
                case MotionEvent.ACTION_CANCEL:
                    scaleView(view, (float) 1.0, (float) 1.0, motionEvent.getAction(), viewType, item_position);
                    break;
            }
            return true;
        }
    }

    private void scaleView(View v, float startScale, float endScale, int motionEvent, int viewType, int item_position) {

        v.setOnTouchListener(new SetOnTouchList(viewType, item_position, true));

        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(100);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (motionEvent == MotionEvent.ACTION_UP && list_item.size() > item_position) {
                    list_item.get(item_position).put("LAST_CLICK_TIME", "" + System.currentTimeMillis());
                    v.performClick();
                }
                v.setOnTouchListener(new SetOnTouchList(viewType, item_position, false));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (CAT_TYPE_MODE.equals("0")) {
            HashMap<String, String> dataMap = list_item.get(position);
            String eShowType = dataMap.get("eShowType");
            if (eShowType != null && !eShowType.equals("") && eShowType.equalsIgnoreCase("List") && !isForceToGrid) {
                return TYPE_LIST_HORIZONTAL_INNER;
            }
        }
        return NO_TYPE;
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position, int parentPos/*, Boolean isMore*/);

        void onMultiItem(String id, boolean b);
    }

    private static class ListViewHolderHorizontalInner extends RecyclerView.ViewHolder {
        public TextView catNameTxt;
        public View contentArea;
        public ImageView catImgView;

        private ListViewHolderHorizontalInner(View view) {
            super(view);
            catNameTxt = (TextView) view.findViewById(R.id.catNameTxt);
            contentArea = view.findViewById(R.id.contentArea);
            catImgView = (ImageView) view.findViewById(R.id.catImgView);
        }
    }
}
