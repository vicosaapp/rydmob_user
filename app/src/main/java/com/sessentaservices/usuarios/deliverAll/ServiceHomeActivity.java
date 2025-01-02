package com.sessentaservices.usuarios.deliverAll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.activity.ParentActivity;
import com.adapter.files.deliverAll.FoodDeliveryHomeAdapter;
import com.adapter.files.deliverAll.ServiceHomeAdapter;
import com.andremion.counterfab.CounterFab;
import com.dialogs.BottomInfoDialog;
import com.fragments.MyBookingFragment;
import com.fragments.MyProfileFragment;
import com.fragments.MyWalletFragment;
import com.fragments.deliverall.FoodDeliveryHomeFragment;
import com.fragments.deliverall.RestaurantAllDetailsNewFragment;
import com.general.files.ActUtils;
import com.general.files.AddBottomBar;
import com.general.files.DividerItemDecoration;
import com.general.files.GeneralFunctions;
import com.general.files.GetUserData;
import com.general.files.IOnBackPressed;
import com.general.files.MyApp;
import com.general.files.OpenAdvertisementDialog;
import com.general.files.RecurringTask;
import com.general.files.showTermsDialog;
import com.sessentaservices.usuarios.R;
import com.model.ServiceModule;
import com.realmModel.Cart;
import com.service.handler.ApiHandler;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.ErrorView;
import com.view.LoopingCirclePageIndicator;
import com.view.MTextView;
import com.view.SelectableRoundedImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class ServiceHomeActivity extends ParentActivity implements ServiceHomeAdapter.OnItemClickList, RecurringTask.OnTaskRunCalled,
        ViewPager.OnPageChangeListener {


    ServiceHomeAdapter serviceHomeAdapter;
    ArrayList<HashMap<String, String>> generalCategoryList;
    RecyclerView serviceListRecyclerView;
    DividerItemDecoration itemDecoration;
    MTextView headerTxt;
    MTextView titleTxt;
    JSONObject obj_userProfile;

    LinearLayout errorViewArea;
    ErrorView errorView;

    int position = -1;
    boolean isClicked = false;
    ImageView headerLogo;
    RelativeLayout headerArea;

    ViewPager bannerViewPager;
    LoopingCirclePageIndicator bannerCirclePageIndicator;
    FoodDeliveryHomeAdapter bannerAdapter;
    int currentBannerPosition = 0;
    LinearLayout bottomMenuArea;
    RelativeLayout cartArea;
    CounterFab fabcartIcon;
    AddBottomBar addBottomBar;
    FrameLayout container;
    boolean isFoodOnly = true;
    public String latitude = "0.0";
    public String longitude = "0.0";
    private RealmResults<Cart> realmCartList;
    private String cartDataServiceID = "";
    boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home);

        ServiceModule.configure();

//        if (Utils.checkText(generalFunc.getMemberId())) {
        addBottomBar = new AddBottomBar(getActContext(), obj_userProfile);
