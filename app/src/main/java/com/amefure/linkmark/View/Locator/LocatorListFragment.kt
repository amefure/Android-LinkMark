package com.amefure.linkmark.View.Locator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Category.CategoryInputFragment
import com.amefure.linkmark.View.Category.RecycleViewSetting.CategoryAdapter
import com.amefure.linkmark.View.Category.RecycleViewSetting.CategoryItemTouchListener
import com.amefure.linkmark.View.Category.RecycleViewSetting.OneTouchHelperCallback
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.View.Utility.ClipOutlineProvider
import com.amefure.linkmark.View.WebView.ControlWebViewFragment
import com.amefure.linkmark.ViewModel.LocatorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LocatorListFragment : Fragment() {

    private var categoryId: Int = 0
    private var categoryName: String = ""

    private val viewModel: LocatorViewModel by viewModels()

    // UI
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(AppArgKey.ARG_CATEGORY_ID_KEY)
            categoryName = it.getString(AppArgKey.ARG_CATEGORY_NAME_KEY).toString()
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
        val categoryNameView: TextView = view.findViewById(R.id.category_name_label)
        categoryNameView.text = categoryName.take(7)

        // ヘッダーセットアップ
        setUpHeaderAction(view)


        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_frame, LocatorInputFragment.newInstance(categoryId))
                addToBackStack(null)
                commit()
            }
        }

        recyclerView = view.findViewById(R.id.locator_list)
        recyclerView.outlineProvider = ClipOutlineProvider()
        recyclerView.clipToOutline = true

        // 対象カテゴリIDに紐づくデータを取得する
        viewModel.fetchAllLocator(categoryId = categoryId)

        setUpRecyclerView()
    }

    /**
     * ヘッダーボタンセットアップ
     * [LeftButton]：backButton
     * [RightButton]：非表示(GONE)
     */
    private fun setUpHeaderAction(view: View) {
        val headerView: ConstraintLayout = view.findViewById(R.id.include_header)

        val leftButton: ImageButton = headerView.findViewById(R.id.left_button)
        leftButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.visibility = View.GONE
    }

    /**
     * リサイクルビューセットアップ
     * [locatorList]を観測して変更があるたびに自動描画
     */
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireActivity(), DividerItemDecoration.VERTICAL)
        )
        viewModel.locatorList.observe(viewLifecycleOwner) { it
            val adapter = LocatorAdapter(viewModel, it, this.requireContext())
            adapter.setOnTapedListner(
                object :LocatorAdapter.onTappedListner{
                    override fun onTapped(url: String) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, ControlWebViewFragment.newInstance(url))
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
            )
            OneTouchHelperCallback(recyclerView).build()

            recyclerView.adapter = adapter
        }
    }


    /**
     * 引数を渡すため
     * シングルトンインスタンス生成
     */
    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int, name: String) =
            LocatorListFragment().apply {
                arguments = Bundle().apply {
                    putInt(AppArgKey.ARG_CATEGORY_ID_KEY, categoryId)
                    putString(AppArgKey.ARG_CATEGORY_NAME_KEY, name)
                }
            }
    }
}