package com.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.general.files.GeneralFunctions;

public class ParentActivity extends AppCompatActivity implements View.OnClickListener {
    public GeneralFunctions generalFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            // Inicializa GeneralFunctions antes de usar
            generalFunc = new GeneralFunctions(this);
        } catch (Exception e) {
            Log.e("ParentActivity", "Erro ao inicializar: " + e.getMessage(), e);
        }
    }

    protected void addToClickHandler(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // Implementação padrão vazia - será sobrescrita pelas classes filhas
    }
} 