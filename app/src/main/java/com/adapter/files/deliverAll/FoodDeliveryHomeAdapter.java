package com.adapter.files.deliverAll;

import android.content.Context;
import android.os.Parcelable;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sessentaservices.usuarios.R;
import com.utils.LoadImage;

import java.util.ArrayList;

/**
 * Created by Admin on 02-03-2017.
 */
public class FoodDeliveryHomeAdapter extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public FoodDeliveryHomeAdapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.item_food_delivery_home_design, view, false);
        assert imageLayout != null;

        final ImageView bannerImgView = (ImageView) imageLayout.findViewById(R.id.bannerImgView);

        position = position % IMAGES.size();

        new LoadImage.builder(LoadImage.bind(IMAGES.get(position)), bannerImgView).build();

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}