package com.bsstandard.piece.view.bookmark

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.viewmodel.BookMarkViewModel
import com.bsstandard.piece.databinding.BookmarkItemBinding
import com.bsstandard.piece.view.common.LoginChkActivity

/**
 *packageName    : com.bsstandard.piece.view.bookmark
 * fileName       : BookMarkAdapter
 * author         : piecejhm
 * date           : 2022/08/31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/31        piecejhm       최초 생성
 */
class BookMarkAdapter(viewModel: BookMarkViewModel, val context: Context) :
    RecyclerView.Adapter<BookMarkAdapter.ViewHolder>(){
        private val bookMarkViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkAdapter.ViewHolder {
        val binding: BookmarkItemBinding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = bookMarkViewModel.getBookMarkItem().size

    override fun onBindViewHolder(holder: BookMarkAdapter.ViewHolder, position: Int) {
        holder.bind(bookMarkViewModel, position , listener)

        if(bookMarkViewModel.getBookMarkItem()[position].isFavorite == "Y") {
            holder.binding.bookmark.isSelected = true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, tag:String, magazineId: String, magazineImagePath: String, isFavorite: Boolean, smallTitle: String, position: Int)
    }
    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: BookmarkItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: BookMarkViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.bookmarkViewModel = viewModel
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if(SystemClock.elapsedRealtime() - mLastClickTime > 300) {
                    if(pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            "webView",
                            bookMarkViewModel.getBookMarkItem()[pos].magazineId,
                            bookMarkViewModel.getBookMarkItem()[pos].representThumbnailPath,
                            binding.bookmark.isSelected,
                            bookMarkViewModel.getBookMarkItem()[pos].smallTitle,
                            pos
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }

            binding.bookmark.setOnClickListener {
                // member/bookmark 에서 isFavorite 컬럼 추가로 내려달라고 요청하고 작업 진행.. - jhm 2022/08/31
                // 북마크 클릭시 로그인 판별 - jhm 2022/08/30
                if (PrefsHelper.read("memberId", "").equals("")) {
                    val intent = Intent(context, LoginChkActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    if (SystemClock.elapsedRealtime() - mLastClickTime > 300) {
                        binding.bookmark.isSelected = !binding.bookmark.isSelected
                        listener?.onItemClick(
                            binding.bookmark,
                            "bookMark",
                            bookMarkViewModel.getBookMarkItem()[pos].magazineId,
                            bookMarkViewModel.getBookMarkItem()[pos].representThumbnailPath,
                            binding.bookmark.isSelected,
                            bookMarkViewModel.getBookMarkItem()[pos].smallTitle,
                            pos
                        )
                    }
                    mLastClickTime = SystemClock.elapsedRealtime()
                }
            }


        }
    }


}