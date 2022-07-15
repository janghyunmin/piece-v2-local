package com.bsstandard.piece.view.splash;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bsstandard.piece.R;
import com.bsstandard.piece.data.datasource.factory.ViewModelFactory;
import com.bsstandard.piece.data.datasource.shared.PrefsHelper;
import com.bsstandard.piece.data.viewmodel.VersionViewModel;
import com.bsstandard.piece.databinding.ActivitySplashBinding;
import com.bsstandard.piece.view.intro.IntroActivity;
import com.bsstandard.piece.view.passcode.PassCodeActivity;
import com.bsstandard.piece.widget.utils.CustomDialog;
import com.bsstandard.piece.widget.utils.CustomDialogListener;
import com.bsstandard.piece.widget.utils.CustomDialogPassCodeListener;
import com.bsstandard.piece.widget.utils.Division;
import com.bsstandard.piece.widget.utils.LogUtil;

/**
 * packageName    : com.bsstandard.piece.view.splash
 * fileName       : SplashActivity
 * author         : piecejhm
 * date           : 2022/06/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/13        piecejhm       최초 생성
 */
public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Context context;
    private VersionViewModel versionViewModel;
    private LottieAnimationView lottieAnimationView;
    public String AppVersion = "";
    public String StoreVersion = "";

    public CustomDialog customDialog;
    // public CommonDialog commonDialog;

    // id값이 있다면 바로 PassCodeActivity - jhm 2022/07/03
    private String memberId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        lottieAnimationView = findViewById(R.id.splash_animation_view);
        context = this;
        //commonDialog = new CommonDialog(context,this, Division.DIALOG_C_VERSION);

        PrefsHelper.init(context);

        // 현재 로컬 앱의 버전을 가져옴 - jhm 2022/06/13
        try {
            AppVersion = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        setStatusBar();
        getStart();



    }
    public void setStatusBar(){
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }



    public void getStart(){
        versionViewModel = new ViewModelProvider(this , new ViewModelFactory(this.getApplication())).get(VersionViewModel.class);
        versionViewModel.getVersionData("MDO0101").observe(this, response -> {
            LogUtil.logE("version : " + response.getData().getVersion());
            StoreVersion = response.getData().getVersion();
        });

        lottieAnimationView.setAnimation("splash1.json");
        lottieAnimationView.loop(false);
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.logE("start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.logE("end");
                versionChk();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.logE("cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtil.logE("repeat");
            }
        });


    }

    public void versionChk(){
        if(StoreVersion.compareTo(AppVersion) > 0) {
            LogUtil.logE("업데이트 필요..");

            CustomDialogListener customDialogListener = new CustomDialogListener() {
                @Override
                public void onOkButtonClicked() {
                    LogUtil.logE("업데이트 하러 이동");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(context.getResources().getString(R.string.playstore_url)));
                    context.startActivity(intent);
                    customDialog.dismiss();
                    finish();
                }
            };
            CustomDialogPassCodeListener customDialogPassCodeListener = new CustomDialogPassCodeListener() {
                @Override
                public void onCancleButtonClicked() { }

                @Override
                public void onRetryPassCodeButtonClicked() { }
            };
            customDialog = new CustomDialog(this,Division.DIALOG_C_VERSION,customDialogListener,customDialogPassCodeListener);
            customDialog.show();

        }else {
            LogUtil.logE("업데이트 불필요..");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    memberId = PrefsHelper.read("memberId","");
                    LogUtil.logE("memberId : " + memberId);
                    if(memberId.equals("")){
                        Intent intent = new Intent(context, IntroActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, PassCodeActivity.class);
                        startActivity(intent);
                    }
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();
                }
            },500);
        }
    }
}
