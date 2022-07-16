package com.bsstandard.piece.view.join;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bsstandard.piece.R;
import com.bsstandard.piece.databinding.ActivitySuccessBinding;
import com.bsstandard.piece.view.main.MainActivity;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bumptech.glide.Glide;

/**
 * packageName    : com.bsstandard.piece.view.join
 * fileName       : JoinSuccessActivity
 * author         : piecejhm
 * date           : 2022/07/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/03        piecejhm       최초 생성
 */
public class JoinSuccessActivity extends AppCompatActivity {

    Context context;
    ActivitySuccessBinding binding;
    String user_name = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_success);
        binding.setLifecycleOwner(this);
        context = this;

        Glide.with(this).load(R.drawable.join_complete_looping).into(binding.lottieLayout);

        Intent intent = getIntent();
        user_name = intent.getExtras().getString("name");
        binding.title.setText("반가워요, " + user_name + "님");
        binding.subTitle.setText("이제 " + user_name +" 님만의 조각을 만나 보세요!");

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.logE("회원가입 확인 후 확인 버튼 OnClick..");
                Intent go_main = new Intent(context, MainActivity.class);
                go_main.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go_main);
                finish();
            }
        });

    }
}
