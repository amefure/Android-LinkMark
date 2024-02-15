package com.amefure.linkmark.View.Category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.amefure.linkmark.R
import com.amefure.linkmark.ViewModel.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryListFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var addButton: FloatingActionButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            Log.e("------","Tapped")
            viewModel.insertCategory("Test","red")
            var es = viewModel.fetchAllCategorys()
            Log.e("------",es.toString())
        }

    }
}