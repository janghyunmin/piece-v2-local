package com.bsstandard.piece.view.adapter.magazine

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.MagazineViewModel
import com.bsstandard.piece.databinding.MagazineItemBinding

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
        fun onItemClick(v: View, magazineId: String, magazineImagePath: String)
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

            itemView.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            // 아이템 클릭시 webview 호출해야함 - jhm 2022/08/26
                            itemView, magazineViewModel.getMagazineItem().get(pos).magazineId,
                            magazineViewModel.getMagazineItem().get(pos).representThumbnailPath
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }

        }

    }
}