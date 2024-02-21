package com.amefure.linkmark.View.Category

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.amefure.linkmark.Model.AppThemaColor
import com.amefure.linkmark.R
import com.amefure.linkmark.ViewModel.CategoryViewModel
class CategoryInputFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels()

    private var selectedColor: String = AppThemaColor.RED.name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ヘッダーセットアップ
        setUpHeaderAction(view)
        // カラー選択ボタン押下時のセットアップ
        setUpColorButton(view)
    }


    /**
     * ヘッダーボタンセットアップ
     * [LeftButton]：backButton
     * [RightButton]：登録処理ボタン
     */
    private fun setUpHeaderAction(view: View) {
        val headerView: ConstraintLayout = view.findViewById(R.id.include_header)

        val leftButton: ImageButton = headerView.findViewById(R.id.left_button)
        leftButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.setOnClickListener {
            registerAction(view)
        }
    }

    /**
     * Headerにある右側のボタンに登録処理を実装
     */
    private fun registerAction(view: View) {
        val inputNameText: EditText = view.findViewById(R.id.name_edit_text)

        val name: String = inputNameText.text.toString()
        if (!name.isEmpty()) {
            viewModel.insertCategory(name, selectedColor)
            closedKeyBoard()
            parentFragmentManager.popBackStack()
        }
    }


    /**
     * カラー選択ボタン押下時の処理
     */
    private fun setUpColorButton(view: View) {
        val redButton: Button = view.findViewById(R.id.select_red_button)
        val yellowButton: Button = view.findViewById(R.id.select_yellow_button)
        val blueButton: Button = view.findViewById(R.id.select_blue_button)
        val greenButton: Button = view.findViewById(R.id.select_green_button)
        val purpleButton: Button = view.findViewById(R.id.select_purple_button)

        redButton.setOnClickListener {
            selectedColor = AppThemaColor.RED.name
        }
        yellowButton.setOnClickListener {
            selectedColor = AppThemaColor.YELLOW.name
        }
        blueButton.setOnClickListener {
            selectedColor = AppThemaColor.BLUE.name
        }
        greenButton.setOnClickListener {
            selectedColor = AppThemaColor.GREEN.name
        }
        purpleButton.setOnClickListener {
            selectedColor = AppThemaColor.PURPLE.name
        }
    }


    /**
     * キーボードを閉じるメソッド
     */
    private fun closedKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}