package com.sessentaservices.usuarios.deliverAll;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.activity.ParentActivity;
import com.adapter.files.CommonBanner23Adapter;
import com.adapter.files.deliverAll.CuisinesAdapter;
import com.adapter.files.deliverAll.MainCategoryAdapter;
import com.adapter.files.deliverAll.RestaurantAdapter;
import com.adapter.files.deliverAll.RestaurantChildAdapter;
import com.andremion.counterfab.CounterFab;
import com.dialogs.OpenListView;
import com.fragments.MyBookingFragment;
import com.fragments.MyProfileFragment;
import com.fragments.MyWalletFragment;
import com.general.files.ActUtils;
import com.general.files.AddBottomBar;
import com.general.files.GeneralFunctions;
import com.general.files.GetAddressFromLocation;
import com.general.files.GetLocationUpdates;
import com.general.files.InternetConnection;
import com.general.files.KmHeaderItemDecoration;
import com.general.files.KmStickyListener;
import com.general.files.ManageScroll;
import com.general.files.MyApp;
import com.general.files.MySwipeRefreshLayout;
import com.general.files.OpenAdvertisementDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.SearchLocationActivity;
import com.model.MainCategoryModel;
import com.model.ServiceModule;
import com.realmModel.Cart;
import com.service.handler.ApiHandler;
import com.utils.Logger;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FoodDeliveryHomeActivity extends ParentActivity implements GetAddressFromLocation.AddressFound, GetLocationUpdates.LocationUpdates, RestaurantAdapter.RestaurantOnClickListener, AppBarLayout.OnOffsetChangedListener, CuisinesAdapter.CuisinesOnClickListener, SwipeRefreshLayout.OnRefreshListener, MainCategoryAdapter.CategoryOnClickListener, RestaurantChildAdapter.RestaurantOnClickListener {

    public LinearLayout noloactionview;
    public ImageView nolocmenuImgView;
    public ImageView nolocbackImgView;
    public String userProfileJson = "";
    public JSONObject obj_userProfile;
    RecyclerView dataListRecyclerView;
    private MySwipeRefreshLayout swipeRefreshLayout;


    MTextView noOfServiceTxt;
    TextView headerAddressTxt;
    ArrayList<HashMap<String, String>> filterList = new ArrayList<>();
    public String latitude = "0.0";
    public String longitude = "0.0";
    ImageView backImgView;
    String address;
    GetLocationUpdates getLastLocation;
    boolean isUfxaddress = false;
    GetAddressFromLocation getAddressFromLocation;
    boolean isgpsview = false;
    MTextView noLocMsgTxt, deliveryAreaTxt;
    MTextView noLocTitleTxt;

    boolean isArrivedPopup = false;
    boolean isstartPopup = false;
    boolean isEndpopup = false;
    boolean isCancelpopup = false;
    MButton noThanksBtn;
    boolean isFilter = true;
    MTextView outAreaTitle;
    int numberOfRestaurant = 0;

    Location currentLocation;

    RestaurantAdapter restaurantAdapter;
    ArrayList<HashMap<String, String>> restaurantArr_List = new ArrayList<>();

    ArrayList<MainCategoryModel> categoryModels = new ArrayList<>();
    ArrayList<HashMap<String, String>> sortby_List = new ArrayList<>();

    private CommonBanner23Adapter mBannerAdapter;
    private JSONArray bannerListArray;

    MTextView filterTxtView, relevenceTxt;
    ImageView filterImage;
    MButton changeLocBtn, editLocationBtn;
    LinearLayout locationPopup;
    Location filterLocation;
    String isOfferCheck = "No";
    String isFavCheck = "No";
    String selectedFilterId = "";
    boolean filterClick = false;
    String selectedSort = "";
    boolean iServiceCategoryIdMatch = false;

    private AppBarLayout app_bar_layout_home;
    LinearLayout ratingArea;

    MTextView orderHotelName;
    ImageView ratingCancel;
    SimpleRatingBar ratingBar;

    LinearLayout errorViewArea;
    ErrorView errorView;

    InternetConnection internetConnection;
    String isOfferApply = "No";
    RealmResults<Cart> realmCartList;
    CounterFab fabcartIcon;
    String next_page_str = "";
    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;
    MTextView NoDataTxt;

    LinearLayout searchArea;
    MTextView searchTxtView;
    LinearLayout filterArea, cusineArea;
    MTextView cuisinesTxt;
    RecyclerView rvBanner, cuisinesListRecyclerView;
    CuisinesAdapter cuisinesAdapter;


    MainCategoryAdapter mainCategoryAdapter;

    LinearLayout bottomMenuArea;
    AddBottomBar bottomBar;
    FrameLayout container;
    private static final int SEL_CARD = 004;
    public static final int TRANSFER_MONEY = 87;

    String LBL_RESTAURANTS = "";

    String LBL_RELEVANCE = "";
    String LBL_RATING = "";
    String LBL_COST_LTOH = "";
    String LBL_COST_HTOL = "";

    String LBL_NO_INTERNET_TXT = "";

    String LBL_TIME = "";
    String LBL_FAVOURITE_STORE = "";

    String LBL_SHOW_RESTAURANTS_WITH = "";
    String LBL_CUISINES = "";
    String LBL_SEARCH_RESTAURANT = "";

    String LBL_OFFER = "";
    String LBL_APPLY_FILTER = "";
    String LBL_CLOSE_TXT = "";
    String LBL_RESET = "";

    String LBL_SORT_BY = "";
    RecyclerView main_category_Rec;
    LinearLayout categoryArea;
    LinearLayout storeCounterArea;

    String iCategoryId = "";
    ProgressBar loading;
    LinearLayout DataArea, DataLoadingArea;
    private RelativeLayout MainArea;
    CollapsingToolbarLayout collapsing_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_delivery_home_23);

        generalFunc = MyApp.getInstance().getGeneralFun(getActContext());
        internetConnection = new InternetConnection(getActContext());
        isUfxaddress = false;
        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
//        if (Utils.checkText(generalFunc.getMemberId())) {
        bottomBar = new AddBottomBar(getActContext(), obj_userProfile);
