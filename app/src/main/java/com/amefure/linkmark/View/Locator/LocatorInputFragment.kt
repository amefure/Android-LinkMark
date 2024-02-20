package com.amefure.linkmark.View.Locator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R
import com.amefure.linkmark.ViewModel.LocatorViewModel

class LocatorInputFragment : Fragment() {

    private var categoryId: Int = 0

    private val viewModel: LocatorViewModel by viewModels()

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
        return inflater.inflate(R.layout.fragment_locator_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 登録ボタンのセットアップ
        setUpRegisterButton(view)
    }

    /**
     * Headerにある右側のボタンに登録処理を実装
     */
    private fun setUpRegisterButton(view: View) {
        val inputTitleText: EditText = view.findViewById(R.id.title_edit_text)
        val inputUrlText: EditText = view.findViewById(R.id.url_edit_text)
        val inputMemoText: EditText = view.findViewById(R.id.memo_edit_text)

        val registerButton: ImageButton = view.findViewById<ConstraintLayout>(R.id.include_header).findViewById(R.id.right_button)
        registerButton.setOnClickListener {
            val title: String = inputTitleText.text.toString()
            val url: String = inputUrlText.text.toString()
            val memo: String = inputMemoText.text.toString()

            if (!title.isEmpty() && !url.isEmpty()) {
                viewModel.insertLocator(
                    categoryId = categoryId,
                    title = title,
                    url = url,
                    memo = memo
                )
                closedKeyBoard()
                parentFragmentManager.popBackStack()
            }
        }
    }

    /**
     * キーボードを閉じるメソッド
     */
    private fun closedKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int) =
            LocatorInputFragment().apply {
                arguments = Bundle().apply {
                    putInt(AppArgKey.ARG_CATEGORY_ID_KEY, categoryId)
                }
            }
    }

}