package com.adapter.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sessentaservices.usuarios.R;
import com.general.files.GeneralFunctions;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    private NotificationAdapter.OnItemClickListener mItemClickListener;
    boolean isFooterEnabled = false;


    String type;
    View footerView;
    NotificationAdapter.FooterViewHolder footerHolder;
    int topMargin = 0;
    int topMargin1 = 0;
    int maxheight = 0;
    int minheight = 0;

    public NotificationAdapter(Context mContext, ArrayList<HashMap<String, String>> list, String type, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.type = type;
        this.isFooterEnabled = isFooterEnabled;
        topMargin = (int) mContext.getResources().getDimension(R.dimen._15sdp);
        topMargin1 = (int) mContext.getResources().getDimension(R.dimen._10sdp);
        maxheight = (int) mContext.getResources().getDimension(R.dimen._110sdp);
        minheight = (int) mContext.getResources().getDimension(R.dimen._70sdp);
    }

    public void setOnItemClickListener(NotificationAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_view, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final NotificationAdapter.ViewHolder viewHolder = (NotificationAdapter.ViewHolder) holder;
            HashMap<String, String> map = list.get(position);
            String vTitle = map.get("vTitle");
            if (vTitle != null && !vTitle.equalsIgnoreCase("")) {
                viewHolder.titleTxt.setText(vTitle);
            } else {
                viewHolder.titleTxt.setVisibility(View.GONE);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.detailsTxt.getLayoutParams();
                params.setMargins(0, topMargin, 0, 0);
                viewHolder.detailsTxt.setLayoutParams(params);
            }

            String tDescription = map.get("tDescription");
            if (tDescription != null && !tDescription.equalsIgnoreCase("")) {
                viewHolder.detailsTxt.setText(tDescription);
            } else {
                viewHolder.detailsTxt.setVisibility(View.GONE);
            }

            if (viewHolder.titleTxt.getVisibility() == View.VISIBLE && viewHolder.detailsTxt.getVisibility() == View.VISIBLE) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.titleTxt.getLayoutParams();
                params.setMargins(0, topMargin1, 0, 0);
                viewHolder.titleTxt.setLayoutParams(params);
                viewHolder.cardArea.setMinimumHeight(maxheight);
            } else {
                viewHolder.cardArea.setMinimumHeight(minheight);
            }
            if (generalFunc.isRTLmode()) {
                viewHolder.btnArea.setBackground(mContext.getResources().getDrawable(R.drawable.login_border_rtl));
            }

            String dDateTime = map.get("dDateTime");
            if (dDateTime != null && !dDateTime.equalsIgnoreCase("")) {
                viewHolder.dateTxt.setText(dDateTime);
            }

            viewHolder.readMoreTxt.setText(map.get("readMoreLbl"));

            viewHolder.readMoreTxt.setOnClickListener(view -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onReadMoreItemClick(view, position);
                }
            });
        } else {
            this.footerHolder = (FooterViewHolder) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position) && isFooterEnabled) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == list.size();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isFooterEnabled) {
            return list.size() + 1;
        } else {
            return list.size();
        }

    }

    public void addFooterView() {
        this.isFooterEnabled = true;
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.GONE);
    }

    public interface OnItemClickListener {
        void onReadMoreItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MTextView titleTxt;
        public MTextView detailsTxt;
        public MTextView dateTxt;
        public MTextView readMoreTxt;
        public LinearLayout cardArea;
        public LinearLayout btnArea;


        public ViewHolder(View view) {
            super(view);

            titleTxt = (MTextView) view.findViewById(R.id.titleTxt);
            detailsTxt = (MTextView) view.findViewById(R.id.detailsTxt);
            dateTxt = (MTextView) view.findViewById(R.id.dateTxt);
            readMoreTxt = (MTextView) view.findViewById(R.id.readMoreTxt);
            cardArea = (LinearLayout) view.findViewById(R.id.cardArea);
            btnArea = (LinearLayout) view.findViewById(R.id.btnArea);


        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressArea = (LinearLayout) itemView;
        }
    }

}
