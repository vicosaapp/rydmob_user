package com.sessentaservices.usuarios;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ParentActivity extends AppCompatActivity {
    
    // ... outros métodos existentes ...

    protected void manageVectorImage(View view, int drawableId, int compatDrawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            view.setBackgroundResource(compatDrawableId);
        } else {
            view.setBackgroundResource(drawableId);
        }
    }

    protected void manageVectorImage(ImageView imageView, int drawableId, int compatDrawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageResource(compatDrawableId);
        } else {
            imageView.setImageResource(drawableId);
        }
    }
} 