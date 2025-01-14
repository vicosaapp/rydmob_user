package com.sessentaservices.usuarios.deliverAll;

import android.os.Bundle;
import android.view.View;
import com.activity.ParentActivity;
import android.widget.ImageView;
import android.content.Context;

public class GenieDeliveryHomeActivity extends ParentActivity implements View.OnClickListener {
    
    private ImageView backImgView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie_delivery_home);
        
        // Inicialização das views
        backImgView = findViewById(R.id.backImgView);
        addToClickHandler(backImgView);
        
        // ... resto do código de inicialização
    }

    @Override
    public void onClick(View v) {
        Utils.hideKeyboard(getActContext());
        Bundle bn = new Bundle();
        
        int id = v.getId();
        if (id == R.id.backImgView) {
            onBackPressed();
        } else if (id == R.id.howitWorkImg) {
            // ... código do howitWorkImg
        } else if (id == R.id.fabcartIcon) {
            ActUtils(getActContext()).startAct(EditCartActivity.class);
        }
    }

    private Context getActContext() {
        return GenieDeliveryHomeActivity.this;
    }

    // ... resto do código
} 