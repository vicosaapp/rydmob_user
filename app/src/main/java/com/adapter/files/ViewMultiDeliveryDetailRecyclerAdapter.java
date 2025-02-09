package com.adapter.files;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.ViewMultiDeliveryDetailsActivity;
import com.general.files.CustomLinearLayoutManager;
import com.general.files.GeneralFunctions;
import com.model.Trip_Status;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.MTextView;

import java.util.ArrayList;

/**
 * Created by Admin on 03-11-2017.
 */

public class ViewMultiDeliveryDetailRecyclerAdapter extends RecyclerView.Adapter<ViewMultiDeliveryDetailRecyclerAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<Trip_Status> list_item;
    Context mContext;
    ViewMultiDeliveryDetailsActivity mActivity;
    OnItemClickList onItemClickList;
    int lineColor=-1;

    public ViewMultiDeliveryDetailRecyclerAdapter(Context mContext, ViewMultiDeliveryDetailsActivity mActivity, ArrayList<Trip_Status> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;
        this.mActivity = mActivity;

        lineColor=mContext.getResources().getColor(R.color.appThemeColor_1);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_view_multi_delivery_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Trip_Status item = list_item.get(position);

       /* if (item.isShowDetails()) {
            viewHolder.arrowArea.setVisibility(View.VISIBLE);
        } else {
            viewHolder.arrowArea.setVisibility(View.INVISIBLE);
        }
*/
        viewHolder.arrowArea.setOnClickListener(view -> {

            if (viewHolder.recipientDetailArea.getVisibility() == View.VISIBLE) {
                viewHolder.recipientDetailArea.setVisibility(View.GONE);
                viewHolder.img_arrow.setRotation(0);
                viewHolder.amountDetailArea.setVisibility(View.GONE);

            } else {
                Transition fadeTransition = new Fade();
                fadeTransition.setDuration(1000);
                fadeTransition.addTarget(viewHolder.recipientDetailArea);
                TransitionManager.beginDelayedTransition(viewHolder.recipientDetailArea, fadeTransition);

                viewHolder.recipientDetailArea.setVisibility(View.VISIBLE);
                viewHolder.img_arrow.setRotation(-180);
                viewHolder.amountDetailArea.setVisibility(View.VISIBLE);


            }
        });


        String recipientName=item.getRecepientName();
        String recepientNum=item.getRecepientNum();

        viewHolder.recipientNoTxt.setText("" + item.getLBL_RECIPIENT() + " " + (position + 1));
        viewHolder.recipientNoTxt.setText("" + recipientName);
        viewHolder.contactTxt.setText(item.getLBL_CALL_TXT());
        viewHolder.cancelTxt.setText(item.getLBL_MESSAGE_ACTIVE_TRIP());
        viewHolder.responsibleForPaymentTxt.setText(item.getLBL_RESPONSIBLE_FOR_PAYMENT_TXT());

        if (list_item.size() == 1) {
            viewHolder.mainLine.setVisibility(View.INVISIBLE);
            viewHolder.mainLineTop.setVisibility(View.GONE);
            //viewHolder.nextDeliveryLocArea.setVisibility(View.GONE);
            viewHolder.arrowArea.performClick();
            viewHolder.arrowArea.setVisibility(View.GONE);
        }else {
            if (position==0){
                viewHolder.mainLineTop.setVisibility(View.GONE);
            }
        }


        if (Utils.checkText(item.getReceipent_Signature())) {
            viewHolder.ricipientSignTxt.setText(item.getLBL_VIEW_SIGN_TXT());
            viewHolder.ricipientSignTxt.setTag(position);
            viewHolder.ricipientSignTxt.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ricipientSignTxt.setVisibility(View.INVISIBLE);
        }

        viewHolder.ricipientSignTxt.setOnClickListener(v -> {
            if (onItemClickList != null) {
                onItemClickList.onItemClick("ViewSignature", position);
            }
        });


        if (item.getePaymentByReceiver().equalsIgnoreCase("Yes")) {
            viewHolder.responsibleForPaymentArea.setVisibility(View.VISIBLE);
        } else {
            viewHolder.responsibleForPaymentArea.setVisibility(View.GONE);
        }


        if (viewHolder.deliveryDetailsList.getAdapter() == null) {
            RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(mActivity);
            viewHolder.deliveryDetailsList.setLayoutManager(layoutManager);
            MultiDeliveryDetailAdapter adapter = new MultiDeliveryDetailAdapter(mActivity, item.getListOfDeliveryItems());
            viewHolder.deliveryDetailsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


        viewHolder.upcomingDeliveryLocArea.setVisibility(View.GONE);
        viewHolder.nextDeliveryLocTxt.setText("" + item.getLBL_RECIPIENT() + " " + (position + 1));
        viewHolder.recipient_no_txt.setText("" + (position + 1));
        viewHolder.recipientNameTxt.setText("" + recipientName);

        if (item.getShowMobile().equals("Yes")) {
            viewHolder.recipientPhnNumberTxt.setVisibility(View.VISIBLE);
            viewHolder.callMsgArea.setVisibility(View.VISIBLE);
            if (item.getePaymentByReceiver().equalsIgnoreCase("Yes")) {
                viewHolder.responsibleForPaymentArea.setVisibility(View.VISIBLE);
            } else {
                viewHolder.responsibleForPaymentArea.setVisibility(View.GONE);
            }

        /*    if (item.getePaymentBy().equalsIgnoreCase("Individual")) {
                viewHolder.amountToPayTxt.setText("" + item.getLBL_MULTI_AMOUNT_COLLECT_TXT() + " " + item.getFare_Payable());
                viewHolder.amountArea.setVisibility(View.VISIBLE);
            }*/


        } else {
            viewHolder.recipientPhnNumberTxt.setVisibility(View.GONE);
            viewHolder.callMsgArea.setVisibility(View.GONE);
            viewHolder.responsibleForPaymentArea.setVisibility(View.GONE);
        }

        viewHolder.recipientPhnNumberTxt.setText(item.getRecepientMaskNum());
        viewHolder.recipientAddressTxt.setText(item.getRecepientAddress());
        viewHolder.recipient_no_txt.setTextColor(mContext.getResources().getColor(R.color.black));
        viewHolder.iv_completed.setVisibility(View.GONE);

        String iActive=item.getiActive();
        boolean isCancelled=iActive.contains("Canceled");

        if (isCancelled || iActive.contains("Finished")) {
            viewHolder.statusArea.setCardBackgroundColor(Color.parseColor(item.getBGColor()));
            viewHolder.tripstatusTxt.setTextColor(Color.parseColor(item.getTEXTColor()));
            viewHolder.tripstatusTxt.setText("" + (isCancelled ? item.getLBL_CANCELED_TRIP_TXT() : item.getLBL_FINISHED_TRIP_TXT()));
            viewHolder.statusArea.setVisibility(View.VISIBLE);
            viewHolder.callMsgArea.setVisibility(View.GONE);
            viewHolder.recipient_no_txt.setTextColor(mContext.getResources().getColor(R.color.black));
        }


        if (item.getisActiveDelivery().equalsIgnoreCase("Yes")) {
            viewHolder.mainLine.setBackgroundColor(lineColor);
            viewHolder.recipient_no_txt.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.iv_red_dot.setVisibility(View.GONE);
            viewHolder.iv_completed.setVisibility(View.VISIBLE);
            viewHolder.responsibleForPaymentTxt.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drawable_payment_responsible));
            viewHolder.responsibleForPaymentTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            if (list_item.size() > 1) {
                if (position != 0) {
                    if (!list_item.get(position - 1).getiActive().contains("Finished")) {
                        viewHolder.mainLineTop.setBackgroundColor(lineColor);
                    }
                } else {
                    viewHolder.mainLineTop.setBackgroundColor(mContext.getResources().getColor(R.color.delivery_disable_line));
                }
            }
        } else {
            viewHolder.responsibleForPaymentTxt.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drawable_payment_responsible_disable));
            viewHolder.responsibleForPaymentTxt.setTextColor(mContext.getResources().getColor(R.color.black));
            viewHolder.mainLine.setBackgroundColor(mContext.getResources().getColor(R.color.delivery_disable_line));
            if (list_item.size() > 1) {
                viewHolder.recipient_no_txt.setTextColor(mContext.getResources().getColor(R.color.black));
                viewHolder.iv_completed.setVisibility(View.GONE);
                viewHolder.iv_red_dot.setVisibility(View.VISIBLE);
                if (position != 0) {
                    if (list_item.get(position - 1).getisActiveDelivery().equalsIgnoreCase("Yes")) {
                        viewHolder.mainLineTop.setBackgroundColor(lineColor);
                    }
                } else {
                    viewHolder.mainLineTop.setBackgroundColor(mContext.getResources().getColor(R.color.delivery_disable_line));
                }

            }
        }


        viewHolder.contactArea.setOnClickListener(view -> {
            if (onItemClickList != null) {
                onItemClickList.onItemClick(recepientNum, "call", position);
            }
        });
        viewHolder.cancelArea.setOnClickListener(view -> {
            if (onItemClickList != null) {
                onItemClickList.onItemClick(recepientNum, "msg", position);
            }
        });

        if (position==0){
            viewHolder.center_layout.setPadding(0,0,0,50);
        }else if (position==list_item.size()-1){
            viewHolder.center_layout.setPadding(0,0,0,0);
        }else {
            viewHolder.center_layout.setPadding(0,0,0,50);
        }
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recipientDetailArea;
        CardView cv2;

        MTextView nextDeliveryLocTxt, upcomingDeliveryLocTxt;
        MTextView contactTxt, cancelTxt, responsibleForPaymentTxt, amountToPayTxt, amountToPayTitleTxt;
        MTextView recipientNoTxt, recipientNameTxt, recipientPhnNumberTxt, recipientAddressTxt;
        LinearLayout nextDeliveryLocArea, upcomingDeliveryLocArea;
        LinearLayout contactArea, callMsgArea, cancelArea, arrowArea, responsibleForPaymentArea, amountArea, amountDetailArea;
        LinearLayout center_layout;

        CardView statusArea;
        MTextView tripstatusTitleTxt, tripstatusTxt, recipient_no_txt;
        MTextView ricipientSignTxt;

        ImageView iv_red_dot, iv_green_dot, img_arrow, iv_completed;
        View contactVerLine, mainLine, mainLineTop;
        RecyclerView deliveryDetailsList;

        public ViewHolder(View view) {
            super(view);
            recipientDetailArea = (LinearLayout) view.findViewById(R.id.recipientDetailArea);
            deliveryDetailsList = (RecyclerView) view.findViewById(R.id.deliveryDetailsList);
            cv2 = (CardView) view.findViewById(R.id.cv2);
            nextDeliveryLocTxt = (MTextView) view.findViewById(R.id.nextDeliveryLocTxt);
            recipientNoTxt = (MTextView) view.findViewById(R.id.recipientNoTxt);
            recipientNameTxt = (MTextView) view.findViewById(R.id.recipientNameTxt);
            recipientPhnNumberTxt = (MTextView) view.findViewById(R.id.recipientPhnNumberTxt);
            recipientAddressTxt = (MTextView) view.findViewById(R.id.recipientAddressTxt);
            upcomingDeliveryLocTxt = (MTextView) view.findViewById(R.id.upcomingDeliveryLocTxt);

            contactTxt = (MTextView) view.findViewById(R.id.contactTxt);
            cancelTxt = (MTextView) view.findViewById(R.id.cancelTxt);
            responsibleForPaymentTxt = (MTextView) view.findViewById(R.id.responsibleForPaymentTxt);
            responsibleForPaymentArea = (LinearLayout) view.findViewById(R.id.responsibleForPaymentArea);
            ricipientSignTxt = (MTextView) view.findViewById(R.id.ricipientSignTxt);

            amountToPayTxt = (MTextView) view.findViewById(R.id.amountToPayTxt);
            amountToPayTitleTxt = (MTextView) view.findViewById(R.id.amountToPayTitleTxt);
            amountArea = (LinearLayout) view.findViewById(R.id.amountArea);
            amountDetailArea = (LinearLayout) view.findViewById(R.id.amountDetailArea);


            statusArea = (CardView) view.findViewById(R.id.statusArea);
            tripstatusTxt = (MTextView) view.findViewById(R.id.tripstatusTxt);

            contactVerLine = (View) view.findViewById(R.id.contactVerLine);
            mainLine = (View) view.findViewById(R.id.mainLine);
            mainLineTop = (View) view.findViewById(R.id.mainLineTop);

            nextDeliveryLocArea = (LinearLayout) view.findViewById(R.id.nextDeliveryLocArea);
            upcomingDeliveryLocArea = (LinearLayout) view.findViewById(R.id.upcomingDeliveryLocArea);
            arrowArea = (LinearLayout) view.findViewById(R.id.arrowArea);

            contactArea = (LinearLayout) view.findViewById(R.id.contactArea);
            callMsgArea = (LinearLayout) view.findViewById(R.id.callMsgArea);
            cancelArea = (LinearLayout) view.findViewById(R.id.cancelArea);
            center_layout = (LinearLayout) view.findViewById(R.id.center_layout);

            img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            iv_red_dot = (ImageView) view.findViewById(R.id.iv_red_dot);
            iv_green_dot = (ImageView) view.findViewById(R.id.iv_green_dot);
            iv_completed = (ImageView) view.findViewById(R.id.iv_completed);
            recipient_no_txt = (MTextView) view.findViewById(R.id.recipient_no_txt);
            deliveryDetailsList = (RecyclerView) view.findViewById(R.id.deliveryDetailsList);


            int strokecolor = mActivity.getActContext().getResources().getColor(R.color.gray);
            int backColor = mActivity.getActContext().getResources().getColor(R.color.transparent_full);
            int cornorRadius = Utils.dipToPixels(mActivity.getActContext(), 5);
            int strokeWidth = Utils.dipToPixels(mActivity.getActContext(), 1);


            new CreateRoundedView(backColor, cornorRadius, strokeWidth, strokecolor,  contactArea);
            new CreateRoundedView(backColor, cornorRadius, strokeWidth, strokecolor,  cancelArea);

        }
    }


    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(String data, String type, int position);
        void onItemClick(String type, int position);
    }


}
