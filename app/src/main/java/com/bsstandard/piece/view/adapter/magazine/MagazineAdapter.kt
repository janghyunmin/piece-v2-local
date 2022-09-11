package com.bsstandard.piece.view.adapter.magazine

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.MagazineViewModel
import com.bsstandard.piece.databinding.MagazineItemBinding
import com.bsstandard.piece.view.common.LoginChkActivity

/**
 *packageName    : com.bsstandard.piece.view.adapter.magazine
 * fileName       : MagazineAdapter
 * author         : piecejhm
 * date           : 2022/08/26
 * description    : 매거진 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/26        piecejhm       최초 생성
 */
class MagazineAdapter(viewModel: MagazineViewModel, val context: Context) :
    RecyclerView.Adapter<MagazineAdapter.ViewHolder>() {
    private val magazineViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagazineAdapter.ViewHolder {
        val binding: MagazineItemBinding =
            MagazineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = magazineViewModel.getMagazineItem().size

    override fun onBindViewHolder(holder: MagazineAdapter.ViewHolder, position: Int) {
        holder.bind(magazineViewModel, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, tag:String, magazineId: String, magazineImagePath: String , isFavorite: String , smallTitle: String , position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: MagazineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: MagazineViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.magazineViewModel = viewModel
            binding.executePendingBindings()

            // 로그인이 되어있을때에만 isFavorite isSelected 설정 - jhm 2022/08/30
            if (PrefsHelper.read("isJoin", "").equals("true")) {
                binding.bookmark.isSelected = magazineViewModel.getMagazineItem()[pos].isFavorite.equals("Y")
            }

            // item 개별 클릭시 webView Go - jhm 2022/08/29
            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 300) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            // 아이템 클릭시 webview 호출해야함 - jhm 2022/08/26
                            itemView,
                            "webView",
                            magazineViewModel.getMagazineItem().get(pos).magazineId,
                            magazineViewModel.getMagazineItem().get(pos).representThumbnailPath,
                            "",
                            magazineViewModel.getMagazineItem().get(pos).smallTitle,
                            pos
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }


            // 북마크 클릭시 - jhm 2022/08/29
            binding.bookmark.setOnClickListener {

                // 북마크 클릭시 로그인 판별 - jhm 2022/08/30
                if (!PrefsHelper.read("isJoin", "").equals("true")) {
                    val intent = Intent(context,LoginChkActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    if (SystemClock.elapsedRealtime() - mLastClickTime > 300) {
                        binding.bookmark.isSelected = !binding.bookmark.isSelected
                        listener?.onItemClick(
                            binding.bookmark,
                            "bookMark",
                            magazineViewModel.getMagazineItem().get(pos).magazineId,
                            magazineViewModel.getMagazineItem().get(pos).representThumbnailPath,
                            magazineViewModel.getMagazineItem().get(pos).isFavorite,
                            magazineViewModel.getMagazineItem().get(pos).smallTitle,
                            pos
                        )
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
            }

        }

    }
}