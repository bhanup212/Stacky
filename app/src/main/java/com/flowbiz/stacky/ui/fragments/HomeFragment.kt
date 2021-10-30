package com.flowbiz.stacky.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.flowbiz.stacky.databinding.FragmentHomeBinding
import com.flowbiz.stacky.di.AppComponentInitializer
import com.flowbiz.stacky.ui.adapter.QuestionAdapter
import com.flowbiz.stacky.ui.viewmodel.HomeViewModel
import java.util.HashSet
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: QuestionAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponentInitializer.getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        viewModel.getQuestionList()
        observeViewModel()
        binding.searchEdt.doAfterTextChanged {
            adapter.filter.filter(it.toString())
        }
        binding.filterImg.setOnClickListener {
            viewModel.onFilter()
        }
    }

    private fun setAdapter() {
        adapter = QuestionAdapter(
            {
                openLink(it.link)
            },
            {
                viewModel.calculateAverageCount(it)
            }
        )
        binding.questionRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(
            viewLifecycleOwner,
            Observer { isVisible ->
                binding.progressBar.isVisible = isVisible
            }
        )
        viewModel.questionList.observe(
            viewLifecycleOwner,
            Observer { items ->
                adapter.setQuestions(items)
            }
        )
        viewModel.averageViewAnswerCount.observe(
            viewLifecycleOwner,
            {
                binding.countLl.isVisible = it != null
                if (it != null) {
                    binding.viewCountTv.text = String.format("%.2f", it.first)
                    binding.answerCountTv.text = String.format("%.2f", it.second)
                }
            }
        )
        viewModel.tagList.observe(
            viewLifecycleOwner,
            {
                showBottomSheet(it)
            }
        )
    }

    private fun showBottomSheet(set: HashSet<String>?) {
        if (set != null && set.isNotEmpty()) {
            FilterBottomSheet().show(
                childFragmentManager, set.toList(),
                {
                    adapter.filterList(it)
                },
                {
                    adapter.onClear()
                }
            )
        }
    }

    private fun openLink(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}