//        }
        loading = (ProgressBar) findViewById(R.id.loading);
        MainArea = (RelativeLayout) findViewById(R.id.MainArea);
        MainArea.setVisibility(View.GONE);
        DataArea = (LinearLayout) findViewById(R.id.DataArea);
        DataLoadingArea = (LinearLayout) findViewById(R.id.DataLoadingArea);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        if (obj_userProfile != null) {
            JSONArray serviceArray = generalFunc.getJsonArray("ServiceCategories", obj_userProfile);

            if (serviceArray != null && serviceArray.length() > 1 && ServiceModule.isDeliverAllOnly()) {
                JSONObject advertise_banner_data = generalFunc.getJsonObject("advertise_banner_data", obj_userProfile);
                if (advertise_banner_data != null && advertise_banner_data.length() > 0) {

                    String image_url = generalFunc.getJsonValueStr("image_url", advertise_banner_data);
                    if (image_url != null && !image_url.equalsIgnoreCase("")) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("image_url", generalFunc.getJsonValueStr("image_url", advertise_banner_data));
                        map.put("tRedirectUrl", generalFunc.getJsonValueStr("tRedirectUrl", advertise_banner_data));
                        map.put("vImageWidth", generalFunc.getJsonValueStr("vImageWidth", advertise_banner_data));
                        map.put("vImageHeight", generalFunc.getJsonValueStr("vImageHeight", advertise_banner_data));
                        new OpenAdvertisementDialog(getActContext(), map, generalFunc);
                    }
                }
                MyApp.getInstance().showOutsatandingdilaog(backImgView);
            }
        }
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        categoryArea = (LinearLayout) findViewById(R.id.categoryArea);
        storeCounterArea = (LinearLayout) findViewById(R.id.storeCounterArea);
        main_category_Rec = (RecyclerView) findViewById(R.id.main_category_Rec);
        container = (FrameLayout) findViewById(R.id.container);
        bottomMenuArea = (LinearLayout) findViewById(R.id.bottomMenuArea);
        fabcartIcon = (CounterFab) findViewById(R.id.fabcartIcon);
        searchArea = (LinearLayout) findViewById(R.id.searchArea);
        addToClickHandler(searchArea);
        searchTxtView = (MTextView) findViewById(R.id.searchTxtView);
        filterArea = (LinearLayout) findViewById(R.id.filterArea);
        cusineArea = (LinearLayout) findViewById(R.id.cusineArea);
        cuisinesTxt = (MTextView) findViewById(R.id.cuisinesTxt);
        cuisinesListRecyclerView = (RecyclerView) findViewById(R.id.cuisinesListRecyclerView);
        addToClickHandler(filterArea);

        cuisinesAdapter = new CuisinesAdapter(getActContext(), filterList, this);
        cuisinesListRecyclerView.setAdapter(cuisinesAdapter);

        app_bar_layout_home = (AppBarLayout) findViewById(R.id.app_bar_layout_home);

        ratingArea = (LinearLayout) findViewById(R.id.ratingArea);
        orderHotelName = (MTextView) findViewById(R.id.orderHotelName);
        ratingCancel = (ImageView) findViewById(R.id.ratingCancel);
        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        if (generalFunc.getJsonValue("LastOrderFoodDetailRatingShow", userProfileJson).equalsIgnoreCase("Yes")) {
            ratingBar.setPressedFillColor(ContextCompat.getColor(getActContext(), R.color.white));
        }
        errorViewArea = (LinearLayout) findViewById(R.id.errorViewArea);
        errorView = (ErrorView) findViewById(R.id.errorView);
        NoDataTxt = (MTextView) findViewById(R.id.NoDataTxt);


        ratingCancel.setOnClickListener(v -> {
            ratingArea.setVisibility(View.GONE);
            manageRecyclviewHeight();
        });

        ratingBar.setOnClickListener(v -> {

            Bundle bn = new Bundle();

            bn.putFloat("rating", ratingBar.getRating());

            if (generalFunc.getJsonValue("LastOrderFoodDetailRatingShow", userProfileJson).equalsIgnoreCase("Yes")) {
                bn.putBoolean("IS_NEW", true);
                bn.putString("listDriverFeedbackQuestions", generalFunc.getJsonValue("DRIVER_FEEDBACK_QUESTIONS", userProfileJson));
            } else {
                bn.putBoolean("IS_NEW", false);
            }
            bn.putString("iOrderId", generalFunc.getJsonValue("LastOrderId", userProfileJson));
            bn.putString("vOrderNo", generalFunc.getJsonValue("LastOrderNo", userProfileJson));
            bn.putString("driverName", generalFunc.getJsonValue("LastOrderDriverName", userProfileJson));
            bn.putString("vCompany", generalFunc.getJsonValue("LastOrderCompanyName", userProfileJson));
            bn.putString("eTakeaway", generalFunc.getJsonValue("LastOrderTakeaway", userProfileJson));

            new ActUtils(getActContext()).startActWithData(FoodRatingActivity.class, bn);
            ratingArea.setVisibility(View.GONE);
            manageRecyclviewHeight();
        });

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {

        //  getFilterList();

//                generateErrorView();
//                if (filterLocation != null) {
//                    getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), false);
//
//                    getFilterList(filterLocation.getLatitude(), filterLocation.getLongitude());
//                }
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
        addToClickHandler(fabcartIcon);

        String Ratings_From_DeliverAll = generalFunc.getJsonValueStr("Ratings_From_DeliverAll", obj_userProfile);

        if (Ratings_From_DeliverAll != null && !Ratings_From_DeliverAll.equalsIgnoreCase("") && !Ratings_From_DeliverAll.equalsIgnoreCase("Done")) {
            ratingArea.setVisibility(View.VISIBLE);
            manageRecyclviewHeight();


            orderHotelName.setText(generalFunc.getJsonValueStr("LastOrderCompanyName", obj_userProfile));
        }

        boolean isback = getIntent().getBooleanExtra("isback", false);

        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        dataListRecyclerView = (RecyclerView) findViewById(R.id.dataListRecyclerView);

        filterTxtView = (MTextView) findViewById(R.id.filterTxtView);
        filterImage = (ImageView) findViewById(R.id.filterImage);
        relevenceTxt = (MTextView) findViewById(R.id.relevenceTxt);
        headerAddressTxt = (TextView) findViewById(R.id.headerAddressTxt);
        noOfServiceTxt = (MTextView) findViewById(R.id.noOfServiceTxt);
        outAreaTitle = (MTextView) findViewById(R.id.outAreaTitle);

        if (generalFunc.isRTLmode()) {
            backImgView.setRotation(180);
        }
        changeLocBtn = (MButton) findViewById(R.id.changeLocBtn);
        locationPopup = (LinearLayout) findViewById(R.id.locationPopup);
        editLocationBtn = (MButton) findViewById(R.id.editLocationBtn);
        noThanksBtn = (MButton) findViewById(R.id.noThanksBtn);
        addToClickHandler(noThanksBtn);
        addToClickHandler(changeLocBtn);
        addToClickHandler(editLocationBtn);
        addToClickHandler(backImgView);
        addToClickHandler(filterTxtView);
        addToClickHandler(relevenceTxt);

        noloactionview = (LinearLayout) findViewById(R.id.noloactionview);
        noLocTitleTxt = (MTextView) findViewById(R.id.noLocTitleTxt);
        noLocMsgTxt = (MTextView) findViewById(R.id.noLocMsgTxt);
        deliveryAreaTxt = (MTextView) findViewById(R.id.deliveryAreaTxt);

        nolocmenuImgView = (ImageView) findViewById(R.id.nolocmenuImgView);
        nolocbackImgView = (ImageView) findViewById(R.id.nolocbackImgView);

        nolocmenuImgView.setOnClickListener(new setOnClickList());
        nolocbackImgView.setOnClickListener(new setOnClickList());

        if (isback) {
            bottomMenuArea.setVisibility(View.GONE);
            backImgView.setVisibility(View.VISIBLE);
        }

        dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));

        //  dataListRecyclerView.setAdapter(restaurantAdapter);

        addToClickHandler(headerAddressTxt);

        setData();
        setSortList();
        bannerData();
        restaurantAdapter = new RestaurantAdapter(getActContext(), restaurantArr_List, false);
        dataListRecyclerView.setAdapter(restaurantAdapter);

        dataListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                     @Override
                                                     public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                         super.onScrolled(recyclerView, dx, dy);

                                                         int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                                                         int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                                                         int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                                                         int lastInScreen = firstVisibleItemPosition + visibleItemCount;
                                                         if ((lastInScreen == totalItemCount) && !(mIsLoading) && isNextPageAvailable == true) {
                                                             mIsLoading = true;
                                                             restaurantAdapter.addFooterView();

                                                             getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), true);
                                                         } else if (isNextPageAvailable == false) {
                                                             restaurantAdapter.removeFooterView();
                                                         }
                                                     }
                                                 }

        );

        restaurantAdapter.setOnRestaurantItemClick(this);

        mainCategoryAdapter = new MainCategoryAdapter(getActContext(), categoryModels, generalFunc, this, this);
        main_category_Rec.setAdapter(mainCategoryAdapter);
        main_category_Rec.addItemDecoration(new KmHeaderItemDecoration((KmStickyListener) main_category_Rec.getAdapter()));

        generateErrorView();
        try {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            address = getIntent().getStringExtra("address");
            if (Utils.checkText(latitude)) {
                Location temploc = new Location("source");
                temploc.setLatitude(GeneralFunctions.parseDoubleValue(0.0, latitude));
                temploc.setLongitude(GeneralFunctions.parseDoubleValue(0.0, longitude));
                onLocationUpdate(temploc);
            } else {
                getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, false, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void bannerData() {
        rvBanner = findViewById(R.id.rvBanner);
        mBannerAdapter = new CommonBanner23Adapter(this, generalFunc, bannerListArray);
        rvBanner.setAdapter(mBannerAdapter);
        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(rvBanner);
    }

    public void manageRecyclviewHeight() {
        if (dataListRecyclerView != null) {
            LinearLayout.LayoutParams marginLayoutParams = new LinearLayout.LayoutParams(dataListRecyclerView.getLayoutParams());

            if (ratingArea.getVisibility() == View.VISIBLE) {
                marginLayoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen._60sdp));
            } else {
                marginLayoutParams.setMargins(0, 0, 0, 0);
            }

            dataListRecyclerView.setLayoutParams(marginLayoutParams);
        }
    }


    public void generateErrorView() {

        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");

        if (internetConnection.isNetworkConnected()) {
            errorViewArea.setVisibility(View.GONE);
        } else {
            if (errorViewArea.getVisibility() != View.VISIBLE) {
                errorViewArea.setVisibility(View.VISIBLE);
            }
            errorView.setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    if (filterLocation != null) {
                        getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), false);
                        //getFilterList(filterLocation.getLatitude(), filterLocation.getLongitude());
                        isFilter = true;
                    }
                }
            });
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //The Refresh must be only active when the offset is zero :
        swipeRefreshLayout.setEnabled(i == 0);
        Logger.d("onOffsetChanged", "::Called::" + i);
    }

    public void setSortList() {
        HashMap<String, String> relevanceMap = new HashMap<>();
        relevanceMap.put("label", LBL_RELEVANCE);
        relevanceMap.put("value", "relevance");
        filterPosition = 0;

        HashMap<String, String> ratingMap = new HashMap<>();
        ratingMap.put("label", LBL_RATING);
        ratingMap.put("value", "rating");

        HashMap<String, String> timeMap = new HashMap<>();
        timeMap.put("label", LBL_TIME);
        timeMap.put("value", "time");

        sortby_List.add(relevanceMap);
        sortby_List.add(ratingMap);
        sortby_List.add(timeMap);
        if (generalFunc.getServiceId().equalsIgnoreCase("1") || generalFunc.getServiceId().equalsIgnoreCase("")) {
            HashMap<String, String> costlTohMap = new HashMap<>();
            costlTohMap.put("label", LBL_COST_LTOH);
            costlTohMap.put("value", "costlth");

            HashMap<String, String> costhTolMap = new HashMap<>();
            costhTolMap.put("label", LBL_COST_HTOL);
            costhTolMap.put("value", "costhtl");

            sortby_List.add(costlTohMap);
            sortby_List.add(costhTolMap);
        }
    }


    public boolean isPubNubEnabled() {
        String ENABLE_PUBNUB = generalFunc.getJsonValueStr("ENABLE_PUBNUB", obj_userProfile);
        return ENABLE_PUBNUB.equalsIgnoreCase("Yes");
    }

    public void NoLocationView() {

//        if (!generalFunc.isLocationEnabled()) {
//            noloactionview.setVisibility(View.VISIBLE);
//
//        } else {
//            if (getLastLocation != null) {
//                getLastLocation.stopLocationUpdates();
//                getLastLocation = null;
//            }
//            getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, false, this);
//
//            noloactionview.setVisibility(View.GONE);
//        }


    }


    private void setData() {
        LBL_RESTAURANTS = generalFunc.retrieveLangLBl("", "LBL_RESTAURANTS");
        LBL_RELEVANCE = generalFunc.retrieveLangLBl("", "LBL_RELEVANCE");
        LBL_RATING = generalFunc.retrieveLangLBl("", "LBL_RATING");
        LBL_COST_LTOH = generalFunc.retrieveLangLBl("", "LBL_COST_LTOH");
        LBL_COST_HTOL = generalFunc.retrieveLangLBl("", "LBL_COST_HTOL");
        LBL_NO_INTERNET_TXT = generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT");
        LBL_TIME = generalFunc.retrieveLangLBl("", "LBL_TIME");
        LBL_FAVOURITE_STORE = generalFunc.retrieveLangLBl("", "LBL_FAVOURITE_STORE");
        LBL_SHOW_RESTAURANTS_WITH = generalFunc.retrieveLangLBl("", "LBL_SHOW_RESTAURANTS_WITH");
        LBL_CUISINES = generalFunc.retrieveLangLBl("", "LBL_CUISINES");


        if (generalFunc.getJsonValueStr("ENABLE_ITEM_SEARCH_STORE_ORDER", obj_userProfile).equalsIgnoreCase("Yes")) {
            LBL_SEARCH_RESTAURANT = generalFunc.retrieveLangLBl("", "LBL_SEARCH_RESTAURANT");
        } else {
            LBL_SEARCH_RESTAURANT = generalFunc.retrieveLangLBl("", "LBL_RESTAURANT_SEARCH");
        }

        LBL_OFFER = generalFunc.retrieveLangLBl("", "LBL_OFFER");
        LBL_APPLY_FILTER = generalFunc.retrieveLangLBl("", "LBL_APPLY_FILTER");
        LBL_CLOSE_TXT = generalFunc.retrieveLangLBl("", "LBL_CLOSE_TXT");
        LBL_RESET = generalFunc.retrieveLangLBl("", "LBL_RESET");

        LBL_SORT_BY = generalFunc.retrieveLangLBl("", "LBL_SORT_BY");

        relevenceTxt.setText(LBL_RELEVANCE);

        noOfServiceTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_RESTAURANT_TXT"));
        noLocTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOCATION_CHANGE_TITLE"));
        noLocMsgTxt.setText(generalFunc.retrieveLangLBl("", "LBL_LOCATION_CHANGE_NOTE"));
        noThanksBtn.setText(generalFunc.retrieveLangLBl("", "LBL_NO_THANKS"));
        changeLocBtn.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE_LOCATION"));
        editLocationBtn.setText(generalFunc.retrieveLangLBl("", "LBL_EDIT_LOCATION"));
        outAreaTitle.setText(generalFunc.retrieveLangLBl("", "LBL_OUT_OF_DELIVERY_AREA"));
        deliveryAreaTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVERY_AREA_NOTE"));
        filterTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_FILTER"));
        searchTxtView.setText(LBL_SEARCH_RESTAURANT);
        cuisinesTxt.setText(LBL_CUISINES);
    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public Context getActContext() {
        return FoodDeliveryHomeActivity.this;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void getRestaurantList(final double latitude, final double longitude, boolean isLoadMore) {


        if (noloactionview.getVisibility() == View.VISIBLE) {
            noloactionview.setVisibility(View.GONE);
            MainArea.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
//            DataLoadingArea.setVisibility(View.VISIBLE);
        }
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "loadAvailableRestaurants");
        parameters.put("PassengerLat", "" + latitude);
        parameters.put("PassengerLon", "" + longitude);
        parameters.put("fOfferType", isOfferCheck);
        parameters.put("eFavStore", isFavCheck);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("UserType", Utils.app_type);

        if (!iCategoryId.equalsIgnoreCase("")) {
            parameters.put("iCategoryId", iCategoryId);

        }

        if (headerAddressTxt != null && headerAddressTxt.getText() != null && !headerAddressTxt.getText().toString().equals("")) {
            parameters.put("vAddress", headerAddressTxt.getText().toString());
        }
        if (selectedFilterId == null) {
            selectedFilterId = "";
        }
        parameters.put("cuisineId", selectedFilterId);
        parameters.put("sortby", selectedSort);

        if (isLoadMore) {
            parameters.put("page", next_page_str);
        }

        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, responseString -> {

            swipeRefreshLayout.setRefreshing(false);
            generateErrorView();
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            DataLoadingArea.setVisibility(View.GONE);
            noOfServiceTxt.setVisibility(View.VISIBLE);
            if (responseObj != null && !responseObj.equals("")) {

                //it's use for deliverAll only
                //if items added in cart then login first time it's redirect to edit cart screen
                if (ServiceModule.isDeliverAllOnly() && !generalFunc.getMemberId().equalsIgnoreCase("") && getCartData() != null &&
                        getCartData().size() > 0 && !generalFunc.retrieveValue("IS_DELIVERALL_LOGIN").equalsIgnoreCase("Yes")) {
                    generalFunc.storeData("IS_DELIVERALL_LOGIN", "Yes");
                    new ActUtils(getActContext()).startAct(EditCartActivity.class);
                }

                backImgView.setVisibility(View.VISIBLE);
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                DataArea.setVisibility(View.VISIBLE);
                String nextPage = generalFunc.getJsonValueStr("NextPage", responseObj);
                String totalStore = generalFunc.getJsonValueStr("totalStore", responseObj);
                MainArea.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                storeCounterArea.setVisibility(View.VISIBLE);
                //add banner handling
                bannerListArray = generalFunc.getJsonArray("banner_data", responseString);
                mBannerAdapter.updateData(bannerListArray);

                if (isDataAvail) {
                    new ManageScroll(collapsing_toolbar).scroll(true);
                    NoDataTxt.setVisibility(View.GONE);
                    if (!isLoadMore) {
                        restaurantArr_List.clear();
                        restaurantAdapter.notifyDataSetChanged();
                        categoryModels.clear();
                        mainCategoryAdapter.notifyDataSetChanged();
                        numberOfRestaurant = 0;
                    }

                    //handling cuisine
                    if (isFilter) {
                        isFilter = false;
                        String getCuisineListData = generalFunc.getJsonValue("getCuisineList", responseString);
                        isOfferApply = generalFunc.getJsonValue("isOfferApply", getCuisineListData);
                        JSONArray cuisineArray = generalFunc.getJsonArray("CuisineList", getCuisineListData);
                        filterList.clear();
                        if (cuisineArray != null) {

                            for (int i = 0; i < cuisineArray.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                JSONObject filter_Obj = generalFunc.getJsonObject(cuisineArray, i);
                                map.put("cuisineId", generalFunc.getJsonValueStr("cuisineId", filter_Obj));
                                map.put("cuisineName", generalFunc.getJsonValueStr("cuisineName", filter_Obj));
                                map.put("eStatus", generalFunc.getJsonValueStr("eStatus", filter_Obj));
                                map.put("vImage", generalFunc.getJsonValueStr("vImage", filter_Obj));
                                map.put("vBgColor", generalFunc.getJsonValueStr("vBgColor", filter_Obj));
                                map.put("vTextColor", generalFunc.getJsonValueStr("vTextColor", filter_Obj));
                                map.put("vBorderColor", generalFunc.getJsonValueStr("vBorderColor", filter_Obj));

                                if (i == 0) {
                                    map.put("isCheck", "Yes");
                                }

                                filterList.add(map);
                            }
                            // filterTxtView.setVisibility(View.VISIBLE);
                            cusineArea.setVisibility(View.VISIBLE);
                            cuisinesAdapter.notifyDataSetChanged();
                        } else {
                            cusineArea.setVisibility(View.GONE);
                            //filterTxtView.setVisibility(View.GONE);
                        }
                    }

                    JSONArray restaurant_Arr = generalFunc.getJsonArray("message", responseObj);

                    if (generalFunc.retrieveValue("ENABLE_CATEGORY_WISE_STORES").equalsIgnoreCase("Yes") && iCategoryId.equalsIgnoreCase("")) {
                        if (!isLoadMore) {
                            restaurantArr_List.clear();
                            restaurantAdapter.notifyDataSetChanged();
                            categoryModels.clear();
                            mainCategoryAdapter.notifyDataSetChanged();
                        }

                        JSONArray categorywisestore_Arr = generalFunc.getJsonArray("CategoryWiseStores", responseObj);
                        if (categorywisestore_Arr != null && categorywisestore_Arr.length() > 0) {
                            String LBL_OPEN_NOW = "", LBL_CLOSED_TXT = "", LBL_NOT_ACCEPT_ORDERS_TXT = "";
                            int dimension = 0;
                            dimension = getActContext().getResources().getDimensionPixelSize(R.dimen.restaurant_img_size_home_screen);
                            LBL_OPEN_NOW = generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                            LBL_CLOSED_TXT = generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                            LBL_NOT_ACCEPT_ORDERS_TXT = generalFunc.retrieveLangLBl("close", "LBL_NOT_ACCEPT_ORDERS_TXT");
                            ArrayList<HashMap<String, String>> mainCategoryList = new ArrayList<>();

                            MainCategoryModel categoryModel;
                            for (int i = 0; i < categorywisestore_Arr.length(); i++) {
                                categoryModel = new MainCategoryModel();

                                HashMap<String, String> mainMap = new HashMap<String, String>();
                                JSONObject cateogry_Obj = generalFunc.getJsonObject(categorywisestore_Arr, i);
                                mainMap.put("vTitle", generalFunc.getJsonValueStr("vTitle", cateogry_Obj));
                                mainMap.put("vDescription", generalFunc.getJsonValueStr("vDescription", cateogry_Obj));
                                mainMap.put("vCategoryImage", generalFunc.getJsonValueStr("vCategoryImage", cateogry_Obj));
                                mainMap.put("iDisplayOrder", generalFunc.getJsonValueStr("iDisplayOrder", cateogry_Obj));

                                categoryModel.setvTitle(generalFunc.getJsonValueStr("vTitle", cateogry_Obj));
                                categoryModel.setIsShowall(generalFunc.getJsonValueStr("IS_SHOW_ALL", cateogry_Obj));
                                categoryModel.seteType(generalFunc.getJsonValueStr("eType", cateogry_Obj));
                                categoryModel.setiCategoryId(generalFunc.getJsonValueStr("iCategoryId", cateogry_Obj));
                                categoryModel.setvDescription(generalFunc.getJsonValueStr("vDescription", cateogry_Obj));
                                categoryModel.setvCategoryImage(generalFunc.getJsonValueStr("vCategoryImage", cateogry_Obj));
                                categoryModel.setiDisplayOrder(generalFunc.getJsonValueStr("iDisplayOrder", cateogry_Obj));

                                categoryModel.setViewType(MainCategoryModel.ViewType.Header);

                                JSONArray subData_Arr = generalFunc.getJsonArray("subData", cateogry_Obj);
                                ArrayList<HashMap<String, String>> childCategoryList = new ArrayList<>();
                                if (subData_Arr != null && subData_Arr.length() > 0) {

                                    try {
                                        categoryModels.add((MainCategoryModel) categoryModel.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }

                                    for (int j = 0; j < subData_Arr.length(); j++) {

                                        HashMap<String, String> map = new HashMap<String, String>();
                                        JSONObject restaurant_Obj = generalFunc.getJsonObject(subData_Arr, j);
                                        map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                                        map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                                        map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                                        map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                                        String vImage = generalFunc.getJsonValueStr("vImage", restaurant_Obj);
//                                        map.put("vImage", Utils.getResizeImgURL(getActContext(), vImage, dimension, dimension));
                                        map.put("vImage", vImage);
                                        map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", restaurant_Obj));
                                        map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", restaurant_Obj));
                                        map.put("vFromTimeSlot1", generalFunc.getJsonValueStr("vFromTimeSlot1", restaurant_Obj));
                                        map.put("vToTimeSlot1", generalFunc.getJsonValueStr("vToTimeSlot1", restaurant_Obj));
                                        map.put("vFromTimeSlot2", generalFunc.getJsonValueStr("vFromTimeSlot2", restaurant_Obj));
                                        map.put("vToTimeSlot2", generalFunc.getJsonValueStr("vToTimeSlot2", restaurant_Obj));
                                        map.put("fMinOrderValue", generalFunc.getJsonValueStr("fMinOrderValue", restaurant_Obj));
                                        map.put("Restaurant_Cuisine", generalFunc.getJsonValueStr("Restaurant_Cuisine", restaurant_Obj));
                                        map.put("fPrepareTime", generalFunc.getJsonValueStr("fPrepareTime", restaurant_Obj));

                                        String Restaurant_PricePerPerson = generalFunc.getJsonValueStr("Restaurant_PricePerPerson", restaurant_Obj);
                                        map.put("Restaurant_PricePerPerson", Restaurant_PricePerPerson);
                                        map.put("Restaurant_PricePerPersonConverted", generalFunc.convertNumberWithRTL(Restaurant_PricePerPerson));

                                        String Restaurant_OrderPrepareTime = generalFunc.getJsonValueStr("Restaurant_OrderPrepareTime", restaurant_Obj);
                                        map.put("Restaurant_OrderPrepareTime", Restaurant_OrderPrepareTime);
                                        map.put("Restaurant_OrderPrepareTimeConverted", generalFunc.convertNumberWithRTL(Restaurant_OrderPrepareTime));

                                        map.put("Restaurant_Status", generalFunc.getJsonValueStr("restaurantstatus", restaurant_Obj));
                                        map.put("Restaurant_Safety_Status", generalFunc.getJsonValueStr("Restaurant_Safety_Status", restaurant_Obj));
                                        map.put("Restaurant_Safety_Icon", generalFunc.getJsonValueStr("Restaurant_Safety_Icon", restaurant_Obj));
                                        map.put("Restaurant_Safety_URL", generalFunc.getJsonValueStr("Restaurant_Safety_URL", restaurant_Obj));

                                        String Restaurant_Opentime = generalFunc.getJsonValueStr("Restaurant_Opentime", restaurant_Obj);
                                        map.put("Restaurant_Opentime", Restaurant_Opentime);
                                        map.put("Restaurant_OpentimeConverted", generalFunc.convertNumberWithRTL(Restaurant_Opentime));

                                        map.put("Restaurant_Closetime", generalFunc.getJsonValueStr("Restaurant_Closetime", restaurant_Obj));
                                        map.put("Restaurant_OfferMessage", generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj));

                                        // String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage_short", restaurant_Obj);
                                        String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj);
                                        map.put("Restaurant_OfferMessage_short", Restaurant_OfferMessage_short);
                                        map.put("Restaurant_OfferMessage_shortConverted", generalFunc.convertNumberWithRTL(Restaurant_OfferMessage_short));

                                        String vAvgRating = generalFunc.getJsonValueStr("vAvgRating", restaurant_Obj);
                                        map.put("vAvgRating", vAvgRating);
                                        map.put("vAvgRatingConverted", generalFunc.convertNumberWithRTL(vAvgRating));

                                        String Restaurant_MinOrderValue = generalFunc.getJsonValueStr("Restaurant_MinOrderValue_Orig", restaurant_Obj);
                                        map.put("Restaurant_MinOrderValue", Restaurant_MinOrderValue);
                                        map.put("Restaurant_MinOrderValueConverted", generalFunc.convertNumberWithRTL(Restaurant_MinOrderValue));

                                        map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                                        map.put("timeslotavailable", generalFunc.getJsonValueStr("timeslotavailable", restaurant_Obj));
                                        map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));
                                        map.put("eFavStore", generalFunc.getJsonValueStr("eFavStore", restaurant_Obj));

                                        map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                                        map.put("LBL_CLOSED_TXT", LBL_CLOSED_TXT);
                                        map.put("LBL_NOT_ACCEPT_ORDERS_TXT", LBL_NOT_ACCEPT_ORDERS_TXT);

                                        childCategoryList.add(map);
                                    }

                                    mainMap.put("ChildData", childCategoryList.toString());
                                    categoryModel.setList(childCategoryList);


                                    categoryModel.setViewType(MainCategoryModel.ViewType.Data);
                                    try {
                                        categoryModels.add((MainCategoryModel) categoryModel.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //categoryModels.add(categoryModel);

                                mainCategoryList.add(i, mainMap);

                                //  categoryrestaurantArr_List.add(i, childCategoryList.toString());

                                main_category_Rec.getRecycledViewPool().clear();
                                mainCategoryAdapter.notifyDataSetChanged();

                                dataListRecyclerView.setVisibility(View.GONE);
                                categoryArea.setVisibility(View.VISIBLE);
                                storeCounterArea.setVisibility(View.GONE);
                                noloactionview.setVisibility(View.GONE);
                                searchArea.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                    }

                    categoryArea.setVisibility(View.GONE);
                    dataListRecyclerView.setVisibility(View.VISIBLE);
                    storeCounterArea.setVisibility(View.VISIBLE);

                    if (restaurant_Arr != null && restaurant_Arr.length() > 0) {
                        String LBL_OPEN_NOW = "", LBL_CLOSED_TXT = "", LBL_NOT_ACCEPT_ORDERS_TXT = "";
                        int dimension = 0;
                        if (restaurant_Arr.length() > 0) {
                            dimension = getActContext().getResources().getDimensionPixelSize(R.dimen.restaurant_img_size_home_screen);
                            LBL_OPEN_NOW = generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                            LBL_CLOSED_TXT = generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                            LBL_NOT_ACCEPT_ORDERS_TXT = generalFunc.retrieveLangLBl("close", "LBL_NOT_ACCEPT_ORDERS_TXT");
                        }

                        for (int i = 0; i < restaurant_Arr.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject restaurant_Obj = generalFunc.getJsonObject(restaurant_Arr, i);
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                            map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                            map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                            map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                            String vImage = generalFunc.getJsonValueStr("vImage", restaurant_Obj);
                            map.put("vImage", Utils.getResizeImgURL(getActContext(), vImage, dimension, dimension));
                            map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", restaurant_Obj));
                            map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", restaurant_Obj));
                            map.put("vFromTimeSlot1", generalFunc.getJsonValueStr("vFromTimeSlot1", restaurant_Obj));
                            map.put("vToTimeSlot1", generalFunc.getJsonValueStr("vToTimeSlot1", restaurant_Obj));
                            map.put("vFromTimeSlot2", generalFunc.getJsonValueStr("vFromTimeSlot2", restaurant_Obj));
                            map.put("vToTimeSlot2", generalFunc.getJsonValueStr("vToTimeSlot2", restaurant_Obj));
                            map.put("fMinOrderValue", generalFunc.getJsonValueStr("fMinOrderValue", restaurant_Obj));
                            map.put("Restaurant_Cuisine", generalFunc.getJsonValueStr("Restaurant_Cuisine", restaurant_Obj));
                            map.put("fPrepareTime", generalFunc.getJsonValueStr("fPrepareTime", restaurant_Obj));

                            String Restaurant_PricePerPerson = generalFunc.getJsonValueStr("Restaurant_PricePerPerson", restaurant_Obj);
                            map.put("Restaurant_PricePerPerson", Restaurant_PricePerPerson);
                            map.put("Restaurant_PricePerPersonConverted", generalFunc.convertNumberWithRTL(Restaurant_PricePerPerson));

                            String Restaurant_OrderPrepareTime = generalFunc.getJsonValueStr("Restaurant_OrderPrepareTime", restaurant_Obj);
                            map.put("Restaurant_OrderPrepareTime", Restaurant_OrderPrepareTime);
                            map.put("Restaurant_OrderPrepareTimeConverted", generalFunc.convertNumberWithRTL(Restaurant_OrderPrepareTime));

                            map.put("Restaurant_Status", generalFunc.getJsonValueStr("restaurantstatus", restaurant_Obj));
                            map.put("Restaurant_Safety_Status", generalFunc.getJsonValueStr("Restaurant_Safety_Status", restaurant_Obj));
                            map.put("Restaurant_Safety_Icon", generalFunc.getJsonValueStr("Restaurant_Safety_Icon", restaurant_Obj));
                            map.put("Restaurant_Safety_URL", generalFunc.getJsonValueStr("Restaurant_Safety_URL", restaurant_Obj));

                            String Restaurant_Opentime = generalFunc.getJsonValueStr("Restaurant_Opentime", restaurant_Obj);
                            map.put("Restaurant_Opentime", Restaurant_Opentime);
                            map.put("Restaurant_OpentimeConverted", generalFunc.convertNumberWithRTL(Restaurant_Opentime));

                            map.put("Restaurant_Closetime", generalFunc.getJsonValueStr("Restaurant_Closetime", restaurant_Obj));
                            map.put("Restaurant_OfferMessage", generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj));

                            // String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage_short", restaurant_Obj);
                            String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj);
                            map.put("Restaurant_OfferMessage_short", Restaurant_OfferMessage_short);
                            map.put("Restaurant_OfferMessage_shortConverted", generalFunc.convertNumberWithRTL(Restaurant_OfferMessage_short));


                            String vAvgRating = generalFunc.getJsonValueStr("vAvgRating", restaurant_Obj);
                            map.put("vAvgRating", vAvgRating);
                            map.put("vAvgRatingConverted", generalFunc.convertNumberWithRTL(vAvgRating));

                            String Restaurant_MinOrderValue = generalFunc.getJsonValueStr("Restaurant_MinOrderValue_Orig", restaurant_Obj);
                            map.put("Restaurant_MinOrderValue", Restaurant_MinOrderValue);
                            map.put("Restaurant_MinOrderValueConverted", generalFunc.convertNumberWithRTL(Restaurant_MinOrderValue));

                            map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                            map.put("timeslotavailable", generalFunc.getJsonValueStr("timeslotavailable", restaurant_Obj));
                            map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));
                            map.put("eFavStore", generalFunc.getJsonValueStr("eFavStore", restaurant_Obj));

                            map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                            map.put("LBL_CLOSED_TXT", LBL_CLOSED_TXT);
                            map.put("LBL_NOT_ACCEPT_ORDERS_TXT", LBL_NOT_ACCEPT_ORDERS_TXT);

                            numberOfRestaurant += 1;
                            restaurantArr_List.add(map);
                        }
                    }

                    generalFunc.storeData("PassengerLat", latitude + "");
                    generalFunc.storeData("PassengerLon", longitude + "");
                    //dataListRecyclerView.getRecycledViewPool().clear();

                    restaurantAdapter.notifyDataSetChanged();

                   /* if (restaurant_Arr != null && restaurant_Arr.length() > 0) {
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                    } else {
                        numberOfRestaurant = 0;
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                    }
*/
                    noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(totalStore + "") + " " + LBL_RESTAURANTS);

                    if (restaurantArr_List.size() > 0) {
                        noloactionview.setVisibility(View.GONE);
                        searchArea.setVisibility(View.VISIBLE);
                    }

                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }
                    mIsLoading = false;

                } else {
                    new ManageScroll(collapsing_toolbar).scroll(false);
                    String msg_str = generalFunc.getJsonValue("message", responseString);

                    if (!isLoadMore && msg_str.equalsIgnoreCase("LBL_NO_RESTAURANT_FOUND_TXT")) {
                        restaurantArr_List.clear();
                        categoryModels.clear();
                    }

                    NoDataTxt.setVisibility(View.GONE);
                    if (restaurantArr_List.size() == 0) {

                        if (!generalFunc.getJsonValue("message1", responseString).equalsIgnoreCase("")) {
                            // dataListRecyclerView.setVisibility(View.GONE);

                            NoDataTxt.setVisibility(View.VISIBLE);
                            NoDataTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue("message1", responseString)));

                        } else if (filterImage.getVisibility() == View.GONE) {
                            noloactionview.setVisibility(View.VISIBLE);
                            MainArea.setVisibility(View.GONE);
                        }

                        numberOfRestaurant = 0;
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                        restaurantArr_List.clear();
                        categoryModels.clear();


                    }
                    //dataListRecyclerView.getRecycledViewPool().clear();
                    restaurantAdapter.notifyDataSetChanged();

                }

            } else {
                generalFunc.showError();
            }
        });

    }

    private void manageData(String responseString, boolean isLoadMore) {

        new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            generateErrorView();
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            DataLoadingArea.setVisibility(View.GONE);
            noOfServiceTxt.setVisibility(View.VISIBLE);
            if (responseObj != null && !responseObj.equals("")) {

                //it's use for deliverAll only
                //if items added in cart then login first time it's redirect to edit cart screen
                if (ServiceModule.isDeliverAllOnly() && !generalFunc.getMemberId().equalsIgnoreCase("") && getCartData() != null &&
                        getCartData().size() > 0 && !generalFunc.retrieveValue("IS_DELIVERALL_LOGIN").equalsIgnoreCase("Yes")) {
                    generalFunc.storeData("IS_DELIVERALL_LOGIN", "Yes");
                    new ActUtils(getActContext()).startAct(EditCartActivity.class);
                }

                backImgView.setVisibility(View.VISIBLE);
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                DataArea.setVisibility(View.VISIBLE);
                String nextPage = generalFunc.getJsonValueStr("NextPage", responseObj);
                String totalStore = generalFunc.getJsonValueStr("totalStore", responseObj);
                MainArea.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                storeCounterArea.setVisibility(View.VISIBLE);
                //add banner handling
                bannerListArray = generalFunc.getJsonArray("banner_data", responseString);
                mBannerAdapter.updateData(bannerListArray);

                if (isDataAvail) {
                    new ManageScroll(collapsing_toolbar).scroll(true);
                    NoDataTxt.setVisibility(View.GONE);
                    if (!isLoadMore) {
                        restaurantArr_List.clear();
                        restaurantAdapter.notifyDataSetChanged();
                        categoryModels.clear();
                        mainCategoryAdapter.notifyDataSetChanged();
                        numberOfRestaurant = 0;
                    }


                    //handling cuisine
                    if (isFilter) {
                        isFilter = false;
                        String getCuisineListData = generalFunc.getJsonValue("getCuisineList", responseString);
                        isOfferApply = generalFunc.getJsonValue("isOfferApply", getCuisineListData);
                        JSONArray cuisineArray = generalFunc.getJsonArray("CuisineList", getCuisineListData);
                        filterList.clear();
                        if (cuisineArray != null) {

                            for (int i = 0; i < cuisineArray.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                JSONObject filter_Obj = generalFunc.getJsonObject(cuisineArray, i);
                                map.put("cuisineId", generalFunc.getJsonValueStr("cuisineId", filter_Obj));
                                map.put("cuisineName", generalFunc.getJsonValueStr("cuisineName", filter_Obj));
                                map.put("eStatus", generalFunc.getJsonValueStr("eStatus", filter_Obj));
                                map.put("vImage", generalFunc.getJsonValueStr("vImage", filter_Obj));
                                map.put("vBgColor", generalFunc.getJsonValueStr("vBgColor", filter_Obj));
                                map.put("vTextColor", generalFunc.getJsonValueStr("vTextColor", filter_Obj));
                                map.put("vBorderColor", generalFunc.getJsonValueStr("vBorderColor", filter_Obj));

                                if (i == 0) {
                                    map.put("isCheck", "Yes");
                                }

                                filterList.add(map);
                            }
                            // filterTxtView.setVisibility(View.VISIBLE);
                            cusineArea.setVisibility(View.VISIBLE);
                            cuisinesAdapter.notifyDataSetChanged();
                        } else {
                            cusineArea.setVisibility(View.GONE);
                            //filterTxtView.setVisibility(View.GONE);
                        }
                    }

                    JSONArray restaurant_Arr = generalFunc.getJsonArray("message", responseObj);

                    if (generalFunc.retrieveValue("ENABLE_CATEGORY_WISE_STORES").equalsIgnoreCase("Yes") && iCategoryId.equalsIgnoreCase("")) {
                        if (!isLoadMore) {
                            restaurantArr_List.clear();
                            restaurantAdapter.notifyDataSetChanged();
                            categoryModels.clear();
                            mainCategoryAdapter.notifyDataSetChanged();
                        }


                        JSONArray categorywisestore_Arr = generalFunc.getJsonArray("CategoryWiseStores", responseObj);
                        if (categorywisestore_Arr != null && categorywisestore_Arr.length() > 0) {
                            String LBL_OPEN_NOW = "", LBL_CLOSED_TXT = "", LBL_NOT_ACCEPT_ORDERS_TXT = "";
                            int dimension = 0;
                            dimension = getActContext().getResources().getDimensionPixelSize(R.dimen.restaurant_img_size_home_screen);
                            LBL_OPEN_NOW = generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                            LBL_CLOSED_TXT = generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                            LBL_NOT_ACCEPT_ORDERS_TXT = generalFunc.retrieveLangLBl("close", "LBL_NOT_ACCEPT_ORDERS_TXT");
                            ArrayList<HashMap<String, String>> mainCategoryList = new ArrayList<>();

                            MainCategoryModel categoryModel;
                            for (int i = 0; i < categorywisestore_Arr.length(); i++) {
                                categoryModel = new MainCategoryModel();

                                HashMap<String, String> mainMap = new HashMap<String, String>();
                                JSONObject cateogry_Obj = generalFunc.getJsonObject(categorywisestore_Arr, i);
                                mainMap.put("vTitle", generalFunc.getJsonValueStr("vTitle", cateogry_Obj));
                                mainMap.put("vDescription", generalFunc.getJsonValueStr("vDescription", cateogry_Obj));
                                mainMap.put("vCategoryImage", generalFunc.getJsonValueStr("vCategoryImage", cateogry_Obj));
                                mainMap.put("iDisplayOrder", generalFunc.getJsonValueStr("iDisplayOrder", cateogry_Obj));

                                categoryModel.setvTitle(generalFunc.getJsonValueStr("vTitle", cateogry_Obj));
                                categoryModel.setIsShowall(generalFunc.getJsonValueStr("IS_SHOW_ALL", cateogry_Obj));
                                categoryModel.seteType(generalFunc.getJsonValueStr("eType", cateogry_Obj));
                                categoryModel.setiCategoryId(generalFunc.getJsonValueStr("iCategoryId", cateogry_Obj));
                                categoryModel.setvDescription(generalFunc.getJsonValueStr("vDescription", cateogry_Obj));
                                categoryModel.setvCategoryImage(generalFunc.getJsonValueStr("vCategoryImage", cateogry_Obj));
                                categoryModel.setiDisplayOrder(generalFunc.getJsonValueStr("iDisplayOrder", cateogry_Obj));

                                categoryModel.setViewType(MainCategoryModel.ViewType.Header);

                                JSONArray subData_Arr = generalFunc.getJsonArray("subData", cateogry_Obj);
                                ArrayList<HashMap<String, String>> childCategoryList = new ArrayList<>();
                                if (subData_Arr != null && subData_Arr.length() > 0) {

                                    try {
                                        categoryModels.add((MainCategoryModel) categoryModel.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }

                                    for (int j = 0; j < subData_Arr.length(); j++) {

                                        HashMap<String, String> map = new HashMap<String, String>();
                                        JSONObject restaurant_Obj = generalFunc.getJsonObject(subData_Arr, j);
                                        map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                                        map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                                        map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                                        map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                                        String vImage = generalFunc.getJsonValueStr("vImage", restaurant_Obj);
                                        map.put("vImage", Utils.getResizeImgURL(getActContext(), vImage, dimension, dimension));
                                        map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", restaurant_Obj));
                                        map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", restaurant_Obj));
                                        map.put("vFromTimeSlot1", generalFunc.getJsonValueStr("vFromTimeSlot1", restaurant_Obj));
                                        map.put("vToTimeSlot1", generalFunc.getJsonValueStr("vToTimeSlot1", restaurant_Obj));
                                        map.put("vFromTimeSlot2", generalFunc.getJsonValueStr("vFromTimeSlot2", restaurant_Obj));
                                        map.put("vToTimeSlot2", generalFunc.getJsonValueStr("vToTimeSlot2", restaurant_Obj));
                                        map.put("fMinOrderValue", generalFunc.getJsonValueStr("fMinOrderValue", restaurant_Obj));
                                        map.put("Restaurant_Cuisine", generalFunc.getJsonValueStr("Restaurant_Cuisine", restaurant_Obj));
                                        map.put("fPrepareTime", generalFunc.getJsonValueStr("fPrepareTime", restaurant_Obj));

                                        String Restaurant_PricePerPerson = generalFunc.getJsonValueStr("Restaurant_PricePerPerson", restaurant_Obj);
                                        map.put("Restaurant_PricePerPerson", Restaurant_PricePerPerson);
                                        map.put("Restaurant_PricePerPersonConverted", generalFunc.convertNumberWithRTL(Restaurant_PricePerPerson));

                                        String Restaurant_OrderPrepareTime = generalFunc.getJsonValueStr("Restaurant_OrderPrepareTime", restaurant_Obj);
                                        map.put("Restaurant_OrderPrepareTime", Restaurant_OrderPrepareTime);
                                        map.put("Restaurant_OrderPrepareTimeConverted", generalFunc.convertNumberWithRTL(Restaurant_OrderPrepareTime));

                                        map.put("Restaurant_Status", generalFunc.getJsonValueStr("restaurantstatus", restaurant_Obj));
                                        map.put("Restaurant_Safety_Status", generalFunc.getJsonValueStr("Restaurant_Safety_Status", restaurant_Obj));
                                        map.put("Restaurant_Safety_Icon", generalFunc.getJsonValueStr("Restaurant_Safety_Icon", restaurant_Obj));
                                        map.put("Restaurant_Safety_URL", generalFunc.getJsonValueStr("Restaurant_Safety_URL", restaurant_Obj));

                                        String Restaurant_Opentime = generalFunc.getJsonValueStr("Restaurant_Opentime", restaurant_Obj);
                                        map.put("Restaurant_Opentime", Restaurant_Opentime);
                                        map.put("Restaurant_OpentimeConverted", generalFunc.convertNumberWithRTL(Restaurant_Opentime));

                                        map.put("Restaurant_Closetime", generalFunc.getJsonValueStr("Restaurant_Closetime", restaurant_Obj));
                                        map.put("Restaurant_OfferMessage", generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj));

                                        // String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage_short", restaurant_Obj);
                                        String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj);
                                        map.put("Restaurant_OfferMessage_short", Restaurant_OfferMessage_short);
                                        map.put("Restaurant_OfferMessage_shortConverted", generalFunc.convertNumberWithRTL(Restaurant_OfferMessage_short));

                                        String vAvgRating = generalFunc.getJsonValueStr("vAvgRating", restaurant_Obj);
                                        map.put("vAvgRating", vAvgRating);
                                        map.put("vAvgRatingConverted", generalFunc.convertNumberWithRTL(vAvgRating));

                                        String Restaurant_MinOrderValue = generalFunc.getJsonValueStr("Restaurant_MinOrderValue_Orig", restaurant_Obj);
                                        map.put("Restaurant_MinOrderValue", Restaurant_MinOrderValue);
                                        map.put("Restaurant_MinOrderValueConverted", generalFunc.convertNumberWithRTL(Restaurant_MinOrderValue));

                                        map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                                        map.put("timeslotavailable", generalFunc.getJsonValueStr("timeslotavailable", restaurant_Obj));
                                        map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));
                                        map.put("eFavStore", generalFunc.getJsonValueStr("eFavStore", restaurant_Obj));

                                        map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                                        map.put("LBL_CLOSED_TXT", LBL_CLOSED_TXT);
                                        map.put("LBL_NOT_ACCEPT_ORDERS_TXT", LBL_NOT_ACCEPT_ORDERS_TXT);

                                        childCategoryList.add(map);
                                    }

                                    mainMap.put("ChildData", childCategoryList.toString());
                                    categoryModel.setList(childCategoryList);


                                    categoryModel.setViewType(MainCategoryModel.ViewType.Data);
                                    try {
                                        categoryModels.add((MainCategoryModel) categoryModel.clone());
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //categoryModels.add(categoryModel);

                                mainCategoryList.add(i, mainMap);

                                //  categoryrestaurantArr_List.add(i, childCategoryList.toString());

                                main_category_Rec.getRecycledViewPool().clear();
                                mainCategoryAdapter.notifyDataSetChanged();

                                dataListRecyclerView.setVisibility(View.GONE);
                                categoryArea.setVisibility(View.VISIBLE);
                                storeCounterArea.setVisibility(View.GONE);
                                noloactionview.setVisibility(View.GONE);
                                searchArea.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                    }

                    categoryArea.setVisibility(View.GONE);
                    dataListRecyclerView.setVisibility(View.VISIBLE);
                    storeCounterArea.setVisibility(View.VISIBLE);

                    if (restaurant_Arr != null && restaurant_Arr.length() > 0) {
                        String LBL_OPEN_NOW = "", LBL_CLOSED_TXT = "", LBL_NOT_ACCEPT_ORDERS_TXT = "";
                        int dimension = 0;
                        if (restaurant_Arr.length() > 0) {
                            dimension = getActContext().getResources().getDimensionPixelSize(R.dimen.restaurant_img_size_home_screen);
                            LBL_OPEN_NOW = generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                            LBL_CLOSED_TXT = generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                            LBL_NOT_ACCEPT_ORDERS_TXT = generalFunc.retrieveLangLBl("close", "LBL_NOT_ACCEPT_ORDERS_TXT");
                        }

                        for (int i = 0; i < restaurant_Arr.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject restaurant_Obj = generalFunc.getJsonObject(restaurant_Arr, i);
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                            map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                            map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                            map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                            String vImage = generalFunc.getJsonValueStr("vImage", restaurant_Obj);
                            map.put("vImage", Utils.getResizeImgURL(getActContext(), vImage, dimension, dimension));
                            map.put("vLatitude", generalFunc.getJsonValueStr("vLatitude", restaurant_Obj));
                            map.put("vLongitude", generalFunc.getJsonValueStr("vLongitude", restaurant_Obj));
                            map.put("vFromTimeSlot1", generalFunc.getJsonValueStr("vFromTimeSlot1", restaurant_Obj));
                            map.put("vToTimeSlot1", generalFunc.getJsonValueStr("vToTimeSlot1", restaurant_Obj));
                            map.put("vFromTimeSlot2", generalFunc.getJsonValueStr("vFromTimeSlot2", restaurant_Obj));
                            map.put("vToTimeSlot2", generalFunc.getJsonValueStr("vToTimeSlot2", restaurant_Obj));
                            map.put("fMinOrderValue", generalFunc.getJsonValueStr("fMinOrderValue", restaurant_Obj));
                            map.put("Restaurant_Cuisine", generalFunc.getJsonValueStr("Restaurant_Cuisine", restaurant_Obj));
                            map.put("fPrepareTime", generalFunc.getJsonValueStr("fPrepareTime", restaurant_Obj));

                            String Restaurant_PricePerPerson = generalFunc.getJsonValueStr("Restaurant_PricePerPerson", restaurant_Obj);
                            map.put("Restaurant_PricePerPerson", Restaurant_PricePerPerson);
                            map.put("Restaurant_PricePerPersonConverted", generalFunc.convertNumberWithRTL(Restaurant_PricePerPerson));

                            String Restaurant_OrderPrepareTime = generalFunc.getJsonValueStr("Restaurant_OrderPrepareTime", restaurant_Obj);
                            map.put("Restaurant_OrderPrepareTime", Restaurant_OrderPrepareTime);
                            map.put("Restaurant_OrderPrepareTimeConverted", generalFunc.convertNumberWithRTL(Restaurant_OrderPrepareTime));

                            map.put("Restaurant_Status", generalFunc.getJsonValueStr("restaurantstatus", restaurant_Obj));
                            map.put("Restaurant_Safety_Status", generalFunc.getJsonValueStr("Restaurant_Safety_Status", restaurant_Obj));
                            map.put("Restaurant_Safety_Icon", generalFunc.getJsonValueStr("Restaurant_Safety_Icon", restaurant_Obj));
                            map.put("Restaurant_Safety_URL", generalFunc.getJsonValueStr("Restaurant_Safety_URL", restaurant_Obj));

                            String Restaurant_Opentime = generalFunc.getJsonValueStr("Restaurant_Opentime", restaurant_Obj);
                            map.put("Restaurant_Opentime", Restaurant_Opentime);
                            map.put("Restaurant_OpentimeConverted", generalFunc.convertNumberWithRTL(Restaurant_Opentime));

                            map.put("Restaurant_Closetime", generalFunc.getJsonValueStr("Restaurant_Closetime", restaurant_Obj));
                            map.put("Restaurant_OfferMessage", generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj));

                            // String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage_short", restaurant_Obj);
                            String Restaurant_OfferMessage_short = generalFunc.getJsonValueStr("Restaurant_OfferMessage", restaurant_Obj);
                            map.put("Restaurant_OfferMessage_short", Restaurant_OfferMessage_short);
                            map.put("Restaurant_OfferMessage_shortConverted", generalFunc.convertNumberWithRTL(Restaurant_OfferMessage_short));


                            String vAvgRating = generalFunc.getJsonValueStr("vAvgRating", restaurant_Obj);
                            map.put("vAvgRating", vAvgRating);
                            map.put("vAvgRatingConverted", generalFunc.convertNumberWithRTL(vAvgRating));

                            String Restaurant_MinOrderValue = generalFunc.getJsonValueStr("Restaurant_MinOrderValue_Orig", restaurant_Obj);
                            map.put("Restaurant_MinOrderValue", Restaurant_MinOrderValue);
                            map.put("Restaurant_MinOrderValueConverted", generalFunc.convertNumberWithRTL(Restaurant_MinOrderValue));

                            map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                            map.put("timeslotavailable", generalFunc.getJsonValueStr("timeslotavailable", restaurant_Obj));
                            map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));
                            map.put("eFavStore", generalFunc.getJsonValueStr("eFavStore", restaurant_Obj));

                            map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                            map.put("LBL_CLOSED_TXT", LBL_CLOSED_TXT);
                            map.put("LBL_NOT_ACCEPT_ORDERS_TXT", LBL_NOT_ACCEPT_ORDERS_TXT);

                            numberOfRestaurant += 1;
                            restaurantArr_List.add(map);
                        }
                    }

                    generalFunc.storeData("PassengerLat", latitude + "");
                    generalFunc.storeData("PassengerLon", longitude + "");
                    //dataListRecyclerView.getRecycledViewPool().clear();

                    restaurantAdapter.notifyDataSetChanged();

                   /* if (restaurant_Arr != null && restaurant_Arr.length() > 0) {
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                    } else {
                        numberOfRestaurant = 0;
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                    }
*/
                    noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(totalStore + "") + " " + LBL_RESTAURANTS);

                    if (restaurantArr_List.size() > 0) {
                        noloactionview.setVisibility(View.GONE);
                        searchArea.setVisibility(View.VISIBLE);
                    }

                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }
                    mIsLoading = false;

                } else {
                    new ManageScroll(collapsing_toolbar).scroll(false);
                    String msg_str = generalFunc.getJsonValue("message", responseString);

                    if (!isLoadMore && msg_str.equalsIgnoreCase("LBL_NO_RESTAURANT_FOUND_TXT")) {
                        restaurantArr_List.clear();
                        categoryModels.clear();
                    }

                    NoDataTxt.setVisibility(View.GONE);
                    if (restaurantArr_List.size() == 0) {

                        if (!generalFunc.getJsonValue("message1", responseString).equalsIgnoreCase("")) {
                            // dataListRecyclerView.setVisibility(View.GONE);

                            NoDataTxt.setVisibility(View.VISIBLE);
                            NoDataTxt.setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue("message1", responseString)));

                        } else if (filterImage.getVisibility() == View.GONE) {
                            noloactionview.setVisibility(View.VISIBLE);
                            MainArea.setVisibility(View.GONE);
                        }

                        numberOfRestaurant = 0;
                        noOfServiceTxt.setText(generalFunc.convertNumberWithRTL(numberOfRestaurant + "") + " " + LBL_RESTAURANTS);
                        restaurantArr_List.clear();
                        categoryModels.clear();


                    }
                    //dataListRecyclerView.getRecycledViewPool().clear();
                    restaurantAdapter.notifyDataSetChanged();

                }

            }

        }, 50);

    }


    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        restaurantAdapter.removeFooterView();
    }

    public RealmResults<Cart> getCartData() {
        try {
            Realm realm = MyApp.getRealmInstance();
            return realm.where(Cart.class).findAll();
        } catch (Exception e) {

        }
        return null;
    }

    boolean isProfilefrg = false;
    boolean isWalletfrg = false;
    boolean isBookingfrg = false;

    @Override
    protected void onResume() {
        super.onResume();
        try {

            if (myProfileFragment != null && isProfilefrg) {
                myProfileFragment.onResume();
            }

            if (myWalletFragment != null && isWalletfrg) {
                myWalletFragment.onResume();
            }

            if (myBookingFragment != null && isBookingfrg) {
                myBookingFragment.onResume();
            }

            app_bar_layout_home.addOnOffsetChangedListener(this);

            realmCartList = getCartData();

            if (realmCartList != null && realmCartList.size() > 0 && !isProfilefrg && !isWalletfrg && !isBookingfrg) {
                fabcartIcon.setVisibility(View.VISIBLE);
            } else {
                fabcartIcon.setVisibility(View.GONE);
            }
            if (!isUfxaddress) {
                NoLocationView();
            }

            if (generalFunc.retrieveValue(Utils.ISWALLETBALNCECHANGE).equalsIgnoreCase("Yes")) {
                getWalletBalDetails();
            } else {
                obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
            }

            if (fabcartIcon != null && fabcartIcon.getVisibility() == View.VISIBLE) {
                if (realmCartList != null) {
                    int cnt = 0;
                    for (int i = 0; i < realmCartList.size(); i++) {
                        cnt = cnt + GeneralFunctions.parseIntegerValue(0, realmCartList.get(i).getQty());
                    }
                    fabcartIcon.setCount(cnt);
                }

            }
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        app_bar_layout_home.removeOnOffsetChangedListener(this);
    }


    public void getWalletBalDetails() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);
        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    try {
                        generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "No");
                        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJson);
                        object.put("user_available_balance", generalFunc.getJsonValue("MemberBalance", responseString));
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, object.toString());

                        userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        obj_userProfile = generalFunc.getJsonObject(userProfileJson);
                    } catch (Exception e) {

                    }

                }
            }
        });

    }

    @Override
    public void onAddressFound(String address, double latitude, double longitude, String geocodeobject) {

        if (address != null && !address.equals("")) {
            this.latitude = latitude + "";
            this.longitude = longitude + "";
            headerAddressTxt.setText(address);

            HashMap<String, String> storeData = new HashMap<>();
            storeData.put(Utils.SELECT_ADDRESSS, address);
            storeData.put(Utils.SELECT_LATITUDE, latitude + "");
            storeData.put(Utils.SELECT_LONGITUDE, longitude + "");

            storeData.put(Utils.CURRENT_ADDRESSS, address);
            storeData.put(Utils.CURRENT_LATITUDE, latitude + "");
            storeData.put(Utils.CURRENT_LONGITUDE, longitude + "");
            generalFunc.storeData(storeData);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
            if (myProfileFragment != null) {
                myProfileFragment.onActivityResult(requestCode, resultCode, data);
            }

        } else if (requestCode == Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            headerAddressTxt.setText(data.getStringExtra("Address"));
            this.latitude = data.getStringExtra("Latitude") == null ? "0.0" : data.getStringExtra("Latitude");
            this.longitude = data.getStringExtra("Longitude") == null ? "0.0" : data.getStringExtra("Longitude");

            if (!this.latitude.equalsIgnoreCase("0.0") && !this.longitude.equalsIgnoreCase("0.0")) {
                isUfxaddress = true;
                HashMap<String, String> storeData = new HashMap<>();
                storeData.put(Utils.SELECT_ADDRESSS, data.getStringExtra("Address"));
                storeData.put(Utils.SELECT_LATITUDE, latitude + "");
                storeData.put(Utils.SELECT_LONGITUDE, longitude + "");


                storeData.put(Utils.CURRENT_ADDRESSS, data.getStringExtra("Address"));
                storeData.put(Utils.CURRENT_LATITUDE, latitude + "");
                storeData.put(Utils.CURRENT_LONGITUDE, longitude + "");

                generalFunc.storeData(storeData);

                filterLocation = new Location("filter");
                filterLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, latitude));
                filterLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, longitude));

                setNewListData(GeneralFunctions.parseDoubleValue(0.0, latitude), GeneralFunctions.parseDoubleValue(0.0, longitude));
                // getFilterList(GeneralFunctions.parseDoubleValue(0.0, latitude), GeneralFunctions.parseDoubleValue(0.0, longitude));
                isFilter = true;

                if (currentLocation != null) {
                    double distance = GeneralFunctions.calculationByLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), GeneralFunctions.parseDoubleValue(0.0, latitude), GeneralFunctions.parseDoubleValue(0.0, longitude), "KM");
                    if (distance > 10) {
                        //comment for demo mode
                        //locationPopup.setVisibility(View.VISIBLE);

                        restaurantAdapter.removeFooterView();
                    } else {
                        locationPopup.setVisibility(View.GONE);

                    }
                }
            }


        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
            obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));

        } else if (requestCode == Utils.REQUEST_CODE_GPS_ON) {

            isgpsview = true;

            if (generalFunc.isLocationEnabled()) {

                if (getLastLocation != null) {
                    getLastLocation.stopLocationUpdates();
                    getLastLocation = null;
                }
                getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, false, this);

                if (getLastLocation != null) {

                    final Handler handler = new Handler();
                    int delay = 1000; //milliseconds

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            isgpsview = true;
                            //do something
                            if (getLastLocation.getLastLocation() != null) {
                                isgpsview = false;
                                NoLocationView();
                            } else {
                                handler.postDelayed(this, 1000);
                            }
                        }
                    }, delay);
                }
            } else {
                isgpsview = false;
            }

        } else if (requestCode == Utils.SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {

//            mainAct.configPickUpDrag(true, false, false);
            if (resultCode == RESULT_OK) {
                try {

                    isUfxaddress = true;
                    noloactionview.setVisibility(View.GONE);

                    Place place = PlaceAutocomplete.getPlace(getActContext(), data);


                    LatLng placeLocation = place.getLatLng();

                    headerAddressTxt.setText(place.getAddress().toString());
                    this.latitude = placeLocation.latitude + "";
                    this.longitude = placeLocation.longitude + "";

                    HashMap<String, String> storeData = new HashMap<>();
                    storeData.put(Utils.SELECT_ADDRESSS, place.getAddress().toString());
                    storeData.put(Utils.SELECT_LATITUDE, placeLocation.latitude + "");
                    storeData.put(Utils.SELECT_LONGITUDE, placeLocation.longitude + "");


                    storeData.put(Utils.CURRENT_ADDRESSS, place.getAddress().toString());
                    storeData.put(Utils.CURRENT_LATITUDE, latitude + "");
                    storeData.put(Utils.CURRENT_LONGITUDE, longitude + "");

                    generalFunc.storeData(storeData);

                    if (currentLocation != null) {
                        double distance = GeneralFunctions.calculationByLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), placeLocation.latitude, placeLocation.longitude, "KM");
                        if (distance > 10) {
                            //comment for demo mode
                            locationPopup.setVisibility(View.VISIBLE);
                            restaurantAdapter.removeFooterView();
                        } else {
                            locationPopup.setVisibility(View.GONE);

                        }
                    }

                    filterLocation = new Location("filter");
                    filterLocation.setLatitude(placeLocation.latitude);
                    filterLocation.setLongitude(placeLocation.longitude);
                    getRestaurantList(placeLocation.latitude, placeLocation.longitude, false);
                    // getFilterList(placeLocation.latitude, placeLocation.longitude);
                    isFilter = true;
                } catch (Exception e) {
                    Logger.d("ExceptionFoodHome", "::" + e.toString());
                }
            }
        } else if (requestCode == 111 && resultCode == RESULT_OK) {
            if (filterLocation != null) {
                getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), false);
            }

        } else if (resultCode == RESULT_OK && requestCode == SEL_CARD) {

            if (myWalletFragment != null) {
                myWalletFragment.onActivityResult(requestCode, resultCode, data);
            }


        } else if (resultCode == RESULT_OK && requestCode == TRANSFER_MONEY) {
            if (myWalletFragment != null) {
                myWalletFragment.onActivityResult(requestCode, resultCode, data);
            }


        }


    }

    private void setNewListData(double mLatitude, double mLongitude) {
        DataArea.setVisibility(View.GONE);
        DataLoadingArea.setVisibility(View.VISIBLE);
        storeCounterArea.setVisibility(View.GONE);
        getRestaurantList(mLatitude, mLongitude, false);
    }

    public void pubNubMsgArrived(final String message) {

        runOnUiThread(() -> {

            String msgType = generalFunc.getJsonValue("MsgType", message);
            String iDriverId = generalFunc.getJsonValue("iDriverId", message);

            if (msgType.equals("DriverArrived")) {
                if (!isArrivedPopup) {
                    isArrivedPopup = true;
                }
            } else {
                onGcmMessageArrived(message);
            }
        });
    }

    public void buildMessage(String message, String positiveBtn) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> generateAlert.closeAlertBox());
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(positiveBtn);
        generateAlert.showAlertBox();
    }

    public void onGcmMessageArrived(String message) {

        String driverMsg = generalFunc.getJsonValue("Message", message);

        if (driverMsg.equals("TripEnd")) {

// show alert - - not cancabable - ok button only - on click OK finish screen

            if (!isEndpopup) {
                isEndpopup = true;
                // Utils.generateNotification(getActContext(), generalFunc.retrieveLangLBl("", "LBL_END_TRIP_DIALOG_TXT"));
                //   buildMessage(generalFunc.retrieveLangLBl("", "LBL_END_TRIP_DIALOG_TXT"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
            }

        } else if (driverMsg.equals("TripStarted")) {
            if (!isstartPopup) {
                isstartPopup = true;
                // Utils.generateNotification(getActContext(), generalFunc.retrieveLangLBl("", "LBL_START_TRIP_DIALOG_TXT"));
                // buildMessage(generalFunc.retrieveLangLBl("", "LBL_START_TRIP_DIALOG_TXT"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
            }

        } else if (driverMsg.equals("TripCancelledByDriver") || driverMsg.equalsIgnoreCase("TripCancelled")) {

            if (!isCancelpopup) {
                isCancelpopup = true;
                String reason = generalFunc.getJsonValue("Reason", message);
                //  Utils.generateNotification(getActContext(), generalFunc.retrieveLangLBl("", "LBL_PREFIX_TRIP_CANCEL_DRIVER") + " " + reason);

                // buildMessage((generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_PREFIX_TRIP_CANCEL_DRIVER") )+ " " + reason), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
        }
        releaseResources();
    }


    @Override
    public void onBackPressed() {

        if (!iCategoryId.equalsIgnoreCase("")) {
            iCategoryId = "";
            isOfferCheck = "No";
            isFavCheck = "No";
            storeCounterArea.setVisibility(View.GONE);
            backImgView.setVisibility(View.GONE);
            DataLoadingArea.setVisibility(View.VISIBLE);
            rvBanner.setVisibility(View.VISIBLE);
            main_category_Rec.scrollToPosition(0);
            getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), false);
            return;
        }
        generalFunc.storeData("DEFAULT_SERVICE_CATEGORY_ID", "");

        super.onBackPressed();
    }

    public void releaseResources() {
        try {
            getAddressFromLocation.setAddressList(null);
            getAddressFromLocation = null;
        } catch (Exception e) {

        }
    }

    boolean isFirst = true;

    @Override
    public void onLocationUpdate(Location mLastLocation) {


        latitude = mLastLocation.getLatitude() + "";
        longitude = mLastLocation.getLongitude() + "";

        if (getIntent().getStringExtra("latitude") != null && !getIntent().getStringExtra("latitude").equalsIgnoreCase("")) {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            address = getIntent().getStringExtra("address");
            onAddressFound(address, GeneralFunctions.parseDoubleValue(0, latitude), GeneralFunctions.parseDoubleValue(0, longitude), "");

            Location tempLoc = new Location("source");
            tempLoc.setLatitude(GeneralFunctions.parseDoubleValue(0, latitude));
            tempLoc.setLongitude(GeneralFunctions.parseDoubleValue(0, longitude));
            currentLocation = tempLoc;
            filterLocation = currentLocation;

            //  getFilterList(currentLocation.getLatitude(), currentLocation.getLongitude());

            if (generalFunc.retrieveValue("SubcategoryForAllDeliver") != null) {
                String responseString = generalFunc.retrieveValue("SubcategoryForAllDeliver");

                JSONArray messageArray = generalFunc.getJsonArray(Utils.message_str, responseString);
                if (messageArray != null) {

                    DataLoadingArea.setVisibility(View.VISIBLE);
                    iServiceCategoryIdMatch = false;
                    for (int i = 0; i < messageArray.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(messageArray, i);
                        String iServiceId = generalFunc.getJsonValueStr("iServiceId", obj_temp);

                        if (iServiceId.equalsIgnoreCase(generalFunc.retrieveValue("DEFAULT_SERVICE_CATEGORY_ID"))) {
                            manageData(obj_temp.toString(), false);

                            iServiceCategoryIdMatch = true;
                            break;

                        }
                    }
                }
            }

            if (!iServiceCategoryIdMatch) {
                getRestaurantList(currentLocation.getLatitude(), currentLocation.getLongitude(), false);
            }
            isFilter = true;
            isFirst = false;
            return;

        }

        if (mLastLocation != null && isFirst) {
            currentLocation = mLastLocation;
            filterLocation = currentLocation;
            getRestaurantList(mLastLocation.getLatitude(), mLastLocation.getLongitude(), false);
            //(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            isFirst = false;
        }


        getAddressFromLocation = new GetAddressFromLocation(getActContext(), generalFunc);
        getAddressFromLocation.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        getAddressFromLocation.setAddressList(this);
        getAddressFromLocation.execute();
    }

    @Override
    public void setOnRestaurantclick(int position) {

        if (internetConnection.isNetworkConnected()) {

            HashMap<String, String> posData = restaurantArr_List.get(position);
            Bundle bn = new Bundle();
            bn.putString("iCompanyId", posData.get("iCompanyId"));
            bn.putString("Restaurant_Status", posData.get("Restaurant_Status"));
            bn.putString("Restaurant_Safety_Status", posData.get("Restaurant_Safety_Status"));
            bn.putString("Restaurant_Safety_Icon", posData.get("Restaurant_Safety_Icon"));
            bn.putString("Restaurant_Safety_URL", posData.get("Restaurant_Safety_URL"));
            bn.putString("ispriceshow", posData.get("ispriceshow"));
            bn.putString("lat", latitude);
            bn.putString("long", longitude);
            bn.putString("eAvailable", posData.get("eAvailable"));
            bn.putString("timeslotavailable", posData.get("timeslotavailable"));
            new ActUtils(getActContext()).startActForResult(RestaurantAllDetailsNewActivity.class, bn, 111);
        } else {
            generalFunc.showMessage(backImgView, LBL_NO_INTERNET_TXT);


        }
    }

    @Override
    public void setOnRestaurantCategoryclick(int position, int mainPos) {
        if (internetConnection.isNetworkConnected()) {

            HashMap<String, String> posData = categoryModels.get(mainPos).getList().get(position);
            Bundle bn = new Bundle();
            bn.putString("iCompanyId", posData.get("iCompanyId"));
            bn.putString("Restaurant_Status", posData.get("Restaurant_Status"));
            bn.putString("Restaurant_Safety_Status", posData.get("Restaurant_Safety_Status"));
            bn.putString("Restaurant_Safety_Icon", posData.get("Restaurant_Safety_Icon"));
            bn.putString("Restaurant_Safety_URL", posData.get("Restaurant_Safety_URL"));
            bn.putString("ispriceshow", posData.get("ispriceshow"));
            bn.putString("lat", latitude);
            bn.putString("long", longitude);
            bn.putString("eAvailable", posData.get("eAvailable"));
            bn.putString("timeslotavailable", posData.get("timeslotavailable"));


            new ActUtils(getActContext()).startActForResult(RestaurantAllDetailsNewActivity.class, bn, 111);
        } else {
            generalFunc.showMessage(backImgView, LBL_NO_INTERNET_TXT);
        }

    }

    @Override
    public void setOnRestaurantclick(int position, boolean liked) {

    }

    int selFilterPos = -1;

    @Override
    public void setOnCuisinesClick(int position) {
        isNextPageAvailable = false;
        next_page_str = "";

        selectedFilterId = filterList.get(position).get("cuisineId");
        if (selFilterPos != position) {
            setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
        }

        HashMap<String, String> checkBox = filterList.get(position);
        checkBox.put("isCheck", "Yes");
        filterList.set(position, checkBox);

        if (selFilterPos != position && selFilterPos != -1) {
            HashMap<String, String> ChangecheckBox = filterList.get(selFilterPos);
            ChangecheckBox.put("isCheck", "No");
            filterList.set(selFilterPos, ChangecheckBox);
        }
        selFilterPos = position;

        if (position != 0) {
            HashMap<String, String> ChangecheckBox = filterList.get(0);
            ChangecheckBox.put("isCheck", "No");
            filterList.set(0, ChangecheckBox);
        }

        cuisinesAdapter.notifyDataSetChanged();
        cuisinesListRecyclerView.scrollToPosition(position);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        if (filterLocation != null) {
            setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
        }
    }

    @Override
    public void setOnCategoryClick(int position) {

        iCategoryId = categoryModels.get(position).getiCategoryId();
        rvBanner.setVisibility(View.GONE);
        setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
    }

    public class setOnClickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int i = view.getId();

            if (i == deliveryAreaTxt.getId()) {
                isgpsview = true;
                new ActUtils(getActContext()).
                        startActForResult(Settings.ACTION_LOCATION_SOURCE_SETTINGS, Utils.REQUEST_CODE_GPS_ON);

            } else if (i == nolocbackImgView.getId()) {
                nolocmenuImgView.setVisibility(View.VISIBLE);
                nolocbackImgView.setVisibility(View.GONE);
            } else if (i == nolocmenuImgView.getId()) {
                //addDrawer.checkDrawerState(true);
            }
        }
    }

    public void setCancelable(Dialog dialogview, boolean cancelable) {
        final Dialog dialog = dialogview;
        View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        if (cancelable) {
            touchOutsideView.setOnClickListener(v -> {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            });
            BottomSheetBehavior.from(bottomSheetView).setHideable(true);
        } else {
            touchOutsideView.setOnClickListener(null);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
        }
    }

    int checkcnt = 0;


    public void openFilterDilaog() {
        int color = Color.parseColor("#BABABA");

        int height = Utils.dpToPx(250, getActContext());

        final BottomSheetDialog faredialog = new BottomSheetDialog(getActContext());

        View contentView = View.inflate(getActContext(), R.layout.deliver_all_dialog_filter, null);
        if (generalFunc.isRTLmode()) {
            contentView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        faredialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                height));

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(height);

        MTextView menuTitle = (MTextView) faredialog.findViewById(R.id.menuTitle);
        MTextView closeTxt = (MTextView) faredialog.findViewById(R.id.closeTxt);
        MTextView TitleTxt = (MTextView) faredialog.findViewById(R.id.TitleTxt);
        MTextView resetTxt = (MTextView) faredialog.findViewById(R.id.resetTxt);
        MTextView offerTxtView = (MTextView) faredialog.findViewById(R.id.offerTxtView);
        CheckBox offerchkBox = (CheckBox) faredialog.findViewById(R.id.offerchkBox);
        LinearLayout offerArea = (LinearLayout) faredialog.findViewById(R.id.offerArea);
        View offerview = (View) faredialog.findViewById(R.id.offerview);

        LinearLayout favArea = (LinearLayout) faredialog.findViewById(R.id.favArea);
        MTextView favTxtView = (MTextView) faredialog.findViewById(R.id.favTxtView);
        CheckBox favchkBox = (CheckBox) faredialog.findViewById(R.id.favchkBox);
        View favview = (View) faredialog.findViewById(R.id.favview);

        favTxtView.setText(LBL_FAVOURITE_STORE);

        if (!generalFunc.getMemberId().equalsIgnoreCase("") && generalFunc.getJsonValueStr("ENABLE_FAVORITE_STORE_MODULE", obj_userProfile).equalsIgnoreCase("Yes")) {
            favArea.setVisibility(View.VISIBLE);
            favview.setVisibility(View.VISIBLE);
        } else {
            favArea.setVisibility(View.GONE);
            favview.setVisibility(View.GONE);
        }

        offerTxtView.setText(LBL_OFFER);
        MButton applyRatingBtn = ((MaterialRippleLayout) faredialog.findViewById(R.id.btn_type2)).getChildView();
        applyRatingBtn.setText(LBL_APPLY_FILTER);
        closeTxt.setText(LBL_CLOSE_TXT);
        resetTxt.setText(LBL_RESET);
        applyRatingBtn.setEnabled(false);
        menuTitle.setText(LBL_SHOW_RESTAURANTS_WITH);
        applyRatingBtn.setBackgroundColor(color);

        offerArea.setOnClickListener(v -> offerchkBox.performClick());
        favArea.setOnClickListener(v -> favchkBox.performClick());

        if (isOfferCheck.equalsIgnoreCase("Yes")) {
            offerchkBox.setChecked(true);
        }

        if (isFavCheck.equalsIgnoreCase("Yes")) {
            favchkBox.setChecked(true);
        }

        if (isOfferApply.equalsIgnoreCase("Yes")) {
            offerArea.setVisibility(View.VISIBLE);
        } else {
            offerArea.setVisibility(View.GONE);
        }

        applyRatingBtn.setOnClickListener(v -> {

            List<String> searchList = new ArrayList<>();
            selectedFilterId = "";
            for (int i = 0; i < filterList.size(); i++) {
                int pos = i;
                HashMap<String, String> indexData = filterList.get(i);
                String isCheck = indexData.get("isCheck");
                if (isCheck != null && isCheck.equalsIgnoreCase("Yes")) {
                    searchList.add(indexData.get("cuisineId"));
                } else {
                    if (searchList.contains(indexData.get("cuisineId"))) {
                        searchList.remove(i);
                    }
                }
            }

            if (searchList != null && searchList.size() > 0) {
                for (int j = 0; j < searchList.size(); j++) {
                    if (searchList.size() == 1) {
                        selectedFilterId = searchList.get(j);
                    } else {
                        if (selectedFilterId.equalsIgnoreCase("")) {
                            selectedFilterId = searchList.get(j);
                        } else {
                            selectedFilterId = selectedFilterId + "," + searchList.get(j);
                        }
                    }
                }
            } else {
                selectedFilterId = "";
            }
            if (filterLocation != null) {
                if (searchList == null || searchList.size() == 0) {
                    if (isOfferCheck.equalsIgnoreCase("Yes") || isFavCheck.equalsIgnoreCase("Yes")) {
                        filterImage.setVisibility(View.VISIBLE);
                        if (filterImage.getVisibility() == View.VISIBLE) {
                            resetTxt.setVisibility(View.VISIBLE);
                        }
                    } else {
                        selectedFilterId = "";
                        filterImage.setVisibility(View.GONE);
                        resetTxt.setVisibility(View.GONE);
                    }
                } else {
                    filterImage.setVisibility(View.VISIBLE);
                    resetTxt.setVisibility(View.VISIBLE);
                }

                setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
            }
            faredialog.dismiss();
        });


        offerchkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                checkcnt++;
                isOfferCheck = "Yes";
            } else {
                isOfferCheck = "No";
                checkcnt--;
            }

            if (checkcnt > 0) {
                applyRatingBtn.setEnabled(true);

                applyRatingBtn.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
                if (filterImage.getVisibility() == View.VISIBLE) {
                    resetTxt.setVisibility(View.VISIBLE);
                }
            } else {
                if (filterImage.getVisibility() == View.GONE) {
                    applyRatingBtn.setEnabled(false);
                    resetTxt.setVisibility(View.GONE);
                    applyRatingBtn.setBackgroundColor(color);
                }

            }

        });

        favchkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                checkcnt++;
                isFavCheck = "Yes";
            } else {
                isFavCheck = "No";
                checkcnt--;
            }

            if (checkcnt > 0) {
                applyRatingBtn.setEnabled(true);

                applyRatingBtn.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
                if (filterImage.getVisibility() == View.VISIBLE) {
                    resetTxt.setVisibility(View.VISIBLE);
                }
            } else {
                if (filterImage.getVisibility() == View.GONE) {
                    applyRatingBtn.setEnabled(false);
                    resetTxt.setVisibility(View.GONE);
                    applyRatingBtn.setBackgroundColor(color);
                }

            }

        });


        for (int i = 0; i < filterList.size(); i++) {
            final int pos = i;
            LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_filter, null);
            MTextView rowDataTxtView = (MTextView) view.findViewById(R.id.rowDataTxtView);
            MTextView rowTitleTxtView = (MTextView) view.findViewById(R.id.rowTitleTxtView);
            LinearLayout rowarea = (LinearLayout) view.findViewById(R.id.rowarea);
            View nameView = (View) view.findViewById(R.id.nameView);
            CheckBox chkBox = (CheckBox) view.findViewById(R.id.chkBox);

            String[] selectedFilterArray = selectedFilterId.split(",");
            if (selectedFilterId != null && !selectedFilterId.equalsIgnoreCase("") && Arrays.asList(selectedFilterArray).contains(filterList.get(pos).get("cuisineId"))) {
                chkBox.setChecked(true);
            }
            chkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    checkcnt++;
                    if (checkcnt > 0) {
                        HashMap<String, String> checkBox = filterList.get(pos);
                        checkBox.put("isCheck", "Yes");
                        filterList.set(pos, checkBox);
                    }
                } else {
                    checkcnt--;
                    HashMap<String, String> checkBox = filterList.get(pos);
                    checkBox.put("isCheck", "No");
                    filterList.set(pos, checkBox);
                }

                if (checkcnt > 0) {
                    applyRatingBtn.setEnabled(true);
                    applyRatingBtn.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
                    if (filterImage.getVisibility() == View.VISIBLE) {
                        resetTxt.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (filterImage.getVisibility() == View.GONE) {
                        applyRatingBtn.setEnabled(false);
                        applyRatingBtn.setBackgroundColor(color);
                        resetTxt.setVisibility(View.GONE);
                    }
                }

            });
            if (i == 0) {
                rowTitleTxtView.setText(LBL_CUISINES);
                rowTitleTxtView.setVisibility(View.VISIBLE);
                nameView.setVisibility(View.VISIBLE);
            } else {
                rowTitleTxtView.setVisibility(View.GONE);
                nameView.setVisibility(View.GONE);
            }

            rowDataTxtView.setText(filterList.get(i).get("cuisineName"));

            rowarea.setOnClickListener(v -> chkBox.performClick());


        }

        if (checkcnt > 0) {
            applyRatingBtn.setEnabled(true);
            applyRatingBtn.setBackgroundColor(getActContext().getResources().getColor(R.color.appThemeColor_1));
            if (filterImage.getVisibility() == View.VISIBLE) {
                resetTxt.setVisibility(View.VISIBLE);
            }
        } else {
            if (filterImage.getVisibility() == View.GONE) {
                applyRatingBtn.setEnabled(false);
                resetTxt.setVisibility(View.GONE);
                applyRatingBtn.setBackgroundColor(color);
            }
        }


        closeTxt.setOnClickListener(v -> faredialog.dismiss());

        resetTxt.setOnClickListener(v -> {
            faredialog.dismiss();
            selectedFilterId = "";
            isOfferCheck = "No";
            isFavCheck = "No";
            checkcnt = 0;
            if (filterLocation != null) {
                setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
                // getFilterList(filterLocation.getLatitude(), filterLocation.getLongitude());
                // isFilter = true;
                filterImage.setVisibility(View.GONE);
                resetTxt.setVisibility(View.GONE);
            }
        });


        faredialog.show();
    }


    public void openRelevenceDilaog() {

//        final BottomSheetDialog faredialog = new BottomSheetDialog(getActContext());
//
//
//        View contentView = View.inflate(getActContext(), R.layout.dialog_relevance, null);
//
//        if (generalFunc.getServiceId().equalsIgnoreCase("") || generalFunc.getServiceId().equalsIgnoreCase("1")) {
//
//            faredialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    Utils.dpToPx(300, getActContext())));
//        } else {
//            faredialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    Utils.dpToPx(200, getActContext())));
//
//        }
//        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
//        if (generalFunc.getServiceId().equalsIgnoreCase("") || generalFunc.getServiceId().equalsIgnoreCase("1")) {
//            mBehavior.setPeekHeight(Utils.dpToPx(300, getActContext()));
//        } else {
//            mBehavior.setPeekHeight(Utils.dpToPx(200, getActContext()));
//        }
//        View bottomSheetView = faredialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);
//        // BottomSheetBehavior.from(bottomSheetView).setHideable(false);
//        // setCancelable(faredialog, false);
//
//        MTextView menuTitle = (MTextView) faredialog.findViewById(R.id.menuTitle);
//        MTextView closeTxt = (MTextView) faredialog.findViewById(R.id.closeTxt);
//        MTextView TitleTxt = (MTextView) faredialog.findViewById(R.id.TitleTxt);
//        LinearLayout detailsArea = (LinearLayout) faredialog.findViewById(R.id.detailsArea);
//        // TitleTxt.setText("sort by");
//        menuTitle.setText(generalFunc.retrieveLangLBl("Sort By", "LBL_SORT_BY"));
//        TitleTxt.setText(generalFunc.retrieveLangLBl("Sort By", "LBL_SORT_BY"));
//
//
//        if (detailsArea.getChildCount() > 0) {
//            detailsArea.removeAllViewsInLayout();
//        }
//
//        for (int i = 0; i < sortby_List.size(); i++) {
//            int pos = i;
//            LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.item_list_design, null);
//            MTextView rowTitleTxtView = (MTextView) view.findViewById(R.id.rowTitleTxtView);
//
//
//            rowTitleTxtView.setText(sortby_List.get(i).get("label"));
//
//            rowTitleTxtView.setOnClickListener(v -> {
//                faredialog.dismiss();
//                selectedSort = sortby_List.get(pos).get("value");
//                relevenceTxt.setText(generalFunc.retrieveLangLBl("", sortby_List.get(pos).get(("label"))));
//                if (filterLocation != null) {
//                    getRestaurantList(filterLocation.getLatitude(), filterLocation.getLongitude(), false);
//                }
//
//            });
//            detailsArea.addView(view);
//        }
//
//        closeTxt.setOnClickListener(v -> faredialog.dismiss());
//
//        faredialog.show();


        OpenListView.getInstance(getActContext(), LBL_SORT_BY, sortby_List, OpenListView.OpenDirection.BOTTOM, true, position -> {


            selectedSort = sortby_List.get(position).get("value");

            filterPosition = position;
            if (filterLocation != null) {
                setNewListData(filterLocation.getLatitude(), filterLocation.getLongitude());
            }
        }).show(filterPosition, "label");
    }

    MyProfileFragment myProfileFragment;
    MyWalletFragment myWalletFragment;
    public MyBookingFragment myBookingFragment;

    public void openProfileFragment() {
        isProfilefrg = true;
        isWalletfrg = false;
        isBookingfrg = false;

//        if (myProfileFragment != null) {
//            myProfileFragment = null;
//            Utils.runGC();
//        }

        fabcartIcon.setVisibility(View.GONE);
        ratingArea.setVisibility(View.GONE);
        manageRecyclviewHeight();

        container.setVisibility(View.VISIBLE);
        // if (myProfileFragment == null) {
        myProfileFragment = new MyProfileFragment();
        // }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myProfileFragment).commit();


    }

    public void openWalletFragment() {
        isProfilefrg = false;
        isWalletfrg = true;
        isBookingfrg = false;
        fabcartIcon.setVisibility(View.GONE);
        ratingArea.setVisibility(View.GONE);
        manageRecyclviewHeight();
//        if (myProfileFragment != null) {
//            myProfileFragment = null;
//            Utils.runGC();
//        }

        container.setVisibility(View.VISIBLE);
        if (myWalletFragment == null) {
            myWalletFragment = new MyWalletFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myWalletFragment).commit();
    }

    public void openHistoryFragment() {

        isProfilefrg = false;
        isWalletfrg = false;


        fabcartIcon.setVisibility(View.GONE);
        ratingArea.setVisibility(View.GONE);
        manageRecyclviewHeight();
        if (myBookingFragment == null) {
            myBookingFragment = new MyBookingFragment();
        } else {
            myBookingFragment.onDestroy();
            myBookingFragment = new MyBookingFragment();
        }

        container.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myBookingFragment).commit();
    }

    public void manageHome() {
        isProfilefrg = false;
        isWalletfrg = false;
        isBookingfrg = false;
        container.setVisibility(View.GONE);
        onResume();
    }


    int filterPosition = -1;


    public void onClick(View v) {
        Utils.hideKeyboard(getActContext());
        Bundle bn = new Bundle();

        switch (v.getId()) {
            case R.id.changeLocBtn:
            case R.id.editLocationBtn:
            case R.id.headerAddressTxt:
                bn.putString("locationArea", "source");
                bn.putBoolean("isaddressview", true);
                bn.putDouble("lat", GeneralFunctions.parseDoubleValue(0.0, latitude));
                bn.putDouble("long", GeneralFunctions.parseDoubleValue(0.0, longitude));
                bn.putString("address", headerAddressTxt.getText().toString().trim());
                bn.putString("eSystem", Utils.eSystem_Type);
                new ActUtils(getActContext()).startActForResult(SearchLocationActivity.class,
                        bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);
                break;
            case R.id.searchArea:
                if (internetConnection.isNetworkConnected()) {
                    if (filterLocation != null) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat", filterLocation.getLatitude());
                        bundle.putDouble("long", filterLocation.getLongitude());
                        bundle.putString("address", headerAddressTxt.getText().toString().trim());
                        new ActUtils(getActContext()).startActForResult(RestaurantsSearchActivity.class, bundle, 111);
                    }
                } else {
                    generalFunc.showMessage(backImgView, LBL_NO_INTERNET_TXT);
                }
                break;

            case R.id.backImgView:
                onBackPressed();
                break;
            case R.id.filterArea:
//                    if (!filterClick) {
//                        filterClick = true;
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> filterClick = false, 1000);
//                        if (filterList != null && filterList.size() > 0) {
//                            openFilterDilaog();
//                        }
//                    }
                openRelevenceDilaog();
                break;

            case R.id.filterTxtView:
                if (!filterClick) {
                    filterClick = true;
                    Handler handler = new Handler();
                    handler.postDelayed(() -> filterClick = false, 1000);
                    if (filterList != null && filterList.size() > 0) {
                        openFilterDilaog();
                    }
                }
                // openRelevenceDilaog();
                break;
            case R.id.relevenceTxt:
                openRelevenceDilaog();
                break;
            case R.id.noThanksBtn:
                locationPopup.setVisibility(View.GONE);
                break;
            case R.id.fabcartIcon:
                new ActUtils(getActContext()).startAct(EditCartActivity.class);
                break;
        }
    }


}