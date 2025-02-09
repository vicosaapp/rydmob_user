package com.general.files;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.sessentaservices.usuarios.R;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;


public class CustomDialog {

    int iconTintColor = R.color.appThemeColor_TXT_1;
    int roundedViewBackgroundColor = R.color.appThemeColor_1;
    int titleTxtColor = R.color.black;
    int messageTxtColor = R.color.black;
    int cardAreaColor = R.color.white;
    int positiveBtnTextColor = R.color.appThemeColor_TXT_1;
    int positiveBtnBorderColor = R.color.appThemeColor_1;
    int positiveBtnBackColor = R.color.appThemeColor_1;
    int negativeBtnColor = R.color.appThemeColor_1;
    int negativeBtnTextColor = R.color.appThemeColor_TXT_1;
    int negativeBtnBackColor = R.color.appThemeColor_1;
    int negativeBtnBorderColor = R.color.appThemeColor_1;
    int btnRadius = 0;
    int imgStrokWidth = 0;
    int imgBorderColor = R.color.appThemeColor_TXT_1;

    String titleTxt;
    String messageTxt;
    int iconType;
    String positiveBtnTxt;
    String negativeBtnTxt;
    boolean setCancelable;
    boolean setAnimation;
    boolean isAlpha;
    int btnOrientation;

    Context mContext;
    private Dialog customDialog;
    private GeneralFunctions generalFunc;
    private Animation animation;
    private Animation animation2;

    MButton tv_Proceed_Button;
    MButton tv_Positive_Button;
    MButton tv_Negative_Button;
    MButton tv_Cancel_Button;
    ImageView iv_icon;


