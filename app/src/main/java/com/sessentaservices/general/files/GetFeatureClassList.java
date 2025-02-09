package com.general.files;


import com.sessentaservices.usuarios.BuildConfig;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class GetFeatureClassList {

    private static String resourceFilePath = "res/layout/";
    private static String drawableFileFilePath = "res/drawable/";
    private static String GeneralDrawableFileFilePath = "res/drawable-nodpi/";
    private static String resourcePath = "layout";

    public static HashMap<String, String> getAllGeneralClasses() {
        HashMap<String, String> classParams = new HashMap<>();

        ArrayList<String> biddingClassList = new ArrayList<>();
        biddingClassList.add(BuildConfig.APPLICATION_ID + ".RequestBidInfoActivity");
        biddingClassList.add(resourceFilePath + "activity_request_bid_info");
        biddingClassList.add("com.fragments.BiddingBookingFragment");
        biddingClassList.add(resourceFilePath + "fragment_bidding");
        biddingClassList.add(BuildConfig.APPLICATION_ID + ".BiddingTaskActivity");
        biddingClassList.add(resourceFilePath + "activity_bidding_task");
        biddingClassList.add("com.adapter.files.BiddingReceivedRecycleAdapter");
        biddingClassList.add(resourceFilePath + "item_bid_received_design");
        biddingClassList.add(BuildConfig.APPLICATION_ID + ".BiddingHistoryDetailActivity");
        biddingClassList.add(resourceFilePath + "activity_bidding_history_detail");
        biddingClassList.add("com.adapter.files.AllBiddingRecycleAdapter");
        biddingClassList.add(resourceFilePath + "item_bidding_layout");
        biddingClassList.add(resourceFilePath + "dialog_bidding_re_offre");

        classParams.put("BID_SERVICE", "No");
        for (String item : biddingClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("BID_SERVICE", "Yes");
                break;
            }
        }

        ArrayList<String> voipServiceClassList = new ArrayList<>();
        voipServiceClassList.add("com.general.call.LocalHandler");
        voipServiceClassList.add("com.general.call.SinchHandler");
        voipServiceClassList.add("com.general.call.TwilioHandler");
        voipServiceClassList.add("com.general.call.AppRTCAudioManager");
        voipServiceClassList.add("com.general.call.CameraCapturerCompat");
        voipServiceClassList.add("com.general.call.V3CallScreen");
        voipServiceClassList.add("com.general.call.V3CallListener");
        voipServiceClassList.add(resourceFilePath + "v3_call_screen");

        classParams.put("VOIP_SERVICE", "No");
        for (String item : voipServiceClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("VOIP_SERVICE", "Yes");
                break;
            }
        }

        ArrayList<String> advertisementClassList = new ArrayList<>();
        advertisementClassList.add("com.general.files.OpenAdvertisementDialog");
        advertisementClassList.add(resourceFilePath + "advertisement_dailog");

        classParams.put("ADVERTISEMENT_MODULE", "No");
        for (String item : advertisementClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("ADVERTISEMENT_MODULE", "Yes");
                break;
            }
        }

        //GiftCard Module
        ArrayList<String> giftCardClassList = new ArrayList<>();
        giftCardClassList.add(BuildConfig.APPLICATION_ID + ".giftcard.adapter.GiftCardImagePagerAdapter");
        giftCardClassList.add(resourceFilePath + "item_giftcard");
        giftCardClassList.add(BuildConfig.APPLICATION_ID + ".giftcard.GiftCardRedeemActivity");
        giftCardClassList.add(resourceFilePath + "activity_giftcard_redeem");
        giftCardClassList.add(BuildConfig.APPLICATION_ID + ".giftcard.GiftCardSendActivity");
        giftCardClassList.add(resourceFilePath + "activity_giftcard_send");
        giftCardClassList.add(BuildConfig.APPLICATION_ID + ".giftcard.GiftRedeemDialog");
        giftCardClassList.add(resourceFilePath + "dialog_gift_redeem_details");

        classParams.put("GIFTCARD_MODULE", "No");
        for (String item : giftCardClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("GIFTCARD_MODULE", "Yes");
                break;
            }
        }

        //nearbyservice Module

        ArrayList<String> nearByServiceClassList = new ArrayList<>();
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.adapter.NearByCategoryAdapter");
        nearByServiceClassList.add(resourceFilePath + "item_near_by_category");
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.adapter.NearByServiceAdapter");
        nearByServiceClassList.add(resourceFilePath + "item_near_by_services");
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.adapter.ServiceActionAdapter");
        nearByServiceClassList.add(resourceFilePath + "item_near_by_service_action");
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.adapter.ServiceCategoryAdapter");
        nearByServiceClassList.add(resourceFilePath + "item_near_by_service_category");
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.NearByDetailsActivity");
        nearByServiceClassList.add(resourceFilePath + "activity_near_by_details");
        nearByServiceClassList.add(BuildConfig.APPLICATION_ID + ".nearbyservice.NearByServicesActivity");
        nearByServiceClassList.add(resourceFilePath + "activity_near_by_service");

        classParams.put("NEARBY_MODULE", "No");
        for (String item : nearByServiceClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("NEARBY_MODULE", "Yes");
                break;
            }
        }

        //RentItem Module
        ArrayList<String> rentItemClassList = new ArrayList<>();
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.PhotosAdapter");
        rentItemClassList.add(resourceFilePath + "item_slider_images");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentCategoryAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_item_category");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentCategorySubCategoryAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_item_sub_category");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentGalleryImagesAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_gallery_list");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentItemDataRecommendedAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_item_post");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentItemListPostAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_item_list_post");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentItemStepsAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_step_view");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentPaymentPlanAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_item_payment_plan");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.adapter.RentPickupTimeSlotAdapter");
        rentItemClassList.add(resourceFilePath + "item_rent_pickup_time_slot");

        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentCategoryFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_category");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemDynamicDetailsFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_dynamic_details");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemLocationDetailsFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_location_details");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemPaymentPlanFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_payment_plan_details");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemPhotosFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_photos");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemPickupAvailabilityFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_pickup_availability");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemPricingDetailsFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_pricing_details");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentItemReviewAllDetailsFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_item_review_all_details");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.fragment.RentSubCategoryFragment");
        rentItemClassList.add(resourceFilePath + "fragment_rent_sub_category");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.model.RentItemData");

        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemFilterActivity");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_filter");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemHomeActivity");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_home");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemInquiry");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_inquiry");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemListPostActivity");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_list_post");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemNewPostActivity");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_new_post");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemOwnerInfo");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_owner_info");
        rentItemClassList.add(BuildConfig.APPLICATION_ID + ".rentItem.RentItemReviewPostActivity");
        rentItemClassList.add(resourceFilePath + "activity_rent_item_review_post");


        classParams.put("RENT_ITEM_SERVICE", "No");
        for (String item : rentItemClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("RENT_ITEM_SERVICE", "Yes");
                break;
            }
        }



        //RideSharing Module

        ArrayList<String> rideSharingClassList = new ArrayList<>();
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.adapter.RideBookSearchAdapter");
        rideSharingClassList.add(resourceFilePath + "item_ride_book_search");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.adapter.RideDocumentAdapter");
        rideSharingClassList.add(resourceFilePath + "item_ride_sharing_document");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.adapter.RideMyPassengerAdapter");
        rideSharingClassList.add(resourceFilePath + "item_ride_my_passenger_list");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.adapter.RideMyPublishAdapter");
        rideSharingClassList.add(resourceFilePath + "item_ride_my_list");

        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RideBookingFragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_booking");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RidePublishFragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_publish");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RidePublishStep1Fragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_publish_step_1");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RidePublishStep2Fragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_publish_step_2");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RidePublishStep3Fragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_publish_step_3");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.fragment.RidePublishStep4Fragment");
        rideSharingClassList.add(resourceFilePath + "fragment_ride_publish_step_4");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.model.RidePublishData");

        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideBook");
        rideSharingClassList.add(resourceFilePath + "activity_ride_book");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideBookDetails");
        rideSharingClassList.add(resourceFilePath + "activity_ride_book_details");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideBookingRequestedActivity");
        rideSharingClassList.add(resourceFilePath + "activity_ride_booking_request");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideBookSearchList");
        rideSharingClassList.add(resourceFilePath + "activity_ride_book_search");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideBookSummary");
        rideSharingClassList.add(resourceFilePath + "activity_ride_book_summary");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideMyDetails");
        rideSharingClassList.add(resourceFilePath + "activity_ride_my_details");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideMyList");
        rideSharingClassList.add(resourceFilePath + "activity_ride_my_list");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RideUploadDocActivity");
        rideSharingClassList.add(resourceFilePath + "activity_ride_upload_doc");
        rideSharingClassList.add(BuildConfig.APPLICATION_ID + ".rideSharing.RidePublish");
        rideSharingClassList.add(resourceFilePath + "activity_ride_publish");



        classParams.put("RIDESHARE_MODULE", "No");
        for (String item : rideSharingClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("RIDESHARE_MODULE", "Yes");
                break;
            }
        }



        //Tracking Module
        ArrayList<String> trackServiceClassList = new ArrayList<>();
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.adapter.TrackAnyAdapter");
        trackServiceClassList.add(resourceFilePath + "item_track_any_user");

        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnyList");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_list");
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnyLiveTracking");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_live_tracking");
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnyProfileSetup");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_profile_setup");
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnyProfileVehicle");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_profile_vehicle");
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnySignUp");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_sign_up");
        trackServiceClassList.add(BuildConfig.APPLICATION_ID + ".trackService.TrackAnyVerification");
        trackServiceClassList.add(resourceFilePath + "activity_track_any_verification");

        classParams.put("TRACKING_MODULE", "No");
        for (String item : trackServiceClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("TRACKING_MODULE", "Yes");
                break;
            }
        }


        ArrayList<String> linkedInClassList = new ArrayList<>();
        linkedInClassList.add("com.general.files.OpenLinkedinDialog");
        linkedInClassList.add("com.general.files.RegisterLinkedinLoginResCallBack");

        classParams.put("LINKEDIN_MODULE", "No");
        for (String item : linkedInClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("LINKEDIN_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> cardIOClassList = new ArrayList<>();
        cardIOClassList.add("io.card.payment.CardIOActivity");

        classParams.put("CARD_IO", "No");
        for (String item : cardIOClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("CARD_IO", "Yes");
                break;
            }
        }

        ArrayList<String> poolClassList = new ArrayList<>();
        poolClassList.add("com.adapter.files.PoolSeatsSelectionAdapter");
        poolClassList.add(resourceFilePath + "design_no_of_seats_pool");

        classParams.put("POOL_MODULE", "No");
        for (String item : poolClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("POOL_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> liveChatClassList = new ArrayList<>();
        liveChatClassList.add("com.livechatinc.inappchat.ChatWindowActivity");

        classParams.put("LIVE_CHAT", "No");
        for (String item : liveChatClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("LIVE_CHAT", "Yes");
                break;
            }
        }

        ArrayList<String> goPayModuleClassList = new ArrayList<>();
        goPayModuleClassList.add(resourceFilePath + "design_transfer_money");

        classParams.put("GO_PAY_SECTION", "No");
        for (String item : goPayModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("GO_PAY_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> deliverAllGeneralClassList = new ArrayList<>();
        deliverAllGeneralClassList.add(BuildConfig.APPLICATION_ID + ".PrescriptionActivity");
        deliverAllGeneralClassList.add(resourceFilePath + "activity_prescription");
        deliverAllGeneralClassList.add(BuildConfig.APPLICATION_ID + ".PrescriptionHistoryImagesActivity");
        deliverAllGeneralClassList.add(resourceFilePath + "activity_prescription_history_images");


        classParams.put("DELIVER_ALL_GENERAL", "No");
        for (String item : deliverAllGeneralClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("DELIVER_ALL_GENERAL", "Yes");
                break;
            }
        }


        ArrayList<String> deliverAllClassList = new ArrayList<>();
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.ActiveOrderActivity");
        deliverAllClassList.add(resourceFilePath + "activity_active_order");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.AddBasketActivity");
        deliverAllClassList.add(resourceFilePath + "activity_add_basket");
        deliverAllClassList.add(resourceFilePath + "item_basket_option");
        deliverAllClassList.add(resourceFilePath + "item_basket_toping");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.CheckOutActivity");
        deliverAllClassList.add(resourceFilePath + "activity_check_out");
        deliverAllClassList.add(resourceFilePath + "item_checkout_row");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.EditCartActivity");
        deliverAllClassList.add(resourceFilePath + "activity_edit_cart");
        deliverAllClassList.add(resourceFilePath + "item_edit_cart_row");
        deliverAllClassList.add(resourceFilePath + "dialog_cart_edit_options");
        deliverAllClassList.add(resourceFilePath + "item_option");
        deliverAllClassList.add(resourceFilePath + "item_toping");
        deliverAllClassList.add(resourceFilePath + "dialog_cart_repeat");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.FoodDeliveryHomeActivity");
        deliverAllClassList.add(resourceFilePath + "deliver_all_dialog_filter");
        deliverAllClassList.add(resourceFilePath + "item_filter");
        deliverAllClassList.add(resourceFilePath + "dialog_relevance");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.FoodRatingActivity");
        deliverAllClassList.add(resourceFilePath + "activity_food_rating");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.LoginActivity");
        deliverAllClassList.add(resourceFilePath + "activity_login");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.OrderDetailsActivity");
        deliverAllClassList.add(resourceFilePath + "activity_order_details");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.OrderPlaceConfirmActivity");
        deliverAllClassList.add(resourceFilePath + "activity_order_place_confirm");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.PaymentCardActivity");
        deliverAllClassList.add(resourceFilePath + "activity_payment_card");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.RestaurantAllDetailsActivity");
        deliverAllClassList.add(resourceFilePath + "activity_restaurant_all_details");
        deliverAllClassList.add(resourceFilePath + "item_menu_design");
        deliverAllClassList.add(resourceFilePath + "item_cusines");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.RestaurantAllDetailsNewActivity");
        deliverAllClassList.add(resourceFilePath + "activity_restaurant_all_details_new");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.RestaurantsSearchActivity");
        deliverAllClassList.add(resourceFilePath + "activity_restaurants_search");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.SearchFoodActivity");
        deliverAllClassList.add(resourceFilePath + "activity_search_food");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.SearchRestaurantListActivity");
        deliverAllClassList.add(resourceFilePath + "activity_search_restaurant_list");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.ServiceHomeActivity");
        deliverAllClassList.add(resourceFilePath + "activity_service_home");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.SignUpActivity");
        deliverAllClassList.add(resourceFilePath + "activity_sign_up");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.TrackOrderActivity");
        deliverAllClassList.add(resourceFilePath + "activity_track_order");
        deliverAllClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.ViewCardActivity");
        deliverAllClassList.add(resourceFilePath + "activity_view_card");
        deliverAllClassList.add("com.realmModel.Options");
        deliverAllClassList.add("com.realmModel.Topping");
        deliverAllClassList.add("com.realmModel.Cart");
        deliverAllClassList.add("com.viewholder.RestaurntCataChildViewHolder");
        deliverAllClassList.add("com.viewholder.RestaurntCataParentViewHolder");
        deliverAllClassList.add("com.viewholder.BiodataExpandable");
        deliverAllClassList.add("com.viewholder.ChildViewHolder");
        deliverAllClassList.add("com.viewholder.ParentViewHolder");
        deliverAllClassList.add("com.model.RestaurantCataChildModel");
        deliverAllClassList.add("com.model.RestaurantCataParentModel");
        deliverAllClassList.add("com.adapter.files.deliverAll.MultiItemOptionAddonPagerAdapter");
        deliverAllClassList.add(resourceFilePath + "item_basket_option_addon");
        deliverAllClassList.add("com.adapter.files.deliverAll.ActiveOrderAdapter");
        deliverAllClassList.add(resourceFilePath + "item_list_orders");
        deliverAllClassList.add("com.adapter.files.deliverAll.CuisinesAdapter");
        deliverAllClassList.add(resourceFilePath + "item_cuisines");
        deliverAllClassList.add(resourceFilePath + "item_cuisines_new");
        deliverAllClassList.add("com.adapter.files.deliverAll.ExpandableRecyclerAdapter");
        deliverAllClassList.add("com.adapter.files.deliverAll.ExpandableRecyclerAdapterHelper");
        deliverAllClassList.add("com.adapter.files.deliverAll.FoodSearchAdapter");
        deliverAllClassList.add(resourceFilePath + "item_food_search_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.FoodDeliveryHomeAdapter");
        deliverAllClassList.add(resourceFilePath + "item_food_delivery_home_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.MenuAdapter");
        deliverAllClassList.add(resourceFilePath + "item_menu");
        deliverAllClassList.add("com.adapter.files.deliverAll.RatingDialogRecycAdapter");
        deliverAllClassList.add(resourceFilePath + "item_rating_dialog_list_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.RestaurantAdapter");
        deliverAllClassList.add(resourceFilePath + "item_restaurant_list_design");
        deliverAllClassList.add(resourceFilePath + "item_restaurant_list_design_new_vertical_23");
        deliverAllClassList.add("com.adapter.files.deliverAll.RecommendedListAdapter");
        deliverAllClassList.add(resourceFilePath + "recommended_item_list_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.RestaurantmenuAdapter");
        deliverAllClassList.add(resourceFilePath + "item_menu_headerview");
        deliverAllClassList.add(resourceFilePath + "item_resmenu_gridview");
        deliverAllClassList.add(resourceFilePath + "item_menu_list");
        deliverAllClassList.add("com.adapter.files.deliverAll.RestaurantRecomMenuAdapter");
        deliverAllClassList.add(resourceFilePath + "item_resmenu_gridview");
        deliverAllClassList.add("com.adapter.files.deliverAll.RestaurantSearchAdapter");
        deliverAllClassList.add(resourceFilePath + "item_restaurant_list_search_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.ServiceHomeAdapter");
        deliverAllClassList.add(resourceFilePath + "item_service_banner_design");
        deliverAllClassList.add("com.adapter.files.deliverAll.TrackOrderAdapter");
        deliverAllClassList.add(resourceFilePath + "track_order_item_design");
        deliverAllClassList.add("com.fragments.OrderFragment");
        classParams.put("DELIVER_ALL", "No");
        for (String item : deliverAllClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("DELIVER_ALL", "Yes");
                break;
            }
        }
        ArrayList<String> storeIndividualDayAvailabilityClassList = new ArrayList<>();
        storeIndividualDayAvailabilityClassList.add(resourceFilePath + "design_opening_hr_cell");
        classParams.put("STORE_INDIVIDUALDAY_AVAILABILITY_MODULE", "No");
        for (String item : storeIndividualDayAvailabilityClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("STORE_INDIVIDUALDAY_AVAILABILITY_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> itemNameWiseSearchClassList = new ArrayList<>();
        itemNameWiseSearchClassList.add(drawableFileFilePath + "ic_star_color1_24dp");
        classParams.put("STORE_SEARCH_BY_ITEM_NAME_MODULE", "No");
        for (String item : itemNameWiseSearchClassList) {
            if ((item.startsWith(drawableFileFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(drawableFileFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("STORE_SEARCH_BY_ITEM_NAME_MODULE", "Yes");
                break;
            }
        }


        ArrayList<String> safetyClassList = new ArrayList<>();
        safetyClassList.add("com.general.files.MySwipeRefreshLayout");
        safetyClassList.add("com.general.files.SafetyDialog");
        safetyClassList.add(resourceFilePath + "dailog_safety_measure");
        classParams.put("SAFETY_MODULE", "No");
        for (String item : safetyClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("SAFETY_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> safetyRatingClassList = new ArrayList<>();
        safetyRatingClassList.add("com.general.files.CustomHorizontalScrollView");
        safetyRatingClassList.add("com.general.files.OnSwipeTouchListener");
        safetyRatingClassList.add("com.general.files.SlideAnimationUtil");
        classParams.put("SAFETY_RATING_MODULE", "No");
        for (String item : safetyRatingClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("SAFETY_RATING_MODULE", "Yes");
                break;
            }
        }


        ArrayList<String> safetyCheckListClassList = new ArrayList<>();
        safetyCheckListClassList.add(resourcePath + "desgin_passenger_limit");
        safetyCheckListClassList.add(GeneralDrawableFileFilePath + "ic_profile_with_mask.png");
        classParams.put("SAFETY_CHECK_LIST_MODULE", "No");
        for (String item : safetyCheckListClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || (item.startsWith(GeneralDrawableFileFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(GeneralDrawableFileFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("SAFETY_RATING_MODULE", "Yes");
                break;
            }
        }


        ArrayList<String> faceMaskVerificationClassList = new ArrayList<>();
        faceMaskVerificationClassList.add(resourcePath + "desgin_mask_verification");
        classParams.put("SAFETY_FACEMASK_VERIFICATION_MODULE", "No");
        for (String item : faceMaskVerificationClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("SAFETY_FACEMASK_VERIFICATION_MODULE", "Yes");
                break;
            }
        }


        ArrayList<String> EighteenPlusFeatureClassList = new ArrayList<>();
        EighteenPlusFeatureClassList.add(resourcePath + "desgin_preference_help");
        EighteenPlusFeatureClassList.add(resourcePath + "design_upload_service_pic");
        EighteenPlusFeatureClassList.add(resourcePath + "proof_dialog_design");
        classParams.put("EIGHTEEN_PLUS_FEATURE_MODULE", "No");
        for (String item : EighteenPlusFeatureClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("EIGHTEEN_PLUS_FEATURE_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> ManualTollFeatureClassList = new ArrayList<>();
        ManualTollFeatureClassList.add(resourcePath + "desgin_preference_help");
        ManualTollFeatureClassList.add(resourcePath + "design_upload_service_pic");
        ManualTollFeatureClassList.add(resourcePath + "proof_dialog_design");
        classParams.put("MANUAL_TOLL_FEATURE_MODULE", "No");
        for (String item : ManualTollFeatureClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("MANUAL_TOLL_FEATURE_MODULE", "Yes");
                break;
            }
        }
        ArrayList<String> genieClassList = new ArrayList<>();
        genieClassList.add("com.adapter.files.deliverAll.AddStoreItemAdapter");
        genieClassList.add("com.adapter.files.deliverAll.StoreCategoryAdapter");
        genieClassList.add(resourceFilePath + "item_store_category");
        genieClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.BuyAnythingActivity");
        genieClassList.add(resourceFilePath + "activity_buy_anything");
        genieClassList.add(resourceFilePath + "bill_details_genie");
        genieClassList.add(resourceFilePath + "design_add_item");
        genieClassList.add(resourceFilePath + "dailog_buyanything_deatils");
        genieClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.FindStoreActivity");
        genieClassList.add(resourceFilePath + "activity_find_store");
        genieClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.GenieDeliveryHomeActivity");
        genieClassList.add(resourceFilePath + "activity_genie_delivery_home");
        genieClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.SearchStoreForGenie.kt");
        genieClassList.add(resourceFilePath + "activity_search_store_for_genie");
        genieClassList.add(resourceFilePath + "view_bill_genie");
        genieClassList.add(resourceFilePath + "review_item_genie");
        genieClassList.add(resourceFilePath + "selectstoreinfo");
        genieClassList.add(resourceFilePath + "deliverall_design_toolbar_new");
        genieClassList.add(resourceFilePath + "confirm_item_genie");
        genieClassList.add(resourceFilePath + "bill_item_genie");
        genieClassList.add(BuildConfig.APPLICATION_ID + ".deliverAll.checkAddView");
        genieClassList.add(resourceFilePath + "activity_check_add_view");
        genieClassList.add("com.general.files.PreferenceDailog");
        classParams.put("GENIE_FEATURE_MODULE", "No");
        for (String item : genieClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("GENIE_FEATURE_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> contactLessDeliveryClassList = new ArrayList<>();
        contactLessDeliveryClassList.add("com.adapter.files.MoreInstructionAdapter");
        contactLessDeliveryClassList.add(BuildConfig.APPLICATION_ID + ".UserPrefrenceActivity");
        contactLessDeliveryClassList.add(resourceFilePath + "activity_user_prefrence");
        contactLessDeliveryClassList.add(resourceFilePath + "desgin_preference_help");
        contactLessDeliveryClassList.add(resourceFilePath + "item_instructions");
        classParams.put("CONTACTLESS_DELIVERY_MODULE", "No");
        for (String item : contactLessDeliveryClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("CONTACTLESS_DELIVERY_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> MultiSingleStoreFeatureClassList = new ArrayList<>();
        MultiSingleStoreFeatureClassList.add("com.fragments.RestaurantAllDetailsNewFragment");
        // MultiSingleStoreFeatureClassList.add(resourceFilePath + "activity_restaurant_all_details_new");
        MultiSingleStoreFeatureClassList.add("com.fragments.FoodDeliveryHomeFragment");
        classParams.put("MULTI_SINGLE_STORE_MODULE", "No");
        for (String item : MultiSingleStoreFeatureClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("MULTI_SINGLE_STORE_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> categoryWiseStoreFeatureClassList = new ArrayList<>();
        categoryWiseStoreFeatureClassList.add("com.adapter.files.deliverAll.MainCategoryAdapter");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "item_category_child");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "item_restaurant_list_header_design");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "item_restaurant_list_header_design_23");
        categoryWiseStoreFeatureClassList.add("com.adapter.files.deliverAll.RestaurantChildAdapter");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "item_childrestaurant_list_design");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "item_childrestaurant_list_design_23");
        categoryWiseStoreFeatureClassList.add(resourceFilePath + "main_category_wise_store");
        categoryWiseStoreFeatureClassList.add("com.model.MainCategoryModel");
        classParams.put("CATEGORY_WISE_STORE_MODULE", "No");
        for (String item : categoryWiseStoreFeatureClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("CATEGORY_WISE_STORE_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> multiDeliveryClassList = new ArrayList<>();
        multiDeliveryClassList.add(BuildConfig.APPLICATION_ID + ".EnterMultiDeliveryDetailsActivity");
        multiDeliveryClassList.add(resourceFilePath + "activity_multi_delivery_detail");
        multiDeliveryClassList.add(BuildConfig.APPLICATION_ID + ".MultiDeliverySecondPhaseActivity");
        multiDeliveryClassList.add(resourceFilePath + "activity_multi_second_phase");
        multiDeliveryClassList.add(BuildConfig.APPLICATION_ID + ".MultiDeliveryThirdPhaseActivity");
        multiDeliveryClassList.add(resourceFilePath + "activity_multi_third_phase_multi");
        multiDeliveryClassList.add(BuildConfig.APPLICATION_ID + ".ViewMultiDeliveryDetailsActivity");
        multiDeliveryClassList.add(resourceFilePath + "activity_multi_delivery_details");
        multiDeliveryClassList.add("com.model.Delivery_Data");
        multiDeliveryClassList.add("com.model.Multi_Delivery_Data");
        multiDeliveryClassList.add("com.model.Multi_Dest_Info_Detail_Data");
        multiDeliveryClassList.add("com.model.Trip_Status");
        multiDeliveryClassList.add("com.adapter.files.MultiDestinationItemAdapter");
        multiDeliveryClassList.add(resourceFilePath + "multi_dest_item_layout");
        multiDeliveryClassList.add("com.adapter.files.MultiListViewAdapter");
        multiDeliveryClassList.add(resourceFilePath + "design_view_multi_delivery_detail");
        multiDeliveryClassList.add("com.adapter.files.ViewMultiDeliveryDetailRecyclerAdapter");
        multiDeliveryClassList.add(resourceFilePath + "multi_delivery_details_design");
        multiDeliveryClassList.add("com.adapter.files.MultiDeliveryDetailAdapter");
        multiDeliveryClassList.add("com.adapter.files.MultiPaymentTypeRecyclerAdapter");
        multiDeliveryClassList.add(resourceFilePath + "multi_item_selected_payment_method");
        multiDeliveryClassList.add("com.fragments.MultiScrollSupportMapFragment");
        multiDeliveryClassList.add("com.general.files.MapComparator");
        multiDeliveryClassList.add("com.general.files.DataParser");
        multiDeliveryClassList.add("com.general.files.TextWatcherExtendedListener");
        multiDeliveryClassList.add("com.general.files.CustomLinearLayoutManager");

        classParams.put("MULTI_DELIVERY", "No");
        for (String item : multiDeliveryClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("MULTI_DELIVERY", "Yes");
                break;
            }
        }


        ArrayList<String> uberXGeneralClassList = new ArrayList<>();
        uberXGeneralClassList.add(BuildConfig.APPLICATION_ID + ".UberXActivity");
        uberXGeneralClassList.add(resourceFilePath + "activity_uber_x");
        uberXGeneralClassList.add("com.adapter.files.UberXCategoryAdapter");
        uberXGeneralClassList.add(resourceFilePath + "item_rdu_banner_design");
        uberXGeneralClassList.add(resourceFilePath + "item_uberx_cat_grid_design");
        uberXGeneralClassList.add(resourceFilePath + "item_uberx_cat_list_design");
        uberXGeneralClassList.add("com.adapter.files.UberXBannerPagerAdapter");
        uberXGeneralClassList.add(resourceFilePath + "item_uber_x_banner_design");


        classParams.put("UBERX_GENERAL_SERVICE", "No");
        for (String item : uberXGeneralClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                Logger.e("EXIST_FILE", "::" + item);
                classParams.put("UBERX_GENERAL_SERVICE", "Yes");
                break;
            }
        }

        ArrayList<String> uberXClassList = new ArrayList<>();

        uberXClassList.add(BuildConfig.APPLICATION_ID + ".UberxCartActivity");
        uberXClassList.add(resourceFilePath + "activity_uberx_cart");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".UberxFilterActivity");
        uberXClassList.add(resourceFilePath + "activity_uberx_filter");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".UberXSelectServiceActivity");
        uberXClassList.add(resourceFilePath + "activity_uber_xselect_service");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".UfxOrderDetailsActivity");
        uberXClassList.add(resourceFilePath + "activity_ufx_order_details");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".UfxPaymentActivity");
        uberXClassList.add(resourceFilePath + "activity_ufx_payment");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".CarWashBookingDetailsActivity");
        uberXClassList.add(resourceFilePath + "activity_car_wash_booking_details");
        uberXClassList.add(resourceFilePath + "item_uberxcheckout_row");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".MoreInfoActivity");
        uberXClassList.add(resourceFilePath + "activity_more_info");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".ProviderInfoActivity");
        uberXClassList.add(resourceFilePath + "activity_provider_info");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".MoreServiceInfoActivity");
        uberXClassList.add(resourceFilePath + "activity_more_service_info");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".BookingSummaryActivity");
        uberXClassList.add(resourceFilePath + "activity_booking_summary");
        uberXClassList.add(BuildConfig.APPLICATION_ID + ".ScheduleDateSelectActivity");
        uberXClassList.add(resourceFilePath + "activity_schedule_date_select");
        uberXClassList.add("com.fragments.ServiceFragment");
        uberXClassList.add(resourceFilePath + "fragment_services");
        uberXClassList.add("com.fragments.ReviewsFragment");
        uberXClassList.add(resourceFilePath + "fragment_reviews");
        uberXClassList.add("com.fragments.GalleryFragment");
        uberXClassList.add(resourceFilePath + "fragment_gallery");
        uberXClassList.add("com.fragments.PaymentFrag");
        uberXClassList.add(resourceFilePath + "activity_ufx_payment");
        uberXClassList.add("com.fragments.OrderDetailsFrag");
        uberXClassList.add(resourceFilePath + "activity_ufx_order_details");
        //  uberXClassList.add("com.adapter.files.GalleryImagesRecyclerAdapter");
        //  uberXClassList.add(resourceFilePath + "item_gallery_list");
        uberXClassList.add("com.adapter.files.UberXOnlineDriverListAdapter");
        uberXClassList.add(resourceFilePath + "item_online_driver_list_design");
        uberXClassList.add("com.adapter.files.UberXSubCategoryListAdapter");
        uberXClassList.add(resourceFilePath + "item_sub_catagory_list_design");
        uberXClassList.add("com.adapter.files.UberXMoreCategoryAdapter");

        uberXClassList.add("com.adapter.files.UberXMainCatagoryAdapter");
        uberXClassList.add(resourceFilePath + "item_uberx_main_catagory_design");
        uberXClassList.add("com.adapter.files.UberXHomeActBannerAdapter");
        uberXClassList.add("com.adapter.files.TowTruckVehicleAdpater");
        uberXClassList.add(resourceFilePath + "item_towtruck_vehicle_list_design");
        uberXClassList.add("com.adapter.files.TimeSlotAdapter");
        uberXClassList.add(resourceFilePath + "item_timeslot_view");
        uberXClassList.add("com.realmModel.CarWashCartData");
        uberXClassList.add("com.dialogs.OpenTutorDetailDialog");
        uberXClassList.add("com.adapter.files.PinnedCategorySectionListAdapter");
        uberXClassList.add(resourceFilePath + "service_list_item");
        uberXClassList.add("com.adapter.files.DriverFeedbackRecycleAdapter");
        uberXClassList.add(resourceFilePath + "item_feedback_design");

        classParams.put("UBERX_SERVICE", "No");
        for (String item : uberXClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                Logger.e("EXIST_FILE", "::" + item);
                classParams.put("UBERX_SERVICE", "Yes");
                break;
            }
        }

        ArrayList<String> onGoingJobsClassList = new ArrayList<>();
        onGoingJobsClassList.add(BuildConfig.APPLICATION_ID + ".OnGoingTripsActivity");
        onGoingJobsClassList.add(resourceFilePath + "activity_ongoingtrips_layout");
        onGoingJobsClassList.add(BuildConfig.APPLICATION_ID + ".OnGoingTripDetailsActivity");
        onGoingJobsClassList.add(resourceFilePath + "layout_ongoing_trip_details");
        onGoingJobsClassList.add("com.adapter.files.OngoingTripAdapter");
        onGoingJobsClassList.add(resourceFilePath + "item_ongoing_trips_detail");
        onGoingJobsClassList.add("com.adapter.files.OnGoingTripDetailAdapter");
        onGoingJobsClassList.add(resourceFilePath + "item_design_ongoing_trip_cell");
        onGoingJobsClassList.add("com.general.files.CustomSupportMapFragment");

        classParams.put("ON_GOING_JOB_SECTION", "No");
        for (String item : onGoingJobsClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("ON_GOING_JOB_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> commonDeliveryTypeClassList = new ArrayList<>();
        ArrayList<String> commonDeliveryTypeClassList_tmp = new ArrayList<>();
        commonDeliveryTypeClassList.add(BuildConfig.APPLICATION_ID + ".CommonDeliveryTypeSelectionActivity");
        commonDeliveryTypeClassList.add(resourceFilePath + "activity_multi_type_selection");
        // commonDeliveryTypeClassList.add("com.general.files.OpenCatType");
        commonDeliveryTypeClassList.add("com.adapter.files.DeliveryBannerAdapter");
        commonDeliveryTypeClassList.add(resourceFilePath + "item_delivery_banner_design");
        commonDeliveryTypeClassList.add("com.adapter.files.DeliveryIconAdapter");
        commonDeliveryTypeClassList.add(resourceFilePath + "delivery_icon_layout");
        commonDeliveryTypeClassList.add("com.adapter.files.SubCategoryItemAdapter");
        commonDeliveryTypeClassList.add(resourceFilePath + "item_icon_layout");
        commonDeliveryTypeClassList.add("com.model.DeliveryIconDetails");
        commonDeliveryTypeClassList.add(resourceFilePath + "activity_food_delivery_home");
        commonDeliveryTypeClassList.add(resourceFilePath + "activity_food_delivery_home_23");
        commonDeliveryTypeClassList.add(resourceFilePath + "dialog_selectnavigation_view");
        commonDeliveryTypeClassList_tmp.addAll(commonDeliveryTypeClassList);

        classParams.put("COMMON_DELIVERY_TYPE_SECTION", "No");
        for (String item : commonDeliveryTypeClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("COMMON_DELIVERY_TYPE_SECTION", "Yes");
            } else {
                commonDeliveryTypeClassList_tmp.remove(item);
            }
        }
        commonDeliveryTypeClassList.clear();
        commonDeliveryTypeClassList.addAll(commonDeliveryTypeClassList_tmp);

        ArrayList<String> newsClassList = new ArrayList<>();
        newsClassList.add(BuildConfig.APPLICATION_ID + ".NotificationActivity");
        newsClassList.add(resourceFilePath + "activity_notification");
        newsClassList.add(BuildConfig.APPLICATION_ID + ".NotificationDetailsActivity");
        newsClassList.add(resourceFilePath + "activity_notification_details");
        newsClassList.add("com.fragments.NotiFicationFragment");
        newsClassList.add(resourceFilePath + "fragment_notification");
        newsClassList.add("com.adapter.files.NotificationAdapter");
        newsClassList.add(resourceFilePath + "item_notification_view");

        classParams.put("NEWS_SECTION", "No");
        for (String item : newsClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("NEWS_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> rentalClassList = new ArrayList<>();
        rentalClassList.add(BuildConfig.APPLICATION_ID + ".RentalDetailsActivity");
        rentalClassList.add(resourceFilePath + "activity_rental_details");
        rentalClassList.add(BuildConfig.APPLICATION_ID + ".RentalInfoActivity");
        rentalClassList.add(resourceFilePath + "activity_rental_info");
        rentalClassList.add("com.adapter.files.PackageAdapter");
        rentalClassList.add(resourceFilePath + "item_package_row");

        classParams.put("RENTAL_FEATURE", "No");
        for (String item : rentalClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("RENTAL_FEATURE", "Yes");
                break;
            }
        }

        ArrayList<String> businessProfileClassList = new ArrayList<>();
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".SelectOrganizationActivity");
        businessProfileClassList.add(resourceFilePath + "activity_organization_list");
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".OrganizationActivity");
        businessProfileClassList.add(resourceFilePath + "activity_organization");
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".MyBusinessProfileActivity");
        businessProfileClassList.add(resourceFilePath + "activity_my_business_profile");
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".BusinessSetupActivity");
        businessProfileClassList.add(resourceFilePath + "activity_business_setup");
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".BusinessProfileActivity");
        businessProfileClassList.add(resourceFilePath + "activity_business_profile");
        businessProfileClassList.add(BuildConfig.APPLICATION_ID + ".BusinessSelectPaymentActivity");
        businessProfileClassList.add(resourceFilePath + "activity_business_select_payment");
        businessProfileClassList.add("com.fragments.BusinessProfileIntroFragment");
        businessProfileClassList.add(resourceFilePath + "fragment_business_profile_intro");
        businessProfileClassList.add("com.fragments.BusinessProfileListFragment");
        businessProfileClassList.add(resourceFilePath + "fragment_business_profile_list");
        businessProfileClassList.add("com.adapter.files.OrganizationPinnedSectionListAdapter");
        businessProfileClassList.add(resourceFilePath + "organization_list_item");
        businessProfileClassList.add("com.adapter.files.OrganizationListItem");

        classParams.put("BUSINESS_PROFILE_FEATURE", "No");
        for (String item : businessProfileClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("BUSINESS_PROFILE_FEATURE", "Yes");
                break;
            }
        }


        ArrayList<String> deliveryModuleClassList = new ArrayList<>();
        deliveryModuleClassList.add(BuildConfig.APPLICATION_ID + ".EnterDeliveryDetailsActivity");
        deliveryModuleClassList.add(resourceFilePath + "activity_enter_delivery_details");
        deliveryModuleClassList.add("com.model.DeliveryDetails");

        classParams.put("DELIVERY_MODULE", "No");
        for (String item : deliveryModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("DELIVERY_MODULE", "Yes");
                break;
            }
        }

        ArrayList<String> rideModuleClassList = new ArrayList<>();
        rideModuleClassList.add("com.fragments.DriverAssignedHeaderFragment");
        rideModuleClassList.add(resourceFilePath + "fragment_driver_assigned_header");
        rideModuleClassList.add("com.fragments.DriverDetailFragment");
        rideModuleClassList.add(resourceFilePath + "fragment_driver_detail");

        classParams.put("RIDE_SECTION", "No");
        for (String item : rideModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("RIDE_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> rduModuleClassList = new ArrayList<>();
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".MainActivity");
        rduModuleClassList.add(resourceFilePath + "activity_main");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".PrefranceActivity");
        rduModuleClassList.add(resourceFilePath + "activity_prefrance");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".RatingActivity");
        rduModuleClassList.add(resourceFilePath + "activity_rating");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".MyBookingsActivity");
        rduModuleClassList.add(resourceFilePath + "activity_my_bookings");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".HistoryActivity");
        rduModuleClassList.add(resourceFilePath + "activity_history");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".HistoryDetailActivity");
        rduModuleClassList.add(resourceFilePath + "activity_history_detail");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".ConfirmEmergencyTapActivity");
        rduModuleClassList.add(resourceFilePath + "activity_confirm_emergency_tap");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".EmergencyContactActivity");
        rduModuleClassList.add(resourceFilePath + "activity_emergency_contact");
        rduModuleClassList.add(BuildConfig.APPLICATION_ID + ".ChatActivity");
        rduModuleClassList.add(resourceFilePath + "design_trip_chat_detail_dialog");
        rduModuleClassList.add("com.fragments.BookingFragment");
        //    rduModuleClassList.add(resourceFilePath + "fragment_booking");
        rduModuleClassList.add("com.fragments.CabSelectionFragment");
        rduModuleClassList.add(resourceFilePath + "fragment_new_cab_selection");
        rduModuleClassList.add(resourceFilePath + "input_box_view");
        rduModuleClassList.add(resourceFilePath + "fare_detail_design");
        rduModuleClassList.add(resourceFilePath + "custom_marker");
        rduModuleClassList.add(resourceFilePath + "dailog_faredetails");
        rduModuleClassList.add("com.fragments.HistoryFragment");
        //     rduModuleClassList.add(resourceFilePath + "fragment_booking");
        rduModuleClassList.add("com.fragments.MainHeaderFragment");
        rduModuleClassList.add(resourceFilePath + "fragment_main_header");
        rduModuleClassList.add("com.fragments.PickUpLocSelectedFragment");
        rduModuleClassList.add(resourceFilePath + "fragment_pick_up_loc_selected");
        rduModuleClassList.add("com.adapter.files.CabTypeAdapter");
        rduModuleClassList.add(resourceFilePath + "item_design_cab_type");
        rduModuleClassList.add("com.adapter.files.ChatMessagesRecycleAdapter");
        rduModuleClassList.add(resourceFilePath + "message");
        rduModuleClassList.add("com.adapter.files.CustSpinnerAdapter");
        rduModuleClassList.add(resourceFilePath + "item_spinnertextview");
        rduModuleClassList.add("com.adapter.files.DatesRecyclerAdapter");
        rduModuleClassList.add(resourceFilePath + "item_dates_design");
        rduModuleClassList.add("com.adapter.files.EmergencyContactRecycleAdapter");
        rduModuleClassList.add(resourceFilePath + "emergency_contact_item");
        rduModuleClassList.add("com.adapter.files.HistoryRecycleAdapter");
        rduModuleClassList.add(resourceFilePath + "item_history_design");
        rduModuleClassList.add("com.adapter.files.MyBookingsRecycleAdapter");
        rduModuleClassList.add(resourceFilePath + "item_my_bookings_design");
        rduModuleClassList.add("com.adapter.files.RequestPickUpAdapter");
        rduModuleClassList.add(resourceFilePath + "item_design_request_pick_up");
        rduModuleClassList.add("com.adapter.files.UberXBannerAdapter");
        rduModuleClassList.add(resourceFilePath + "item_rdu_banner_design");
        rduModuleClassList.add("com.general.files.LoadAvailableCab");
        rduModuleClassList.add("com.dialogs.RequestNearestCab");

        classParams.put("RDU_SECTION", "No");
        for (String item : rduModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("RDU_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> tollModuleClassList = new ArrayList<>();
        tollModuleClassList.add("com.utils.CommonUtilities.TOLLURL");

        classParams.put("TOLL_MODULE", "No");

        Class<?> commonUtilsClz = CommonUtilities.class;
        try {
            Field field_chk = commonUtilsClz.getField("TOLLURL");
            if (field_chk != null) {
                classParams.put("TOLL_MODULE", "Yes");
            } else {
                classParams.put("TOLL_MODULE", "No");
            }
        } catch (Exception ex) {
            classParams.put("TOLL_MODULE", "No");
        }


        ArrayList<String> bookForSomeoneModuleClassList = new ArrayList<>();
        bookForSomeoneModuleClassList.add(BuildConfig.APPLICATION_ID + ".BookSomeOneElsePickContactActivity");
        bookForSomeoneModuleClassList.add(resourceFilePath + "design_book_someone_pick_contact");
        bookForSomeoneModuleClassList.add("com.adapter.files.BookSomeOneContactListAdapter");
        bookForSomeoneModuleClassList.add("com.model.ContactModel");
        bookForSomeoneModuleClassList.add(resourceFilePath + "design_book_someone_details");
        bookForSomeoneModuleClassList.add(resourceFilePath + "item_book_someone_contact_design");
        bookForSomeoneModuleClassList.add(resourceFilePath + "item_book_someone_contacts_header");

        classParams.put("BOOK_FOR_ELSE_SECTION", "No");
        for (String item : bookForSomeoneModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("BOOK_FOR_ELSE_SECTION", "Yes");
                break;
            }
        }


        ArrayList<String> favDriverModuleClassList = new ArrayList<>();
        favDriverModuleClassList.add(BuildConfig.APPLICATION_ID + ".FavouriteDriverActivity");
        favDriverModuleClassList.add(resourceFilePath + "activity_favorite_driver");
        favDriverModuleClassList.add("com.adapter.files.FavoriteDriverAdapter");
        favDriverModuleClassList.add("com.fragments.FavDriverFragment");
        favDriverModuleClassList.add(resourceFilePath + "fragment_fav_driver");
        favDriverModuleClassList.add(resourceFilePath + "item_fav_driver_design");
        favDriverModuleClassList.add(resourceFilePath + "item_fav_driver_heder_design");
        favDriverModuleClassList.add("com.general.files.favDriverComparator");

        classParams.put("FAV_DRIVER_SECTION", "No");
        for (String item : favDriverModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("FAV_DRIVER_SECTION", "Yes");
                break;
            }
        }

        ArrayList<String> stopOverPointModuleClassList = new ArrayList<>();
        stopOverPointModuleClassList.add("com.adapter.files.StopOverPointsAdapter");
        stopOverPointModuleClassList.add("com.general.files.StopOverComparator");
        stopOverPointModuleClassList.add("com.general.files.StopOverPointsDataParser");
        stopOverPointModuleClassList.add("com.model.Stop_Over_Points_Data");
        stopOverPointModuleClassList.add(resourceFilePath + "design_stopover_locations");

        classParams.put("STOP_OVER_POINT_SECTION", "No");
        for (String item : stopOverPointModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("STOP_OVER_POINT_SECTION", "Yes");
                break;
            }
        }


        ArrayList<String> donationModuleClassList = new ArrayList<>();
        donationModuleClassList.add("com.adapter.files.DonationBannerAdapter");
        donationModuleClassList.add(resourceFilePath + "item_donation_banner_design");
        donationModuleClassList.add(BuildConfig.APPLICATION_ID + ".DonationActivity");
        donationModuleClassList.add(resourceFilePath + "activity_donation");
        classParams.put("DONATION_SECTION", "No");
        for (String item : donationModuleClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("DONATION_SECTION", "Yes");
                break;
            }
        }


        ArrayList<String> flyClassList = new ArrayList<>();
        flyClassList.add("com.adapter.files.SkyPortsRecyclerAdapter");
        flyClassList.add(resourceFilePath + "design_choose_skyports");
        flyClassList.add("com.fragments.FlyStationSelectionFragment");
        flyClassList.add(resourceFilePath + "design_skyports_bottom_view");
        classParams.put("FLY_MODULE", "No");
        for (String item : flyClassList) {
            if ((item.startsWith(resourceFilePath) && MyApp.getInstance().getApplicationContext() != null && Utils.isResourceFileExist(MyApp.getInstance().getApplicationContext(), item.replace(resourceFilePath, ""), resourcePath)) || Utils.isClassExist(item)) {
                classParams.put("FLY_MODULE", "Yes");
                break;
            }
        }


        /** Removal file of libraries **/
        voipServiceClassList.add("libs/sinch_lib.aar");
        voipServiceClassList.add("Libs folder remove file called 'sinch_lib' Or any lib which is related to SINCH");
        cardIOClassList.add("Go to App's Level build.Gradle File and Remove Library 'io.card:android-sdk'");
        liveChatClassList.add("AGo to pp's Level build.Gradle File and Remove Library 'com.github.livechat:chat-window-android'");
        tollModuleClassList.add("Remove Declaration of Toll URL from CommonUtilities File And remove portion of Toll cost from code. (Remove Network execution of toll URL)");
        /** Removal file of libraries **/

        if (classParams.get("BID_SERVICE") != null && classParams.get("BID_SERVICE").equalsIgnoreCase("Yes")) {
            classParams.put("BID_SERVICE_FILES", android.text.TextUtils.join(",", biddingClassList));
        }

        if (classParams.get("VOIP_SERVICE") != null && classParams.get("VOIP_SERVICE").equalsIgnoreCase("Yes")) {
            classParams.put("VOIP_SERVICE_FILES", android.text.TextUtils.join(",", voipServiceClassList));
        }

        if (classParams.get("ADVERTISEMENT_MODULE") != null && classParams.get("ADVERTISEMENT_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("ADVERTISEMENT_MODULE_FILES", android.text.TextUtils.join(",", advertisementClassList));
        }

        if (classParams.get("GIFTCARD_MODULE") != null && classParams.get("GIFTCARD_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("GIFTCARD_MODULE_FILES", android.text.TextUtils.join(",", giftCardClassList));
        }
        if (classParams.get("NEARBY_MODULE") != null && classParams.get("NEARBY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("NEARBY_MODULE_FILES", android.text.TextUtils.join(",", nearByServiceClassList));
        }
        if (classParams.get("RENT_ITEM_SERVICE") != null && classParams.get("RENT_ITEM_SERVICE").equalsIgnoreCase("Yes")) {
            classParams.put("RENT_ITEM_SERVICE_FILES", android.text.TextUtils.join(",", rentItemClassList));
        }
        if (classParams.get("RIDESHARE_MODULE") != null && classParams.get("RIDESHARE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("RIDESHARE_MODULE_FILES", android.text.TextUtils.join(",", rideSharingClassList));
        }
        if (classParams.get("TRACKING_MODULE") != null && classParams.get("TRACKING_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("TRACKING_MODULE_FILES", android.text.TextUtils.join(",", trackServiceClassList));
        }

        if (classParams.get("LINKEDIN_MODULE") != null && classParams.get("LINKEDIN_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("LINKEDIN_MODULE_FILES", android.text.TextUtils.join(",", linkedInClassList));
        }

        if (classParams.get("POOL_MODULE") != null && classParams.get("POOL_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("POOL_MODULE_FILES", android.text.TextUtils.join(",", poolClassList));
        }

        if (classParams.get("CARD_IO") != null && classParams.get("CARD_IO").equalsIgnoreCase("Yes")) {
            classParams.put("CARD_IO_FILES", android.text.TextUtils.join(",", cardIOClassList));
        }

        if (classParams.get("LIVE_CHAT") != null && classParams.get("LIVE_CHAT").equalsIgnoreCase("Yes")) {
            classParams.put("LIVE_CHAT_FILES", android.text.TextUtils.join(",", liveChatClassList));
        }

        if (classParams.get("DELIVER_ALL") != null && classParams.get("DELIVER_ALL").equalsIgnoreCase("Yes")) {
            classParams.put("DELIVER_ALL_FILES", android.text.TextUtils.join(",", deliverAllClassList));
        }

        if (classParams.get("DELIVER_ALL_GENERAL") != null && classParams.get("DELIVER_ALL_GENERAL").equalsIgnoreCase("Yes")) {
            classParams.put("DELIVER_ALL_GENERAL_FILES", android.text.TextUtils.join(",", deliverAllGeneralClassList));
        }

        if (classParams.get("MULTI_DELIVERY") != null && classParams.get("MULTI_DELIVERY").equalsIgnoreCase("Yes")) {
            classParams.put("MULTI_DELIVERY_FILES", android.text.TextUtils.join(",", multiDeliveryClassList));
        }

        if (classParams.get("UBERX_SERVICE") != null && classParams.get("UBERX_SERVICE").equalsIgnoreCase("Yes")) {
            classParams.put("UBERX_FILES", android.text.TextUtils.join(",", uberXClassList));
        }
        if (classParams.get("UBERX_GENERAL_SERVICE") != null && classParams.get("UBERX_GENERAL_SERVICE").equalsIgnoreCase("Yes")) {
            classParams.put("UBERX_GENERAL_SERVICE_FILES", android.text.TextUtils.join(",", uberXGeneralClassList));
        }

        if (classParams.get("ON_GOING_JOB_SECTION") != null && classParams.get("ON_GOING_JOB_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("ON_GOING_JOB_SECTION_FILES", android.text.TextUtils.join(",", onGoingJobsClassList));
        }

        if (classParams.get("COMMON_DELIVERY_TYPE_SECTION") != null && classParams.get("COMMON_DELIVERY_TYPE_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("COMMON_DELIVERY_TYPE_SECTION_FILES", android.text.TextUtils.join(",", commonDeliveryTypeClassList));
        }

        if (classParams.get("NEWS_SECTION") != null && classParams.get("NEWS_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("NEWS_SERVICE_FILES", android.text.TextUtils.join(",", newsClassList));
        }

        if (classParams.get("RENTAL_FEATURE") != null && classParams.get("RENTAL_FEATURE").equalsIgnoreCase("Yes")) {
            classParams.put("RENTAL_SERVICE_FILES", android.text.TextUtils.join(",", rentalClassList));
        }

        if (classParams.get("BUSINESS_PROFILE_FEATURE") != null && classParams.get("BUSINESS_PROFILE_FEATURE").equalsIgnoreCase("Yes")) {
            classParams.put("BUSINESS_PROFILE_FILES", android.text.TextUtils.join(",", businessProfileClassList));
        }

        if (classParams.get("DELIVERY_MODULE") != null && classParams.get("DELIVERY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("DELIVERY_MODULE_FILES", android.text.TextUtils.join(",", deliveryModuleClassList));
        }

        if (classParams.get("RIDE_SECTION") != null && classParams.get("RIDE_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("RIDE_SECTION_FILES", android.text.TextUtils.join(",", rideModuleClassList));
        }

        if (classParams.get("RDU_SECTION") != null && classParams.get("RDU_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("RDU_SECTION_FILES", android.text.TextUtils.join(",", rduModuleClassList));
        }

        if (classParams.get("TOLL_MODULE") != null && classParams.get("TOLL_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("TOLL_MODULE_FILES", android.text.TextUtils.join(",", tollModuleClassList));
        }

        if (classParams.get("BOOK_FOR_ELSE_SECTION") != null && classParams.get("BOOK_FOR_ELSE_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("BOOK_FOR_ELSE_SECTION_FILES", android.text.TextUtils.join(",", bookForSomeoneModuleClassList));
        }

        if (classParams.get("STORE_INDIVIDUALDAY_AVAILABILITY_MODULE") != null && classParams.get("STORE_INDIVIDUALDAY_AVAILABILITY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("STORE_INDIVIDUALDAY_AVAILABILITY_MODULE_FILES", android.text.TextUtils.join(",", storeIndividualDayAvailabilityClassList));
        }

        if (classParams.get("STORE_SEARCH_BY_ITEM_NAME_MODULE") != null && classParams.get("STORE_SEARCH_BY_ITEM_NAME_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("STORE_SEARCH_BY_ITEM_NAME_MODULE_FILES", android.text.TextUtils.join(",", itemNameWiseSearchClassList));
        }

        if (classParams.get("SAFETY_MODULE") != null && classParams.get("SAFETY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("SAFETY_MODULE_FILES", android.text.TextUtils.join(",", safetyClassList));
        }

        if (classParams.get("SAFETY_RATING_MODULE") != null && classParams.get("SAFETY_RATING_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("SAFETY_RATING_MODULE_FILES", android.text.TextUtils.join(",", safetyRatingClassList));
        }

        if (classParams.get("SAFETY_CHECK_LIST_MODULE") != null && classParams.get("SAFETY_CHECK_LIST_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("SAFETY_CHECK_LIST_MODULE_FILES", android.text.TextUtils.join(",", safetyCheckListClassList));
        }

        if (classParams.get("SAFETY_FACEMASK_VERIFICATION_MODULE") != null && classParams.get("SAFETY_FACEMASK_VERIFICATION_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("SAFETY_FACEMASK_VERIFICATION_MODULE_FILES", android.text.TextUtils.join(",", faceMaskVerificationClassList));
        }

        if (classParams.get("EIGHTEEN_PLUS_FEATURE_MODULE") != null && classParams.get("EIGHTEEN_PLUS_FEATURE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("EIGHTEEN_PLUS_FEATURE_MODULE_FILES", android.text.TextUtils.join(",", EighteenPlusFeatureClassList));
        }

        if (classParams.get("MANUAL_TOLL_FEATURE_MODULE") != null && classParams.get("MANUAL_TOLL_FEATURE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("MANUAL_TOLL_FEATURE_MODULE_FILES", android.text.TextUtils.join(",", ManualTollFeatureClassList));
        }

        if (classParams.get("GENIE_FEATURE_MODULE") != null && classParams.get("GENIE_FEATURE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("GENIE_FEATURE_MODULE_FILES", android.text.TextUtils.join(",", genieClassList));
        }
        if (classParams.get("CONTACTLESS_DELIVERY_MODULE") != null && classParams.get("CONTACTLESS_DELIVERY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("CONTACTLESS_DELIVERY_MODULE_FILES", android.text.TextUtils.join(",", contactLessDeliveryClassList));
        }
        if (classParams.get("MULTI_SINGLE_STORE_MODULE") != null && classParams.get("MULTI_SINGLE_STORE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("MULTI_SINGLE_STORE_MODULE_FILES", android.text.TextUtils.join(",", MultiSingleStoreFeatureClassList));
        }
        if (classParams.get("CATEGORY_WISE_STORE_MODULE") != null && classParams.get("CATEGORY_WISE_STORE_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("CATEGORY_WISE_STORE_MODULE_FILES", android.text.TextUtils.join(",", categoryWiseStoreFeatureClassList));
        }

        if (classParams.get("FAV_DRIVER_SECTION") != null && classParams.get("FAV_DRIVER_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("FAV_DRIVER_SECTION_FILES", android.text.TextUtils.join(",", favDriverModuleClassList));
        }

        if (classParams.get("STOP_OVER_POINT_SECTION") != null && classParams.get("STOP_OVER_POINT_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("STOP_OVER_POINT_SECTION_FILES", android.text.TextUtils.join(",", stopOverPointModuleClassList));
        }


        if (classParams.get("DONATION_SECTION") != null && classParams.get("DONATION_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("DONATION_SECTION_FILES", android.text.TextUtils.join(",", donationModuleClassList));
        }

        if (classParams.get("GO_PAY_SECTION") != null && classParams.get("GO_PAY_SECTION").equalsIgnoreCase("Yes")) {
            classParams.put("GO_PAY_SECTION_FILES", android.text.TextUtils.join(",", goPayModuleClassList));
        }


        if (classParams.get("FLY_MODULE") != null && classParams.get("FLY_MODULE").equalsIgnoreCase("Yes")) {
            classParams.put("FLY_MODULE_FILES", android.text.TextUtils.join(",", flyClassList));
        }
        classParams.put("PACKAGE_NAME", BuildConfig.APPLICATION_ID);

        return classParams;
    }
}