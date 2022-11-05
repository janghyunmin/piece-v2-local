package com.bsstandard.piece.view.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.bsstandard.piece.R;
import com.bsstandard.piece.databinding.ActivityConsentWebviewBinding;
import com.bsstandard.piece.view.common.NetworkActivity;
import com.bsstandard.piece.widget.utils.NetworkConnection;

/**
 * packageName    : com.bsstandard.piece.view.webview
 * fileName       : ConsentDetailWebView
 * author         : piecejhm
 * date           : 2022/06/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/21        piecejhm       최초 생성
 */
public class ConsentDetailWebView extends AppCompatActivity {

    private ActivityConsentWebviewBinding binding;
    private String consentTitle = "";
    private String consentContents = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_consent_webview);
        binding.setLifecycleOwner(this);

        // 오른쪽에서 왼쪽으로 슬라이딩 하면서 켜짐 - jhm 2022/06/22
        // overridePendingTransition(R.anim.activity_horizon_enter,R.anim.activity_none);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // statusBar Custom
        setStatusBar();


        NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
        networkConnection.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                // 인터넷 연결되어있음 - jhm 2022/11/02
                if (isConnected) {
                    Intent intent = getIntent();
                    if (intent != null) {
                        consentTitle = intent.getExtras().getString("consentTitle");
                        consentContents = intent.getExtras().getString("consentContents");
                        binding.topTitle.setText(consentTitle);

                        WebSettings mws = binding.webView.getSettings();
                        mws.setJavaScriptEnabled(false); // 자바스크립트 허용 - jhm 2022/06/21
                        mws.setLoadWithOverviewMode(false); // 컨텐츠가 웹뷰보다 클 경우 맞게 조정 - jhm 2022/06/21

                        binding.webView.loadData(consentContents, "text/html; charset=utf-8", "UTF-8");
                        binding.webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                                binding.progressBar.show();
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                binding.progressBar.hide();
                            }
                        });
                    }

                    binding.backImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 뒤로가기시 페이지 종료 - jhm 2022/06/21
                            finish();

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                }
                // 인터넷 연결 안되어있음 - jhm 2022/11/02
                else {
                    Intent intent = new Intent(getApplicationContext(), NetworkActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void setStatusBar() {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        window.setAttributes(winParams);
    }

    // OS BackKey 뒤로가기 - jhm 2022/06/22
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
