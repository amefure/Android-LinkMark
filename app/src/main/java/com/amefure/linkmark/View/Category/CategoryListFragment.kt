package com.amefure.linkmark.View.Category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Category.RecycleViewSetting.CategoryAdapter
import com.amefure.linkmark.View.Category.RecycleViewSetting.ItemTouchListener
import com.amefure.linkmark.View.Category.RecycleViewSetting.OneTouchHelperCallback
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.View.Locator.LocatorListFragment
import com.amefure.linkmark.View.Utility.ClipOutlineProvider
import com.amefure.linkmark.ViewModel.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()

    // UI
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchAllCategorys()

        recyclerView = view.findViewById(R.id.category_list)
        recyclerView.outlineProvider = ClipOutlineProvider()
        recyclerView.clipToOutline = true

        // ヘッダーセットアップ
        setUpHeaderAction(view)

        var addButton: FloatingActionButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_frame, CategoryInputFragment())
                addToBackStack(null)
                commit()
            }
        }

        setUpRecyclerView()
    }


    /**
     * ヘッダーボタンセットアップ
     * [LeftButton]：非表示(GONE)
     * [RightButton]：設定画面遷移ボタン
     */
    private fun setUpHeaderAction(view: View) {
        val headerView: ConstraintLayout = view.findViewById(R.id.include_header)
        val leftButton: ImageButton = headerView.findViewById(R.id.left_button)
        leftButton.visibility = View.GONE

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.setImageResource(R.drawable.button_settings)
        rightButton.setOnClickListener {
            // TODO: 設定画面へ遷移
        }
    }

    /**
     * リサイクルビューセットアップ
     * [categoryList]を観測して変更があるたびに自動描画
     */
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireActivity(), DividerItemDecoration.VERTICAL)
        )

        viewModel.categoryList.observe(this.requireActivity()) {
            val adapter = CategoryAdapter(viewModel, it, this.requireContext())
            adapter.setOnTappedListner(
                object : CategoryAdapter.onTappedListner {
                    override fun onEditTapped(categoryId: Int) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, CategoryInputFragment.newInstance(categoryId))
                            addToBackStack(null)
                            commit()
                        }
                    }

                    override fun onDeleteTapped(category: Category, completion: (result: Boolean) -> Unit) {
                        val dialog = CustomNotifyDialogFragment.newInstance(
                            title = getString(R.string.dialog_title_notice),
                            msg = getString(R.string.dialog_msg_delete_confirm, category.name),
                            showPositive = true,
                            showNegative = true
                        )
                        dialog.setOnTappedListner(
                            object : CustomNotifyDialogFragment.onTappedListner {
                                override fun onNegativeButtonTapped() { }

                                override fun onPositiveButtonTapped() {
                                    viewModel.deleteCategory(category)
                                    completion(true)
                                }
                            }
                        )
                        dialog.show(parentFragmentManager, "ValidateNameDialog")
                    }
                }
            )

            val itemTouchListener = ItemTouchListener()
            itemTouchListener.setOnTappedListner(
                object : ItemTouchListener.onTappedListner{
                    override fun onTapped(categoryId: Int) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, LocatorListFragment.newInstance(categoryId = categoryId))
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
}
