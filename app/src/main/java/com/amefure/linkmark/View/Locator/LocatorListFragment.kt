package com.amefure.linkmark.View.Locator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.Model.Database.Locator
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.View.Locator.RecycleViewSetting.LocatorAdapter
import com.amefure.linkmark.View.Locator.RecycleViewSetting.LocatorItemTouchListener
import com.amefure.linkmark.View.Utility.OneTouchHelperCallback
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
                add(R.id.main_frame, LocatorInputFragment.newInstance(categoryId, null))
                addToBackStack(null)
                commit()
            }
        }

        // リサイクルビューの角丸
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

            adapter.setOnTappedListner(
                object : LocatorAdapter.onTappedListner {
                    override fun onEditTapped(locatorId: Int) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, LocatorInputFragment.newInstance(categoryId, locatorId))
                            commit()
                        }
                    }

                    override fun onDeleteTapped(locator: Locator, completion: (result: Boolean) -> Unit) {
                        val dialog = CustomNotifyDialogFragment.newInstance(
                            title = getString(R.string.dialog_title_notice),
                            msg = getString(R.string.dialog_msg_delete_confirm, locator.title),
                            showPositive = true,
                            showNegative = true
                        )
                        dialog.setOnTappedListner(
                            object : CustomNotifyDialogFragment.onTappedListner {
                                override fun onNegativeButtonTapped() { }

                                override fun onPositiveButtonTapped() {
                                    viewModel.deleteLocator(locator)
                                    completion(true)
                                }
                            }
                        )
                        dialog.show(parentFragmentManager, "ValidateNameDialog")
                    }
                }
            )
            val itemTouchListener = LocatorItemTouchListener()
            itemTouchListener.setOnTappedListner(
                object : LocatorItemTouchListener.onTappedListner{
                    override fun onTapped(url: String) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, ControlWebViewFragment.newInstance(url))
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
            )
            recyclerView.addOnItemTouchListener(itemTouchListener)

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