    public CustomDialog(Context mContext, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.generalFunc = generalFunc;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
        animation2 = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);

    }

    /**
     * @param title          -> Set "" if not required
     * @param message        ->  Set "" if not required
     * @param setCancelable  -> true/false
     * @param positiveBtnTxt ->  Set "" if not required
     * @param negativeBtnTxt -> Set "" if not required
     * @param iconType       ->   ICON IMAGE
     *                       *                     //1-Info - rounded icon , 2-Success,3- Notice - filled icon , 4-Error,5-warning,5-DOne, 6 - Subscription,7-Info Italic icon,8- Notice - unfilled icon,9-Info Normal icon
     * @param setAnimation   -> true/false
     * @param btnOrientation ->   1- Horizontal , 2- vertical
     */
    public void setDetails(String title, String message, String positiveBtnTxt, String negativeBtnTxt, boolean setCancelable, int iconType, boolean setAnimation, int btnOrientation, boolean isAlpha) {
        this.titleTxt = title;
        this.messageTxt = message;
        this.iconType = iconType;
        this.positiveBtnTxt = positiveBtnTxt;
        this.negativeBtnTxt = negativeBtnTxt;
        this.setCancelable = setCancelable;
        this.setAnimation = setAnimation;
        this.btnOrientation = btnOrientation; // 1- Horizontal , 2- vertical
        this.isAlpha = isAlpha;
    }

    public void setIconTintColor(int iconTintColor) {
        this.iconTintColor = iconTintColor;
    }

    public void setTitleTxtColor(int titleTxtColor) {
        this.titleTxtColor = titleTxtColor;
    }

    public void setMessageTxtColor(int messageTxtColor) {
        this.messageTxtColor = messageTxtColor;
    }

    public void setCardAreaColor(int cardAreaColor) {
        this.cardAreaColor = cardAreaColor;
    }


    public void setNegativeBtnColor(int negativeBtnColor) {
        this.negativeBtnColor = negativeBtnColor;
    }


    public void setPositiveBtnTextColor(int positiveBtnTextColor) {
        this.positiveBtnTextColor = positiveBtnTextColor;
    }

    public void setNegativeBtnBackColor(int negativeBtnBackColor) {
        this.negativeBtnBackColor = negativeBtnBackColor;
    }

    public void setPositiveBtnBackColor(int positiveBtnBackColor) {
        this.positiveBtnBackColor = positiveBtnBackColor;
    }

    public void setRoundedViewBorderColor(int imgBorderColor) {
        this.imgBorderColor = imgBorderColor;
    }

    public void setBtnRadius(int btnRadius) {
        this.btnRadius = btnRadius;
    }


    public void setImgStrokWidth(int imgStrokWidth) {
        this.imgStrokWidth = imgStrokWidth;
    }


    public void setRoundedViewBackgroundColor(int roundedViewBackgroundColor) {
        this.roundedViewBackgroundColor = roundedViewBackgroundColor;
    }


    public Dialog show() {
        try {
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing()) {
                    showDialog();
                }
            } else {
                showDialog();
            }
        } catch (Exception e) {
            Log.e("[AwSDialog:showAlert]", "Erro ao exibir alert");
        }

        return customDialog;
    }

    public void createDialog() {
        createCustomDialog();
    }

    public void showDialog() {
        if (customDialog != null) {
            customDialog.show();
            if (setAnimation)
                iv_icon.startAnimation(animation2);
        }
    }

    public void hideDialog() {
        if (customDialog != null) {
            customDialog.hide();
        }
    }

    public void closeDialog() {
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    protected void createCustomDialog() {

        customDialog = new Dialog(mContext);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setContentView(R.layout.custom_dialog);
        customDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final LinearLayout cv_detailArea = (LinearLayout) customDialog.findViewById(R.id.cv_detailArea);
        final MTextView tv_title = (MTextView) customDialog.findViewById(R.id.tv_title);
        final FrameLayout dialogImgAlpha = (FrameLayout) customDialog.findViewById(R.id.dialogImgAlpha);
        final MTextView tv_message = (MTextView) customDialog.findViewById(R.id.tv_message);
        iv_icon = (ImageView) customDialog.findViewById(R.id.iv_icon);
        final LinearLayout horizontalButtonArea = (LinearLayout) customDialog.findViewById(R.id.horizontalButtonArea);
        final LinearLayout verticalButtonArea = (LinearLayout) customDialog.findViewById(R.id.verticalButtonArea);
        final LinearLayout tv_Proceed_Area = (LinearLayout) customDialog.findViewById(R.id.tv_Proceed_Area);
        final LinearLayout tv_Cancel_Area = (LinearLayout) customDialog.findViewById(R.id.tv_Cancel_Area);
        final LinearLayout tv_Positive_Area = (LinearLayout) customDialog.findViewById(R.id.tv_Positive_Area);
        final LinearLayout tv_Negative_Area = (LinearLayout) customDialog.findViewById(R.id.tv_Negative_Area);
        tv_Proceed_Button = ((MaterialRippleLayout) customDialog.findViewById(R.id.tv_Proceed_Button)).getChildView();
        tv_Cancel_Button = ((MaterialRippleLayout) customDialog.findViewById(R.id.tv_Cancel_Button)).getChildView();
        tv_Positive_Button = ((MaterialRippleLayout) customDialog.findViewById(R.id.tv_Positive_Button)).getChildView();
        tv_Negative_Button = ((MaterialRippleLayout) customDialog.findViewById(R.id.tv_Negative_Button)).getChildView();

//        cv_detailArea.setBackgroundColor(mContext.getResources().getColor(cardAreaColor));
        iv_icon.setColorFilter(mContext.getResources().getColor(iconTintColor));
        if (isAlpha) {
            dialogImgAlpha.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_circle));
            dialogImgAlpha.getBackground().setAlpha(70);
        }

        if (btnOrientation == 1) {
            horizontalButtonArea.setVisibility(View.VISIBLE);
        } else {
            verticalButtonArea.setVisibility(View.VISIBLE);
        }

        iv_icon.setImageResource(iconType);
        if (iconType == 0) {
            dialogImgAlpha.setVisibility(View.GONE);
        }

       /* //1-Info - rounded icon , 2-Success,3- Notice - filled icon , 4-Error,5-warning,5-DOne, 6 - Subscription,7-Info Normal icon,8- Notice - unfilled icon
        if (iconType == 1) {
            iv_icon.setImageResource(R.drawable.ic_normal_info);
        } else if (iconType == 2) {
            iv_icon.setImageResource(R.drawable.ic_correct);
        } else if (iconType == 3) {
            iv_icon.setImageResource(R.drawable.ic_hand_gesture);
        } else if (iconType == 4) {
            iv_icon.setImageResource(R.drawable.ic_warning);
        } else if (iconType == 5) {
            iv_icon.setImageResource(R.drawable.ic_done);
        } else if (iconType == 6) {
            iv_icon.setImageResource(R.mipmap.ic_menu_subscription);
        } else if (iconType == 7) {
            iv_icon.setImageResource(R.drawable.ic_info);
        } else if (iconType == 8) {
            iv_icon.setImageResource(R.drawable.ic_fingure_up);
        }else {
            iv_icon.setImageResource(R.drawable.ic_correct);
        }
*/


        if (Utils.checkText(titleTxt)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setTextColor(mContext.getResources().getColor(titleTxtColor));
            tv_title.setText(titleTxt);
        }

        if (Utils.checkText(messageTxt)) {
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setTextColor(mContext.getResources().getColor(messageTxtColor));
            tv_message.setText(messageTxt);
        }

        if (Utils.checkText(positiveBtnTxt)) {
            tv_Positive_Area.setVisibility(View.VISIBLE);
            // tv_Positive_Button.setVisibility(View.VISIBLE);
            //tv_Positive_Button.setTextColor(mContext.getResources().getColor(positiveBtnTextColor));
            tv_Positive_Button.setText(positiveBtnTxt);

//            new CreateRoundedView(mContext.getResources().getColor(positiveBtnBackColor), Utils.dipToPixels(mContext, btnRadius), 0,
//                    mContext.getResources().getColor(positiveBtnBorderColor), tv_Positive_Button);

            if (btnOrientation == 1) {
                tv_Proceed_Area.setVisibility(View.VISIBLE);
                //tv_Proceed_Button.setVisibility(View.VISIBLE);
                //tv_Proceed_Button.setTextColor(mContext.getResources().getColor(negativeBtnTextColor));
                tv_Proceed_Button.setText(positiveBtnTxt);

//                new CreateRoundedView(mContext.getResources().getColor(positiveBtnBackColor), Utils.dipToPixels(mContext, btnRadius), 0,
//                        mContext.getResources().getColor(positiveBtnBorderColor), tv_Proceed_Button);
            }
        }

        if (Utils.checkText(negativeBtnTxt)) {
            tv_Negative_Area.setVisibility(View.VISIBLE);
            //  tv_Negative_Button.setVisibility(View.VISIBLE);
//            tv_Negative_Button.setTextColor(mContext.getResources().getColor(negativeBtnTextColor));
            tv_Negative_Button.setText(negativeBtnTxt);

//            new CreateRoundedView(mContext.getResources().getColor(negativeBtnBackColor), Utils.dipToPixels(mContext, btnRadius), 0,
//                    mContext.getResources().getColor(negativeBtnBorderColor), tv_Negative_Button);


            if (btnOrientation == 1) {
                tv_Cancel_Area.setVisibility(View.VISIBLE);
                //tv_Cancel_Button.setVisibility(View.VISIBLE);
//                tv_Cancel_Button.setTextColor(mContext.getResources().getColor(negativeBtnTextColor));
                tv_Cancel_Button.setText(negativeBtnTxt);

//                new CreateRoundedView(mContext.getResources().getColor(negativeBtnBackColor), Utils.dipToPixels(mContext, btnRadius), 0,
//                        mContext.getResources().getColor(negativeBtnBorderColor), tv_Cancel_Button);
            }

        }

        if (setAnimation) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv_icon.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv_icon.startAnimation(animation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    public void setPositiveButtonClick(@Nullable final Closure selectedNo) {

        if (btnOrientation == 1) {
            tv_Proceed_Button.setOnClickListener(view -> {
                if (selectedNo != null) {
                    selectedNo.exec();
                }

                closeDialog();
            });
        } else {
            tv_Positive_Button.setOnClickListener(view -> {
                if (selectedNo != null) {
                    selectedNo.exec();
                }

                closeDialog();
            });
        }
    }

    public void setNegativeButtonClick(@Nullable final Closure selectedNo) {
        if (btnOrientation == 1) {
            tv_Cancel_Button.setOnClickListener(view -> {
                if (selectedNo != null) {
                    selectedNo.exec();
                }
                closeDialog();
            });
        } else {
            tv_Negative_Button.setOnClickListener(view -> {
                if (selectedNo != null) {
                    selectedNo.exec();
                }
                closeDialog();
            });
        }
    }
}