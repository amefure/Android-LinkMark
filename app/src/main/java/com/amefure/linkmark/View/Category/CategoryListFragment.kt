package com.amefure.linkmark.View.Category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Locator.LocatorListFragment
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

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireActivity(), DividerItemDecoration.VERTICAL)
        )

        viewModel.categoryList.observe(this.requireActivity()) { it
            val adapter = CategoryAdapter(it)
            adapter.setOnTapedListner(
                object :CategoryAdapter.onTappedListner{
                    override fun onTaped(categoryId: Int) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, LocatorListFragment.newInstance(categoryId = categoryId))
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
            )
            recyclerView.adapter = adapter
        }
    }

}