package com.sessentaservices.usuarios.deliverAll;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.activity.ParentActivity;
import com.adapter.files.deliverAll.RestaurantAdapter;
import com.sessentaservices.usuarios.R;
import com.general.files.GeneralFunctions;
import com.general.files.ActUtils;
import com.service.handler.ApiHandler;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchRestaurantListActivity extends ParentActivity implements RestaurantAdapter.RestaurantOnClickListener {


    MTextView titleTxt;
    ImageView backImgView;
    RecyclerView dataListRecyclerView;
    RestaurantAdapter restaurantAdapter;
    ArrayList<HashMap<String, String>> restaurantArr_List = new ArrayList<>();
    String next_page_str = "";
    boolean mIsLoading = false;
    boolean isNextPageAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant_list);
        initView();
    }

    public void initView() {

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        dataListRecyclerView = (RecyclerView) findViewById(R.id.dataListRecyclerView);
        dataListRecyclerView.setLayoutManager(new LinearLayoutManager(getActContext()));

        restaurantAdapter = new RestaurantAdapter(getActContext(), restaurantArr_List, false);
        dataListRecyclerView.setAdapter(restaurantAdapter);
        restaurantAdapter.setOnRestaurantItemClick(this);
        addToClickHandler(backImgView);
        setLabel();
        titleTxt.setText(getIntent().getStringExtra("cname"));

        getRestaurantList(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("long", 0),false);


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

                                                             getRestaurantList(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("long", 0),true);
                                                         } else if (isNextPageAvailable == false) {
                                                             restaurantAdapter.removeFooterView();
                                                         }
                                                     }
                                                 }

        );

    }


    private void getRestaurantList(final double latitude, final double longitude, boolean isLoadMore) {


        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "loadAvailableRestaurants");
        parameters.put("PassengerLat", "" + latitude);
        parameters.put("PassengerLon", "" + longitude);
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("cuisineId", getIntent().getStringExtra("cid"));
        parameters.put("eSystem", Utils.eSystem_Type);
        parameters.put("vLang", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        if (isLoadMore == true) {
            parameters.put("page", next_page_str);
        }
        ApiHandler.execute(getActContext(), parameters, responseString -> {
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseObj);
                String nextPage = generalFunc.getJsonValueStr("NextPage", responseObj);
                if (isDataAvail == true) {
                    if (!isLoadMore) {

                        restaurantArr_List.clear();
                        restaurantAdapter.notifyDataSetChanged();

                    }


                    JSONArray restaurant_Arr = generalFunc.getJsonArray("message", responseObj);

                    if (restaurant_Arr != null) {

                        String LBL_OPEN_NOW = "", LBL_CLOSED_TXT = "", LBL_NOT_ACCEPT_ORDERS_TXT = "";
                        int dimension = 0;
                        if (restaurant_Arr.length() > 0) {
                            dimension = getActContext().getResources().getDimensionPixelSize(R.dimen.restaurant_img_size_home_screen);

                            LBL_OPEN_NOW = generalFunc.retrieveLangLBl("Open Now", "LBL_OPEN_NOW");
                            LBL_CLOSED_TXT = generalFunc.retrieveLangLBl("close", "LBL_CLOSED_TXT");
                            LBL_NOT_ACCEPT_ORDERS_TXT = generalFunc.retrieveLangLBl("", "LBL_NOT_ACCEPT_ORDERS_TXT");
                        }

                        for (int i = 0; i < restaurant_Arr.length(); i++) {

                            HashMap<String, String> map = new HashMap<String, String>();
                            JSONObject restaurant_Obj = generalFunc.getJsonObject(restaurant_Arr, i);
                            map.put("vCompany", generalFunc.getJsonValueStr("vCompany", restaurant_Obj));
                            map.put("tClocation", generalFunc.getJsonValueStr("tClocation", restaurant_Obj));
                            map.put("iCompanyId", generalFunc.getJsonValueStr("iCompanyId", restaurant_Obj));
                            map.put("vPhone", generalFunc.getJsonValueStr("vPhone", restaurant_Obj));
                            String vImage = generalFunc.getJsonValueStr("vImage", restaurant_Obj);
                            map.put("vImage", vImage);
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


                            String Restaurant_MinOrderValue = generalFunc.getJsonValueStr("fMinOrderValueDisplay", restaurant_Obj);
                            map.put("Restaurant_MinOrderValue", Restaurant_MinOrderValue);
                            map.put("Restaurant_MinOrderValueConverted", generalFunc.convertNumberWithRTL(Restaurant_MinOrderValue));


                            map.put("eAvailable", generalFunc.getJsonValueStr("eAvailable", restaurant_Obj));
                            map.put("LBL_OPEN_NOW", LBL_OPEN_NOW);
                            map.put("LBL_CLOSED_TXT", LBL_CLOSED_TXT);
                            map.put("LBL_NOT_ACCEPT_ORDERS_TXT", LBL_NOT_ACCEPT_ORDERS_TXT);

                            map.put("timeslotavailable", generalFunc.getJsonValueStr("timeslotavailable", restaurant_Obj));

                            map.put("ispriceshow", generalFunc.getJsonValueStr("ispriceshow", responseObj));

                            restaurantArr_List.add(map);
                        }
                    }

                    generalFunc.storeData("PassengerLat", latitude + "");
                    generalFunc.storeData("PassengerLon", longitude + "");
                    restaurantAdapter.notifyDataSetChanged();
                    if (!nextPage.equals("") && !nextPage.equals("0")) {
                        next_page_str = nextPage;
                        isNextPageAvailable = true;
                    } else {
                        removeNextPageConfig();
                    }
                    mIsLoading = false;

                } else {
                    restaurantArr_List.clear();
                    restaurantAdapter.notifyDataSetChanged();
                }
            } else {
                generalFunc.showError();
            }
        });

    }


    public void removeNextPageConfig() {
        next_page_str = "";
        isNextPageAvailable = false;
        mIsLoading = false;
        restaurantAdapter.removeFooterView();
    }

    public void setLabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("", ""));
    }

    public Context getActContext() {
        return SearchRestaurantListActivity.this;
    }

    @Override
    public void setOnRestaurantclick(int position) {
        Bundle bn = new Bundle();
        bn.putString("iCompanyId", restaurantArr_List.get(position).get("iCompanyId"));
        bn.putString("Restaurant_Status", restaurantArr_List.get(position).get("Restaurant_Status"));
        bn.putString("Restaurant_Safety_Status",  restaurantArr_List.get(position).get("Restaurant_Safety_Status"));
        bn.putString("Restaurant_Safety_Icon",  restaurantArr_List.get(position).get("Restaurant_Safety_Icon"));
        bn.putString("Restaurant_Safety_URL",  restaurantArr_List.get(position).get("Restaurant_Safety_URL"));

        bn.putString("ispriceshow", restaurantArr_List.get(position).get("ispriceshow"));
        bn.putString("timeslotavailable", restaurantArr_List.get(position).get("timeslotavailable"));
        bn.putString("eAvailable", restaurantArr_List.get(position).get("eAvailable"));
        bn.putString("lat", getIntent().getDoubleExtra("lat", 0) + "");
        bn.putString("long", getIntent().getDoubleExtra("long", 0) + "");

        new ActUtils(getActContext()).startActWithData(RestaurantAllDetailsNewActivity.class, bn);

    }

    @Override
    public void setOnRestaurantclick(int position, boolean liked) {

    }


        public void onClick(View view) {
            int i = view.getId();
            Utils.hideKeyboard(getActContext());

            if (i == R.id.backImgView) {
                onBackPressed();

            }


        }

}
