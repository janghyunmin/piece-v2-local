package com.bsstandard.piece.view.adapter.question

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.viewmodel.BoardViewModel
import com.bsstandard.piece.databinding.QuestionItemBinding

/**
 *packageName    : com.bsstandard.piece.view.adapter.question
 * fileName       : QuestionAdapter
 * author         : piecejhm
 * date           : 2022/09/23
 * description    : 자주 묻는 질문 리스트 Adapter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/23        piecejhm       최초 생성
 */


class QuestionAdapter(viewModel: BoardViewModel, val context: Context) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    private val questionViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val binding: QuestionItemBinding =
            QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = questionViewModel.getQuestionItem().size

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        holder.bind(questionViewModel, position, listener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, title: String, contents: String)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding;
        private var mLastClickTime: Long = 0

        fun bind(viewModel: BoardViewModel, pos: Int, listener: OnItemClickListener?) {
            binding.pos = pos
            binding.boardVm = viewModel
            binding.executePendingBindings()

            itemView.setOnClickListener {
                if(binding.contentLayout.visibility == View.GONE) {
                    binding.contentLayout.visibility = View.VISIBLE
                    binding.contentTitle.text = questionViewModel.getQuestionItem()[pos].title
                    binding.contents.text = questionViewModel.getQuestionItem()[pos].contents
                }
                else {
                    binding.contentLayout.visibility = View.GONE
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime > 300) {
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(
                            itemView,
                            questionViewModel.getQuestionItem()[pos].title,
                            questionViewModel.getQuestionItem()[pos].contents
                        )
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }
    }


}