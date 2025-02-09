package com.adapter.files.deliverAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.sessentaservices.usuarios.R;
import com.general.files.GeneralFunctions;
import com.utils.LoadImage;
import com.utils.Logger;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ViewHolder> {

    ArrayList<HashMap<String, String>> foodSearchArrList;
    Context mContext;
    OnItemClickList onItemClickList;

    public FoodSearchAdapter(Context context, ArrayList<HashMap<String, String>> mapArrayList) {
        this.mContext = context;
        this.foodSearchArrList = mapArrayList;
    }

    @Override
    public FoodSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_search_design, parent, false);
        FoodSearchAdapter.ViewHolder viewHolder = new FoodSearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(FoodSearchAdapter.ViewHolder holder, int position) {

        HashMap<String, String> mapData = foodSearchArrList.get(position);

        holder.name.setText(mapData.get("vItemType"));
        holder.name.setSelected(true);
        holder.price.setText(mapData.get("fPrice"));

        String vItemDesc = mapData.get("vItemDesc");
        if (vItemDesc != null && !vItemDesc.equalsIgnoreCase("")) {
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(vItemDesc);
        } else {
            holder.desc.setVisibility(View.GONE);
        }
        holder.viewArea.setOnClickListener(v -> onItemClickList.onItemClick(position));

        String eFoodType = mapData.get("eFoodType");
        if (eFoodType.equalsIgnoreCase("NonVeg")) {
            holder.nonvegImageView.setVisibility(View.VISIBLE);
            holder.vegImageView.setVisibility(View.GONE);
        } else if (eFoodType.equalsIgnoreCase("Veg")) {
            holder.vegImageView.setVisibility(View.VISIBLE);
            holder.nonvegImageView.setVisibility(View.GONE);
        }

        if (mapData.get("prescription_required").equalsIgnoreCase("Yes")) {
            holder.presImage.setVisibility(View.VISIBLE);
        } else {
            holder.presImage.setVisibility(View.GONE);
        }

        String vImage = mapData.get("vImage");
        if (vImage != null && !vImage.trim().equals("")) {
            new LoadImage.builder(LoadImage.bind(vImage), holder.expandImg).build();

            holder.expandImg.setVisibility(View.VISIBLE);
        } else {
            holder.expandImg.setVisibility(View.GONE);
            //holder.expandImg.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_no_icon));
        }

        if (mapData.get("fOfferAmtNotZero").equalsIgnoreCase("Yes")) {
            holder.price.setText(mapData.get("StrikeoutPrice"));
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.offerPrice.setText(mapData.get("fDiscountPricewithsymbol"));
            holder.offerPrice.setVisibility(View.VISIBLE);
        } else {
            holder.price.setText(mapData.get("StrikeoutPrice"));
            holder.offerPrice.setVisibility(View.GONE);


        }
        final long[] mLastClickTime = {0};
        holder.expandImg.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 800) {
                return;
            }
            mLastClickTime[0] = SystemClock.elapsedRealtime();
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    return;
                }
                TransitionManager.beginDelayedTransition(holder.parent);
                int height = holder.parent.getHeight();
                int width = holder.parent.getWidth();
                int ht = holder.expandImg.getMeasuredHeight();

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int s1 = display.getHeight();

                if (ht >= height || ht > s1 / 4 || ht > Utils.dpToPx(120, mContext)) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.expandImg.getLayoutParams();
                    params.width = (int) mContext.getResources().getDimension(R.dimen._70sdp);
                    params.height = (int) mContext.getResources().getDimension(R.dimen._70sdp);
                    holder.expandImg.requestLayout();

                    holder.tempView.setVisibility(View.INVISIBLE);
                    new GeneralFunctions(mContext).slideAnimView(holder.tempView, holder.tempView.getHeight(), 0, 500);
                    holder.rightImgView.setVisibility(View.INVISIBLE);
                } else { //Expand
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.tempView.getLayoutParams();
                    layoutParams.width = width;
                    layoutParams.height = height;
                    holder.tempView.requestLayout();
                    holder.tempView.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.expandImg.getLayoutParams();
                    params.width = width;
                    params.height = height;
                    holder.expandImg.requestLayout();
                    holder.rightImgView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Logger.d("ScrollException", "::" + e.toString());
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodSearchArrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MTextView name, desc;
        MTextView price, offerPrice;
        LinearLayout viewArea, tempView, rightImgView;
        ImageView vegImageView, nonvegImageView, itemImage, expandImg, presImage;
        RelativeLayout parent;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            offerPrice = itemView.findViewById(R.id.offerPrice);
            viewArea = itemView.findViewById(R.id.viewArea);
            vegImageView = itemView.findViewById(R.id.vegImageView);
            nonvegImageView = itemView.findViewById(R.id.nonvegImageView);
            itemImage = itemView.findViewById(R.id.itemImage);
            expandImg = itemView.findViewById(R.id.expandImg);
            tempView = itemView.findViewById(R.id.tempView);
            presImage = itemView.findViewById(R.id.presImage);
            parent = itemView.findViewById(R.id.parent);
            rightImgView = itemView.findViewById(R.id.rightImgView);
        }
    }


    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }
}
