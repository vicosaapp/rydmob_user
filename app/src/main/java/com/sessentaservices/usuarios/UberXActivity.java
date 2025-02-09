package com.sessentaservices.usuarios;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ViewPagerCards.CardFragmentPagerAdapter;
import com.ViewPagerCards.CardPagerAdapter;
import com.ViewPagerCards.ShadowTransformer;
import com.activity.ParentActivity;
import com.adapter.files.UberXBannerPagerAdapter;
import com.adapter.files.UberXCategoryAdapter;
import com.general.files.ActUtils;
import com.general.files.AddDrawer;
import com.general.files.DividerItemDecoration;
import com.general.files.GeneralFunctions;
import com.general.files.GetAddressFromLocation;
import com.general.files.GetLocationUpdates;
import com.general.files.InternetConnection;
import com.general.files.MyApp;
import com.general.files.OpenAdvertisementDialog;
import com.general.files.OpenCatType;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.model.ServiceModule;
import com.service.handler.ApiHandler;
import com.service.server.ServerTask;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class UberXActivity extends ParentActivity implements UberXCategoryAdapter.OnItemClickList, ViewPager.OnPageChangeListener
        , GetAddressFromLocation.AddressFound, GetLocationUpdates.LocationUpdates {


    public String UBERX_PARENT_CAT_ID = "";
    public JSONObject obj_userProfile;
    int BANNER_AUTO_ROTATE_INTERVAL = 4000;
    RecyclerView dataListRecyclerView;
    View bannerArea;

    ProgressBar serviceLoadingProgressBar;

    ViewPager bannerViewPager;
    UberXBannerPagerAdapter bannerAdapter;
    AddDrawer addDrawer;
    MTextView headerLocAddressTxt, LocStaticTxt, selectServiceTxt;
    HashMap<String, String> generalCategoryIconTypeDataMap = new HashMap<String, String>();
    ArrayList<HashMap<String, String>> generalCategoryList = new ArrayList<>();

    ArrayList<HashMap<String, String>> mainCategoryList = new ArrayList<>();
    ArrayList<HashMap<String, String>> subCategoryList = new ArrayList<>();

    ArrayList<HashMap<String, String>> allMainCategoryList = new ArrayList<>();

    UberXCategoryAdapter ufxCatAdapter;
    String CAT_TYPE_MODE = "0";
    public String latitude = "0.0";
    public String longitude = "0.0";

    ImageView backImgView, menuImgView;
    String address;
    public GetLocationUpdates getLastLocation;
    boolean isUfxaddress = false;
    GetAddressFromLocation getAddressFromLocation;
    boolean isback = false;
    LinearLayout btnArea;
    ImageView headerLogo;
    LinearLayout uberXHeaderLayout;

    InternetConnection intCheck;
    ImageView myjob_img;

    DividerItemDecoration itemDecoration;
    MButton btn_type2;
    int submitBtnId;
    public String vParentCategoryName = "";
    String parentId = "";
    boolean isbanner = false;

    ArrayList<String> multiServiceSelect = new ArrayList<>();


    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    private boolean mShowingFragments = false;
    String MORE_ICON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_x);

        isUfxaddress = false;
        setUserProfileData("", false);
        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();

        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        addToClickHandler(btn_type2);

        String advertise_banner_data = generalFunc.getJsonValueStr("advertise_banner_data", obj_userProfile);
        if (advertise_banner_data != null && !advertise_banner_data.equalsIgnoreCase("")) {
            String image_url = generalFunc.getJsonValue("image_url", advertise_banner_data);
            if (image_url != null && !image_url.equalsIgnoreCase("")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("image_url", image_url);
                map.put("tRedirectUrl", generalFunc.getJsonValue("tRedirectUrl", advertise_banner_data));
                map.put("vImageWidth", generalFunc.getJsonValue("vImageWidth", advertise_banner_data));
                map.put("vImageHeight", generalFunc.getJsonValue("vImageHeight", advertise_banner_data));
                new OpenAdvertisementDialog(getActContext(), map, generalFunc);
            }
        }
        MyApp.getInstance().showOutsatandingdilaog(backImgView);
        initializeLocation();

        itemDecoration = new DividerItemDecoration(getActContext(), DividerItemDecoration.VERTICAL_LIST);

        intCheck = new InternetConnection(getActContext());
        try {
            address = getIntent().getStringExtra("uberXAddress");
        } catch (Exception e) {
        }

        latitude = getIntent().getDoubleExtra("uberXlat", 0.0) + "";
        longitude = getIntent().getDoubleExtra("uberXlong", 0.0) + "";

        isback = getIntent().getBooleanExtra("isback", false);


        btnArea = findViewById(R.id.btnArea);


        serviceLoadingProgressBar = findViewById(R.id.serviceLoadingProgressBar);
        headerLogo = findViewById(R.id.headerLogo);
        uberXHeaderLayout = findViewById(R.id.uberXHeaderLayout);
        selectServiceTxt = (MTextView) findViewById(R.id.selectServiceTxt);


        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));


        dataListRecyclerView = (RecyclerView) findViewById(R.id.dataListRecyclerView);
        bannerArea = findViewById(R.id.bannerArea);
        bannerViewPager = (ViewPager) findViewById(R.id.bannerViewPager);
        String DELIVERALL = generalFunc.getJsonValueStr("DELIVERALL", obj_userProfile);
        boolean isRideDeliveryUberx = ServiceModule.RideDeliveryUbexProduct;
        if (isRideDeliveryUberx ||
                ServiceModule.isServiceProviderOnly()) {
            bannerArea.setVisibility(View.GONE);
            if (isRideDeliveryUberx) {
                headerLogo.setVisibility(View.VISIBLE);
                uberXHeaderLayout.setVisibility(View.GONE);
            } else {
                headerLogo.setVisibility(View.GONE);
                uberXHeaderLayout.setVisibility(View.VISIBLE);
            }

            if (UBERX_PARENT_CAT_ID.equalsIgnoreCase("0")) {
                btnArea.setVisibility(View.GONE);
            } else {
                btnArea.setVisibility(View.VISIBLE);
            }

        } else {
            bannerArea.setVisibility(View.VISIBLE);

            btnArea.setVisibility(View.VISIBLE);
            headerLogo.setVisibility(View.GONE);
            uberXHeaderLayout.setVisibility(View.VISIBLE);
            CollapsingToolbarLayout.LayoutParams lyParamsBannerArea = (CollapsingToolbarLayout.LayoutParams) bannerArea.getLayoutParams();
            lyParamsBannerArea.height = Utils.getHeightOfBanner(getActContext(), 0, "16:9");
            bannerArea.setLayoutParams(lyParamsBannerArea);
        }

        headerLocAddressTxt = (MTextView) findViewById(R.id.headerLocAddressTxt);
        LocStaticTxt = (MTextView) findViewById(R.id.LocStaticTxt);

        backImgView = (ImageView) findViewById(R.id.backImgView);
        menuImgView = (ImageView) findViewById(R.id.menuImgView);
        backImgView.setOnClickListener(new setOnClickLst());


        if (isback) {
            menuImgView.setVisibility(View.GONE);
            backImgView.setVisibility(View.VISIBLE);
        }

        addDrawer = new AddDrawer(getActContext(), obj_userProfile.toString(), isback);

        ufxCatAdapter = new UberXCategoryAdapter(getActContext(), generalCategoryList, generalFunc);

        if (UBERX_PARENT_CAT_ID.equalsIgnoreCase("0")) {
            CAT_TYPE_MODE = "0";
            setParentCategoryLayoutManager();
            btnArea.setVisibility(View.GONE);
        } else {
            dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));

            CAT_TYPE_MODE = "1";
        }
        dataListRecyclerView.setAdapter(ufxCatAdapter);

        uberXHeaderLayout.setOnClickListener(new setOnClickLst());

        if (addDrawer != null) {
            addDrawer.setItemClickList(() -> configCategoryView());
        }

        setData();


        getCategory(UBERX_PARENT_CAT_ID, CAT_TYPE_MODE);

        bannerViewPager.addOnPageChangeListener(this);

        if (Utils.checkText(generalFunc.retrieveValue("OPEN_CHAT"))) {
            generalFunc.removeValue("OPEN_CHAT");
        }

    }

    private void setUserProfileData(String userProfileData, boolean isSet) {
        obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
        UBERX_PARENT_CAT_ID = generalFunc.getJsonValueStr(Utils.UBERX_PARENT_CAT_ID, obj_userProfile);
    }


    public void initializeLocation() {
        if (generalFunc.isLocationPermissionGranted(false) && !isUfxaddress) {
            stopLocationUpdates();
            GetLocationUpdates.locationResolutionAsked = false;
            getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this);
        }
    }

    public void stopLocationUpdates() {
        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
        }
    }

    private void setParentCategoryLayoutManager() {
        GridLayoutManager gridLay = new GridLayoutManager(getActContext(), getNumOfColumns());
        gridLay.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (generalCategoryIconTypeDataMap.get("" + position) != null && !generalCategoryIconTypeDataMap.get("" + position).equalsIgnoreCase("ICON")) {
                    if (getNumOfColumns() > gridLay.getSpanCount()) {
                        return gridLay.getSpanCount();
                    } else {
                        return getNumOfColumns();
                    }
                }
                return 1;
            }
        });
        dataListRecyclerView.setLayoutManager(gridLay);
    }


    public void pubNubMsgArrived(String message) {

        String driverMsg = generalFunc.getJsonValue("Message", message);
        String eType = generalFunc.getJsonValue("eType", message);

        if (driverMsg.equals("CabRequestAccepted")) {
            String eSystem = generalFunc.getJsonValueStr("eSystem", obj_userProfile);
            if (eSystem != null && eSystem.equalsIgnoreCase("DeliverAll")) {
                generalFunc.showGeneralMessage("", generalFunc.getJsonValue("vTitle", message));
                return;
            }


            if (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                return;
            } else if (ServiceModule.RideDeliveryUbexProduct) {

                if (!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                    MyApp.getInstance().restartWithGetDataApp();
                    return;
                }
            }

            if (generalFunc.isJSONkeyAvail("iCabBookingId", message) && !generalFunc.getJsonValue("iCabBookingId", message).trim().equals("")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX) || eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    return;
                } else {
                    MyApp.getInstance().restartWithGetDataApp();
                }
            }

        }

    }


    private void setData() {
        headerLocAddressTxt.setHint(generalFunc.retrieveLangLBl("Enter Location...", "LBL_ENTER_LOC_HINT_TXT"));
        LocStaticTxt.setText(generalFunc.retrieveLangLBl("Location For availing Service", "LBL_LOCATION_FOR_AVAILING_TXT"));
        LocStaticTxt.setVisibility(View.GONE);
        selectServiceTxt.setText(generalFunc.retrieveLangLBl("Select Service", "LBL_SELECT_SERVICE_TXT"));

        if (isback) {
            if (getIntent().getStringExtra("address") != null && !getIntent().getStringExtra("address").equalsIgnoreCase("")) {
                headerLocAddressTxt.setText(getIntent().getStringExtra("address").trim());
                latitude = getIntent().getStringExtra("lat");
                longitude = getIntent().getStringExtra("long");

            }
        }
    }


    public void getBanners() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getBanners");
        parameters.put("iMemberId", generalFunc.getMemberId());


        ApiHandler.execute(getActContext(), parameters, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    JSONArray arr = generalFunc.getJsonArray(Utils.message_str, responseString);
                    ArrayList<String> imagesList = new ArrayList<String>();

                    if (arr != null && arr.length() > 0) {

                        int bannerWidth = Utils.getWidthOfBanner(getActContext(), 0);
                        int bannerHeight = Utils.getHeightOfBanner(getActContext(), Utils.dipToPixels(getActContext(), 0), "16:9");


                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr, i);

                            String vImage = generalFunc.getJsonValueStr("vImage", obj_temp);

                            String imageURL = Utils.getResizeImgURL(getActContext(), vImage, bannerWidth, bannerHeight);

                            imagesList.add(imageURL);
                        }
                    }
                    int imageListSize = imagesList.size();
                    if (imageListSize > 2) {
                        bannerViewPager.setOffscreenPageLimit(3);
                    } else if (imageListSize > 1) {
                        bannerViewPager.setOffscreenPageLimit(2);
                    }

                    UberXBannerPagerAdapter bannerAdapter = new UberXBannerPagerAdapter(getActContext(), imagesList);
                    bannerViewPager.setAdapter(bannerAdapter);

                    UberXActivity.this.bannerAdapter = bannerAdapter;
                }
            }
        });

    }

    public Context getActContext() {
        return UberXActivity.this;
    }

    public void getCategory(String parentId, final String CAT_TYPE_MODE) {

        this.parentId = parentId;
        generalCategoryList.clear();
        if (!CAT_TYPE_MODE.equals("0")) {
            subCategoryList.clear();
        }

        serviceLoadingProgressBar.setVisibility(View.VISIBLE);
        btnArea.setVisibility(View.GONE);

        if (ufxCatAdapter != null) {
            ufxCatAdapter.notifyDataSetChanged();
        }

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getServiceCategories");
        parameters.put("parentId", "" + parentId);
        parameters.put("userId", generalFunc.getMemberId());

        ServerTask exeWebServer = ApiHandler.execute(getActContext(), parameters, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            mainCategoryList.clear();
            subCategoryList.clear();
            generalCategoryList.clear();
            btnArea.setVisibility(View.VISIBLE);
            if (responseObj != null && !responseObj.equals("")) {

                MORE_ICON = generalFunc.getJsonValueStr("MORE_ICON", responseObj);

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                if (this.CAT_TYPE_MODE.equalsIgnoreCase("0")
                        && !CAT_TYPE_MODE.equalsIgnoreCase("0")) {


                    serviceLoadingProgressBar.setVisibility(View.GONE);
                    return;

                }

                if (isDataAvail) {
                    generalCategoryList.clear();
                    mainCategoryList.clear();

                    int GRID_TILES_MAX_COUNT = GeneralFunctions.parseIntegerValue(1, generalFunc.getJsonValueStr("GRID_TILES_MAX_COUNT", obj_userProfile));

                    JSONArray mainCataArray = generalFunc.getJsonArray("message", responseObj);

                    ArrayList<HashMap<String, String>> bannerList = new ArrayList<>();
                    ArrayList<HashMap<String, String>> moreItemList = new ArrayList<>();

                    int gridCount = 0;

                    vParentCategoryName = generalFunc.getJsonValueStr("vParentCategoryName", responseObj);
                    String LBL_BOOK_NOW = generalFunc.retrieveLangLBl("", "LBL_BOOK_NOW");
                    for (int i = 0; i < mainCataArray.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject categoryObj = generalFunc.getJsonObject(mainCataArray, i);
                        map.put("eCatType", generalFunc.getJsonValueStr("eCatType", categoryObj));
                        map.put("iServiceId", generalFunc.getJsonValueStr("iServiceId", categoryObj));
                        map.put("vCategory", generalFunc.getJsonValueStr("vCategory", categoryObj));
                        map.put("vLogo_image", generalFunc.getJsonValueStr("vLogo_image", categoryObj));
                        map.put("iVehicleCategoryId", generalFunc.getJsonValueStr("iVehicleCategoryId", categoryObj));
                        map.put("vCategoryBanner", generalFunc.getJsonValueStr("vCategoryBanner", categoryObj));
                        map.put("vBannerImage", generalFunc.getJsonValueStr("vBannerImage", categoryObj));
                        map.put("tBannerButtonText", generalFunc.getJsonValueStr("tBannerButtonText", categoryObj));
                        String eShowTerms = generalFunc.getJsonValueStr("eShowTerms", categoryObj);
                        map.put("eShowTerms", Utils.checkText(eShowTerms) ? eShowTerms : "");
                        map.put("LBL_BOOK_NOW", LBL_BOOK_NOW);

                        if (CAT_TYPE_MODE.equals("0")) {
                            btnArea.setVisibility(View.GONE);
                            allMainCategoryList.add(map);
                            if (ServiceModule.RideDeliveryUbexProduct
                                    || (ServiceModule.isServiceProviderOnly())) {
                                map.put("eShowType", generalFunc.getJsonValueStr("eShowType", categoryObj));

                                String eShowType = generalFunc.getJsonValueStr("eShowType", categoryObj);
                                if (eShowType.equalsIgnoreCase("ICON") || eShowType.equalsIgnoreCase("ICON-BANNER")) {
                                    map.put("eShowType", "Icon");

                                    if (gridCount < GRID_TILES_MAX_COUNT) {
                                        mainCategoryList.add((HashMap<String, String>) map.clone());
                                    } else {
                                        moreItemList.add((HashMap<String, String>) map.clone());
                                    }
                                    gridCount = gridCount + 1;

                                    if (eShowType.equalsIgnoreCase("ICON-BANNER")) {
                                        map.put("eShowType", "Banner");
                                        bannerList.add((HashMap<String, String>) map.clone());
                                    }
                                } else {
                                    bannerList.add((HashMap<String, String>) map.clone());
                                }
                            } else {
                                // map.put("eShowType", "Icon");
                                mainCategoryList.add((HashMap<String, String>) map.clone());
                            }
                        } else {
                            subCategoryList.add((HashMap<String, String>) map.clone());
                        }
                    }

                    JSONArray arr = generalFunc.getJsonArray("BANNER_DATA", responseObj);

                    if (UBERX_PARENT_CAT_ID.equalsIgnoreCase(parentId)) {

                        int width = Utils.getWidthOfBanner(getActContext(), 0);
                        int height = Utils.getHeightOfBanner(getActContext(), 0, "16:9");


                        if (arr != null && arr.length() > 0) {
                            isbanner = true;
                            bannerArea.setVisibility(View.VISIBLE);
                            //selectServiceTxt.setVisibility(View.VISIBLE);

                            ArrayList<String> imagesList = new ArrayList<String>();
                            mCardAdapter = new CardPagerAdapter();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj_temp = generalFunc.getJsonObject(arr, i);

                                String vImage = generalFunc.getJsonValueStr("vImage", obj_temp);

                                String imageURL = Utils.getResizeImgURL(getActContext(), vImage, width, height);

                                imagesList.add(imageURL);
                                mCardAdapter.addCardItem(imageURL, getActContext(), width, height);
                            }
                            int imageListSize = imagesList.size();
                            if (imageListSize > 2) {
                                bannerViewPager.setOffscreenPageLimit(3);
                            } else if (imageListSize > 1) {
                                bannerViewPager.setOffscreenPageLimit(2);
                            }

                            // UberXBannerPagerAdapter bannerAdapter = new UberXBannerPagerAdapter(getActContext(), imagesList);
                            // bannerViewPager.setAdapter(bannerAdapter);

                            mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                                    dpToPixels(2, this));

                            mCardShadowTransformer = new ShadowTransformer(bannerViewPager, mCardAdapter);
                            mFragmentCardShadowTransformer = new ShadowTransformer(bannerViewPager, mFragmentCardAdapter);

                            bannerViewPager.setAdapter(mCardAdapter);
                            bannerViewPager.setPageTransformer(false, mCardShadowTransformer);
                            bannerViewPager.setOffscreenPageLimit(3);

                            UberXActivity.this.bannerAdapter = bannerAdapter;

                            // bannerCirclePageIndicator.setDataSize(imagesList.size());
                            //bannerCirclePageIndicator.setViewPager(bannerViewPager);


                        } else {
                            isbanner = false;
                            bannerArea.setVisibility(View.GONE);
                            if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
                                selectServiceTxt.setVisibility(View.GONE);
                            } else {
                                //selectServiceTxt.setVisibility(View.VISIBLE);
                            }
                        }
                    }


                    if (CAT_TYPE_MODE.equals("0")) {
                        if (ServiceModule.RideDeliveryUbexProduct
                                || (ServiceModule.isServiceProviderOnly())) {
                            int moreItemListSize = moreItemList.size();
                            if (moreItemListSize > 0) {
                                HashMap<String, String> mapDataMore = new HashMap<>();
                                mapDataMore.put("eCatType", "More");
                                mapDataMore.put("eShowType", "Icon");
                                mapDataMore.put("vCategory", generalFunc.retrieveLangLBl("", "LBL_MORE"));
                                mapDataMore.put("vLogo_image", MORE_ICON);
                                mapDataMore.put("eShowTerms", "No");
                                mainCategoryList.add(mapDataMore);
                                int mainCategoryListSize = mainCategoryList.size();
                                if (mainCategoryListSize % getNumOfColumns() != 0) {
                                    mainCategoryList.remove(mainCategoryListSize - 1);
                                    mainCategoryListSize = mainCategoryList.size();
                                    int totCount = 0;
                                    while ((mainCategoryListSize + 1) % getNumOfColumns() != 0) {
                                        if (totCount >= moreItemListSize) {
                                            break;
                                        }
                                        mainCategoryList.add(moreItemList.get(totCount));
                                        totCount = totCount + 1;

                                        if (totCount >= mainCategoryListSize) {
                                            break;
                                        }
                                    }
                                    moreItemListSize = moreItemList.size();
                                    if (totCount < moreItemListSize) {
                                        mainCategoryList.add(mapDataMore);
                                    }
                                }
                            }

                            if (generalFunc.getJsonValueStr("RDU_HOME_PAGE_LAYOUT_DESIGN", obj_userProfile).equalsIgnoreCase("Banner/Icon")) {
                                mainCategoryList.addAll(0, bannerList);
                            } else {
                                mainCategoryList.addAll(bannerList);
                            }

                        }

                        if (generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", obj_userProfile).equalsIgnoreCase("PROVIDER")) {


                        }
                        generalCategoryList.addAll(mainCategoryList);

                        if (ServiceModule.RideDeliveryUbexProduct
                                || (ServiceModule.isServiceProviderOnly())) {
                            for (int i = 0; i < generalCategoryList.size(); i++) {
                                generalCategoryIconTypeDataMap.put("" + i, "" + (generalCategoryList.get(i).get("eShowType") == null ? "" : generalCategoryList.get(i).get("eShowType")));
                            }
                        }
                    } else {
                        generalCategoryList.addAll(subCategoryList);
                    }

                    ufxCatAdapter = null;
                    ufxCatAdapter = new UberXCategoryAdapter(getActContext(), generalCategoryList, generalFunc);

                    if (!CAT_TYPE_MODE.equalsIgnoreCase("0")) {
                        dataListRecyclerView.addItemDecoration(itemDecoration);
                    } else {
                        dataListRecyclerView.removeItemDecoration(itemDecoration);
                    }

                    ufxCatAdapter.setCategoryMode(CAT_TYPE_MODE);
                    dataListRecyclerView.setAdapter(ufxCatAdapter);
                    ufxCatAdapter.notifyDataSetChanged();
                    ufxCatAdapter.setOnItemClickList(UberXActivity.this);

                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr("message", responseObj)));
                }

            } else {
                generalFunc.showError();
            }

            serviceLoadingProgressBar.setVisibility(View.GONE);
        });
        exeWebServer.setCancelAble(false);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


    @Override
    public void onItemClick(int position) {
        onItemClickHandle(position, "HOME");
    }

    @Override
    public void onMultiItem(String id, boolean b) {

        if (multiServiceSelect.contains(id)) {
            if (!b) {
                while (multiServiceSelect.remove(id)) {
                }
            }

        } else {
            multiServiceSelect.add(id);
        }
    }


    public void onItemClickHandle(int position, String type) {

        Utils.hideKeyboard(getActContext());

        HashMap<String, String> mapData = null;
        if (type.equalsIgnoreCase("HOME")) {
            mapData = generalCategoryList.get(position);
        } else {
            mapData = allMainCategoryList.get(position);
        }

        if (mapData.get("eCatType") != null) {
            String s = mapData.get("eCatType").toUpperCase(Locale.US);
            if ("MORE".equals(s)) {

                openMoreDialog();
                return;
            }
            (new OpenCatType(getActContext(), mapData)).execute();
        }

        if (mapData.get("eCatType").equalsIgnoreCase("ServiceProvider")) {
            if (CAT_TYPE_MODE.equals("0")) {
                setSubCategoryList(mapData);
                return;
            }

            if (latitude.equalsIgnoreCase("0.0") || longitude.equalsIgnoreCase("0.0")) {
                generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("", "LBL_SET_LOCATION"));
            } else {
                checkServiceAvailableOrNot(mapData.get("iVehicleCategoryId"), latitude, longitude, position);
            }
        }

    }

    public void setMainCategory() {
        CAT_TYPE_MODE = "0";
        btnArea.setVisibility(View.GONE);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setLayoutTransition(new LayoutTransition());
        generalCategoryList.clear();

        generalCategoryList.addAll(mainCategoryList);
        ufxCatAdapter = null;
        ufxCatAdapter = new UberXCategoryAdapter(getActContext(), generalCategoryList, generalFunc);

        ufxCatAdapter.setCategoryMode("0");

        ufxCatAdapter.setOnItemClickList(UberXActivity.this);
        dataListRecyclerView.setAdapter(ufxCatAdapter);

        if (ServiceModule.RideDeliveryUbexProduct
                || (ServiceModule.isServiceProviderOnly())) {
            if (ServiceModule.RideDeliveryUbexProduct) {
                headerLogo.setVisibility(View.VISIBLE);
                uberXHeaderLayout.setVisibility(View.GONE);
            } else {
                headerLogo.setVisibility(View.GONE);
                uberXHeaderLayout.setVisibility(View.VISIBLE);
            }
            if (multiServiceSelect != null) {
                multiServiceSelect.clear();
            }
        } else {
            bannerArea.setVisibility(View.VISIBLE);
            headerLogo.setVisibility(View.GONE);
            uberXHeaderLayout.setVisibility(View.VISIBLE);
        }


        selectServiceTxt.setText(generalFunc.retrieveLangLBl("Select Service", "LBL_SELECT_SERVICE_TXT"));
        setParentCategoryLayoutManager();

        dataListRecyclerView.removeItemDecoration(itemDecoration);

        ufxCatAdapter.notifyDataSetChanged();
        if (addDrawer != null) {
            addDrawer.setMenuState(true);
        }

        if (isbanner) {
            bannerArea.setVisibility(View.VISIBLE);
        } else {
            selectServiceTxt.setVisibility(View.GONE);
        }
    }

    public void setSubCategoryList(HashMap<String, String> dataItem) {

        ufxCatAdapter.setCategoryMode("1");

        headerLogo.setVisibility(View.GONE);
        uberXHeaderLayout.setVisibility(View.VISIBLE);
        CAT_TYPE_MODE = "1";
        dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));
        if (UBERX_PARENT_CAT_ID.equalsIgnoreCase("0")) {
            ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setLayoutTransition(null);
            bannerArea.setVisibility(View.GONE);
            selectServiceTxt.setText(dataItem.get("vCategory"));
        }
        String iVehicleCategoryId = dataItem.get("iVehicleCategoryId");

        getCategory(iVehicleCategoryId, "1");

        if (generalFunc.isRTLmode()) {
            menuImgView.setRotation(180);
        }

        if (addDrawer != null) {
            addDrawer.setMenuState(false);
        }

        if (generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", obj_userProfile).equalsIgnoreCase("PROVIDER")) {
            btnArea.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (generalFunc.prefHasKey(Utils.iServiceId_KEY) && generalFunc != null /*&& !ServiceModule.DeliverAllProduct*/) {
            generalFunc.removeValue(Utils.iServiceId_KEY);
        }

        try {
            if (addDrawer != null) {
                setUserProfileData("", false);

                obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
                addDrawer.buildDrawer();
            }


            if (generalFunc.retrieveValue(Utils.ISWALLETBALNCECHANGE).equalsIgnoreCase("Yes")) {
                getWalletBalDetails();
            } else {
                setUserProfileData("", false);
                setUserInfo();
            }


        } catch (Exception e) {

        }
        initializeLocation();
    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int i) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void getWalletBalDetails() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

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

                        setUserProfileData(userProfileJson, true);

                        setUserInfo();
                    } catch (Exception e) {

                    }
                }
            }
        });

    }


    public void setUserInfo() {
        View view = ((Activity) getActContext()).findViewById(android.R.id.content);
        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        ((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));

        String url = CommonUtilities.USER_PHOTO_PATH + generalFunc.getMemberId() + "/" + generalFunc.getJsonValue("vImgName", obj_userProfile);
        generalFunc.checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), url, R.mipmap.ic_no_pic_user, R.mipmap.ic_no_pic_user);

    }


    @Override
    public void onAddressFound(String address, double latitude, double longitude, String geocodeobject) {
        if (isback) {
            if (getIntent().getStringExtra("address") != null) {
                return;
            }
        }

        if (address != null && !address.equals("")) {
            this.latitude = latitude + "";
            this.longitude = longitude + "";
            headerLocAddressTxt.setText(address.trim());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            setUserProfileData("", false);
            addDrawer.changeUserProfileJson(obj_userProfile.toString());
        } else if (requestCode == Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {

            headerLocAddressTxt.setText(data.getStringExtra("Address").trim());
            this.latitude = data.getStringExtra("Latitude") == null ? "0.0" : data.getStringExtra("Latitude");
            this.longitude = data.getStringExtra("Longitude") == null ? "0.0" : data.getStringExtra("Longitude");

            if (!this.latitude.equalsIgnoreCase("0.0") && !this.longitude.equalsIgnoreCase("0.0")) {
                isUfxaddress = true;
            }
        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            setUserProfileData("", false);
            addDrawer.changeUserProfileJson(obj_userProfile.toString());
        } else if (requestCode == Utils.SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            isUfxaddress = true;
            headerLocAddressTxt.setText(data.getStringExtra("Address").trim());
            this.latitude = data.getStringExtra("Latitude");
            this.longitude = data.getStringExtra("Longitude");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();
        releaseResources();
    }

    public void configCategoryView() {
        setMainCategory();
    }

    @Override
    public void onBackPressed() {


        if (CAT_TYPE_MODE.equals("1") && UBERX_PARENT_CAT_ID.equalsIgnoreCase("0")) {
            multiServiceSelect.clear();

            configCategoryView();
            return;
        }

        super.onBackPressed();
    }


    public void releaseResources() {
        if (getAddressFromLocation != null) {
            getAddressFromLocation.setAddressList(null);
            getAddressFromLocation = null;
        }
    }

    @Override
    public void onLocationUpdate(Location mLastLocation) {
        stopLocationUpdates();
        latitude = mLastLocation.getLatitude() + "";
        longitude = mLastLocation.getLongitude() + "";
        if (getAddressFromLocation == null) {
            getAddressFromLocation = new GetAddressFromLocation(getActContext(), generalFunc);
            getAddressFromLocation.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            getAddressFromLocation.setAddressList(this);
            getAddressFromLocation.execute();
        }
    }


    public void checkServiceAvailableOrNot(String iVehicleCategoryId, String latitude, String longitude, int position) {

        HashMap<String, String> parameters = new HashMap<String, String>();

        parameters.put("type", "getServiceCategoryTypes");
        parameters.put("iVehicleCategoryId", iVehicleCategoryId);
        parameters.put("userId", generalFunc.getMemberId());
        parameters.put("vLatitude", latitude);
        parameters.put("vLongitude", longitude);
        parameters.put("UserType", Utils.userType);
        parameters.put("eCheck", "Yes");

        final ServerTask exeWebServer = ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);

            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);

                if (isDataAvail) {
                    Bundle bundle = new Bundle();
                    bundle.putString("iVehicleCategoryId", generalCategoryList.get(position).get("iVehicleCategoryId"));
                    bundle.putString("vCategoryName", generalCategoryList.get(position).get("vCategory"));
                    bundle.putString("latitude", latitude);
                    bundle.putString("longitude", longitude);
                    bundle.putString("address", headerLocAddressTxt.getText().toString());
                    new ActUtils(getActContext()).startActWithData(UberXSelectServiceActivity.class, bundle);
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValueStr("message", responseObj)));
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.setCancelAble(false);
    }


    public void onClick(View view) {
        int i = view.getId();
        Bundle bn = new Bundle();
        if (i == submitBtnId) {
            if (latitude.equalsIgnoreCase("0.0") || longitude.equalsIgnoreCase("0.0")) {
                generalFunc.showMessage(view, generalFunc.retrieveLangLBl("", "LBL_SET_LOCATION"));
            } else {

                String SelectedVehicleTypeId = "";
                if (multiServiceSelect.size() > 0) {

                    SelectedVehicleTypeId = android.text.TextUtils.join(",", multiServiceSelect);
                } else {
                    generalFunc.showMessage(view, generalFunc.retrieveLangLBl("Please Select Service", "LBL_SELECT_SERVICE_TXT"));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("isufx", true);
                bundle.putString("latitude", latitude);
                bundle.putString("longitude", longitude);
                bundle.putString("address", headerLocAddressTxt.getText().toString());
                bundle.putString("SelectvVehicleType", vParentCategoryName);
                bundle.putString("SelectedVehicleTypeId", SelectedVehicleTypeId);
                bundle.putString("parentId", parentId);
                bundle.putBoolean("isCarwash", true);

                new ActUtils(getActContext()).startActWithData(MainActivity.class, bundle);

            }
        }

    }


    private interface BounceAnimListener {
        void onAnimationFinished();
    }

    public class setOnClickLst implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Utils.hideKeyboard(getActContext());
            switch (v.getId()) {
                case R.id.uberXHeaderLayout:
                    Bundle bn = new Bundle();
                    bn.putString("locationArea", "source");
                    bn.putBoolean("isaddressview", true);
                    if (!latitude.equalsIgnoreCase("0.0") && !longitude.equalsIgnoreCase("0.0")) {
                        bn.putDouble("lat", GeneralFunctions.parseDoubleValue(0.0, latitude));
                        bn.putDouble("long", GeneralFunctions.parseDoubleValue(0.0, longitude));
                    }
                    bn.putString("address", headerLocAddressTxt.getText().toString() + "");

                    new ActUtils(getActContext()).startActForResult(SearchLocationActivity.class,
                            bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);

                    break;

                case R.id.backImgView:
                    onBackPressed();
                    break;
            }
        }
    }

    BottomSheetDialog moreDialog;

    public void openMoreDialog() {
        if (moreDialog != null && moreDialog.isShowing()) {
            return;
        }
        moreDialog = new BottomSheetDialog(getActContext());

        View contentView = View.inflate(getActContext(), R.layout.dialog_more, null);
        if (generalFunc.isRTLmode()) {
            contentView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        moreDialog.setContentView(contentView);

        moreDialog.setCancelable(true);
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        int bannerHeight = (int) (Utils.getScreenPixelHeight(getActContext()) * 0.40);
        mBehavior.setPeekHeight(bannerHeight);
        mBehavior.setHideable(true);

        MTextView moreTitleTxt = contentView.findViewById(R.id.moreTitleTxt);
        MTextView cancelTxt = contentView.findViewById(R.id.cancelTxt);

        moreTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_SERVICE"));
        cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        cancelTxt.setOnClickListener(v -> moreDialog.dismiss());

        RecyclerView dataListRecyclerView_more = contentView.findViewById(R.id.dataListRecyclerView_more);
        dataListRecyclerView_more.setLayoutManager(new GridLayoutManager(getActContext(), getNumOfColumns()));
        UberXCategoryAdapter ufxCatAdapter = new UberXCategoryAdapter(getActContext(), allMainCategoryList, generalFunc, true);
        dataListRecyclerView_more.setAdapter(ufxCatAdapter);


        ufxCatAdapter.setOnItemClickList(new UberXCategoryAdapter.OnItemClickList() {
            @Override
            public void onItemClick(int position) {
                moreDialog.cancel();
                onItemClickHandle(position, "MORE");
            }

            @Override
            public void onMultiItem(String id, boolean b) {

            }
        });


        moreDialog.show();
    }

    public Integer getNumOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = (displayMetrics.widthPixels - Utils.dipToPixels(getActContext(), 10)) / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 74);
        return noOfColumns;
    }

}
