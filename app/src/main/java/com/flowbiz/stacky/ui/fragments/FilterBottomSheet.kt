package com.flowbiz.stacky.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.flowbiz.stacky.R
import com.flowbiz.stacky.databinding.FilterBottomSheetLayoutBinding
import com.flowbiz.stacky.databinding.FilterListRowLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FilterBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    private var filterList = ArrayList<String>()
    private lateinit var adapter: FilterAdapter

    private var onSelect: (tagName: String) -> Unit = {}
    private var onClear: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FilterDialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilterBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
        binding.clearBtn.setOnClickListener {
            onClear()
            dismiss()
        }
    }

    private fun setRv(){
        adapter = FilterAdapter {
            onSelect(it)
            dismiss()
        }
        adapter.setList(filterList)
        binding.filterRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager, list: List<String>, onSelect: (tagName: String) -> Unit, onClear: () -> Unit = {}){
        filterList = list as ArrayList<String>
        this.onSelect = onSelect
        this.onClear = onClear
        show(fragmentManager, FilterBottomSheet::class.java.simpleName)
    }

    class FilterAdapter(private val onClick: (tagName: String) -> Unit): RecyclerView.Adapter<FilterAdapter.ViewHolder>(){

        private val list = ArrayList<String>()

        fun setList(list: ArrayList<String>){
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FilterAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FilterListRowLayoutBinding.inflate(inflater, parent, false)
            return ViewHolder(binding, onClick)
        }

        override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
            holder.bindData(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

        class ViewHolder(private val binding: FilterListRowLayoutBinding, private val onClick: (tagName: String) -> Unit): RecyclerView.ViewHolder(binding.root){

            fun bindData(name: String){
                binding.nameBtn.text = name
                binding.nameBtn.setOnClickListener {
                    onClick(name)
                }
            }
        }
    }
}
