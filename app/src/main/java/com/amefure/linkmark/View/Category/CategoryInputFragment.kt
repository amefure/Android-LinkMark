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
import com.amefure.linkmark.Model.Config.AppThemaColor
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.ViewModel.CategoryViewModel

class CategoryInputFragment : Fragment() {

    private var categoryId: Int? = null
    private var category: Category? = null

    private val viewModel: CategoryViewModel by viewModels()

    private var selectedColor: String = AppThemaColor.RED.name

    private lateinit var inputNameText: EditText
    private lateinit var redButton: Button
    private lateinit var yellowButton: Button
    private lateinit var blueButton: Button
    private lateinit var greenButton: Button
    private lateinit var purpleButton: Button

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
        return inflater.inflate(R.layout.fragment_category_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputNameText = view.findViewById(R.id.name_edit_text)
        redButton = view.findViewById(R.id.select_red_button)
        yellowButton = view.findViewById(R.id.select_yellow_button)
        blueButton = view.findViewById(R.id.select_blue_button)
        greenButton = view.findViewById(R.id.select_green_button)
        purpleButton = view.findViewById(R.id.select_purple_button)

        // Updateなら
        if (categoryId != null) {
            subscribeCategory()
            viewModel.fetchAllCategorys()
        }

        // ヘッダーセットアップ
        setUpHeaderAction(view)
        // カラー選択ボタン押下時のセットアップ
        setUpColorButton(view)
    }


    /**
     * カテゴリリスト観測
     */
    private fun subscribeCategory() {
        viewModel.categoryList.observe(viewLifecycleOwner) {
            // Update時の初期値格納
            setUpCategoryView()
        }
    }

    /**
     * Updateの場合にUI更新
     */
    private fun setUpCategoryView() {
        categoryId?.let {
            category = viewModel.categoryList.value?.let {
                it.first { it.id == categoryId }
            }
            category?.let {
                inputNameText.setText(it.name)
                selectedColor = it.color

               when(it.color) {
                    AppThemaColor.RED.name -> redButton.alpha = 0.5f
                    AppThemaColor.YELLOW.name -> yellowButton.alpha = 0.5f
                    AppThemaColor.BLUE.name -> blueButton.alpha = 0.5f
                    AppThemaColor.GREEN.name -> greenButton.alpha = 0.5f
                    AppThemaColor.PURPLE.name -> purpleButton.alpha = 0.5f
                    else -> redButton.alpha = 0.5f
                }
            }
        }
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
            closedKeyBoard()
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.setOnClickListener {
            registerAction()
        }
    }

    /**
     * Headerにある右側のボタンに登録処理を実装
     */
    private fun registerAction() {
        val name: String = inputNameText.text.toString()
        if (!name.isEmpty()) {
            category?.let {
                // Update
                viewModel.updateCategory(it.id, name, selectedColor, it.order)
            }?: run {
                // Create
                viewModel.insertCategory(name, selectedColor)
            }
            closedKeyBoard()
            parentFragmentManager.beginTransaction().remove(this).commit()
        } else {
            val dialog = CustomNotifyDialogFragment.newInstance(
                title = getString(R.string.dialog_title_notice),
                msg = getString(R.string.dialog_msg_validation_name),
                showPositive = true,
                showNegative = false
            )
            dialog.show(parentFragmentManager, "ValidateNameDialog")
            return@registerAction
        }
    }


    /**
     * カラー選択ボタン押下時の処理
     */
    private fun setUpColorButton(view: View) {
        redButton.setOnClickListener {
            resetSelectColorButton()
            redButton.alpha = 0.5f
            selectedColor = AppThemaColor.RED.name
        }
        yellowButton.setOnClickListener {
            resetSelectColorButton()
            yellowButton.alpha = 0.5f
            selectedColor = AppThemaColor.YELLOW.name
        }
        blueButton.setOnClickListener {
            resetSelectColorButton()
            blueButton.alpha = 0.5f
            selectedColor = AppThemaColor.BLUE.name
        }
        greenButton.setOnClickListener {
            resetSelectColorButton()
            greenButton.alpha = 0.5f
            selectedColor = AppThemaColor.GREEN.name
        }
        purpleButton.setOnClickListener {
            resetSelectColorButton()
            purpleButton.alpha = 0.5f
            selectedColor = AppThemaColor.PURPLE.name
        }
    }

    /**
     * 選択状態リセット
     */
    private fun resetSelectColorButton() {
        redButton.alpha = 1f
        yellowButton.alpha = 1f
        blueButton.alpha = 1f
        greenButton.alpha = 1f
        purpleButton.alpha = 1f
    }


    /**
     * キーボードを閉じるメソッド
     */
    private fun closedKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    /**
     * 引数を渡すため
     * シングルトンインスタンス生成
     */
    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int) =
            CategoryInputFragment().apply {
                arguments = Bundle().apply {
                    putInt(AppArgKey.ARG_CATEGORY_ID_KEY, categoryId)
                }
            }
    }
}