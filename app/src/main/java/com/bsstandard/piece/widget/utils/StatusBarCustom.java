package com.bsstandard.piece.widget.utils;

import android.app.Activity;
import android.os.Build;
import androidx.core.content.ContextCompat;
import com.bsstandard.piece.R;

/**
 * packageName    : com.bsstandard.piece.widget.utils
 * fileName       : StatusBarCustom
 * author         : piecejhm
 * date           : 2022/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/05/02        piecejhm       최초 생성
 */


public class StatusBarCustom {

    public enum StatusBarColorType {

        WHITE_STATUS_BAR(R.color.white);
        private int backgroundColorId;
        StatusBarColorType(int backgroundColorId){
            this.backgroundColorId = backgroundColorId;
        }
        public int getBackgroundColorId() {
            return backgroundColorId;
        }
    }

    public static void setStatusBarColor(Activity activity, StatusBarColorType colorType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
        }
    }


}