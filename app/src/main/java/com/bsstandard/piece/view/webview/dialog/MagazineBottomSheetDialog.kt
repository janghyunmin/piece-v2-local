package com.bsstandard.piece.view.webview.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.bsstandard.piece.R
import com.bsstandard.piece.databinding.SlideupSharedBinding
import com.bsstandard.piece.widget.utils.LogUtil

/**
 *packageName    : com.bsstandard.piece.view.webview.dialog
 * fileName       : MagazineBottomSheetDialog
 * author         : piecejhm
 * date           : 2022/11/01
 * description    : 매거진 상세 웹뷰 상단 아이콘 클릭시 BottomSheetDialog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/01        piecejhm       최초 생성
 */


class MagazineBottomSheetDialog(context: Context, layoutId: Int): Dialog(context, layoutId) {
    private lateinit var binding: SlideupSharedBinding
    private lateinit var dialog: Dialog
    private var listener: OnSendFromBottomSheetDialog? = null


    fun showDialog(shareUrl:String,magazineId:String,isFavorite: String) {

        binding = SlideupSharedBinding.inflate(LayoutInflater.from(context))
        dialog = setDialogOptions()

        binding.run {
            LogUtil.logE("전달받은 shareUrl : $shareUrl")
            LogUtil.logE("전달받은 magazineId : $magazineId")
            LogUtil.logE("전달받은 isFavorite : $isFavorite")


            if(isFavorite == "N") {
                binding.bIcon.isSelected = true
                binding.bIcon.background = context.getDrawable(R.drawable.bm_bookmark_on_icon)
                binding.bTitle.text = "북마크"
            } else {
                binding.bIcon.isSelected = false
                binding.bIcon.background = context.getDrawable(R.drawable.bm_bookmark_off_icon)
                binding.bTitle.text = "북마크 취소"
            }


            binding.closeIcon.setOnClickListener {
                dialog.dismiss()
            }

            binding.shareLayout.setOnClickListener{
                if (listener == null) return@setOnClickListener
                listener?.sendValue("공유",false)
                val sendIntent : Intent = Intent().apply{
                    action= Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,shareUrl)
                    type= "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                context.startActivity(shareIntent)
            }

            binding.copyLayout.setOnClickListener {
                if (listener == null) return@setOnClickListener
                listener?.sendValue("링크 복사",false)
                dialog.dismiss()
            }
            binding.bookmarkLayout.setOnClickListener{
                LogUtil.logE("북마크 OnClick..")
                binding.bIcon.isSelected = !binding.bIcon.isSelected

                listener?.sendValue("북마크",binding.bIcon.isSelected)
                dialog.dismiss()
            }

        }
    }


    private fun setDialogOptions(): Dialog = Dialog(context).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)
        window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
        show()
    }

    //  - jhm 2022/11/01
    interface OnSendFromBottomSheetDialog {
        fun sendValue(value: String,boolean: Boolean)
    }

    fun setCallback(listener: OnSendFromBottomSheetDialog) {
        this.listener = listener
    }
}