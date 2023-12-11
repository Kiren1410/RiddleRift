package com.kirenraj.quizapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.utilities.Score
import com.kirenraj.quizapp.data.model.Result
import com.kirenraj.quizapp.databinding.ItemLayoutLeaderboardBinding

class ResultAdapter(
    private var result: List<Result>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutLeaderboardBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun getItemCount() = result.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = result[position]
        if (holder is ResultAdapter.ResultViewHolder) {
            holder.bind(result)
        }
    }

    fun setResult(result: List<Result>) {
        this.result = result
        notifyDataSetChanged()

    }

    inner class ResultViewHolder(
        private val binding: ItemLayoutLeaderboardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.run {
                Log.d("debugging", result.toString())
                tvUsername.text = result.name
                tvScore.text = result.result
                tvQuizId.text = result.quizId
            }
        }
    }

    interface Listener{
        fun onClick(result: Result)
    }
}

