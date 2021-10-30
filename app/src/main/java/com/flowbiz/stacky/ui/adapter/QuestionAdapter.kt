package com.flowbiz.stacky.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowbiz.stacky.R
import com.flowbiz.stacky.data.model.local.Question
import com.flowbiz.stacky.databinding.QuestionRowLayoutBinding

class QuestionAdapter(private val onClick: (question: Question) -> Unit, private val onFilter: (list: ArrayList<Question>) -> Unit) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>(), Filterable {

    private var questionList = ArrayList<Question>()
    private var questionListOriginal = ArrayList<Question>()

    fun setQuestions(list: ArrayList<Question>) {
        questionList.clear()
        questionListOriginal.clear()
        questionListOriginal.addAll(list)
        questionList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QuestionRowLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        val question = questionList[position]
        holder.bindData(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class ViewHolder(private val binding: QuestionRowLayoutBinding, private val onClick: (question: Question) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(question: Question) {
            binding.questionTitle.text = question.title
            binding.postedDate.text = String.format("Posted on:%s", question.creationDate)
            binding.userName.text = question.displayName
            Glide.with(binding.profileImg).load(question.profileImage).placeholder(R.drawable.ic_baseline_person_outline).into(binding.profileImg)

            binding.root.setOnClickListener {
                onClick(question)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                questionList = if (constraint.isNullOrEmpty()) {
                    questionListOriginal
                } else {
                    val list = ArrayList<Question>()
                    questionListOriginal.filter {
                        it.title.contains(constraint) || it.displayName.contains(constraint)
                    }.forEach {
                        list.add(it)
                    }
                    list
                }
                Log.d("TAG", "size: ${questionList.size}")
                return FilterResults().apply { values = questionList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                questionList = if (results?.values == null) ArrayList<Question>()
                else {
                    results.values as ArrayList<Question>
                }
                onFilter(questionList)
                notifyDataSetChanged()
            }
        }
    }

    fun filterList(tag: String) {
        val list = ArrayList<Question>()
        questionListOriginal.forEach {
            if (it.tags.contains(tag)) {
                list.add(it)
            }
        }
        questionList = list
        notifyDataSetChanged()
    }

    fun onClear() {
        questionList = questionListOriginal
        notifyDataSetChanged()
    }
}
