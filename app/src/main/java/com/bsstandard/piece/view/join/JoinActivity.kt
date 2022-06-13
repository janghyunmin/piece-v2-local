package com.bsstandard.piece.view.join;

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.bsstandard.piece.R
import com.bsstandard.piece.base.BaseActivity
import com.bsstandard.piece.databinding.ActivityJoinBinding
import com.bsstandard.piece.view.intro.IntroActivity
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_join.*

/**
 *packageName    : com.bsstandard.piece.view
 * fileName       : JoinActivity
 * author         : piecejhm
 * date           : 2022/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/26        piecejhm       최초 생성
 */


@AndroidEntryPoint
class JoinActivity : BaseActivity<ActivityJoinBinding>(R.layout.activity_join) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selected = false

        val phone_et : TextView = findViewById(R.id.phone_select)
        var agency :String


        BackLogic()
        FocusTextLogic()
        ValidationLogic()



        /** { 통신사 선택 } - jhm 2022/05/06 **/
        phone_et.setOnClickListener {
            val joinBottomDialog: JoinBottomDialog = JoinBottomDialog {
                when(it){
                    0 -> agency = "SKT"
                    1 -> agency = "KT"
                    2 -> agency = "LG U+"
                    3 -> agency = "SKT 알뜰폰"
                    4 -> agency = "KT 알뜰폰"
                    5 -> agency = "LG U+ 알뜰폰"
                    else -> {
                        agency = ""
                    }
                }
                LogUtil.logE("선택한 통신사 : " + agency)
                phone_select.text = agency
            }
            joinBottomDialog.show(supportFragmentManager,joinBottomDialog.tag)
        }

    }

    /** { EditText 선택시 Title 색상 변경 로직 } - jhm 2022/05/06 **/
    fun FocusTextLogic(){
        user_name.setOnFocusChangeListener() { v, hasFocus ->
            if(hasFocus){ title_name.setTextColor(getColor(R.color.black)) }
            else{ title_name.setTextColor(getColor(R.color.c_b8bcc8)) }
        }

        user_birth.setOnFocusChangeListener() { v, hasFocus ->
            if(hasFocus){ title_birth.setTextColor(getColor(R.color.black)) }
            else{ title_birth.setTextColor(getColor(R.color.c_b8bcc8)) }
        }

        phone_num.setOnFocusChangeListener() { v, hasFocus ->
            if(hasFocus){ phone_num_title.setTextColor(getColor(R.color.black)) }
            else{ phone_num_title.setTextColor(getColor(R.color.c_b8bcc8)) }
        }
    }




    /** { EditText 유효성 검사 } - jhm 2022/05/06 **/
    fun ValidationLogic(){

        if(user_name.length() > 0 && user_birth.length() > 0){
            LogUtil.logE("length > 0")
        }else{
            LogUtil.logE("length < 0")
        }


    }


    // 뒤로가기시 IntroActivity 이동
    fun BackLogic(){
        back_img.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish()
        }
    }
}