//        }
        String advertise_banner_data = generalFunc.getJsonValueStr("advertise_banner_data", obj_userProfile);
        if (advertise_banner_data != null && !advertise_banner_data.equalsIgnoreCase("")) {

            if (generalFunc.getJsonValue("image_url", advertise_banner_data) != null && !generalFunc.getJsonValue("image_url", advertise_banner_data).equalsIgnoreCase("")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("image_url", generalFunc.getJsonValue("image_url", advertise_banner_data));
                map.put("tRedirectUrl", generalFunc.getJsonValue("tRedirectUrl", advertise_banner_data));
                map.put("vImageWidth", generalFunc.getJsonValue("vImageWidth", advertise_banner_data));
                map.put("vImageHeight", generalFunc.getJsonValue("vImageHeight", advertise_banner_data));
                new OpenAdvertisementDialog(getActContext(), map, generalFunc);
            }
        }

        container = (FrameLayout) findViewById(R.id.container);
        bottomMenuArea = (LinearLayout) findViewById(R.id.bottomMenuArea);
        cartArea = (RelativeLayout) findViewById(R.id.cartArea);
        fabcartIcon = (CounterFab) findViewById(R.id.fabcartIcon);
        addToClickHandler(fabcartIcon);
        headerLogo = (ImageView) findViewById(R.id.headerLogo);
        headerTxt = (MTextView) findViewById(R.id.headerTxt);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        MyApp.getInstance().showOutsatandingdilaog(titleTxt);
        headerArea = (RelativeLayout) findViewById(R.id.headerArea);
        headerArea.setVisibility(View.VISIBLE);
        if (getIntent().hasExtra("showBackBtn")) {

            bottomMenuArea.setVisibility(View.GONE);
            findViewById(R.id.backImgView).setVisibility(View.VISIBLE);
            addToClickHandler(findViewById(R.id.backImgView));
            headerLogo.setVisibility(View.GONE);
            titleTxt.setVisibility(View.VISIBLE);
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVER_ALL_APP_DELIVERY"));
            headerArea.setVisibility(View.GONE);

        } else {
            bottomMenuArea.setVisibility(View.VISIBLE);

            findViewById(R.id.backImgView).setVisibility(View.GONE);


            headerArea.setVisibility(View.VISIBLE);
            headerLogo.setVisibility(View.VISIBLE);
            titleTxt.setVisibility(View.GONE);
        }

        itemDecoration = new DividerItemDecoration(getActContext(), DividerItemDecoration.VERTICAL_LIST);
        serviceListRecyclerView = (RecyclerView) findViewById(R.id.serviceListRecyclerView);

        errorViewArea = (LinearLayout) findViewById(R.id.errorViewArea);
        errorView = (ErrorView) findViewById(R.id.errorView);


        isFoodOnly = getIntent().getBooleanExtra("isfoodOnly", false);
        //temp set always true
        //   isFoodOnly =true;

        if (isFoodOnly) {

            manageHome();
        } else {
            generalCategoryList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("servicedata");
            serviceHomeAdapter = new ServiceHomeAdapter(getActContext(), generalCategoryList, generalFunc);
//            serviceHomeAdapter.setrealmCartList(realmCartList);
            serviceHomeAdapter.setOnItemClickList(ServiceHomeActivity.this);
            serviceListRecyclerView.setAdapter(serviceHomeAdapter);
            serviceHomeAdapter.notifyDataSetChanged();

            bannerViewPager = (ViewPager) findViewById(R.id.bannerViewPager);
            bannerCirclePageIndicator = (LoopingCirclePageIndicator) findViewById(R.id.bannerCirclePageIndicator);
        }

        if (generalFunc.retrieveValue("isSmartLoginEnable").equalsIgnoreCase("Yes") &&
                !generalFunc.retrieveValue("isFirstTimeSmartLoginView").equalsIgnoreCase("Yes") && !generalFunc.getMemberId().equals("")) {

            BottomInfoDialog bottomInfoDialog = new BottomInfoDialog(getActContext(), generalFunc);
            bottomInfoDialog.setListener(() -> generalFunc.storeData("isFirstTimeSmartLoginView", "Yes"));
            bottomInfoDialog.showPreferenceDialog(generalFunc.retrieveLangLBl("", "LBL_QUICK_LOGIN"), generalFunc.retrieveLangLBl("", "LBL_QUICK_LOGIN_NOTE_TXT"),
                    R.raw.biometric, generalFunc.retrieveLangLBl("", "LBL_OK"), true);
        }

    }


    MyProfileFragment myProfileFragment;
    MyWalletFragment myWalletFragment;
    public MyBookingFragment myBookingFragment;
    FoodDeliveryHomeFragment foodDeliveryHomeFragment;
    RestaurantAllDetailsNewFragment restaurantAllDetailsNewFragment;

    boolean isProfilefrg = false;
    boolean isWalletfrg = false;
    boolean isBookingfrg = false;

    public void openProfileFragment() {
        isProfilefrg = true;
        isWalletfrg = false;
        isBookingfrg = false;

//        if (myProfileFragment != null) {
//            myProfileFragment = null;
//            Utils.runGC();
//        }


        container.setVisibility(View.VISIBLE);
        if (myProfileFragment == null) {
            myProfileFragment = new MyProfileFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myProfileFragment).commit();
    }

    public void openHistoryFragment() {

        isProfilefrg = false;
        isWalletfrg = false;
        isBookingfrg = true;

        container.setVisibility(View.VISIBLE);

        if (myBookingFragment == null) {
            myBookingFragment = new MyBookingFragment();
        } else {
            myBookingFragment.onDestroy();
            myBookingFragment = new MyBookingFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myBookingFragment).commit();
    }

    public void openWalletFragment() {
        isProfilefrg = false;
        isWalletfrg = true;
        isBookingfrg = false;

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

    public void manageHome() {
        String eShowTerms = generalFunc.getJsonValueStr("eShowTerms", obj_userProfile);
        if (Utils.checkText(eShowTerms) && eShowTerms.equalsIgnoreCase("yes")) {
            new showTermsDialog(getActContext(), generalFunc, position, generalFunc.getJsonValueStr("vCategory", obj_userProfile), click, () -> redirectToHome(), true);
        }
        redirectToHome();
    }

    private void redirectToHome() {
        isProfilefrg = false;
        isWalletfrg = false;
        isBookingfrg = false;
        container.setVisibility(View.GONE);


        if (isFoodOnly) {

            if (generalFunc.retrieveValue("CHECK_SYSTEM_STORE_SELECTION").equalsIgnoreCase("Yes")) {
                container.setVisibility(View.VISIBLE);
                if (restaurantAllDetailsNewFragment == null) {
                    restaurantAllDetailsNewFragment = new RestaurantAllDetailsNewFragment();
                }

                Bundle bn = new Bundle();
                bn.putBoolean("isBack", false);
                bn.putString("iCompanyId", getIntent().getStringExtra("iCompanyId"));
                restaurantAllDetailsNewFragment.setArguments(bn);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, restaurantAllDetailsNewFragment).commit();


            } else {


                container.setVisibility(View.VISIBLE);
                Bundle bn = new Bundle();
                bn.putBoolean("isBackHide", true);
                bn.putBoolean("isback", true);
                if (foodDeliveryHomeFragment == null) {
                    foodDeliveryHomeFragment = new FoodDeliveryHomeFragment();
                } else {
                    bn.putString("address", generalFunc.retrieveValue(Utils.CURRENT_ADDRESSS));
                    bn.putString("latitude", generalFunc.retrieveValue(Utils.CURRENT_LATITUDE));
                    bn.putString("longitude", generalFunc.retrieveValue(Utils.CURRENT_LONGITUDE));
                }

                foodDeliveryHomeFragment.setArguments(bn);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, foodDeliveryHomeFragment).commit();

            }


        }
    }

    public void manageVectorImag(View viewGradient, int gradientImg, int gradientCimpatImg) {
        manageVectorImage(viewGradient, gradientImg, gradientCimpatImg);
    }


    @SuppressLint("SetTextI18n")
    public void setUserInfo() {
        View view = ((Activity) getActContext()).findViewById(android.R.id.content);
        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
        ((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));

        String url = CommonUtilities.USER_PHOTO_PATH + generalFunc.getMemberId() + "/" + generalFunc.getJsonValue("vImgName", obj_userProfile);
        generalFunc.checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), url, R.mipmap.ic_no_pic_user, R.mipmap.ic_no_pic_user);

    }


    private void getLanguageLabelServiceWise(int position) {
        generateErrorView();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    HashMap<String, String> storeData = new HashMap<>();
                    storeData.put(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    storeData.put(Utils.LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vCode", responseString));
                    storeData.put(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));
                    storeData.put(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    generalFunc.storeData(storeData);

                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());

                    Utils.setAppLocal(getActContext());

                    Bundle bn = new Bundle();
                    bn.putBoolean("isback", true);
                    resetData();
                    GetUserData getUserData = new GetUserData(generalFunc, MyApp.getInstance().getApplicationContext());
                    getUserData.GetConfigDataForLocalStorage();

                    if (generalFunc.retrieveValue("CHECK_SYSTEM_STORE_SELECTION").equalsIgnoreCase("Yes")) {


                        bn.putString("iCompanyId", generalCategoryList.get(position).get("iCompanyId"));
                        bn.putString("Restaurant_Status", "");
                        bn.putString("ispriceshow", generalCategoryList.get(position).get("ispriceshow"));
                        bn.putString("eAvailable", generalCategoryList.get(position).get("eAvailable"));
                        bn.putString("timeslotavailable", generalCategoryList.get(position).get("timeslotavailable"));
                        bn.putString("lat", "");
                        bn.putString("long", "");

                        new ActUtils(getActContext()).startActWithData(RestaurantAllDetailsNewActivity.class, bn);


                    } else {
                        new ActUtils(getActContext()).startActWithData(FoodDeliveryHomeActivity.class, bn);
                    }


                } else {
                    resetData();
                }
            } else {
                resetData();
            }

        });

    }

    private void resetData() {
        isClicked = false;
        position = -1;
    }

    private Context getActContext() {
        return ServiceHomeActivity.this;
    }


    private void generateErrorView() {


        generalFunc.generateErrorView(errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");


        if (intCheck.isNetworkConnected()) {

            errorViewArea.setVisibility(View.GONE);
        } else {
            if (errorViewArea.getVisibility() != View.VISIBLE) {
                errorViewArea.setVisibility(View.VISIBLE);
            }
            errorView.setOnRetryListener(() -> getLanguageLabelServiceWise(position));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (generalFunc.prefHasKey(Utils.iServiceId_KEY) && generalFunc != null /*&& !ServiceModule.DeliverAllProduct*/) {

                generalFunc.removeValue(Utils.iServiceId_KEY);
            }

            if (myProfileFragment != null && isProfilefrg) {
                myProfileFragment.onResume();
            }

            if (myWalletFragment != null && isWalletfrg) {
                myWalletFragment.onResume();
            }

            if (myBookingFragment != null && isBookingfrg) {
                myBookingFragment.onResume();
            }


            obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));


            setUserInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }

        realmCartList = getCartData();
        cartArea.setVisibility(realmCartList != null && realmCartList.size() > 0 ? View.VISIBLE : View.GONE);
        int cnt = 0;

        if (realmCartList != null) {
            for (int i = 0; i < realmCartList.size(); i++) {
                cartDataServiceID = realmCartList.get(i).getiServiceId();
                cnt = cnt + GeneralFunctions.parseIntegerValue(0, realmCartList.get(i).getQty());
            }
        }

        fabcartIcon.setCount(cnt);
       /* if (serviceHomeAdapter!= null)
        {
            serviceHomeAdapter.setrealmCartList(realmCartList);
            serviceHomeAdapter.notifyDataSetChanged();
        }*/
    }

    private RealmResults<Cart> getCartData() {
        try {
            Realm realm = MyApp.getRealmInstance();
            return realm.where(Cart.class).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.backImgView).getVisibility() == View.VISIBLE) {
            if (generalFunc.prefHasKey(Utils.iServiceId_KEY) && generalFunc != null) {
                generalFunc.removeValue(Utils.iServiceId_KEY);
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onFragmentBackPressed()) {
            super.onBackPressed();
        }
    }


    public void onClick(View view) {
        Utils.hideKeyboard(getActContext());
        int i = view.getId();
        if (i == R.id.backImgView) {
            onBackPressed();
        }
        if (i == R.id.fabcartIcon) {
            if (!generalFunc.retrieveValue(Utils.iServiceId_KEY).equalsIgnoreCase(cartDataServiceID)) {
                generalFunc.storeData(Utils.iServiceId_KEY, cartDataServiceID);
                getLanguageLabelServiceWise();
            } else {
                new ActUtils(getActContext()).startAct(EditCartActivity.class);
            }
        }
    }

    private void getLanguageLabelServiceWise() {
        generateErrorView();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "changelanguagelabel");
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSystem", Utils.eSystem_Type);

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {
            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    HashMap<String, String> storeData = new HashMap<>();
                    storeData.put(Utils.languageLabelsKey, generalFunc.getJsonValue(Utils.message_str, responseString));
                    storeData.put(Utils.LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vCode", responseString));
                    storeData.put(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", responseString));
                    storeData.put(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", responseString));
                    generalFunc.storeData(storeData);

                    GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());

                    Utils.setAppLocal(getActContext());
                    GetUserData getUserData = new GetUserData(generalFunc, MyApp.getInstance().getApplicationContext());
                    getUserData.GetConfigDataForLocalStorage();

                    Bundle bn = new Bundle();
                    bn.putBoolean("isback", true);
//                            resetData();
                    new ActUtils(getActContext()).startAct(EditCartActivity.class);

                } else {
//                            resetData();
                }
            } else {
//                        resetData();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (!isClicked && this.position != position) {
            isClicked = true;
            this.position = position;
            generalFunc.storeData(Utils.iServiceId_KEY, generalCategoryList.get(position).get("iServiceId"));
            getLanguageLabelServiceWise(position);
        }
    }

    @Override
    public void redirectToCheckout() {
        new ActUtils(getActContext()).startAct(EditCartActivity.class);
    }

    @Override
    public void onTaskRun() {
        if (currentBannerPosition < (bannerAdapter.getCount() - 1)) {
            bannerViewPager.setCurrentItem((bannerViewPager.getCurrentItem() + 1), true);
        } else {
            bannerViewPager.setCurrentItem(0, true);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentBannerPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (foodDeliveryHomeFragment != null) {
            foodDeliveryHomeFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (restaurantAllDetailsNewFragment != null) {
            restaurantAllDetailsNewFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (foodDeliveryHomeFragment != null) {
            foodDeliveryHomeFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (myWalletFragment != null && isWalletfrg) {
            myWalletFragment.onActivityResult(requestCode, resultCode, data);
        } else if (myProfileFragment != null && isProfilefrg) {
            obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
            myProfileFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}