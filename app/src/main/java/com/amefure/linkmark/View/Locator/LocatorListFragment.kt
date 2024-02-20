package com.amefure.linkmark.View.Locator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R
import com.amefure.linkmark.ViewModel.LocatorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LocatorListFragment : Fragment() {

    private var categoryId: Int = 0

    private val viewModel: LocatorViewModel by viewModels()

    // UI
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(AppArgKey.ARG_CATEGORY_ID_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locator_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var addButton: FloatingActionButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_frame, LocatorInputFragment.newInstance(categoryId))
                addToBackStack(null)
                commit()
            }
        }

        recyclerView = view.findViewById(R.id.locator_list)

        // 対象カテゴリIDに紐づくデータを取得する
        viewModel.fetchAllLocator(categoryId = categoryId)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireActivity(), DividerItemDecoration.VERTICAL)
        )
        viewModel.locatorList.observe(this.requireActivity()) { it
            Log.e("-------変化したよ", "url")
            val adapter = LocatorAdapter(it)
            adapter.setOnTapedListner(
                object :LocatorAdapter.onTapedListner{
                    override fun onTaped(url: String) {
                        Log.e("-------", url)
                    }
                }
            )
            recyclerView.adapter = adapter
        }
    }




    /**
     * 引数を渡すため
     * シングルトンインスタンス生成
     */
    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int) =
            LocatorListFragment().apply {
                arguments = Bundle().apply {
                    putInt(AppArgKey.ARG_CATEGORY_ID_KEY, categoryId)
                }
            }
    }
}