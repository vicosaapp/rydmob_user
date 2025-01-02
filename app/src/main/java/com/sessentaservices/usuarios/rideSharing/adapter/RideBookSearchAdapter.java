package com.sessentaservices.usuarios.rideSharing.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.databinding.FooterListBinding;
import com.sessentaservices.usuarios.databinding.ItemRideBookSearchBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class RideBookSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final GeneralFunctions generalFunc;
    private static final int TYPE_ITEM = 1, TYPE_FOOTER = 2;
    private final ArrayList<HashMap<String, String>> list;
    private final OnClickListener onClickListener;
    private FooterViewHolder footerHolder;
    private boolean isFooterEnabled = false;

    public RideBookSearchAdapter(GeneralFunctions generalFunc, @NonNull ArrayList<HashMap<String, String>> list, @NonNull OnClickListener listener) {
        this.generalFunc = generalFunc;
        this.list = list;
        this.onClickListener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(FooterListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new DataViewHolder(ItemRideBookSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {

            DataViewHolder viewHolder = (DataViewHolder) holder;
            HashMap<String, String> mapData = list.get(position);

            if (mapData.containsKey("vBookingNoTxt")) {
                viewHolder.binding.bookNoTxt.setText(mapData.get("vBookingNoTxt"));
            } else if (mapData.containsKey("vPublishedRideNoTxt")) {
                viewHolder.binding.bookNoTxt.setText(mapData.get("vPublishedRideNoTxt"));
            }

            ///////////////
            String driverImage = mapData.get("DriverImg");
            if (!Utils.checkText(driverImage)) {
                driverImage = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(driverImage), viewHolder.binding.driverProfileImgView).setErrorImagePath(R.mipmap.ic_no_pic_user).setPlaceholderImagePath(R.mipmap.ic_no_pic_user).build();
            viewHolder.binding.driverNameTxt.setText(mapData.get("DriverName"));
            if (Utils.checkText(mapData.get("vStatusBgColor"))) {
                viewHolder.binding.statusView.setCardBackgroundColor(Color.parseColor(mapData.get("vStatusBgColor")));
            }
            if (mapData.containsKey("eStatus")) {
                viewHolder.binding.statusTxt.setVisibility(View.VISIBLE);
                viewHolder.binding.statusTxt.setText(mapData.get("eStatusText"));
            } else {
                viewHolder.binding.statusTxt.setVisibility(View.GONE);
            }

            //////////////
            viewHolder.binding.startCityTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_DETAILS_START_LOC_TXT"));
            viewHolder.binding.startAddressTxt.setText(mapData.get("tStartLocation"));
            viewHolder.binding.startTimeTxt.setText(mapData.get("StartTime"));

            viewHolder.binding.endCityTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_DETAILS_END_LOC_TXT"));
            viewHolder.binding.endAddressTxt.setText(mapData.get("tEndLocation"));
            viewHolder.binding.endTimeTxt.setText(mapData.get("EndTime"));

            viewHolder.binding.dateTxt.setText(mapData.get("StartDate"));
            viewHolder.binding.priceTxt.setText(mapData.get("fPrice"));
            viewHolder.binding.priceMsgTxt.setText(mapData.get("PriceLabel"));

            viewHolder.binding.itemArea.setOnClickListener(v -> onClickListener.onItemClick(position, mapData));
        } else if (holder instanceof FooterViewHolder) {
            this.footerHolder = (FooterViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        if (isFooterEnabled) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() && isFooterEnabled) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addFooterView() {
        this.isFooterEnabled = true;
        notifyDataSetChanged();
        if (footerHolder != null) {
            footerHolder.binding.progressWheel.setVisibility(View.VISIBLE);
        }
    }

    public void removeFooterView() {
        if (footerHolder != null) {
            footerHolder.binding.progressWheel.setVisibility(View.GONE);
        }
    }

    protected static class FooterViewHolder extends RecyclerView.ViewHolder {

        private final FooterListBinding binding;

        private FooterViewHolder(@NonNull FooterListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    protected static class DataViewHolder extends RecyclerView.ViewHolder {

        private final ItemRideBookSearchBinding binding;

        private DataViewHolder(@NonNull ItemRideBookSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onItemClick(int position, HashMap<String, String> mapData);
    }
}