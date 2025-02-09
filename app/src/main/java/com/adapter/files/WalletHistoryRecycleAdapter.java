package com.adapter.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.sessentaservices.usuarios.R;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class WalletHistoryRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list;
    Context mContext;
    boolean isFooterEnabled;
    View footerView;
    FooterViewHolder footerHolder;

    public WalletHistoryRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list, GeneralFunctions generalFunc, boolean isFooterEnabled) {
        this.mContext = mContext;
        this.list = list;
        this.generalFunc = generalFunc;
        this.isFooterEnabled = isFooterEnabled;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list, parent, false);
            this.footerView = v;
            return new FooterViewHolder(v);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallethistory_design, parent, false);

            return new ViewHolder(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final HashMap<String, String> item = list.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.transactiondateTxt.setText(item.get("listingFormattedDate"));
            viewHolder.transactionDesVTxt.setText(item.get("tDescriptionConverted"));
            viewHolder.tranasctionBalVTxt.setText(item.get("FormattediBalance"));

            if (generalFunc.isRTLmode())
                viewHolder.tranasctionBalVTxt.setTextDirection(View.TEXT_DIRECTION_RTL);

            if (item.get("eType").equalsIgnoreCase("Credit")) {
                viewHolder.arrowImg.setImageResource(R.drawable.ic_credit_new);

            } else {
                viewHolder.arrowImg.setImageResource(R.drawable.ic_debit_new);
            }

            viewHolder.transactionDetailArea.setId(position);
        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
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
        return position == list.size();
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return list.size() + 1;
        } else {
            return list.size();
        }

    }

    public void addFooterView() {
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null)
            footerHolder.progressArea.setVisibility(View.VISIBLE);
    }

    public void removeFooterView() {
        if (footerHolder != null) {
            isFooterEnabled = false;
            footerHolder.progressArea.setVisibility(View.GONE);
        }

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView arrowImg;
        private LinearLayout transactionDetailArea;
        private MTextView transactionDesVTxt;
        private MTextView transactiondateTxt;
        private MTextView tranasctionBalVTxt;


        public ViewHolder(View view) {
            super(view);
            arrowImg = (ImageView) view.findViewById(R.id.arrowImg);
            transactionDetailArea = (LinearLayout) view.findViewById(R.id.transactionDetailArea);
            transactionDesVTxt = (MTextView) view.findViewById(R.id.transactionDesVTxt);
            transactiondateTxt = (MTextView) view.findViewById(R.id.transactiondateTxt);
            tranasctionBalVTxt = (MTextView) view.findViewById(R.id.tranasctionBalVTxt);

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView.findViewById(R.id.progressContainer);

        }
    }
}
