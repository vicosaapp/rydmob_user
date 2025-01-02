package com.sessentaservices.usuarios;

import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.activity.ParentActivity;
import com.utils.Utils;
import com.view.MTextView;

public class PrefranceActivity extends ParentActivity implements CompoundButton.OnCheckedChangeListener {


    MTextView titleTxt;
    ImageView backImgView;
    CheckBox checkboxFemale, checkboxHandicap;
    String ishandicap = "No";
    String isfemale = "No";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrance);

        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        checkboxHandicap = (CheckBox) findViewById(R.id.checkboxHandicap);
        checkboxFemale = (CheckBox) findViewById(R.id.checkboxFemale);
        checkboxFemale.setOnCheckedChangeListener(this);
        checkboxHandicap.setOnCheckedChangeListener(this);
        setLabel();


    }

    public void setLabel() {
        titleTxt.setText(generalFunc.retrieveLangLBl("Prefrance", "LBL_PREFRANCE_TXT"));
        checkboxHandicap.setText(generalFunc.retrieveLangLBl("Filter handicap accessibility drivers only", "LBL_MUST_HAVE_HANDICAP_ASS_CAR"));
        checkboxFemale.setText(generalFunc.retrieveLangLBl("Accept Female Only trip request", "LBL_ACCEPT_FEMALE_REQ_ONLY"));
    }

    public Context getActContext() {
        return PrefranceActivity.this;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Utils.hideKeyboard(getActContext());
        if (buttonView == checkboxFemale) {
            if (checkboxFemale.isChecked()) {
                isfemale = "Yes";

            } else {
                isfemale = "No";
            }

            generalFunc.storeData(Utils.PREF_FEMALE, ishandicap);
        } else if (buttonView == checkboxHandicap) {
            if (checkboxHandicap.isChecked()) {
                ishandicap = "Yes";
            } else {
                ishandicap = "No";
            }
            generalFunc.storeData(Utils.PREF_HANDICAP, ishandicap);

        }

    }
}
