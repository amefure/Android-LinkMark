package com.amefure.linkmark.View.Locator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.Model.Database.Locator
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.ViewModel.LocatorViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class LocatorInputFragment : Fragment() {

    private var categoryId: Int = 0
    private var locatorId: Int = -1
    private var locator: Locator? = null

    private val viewModel: LocatorViewModel by viewModels()

    private lateinit var inputTitleText: EditText
    private lateinit var inputUrlText: EditText
    private lateinit var inputMemoText: EditText

    private var isValidationEmptyFlag = false
    private var isValidationUrlFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(AppArgKey.ARG_CATEGORY_ID_KEY)
            locatorId = it.getInt(AppArgKey.ARG_LOCATOR_ID_KEY)
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

        // 広告の読み込み
        var adView: AdView = view.findViewById(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())

        inputTitleText = view.findViewById(R.id.title_edit_text)
        inputUrlText = view.findViewById(R.id.url_edit_text)
        inputMemoText = view.findViewById(R.id.memo_edit_text)

        setUpHeaderAction(view)

        // Updateなら
        if (locatorId != -1) {
            subscribeLocator()
            viewModel.fetchAllLocator(categoryId)
        }
    }

    /**
     * カテゴリリスト観測
     */
    private fun subscribeLocator() {
        viewModel.locatorList.observe(viewLifecycleOwner) {
            // Update時の初期値格納
            setUpLocatorView()
        }
    }

    /**
     * Updateの場合にUI更新
     */
    private fun setUpLocatorView() {
        locatorId?.let {
            locator = viewModel.locatorList.value?.let {
                it.first { it.id == locatorId }
            }
            locator?.let {
                inputTitleText.setText(it.title)
                inputUrlText.setText(it.url)
                inputMemoText.setText(it.memo)
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
            registerAction(view)
        }
    }

    /**
     * Headerにある右側のボタンに登録処理を実装
     */
    private fun registerAction(view: View) {
        val title: String = inputTitleText.text.toString()
        val url: String = inputUrlText.text.toString()
        val memo: String = inputMemoText.text.toString()

        isValidationEmptyFlag = false
        isValidationUrlFlag = false
        if (title.isEmpty()) {
            isValidationEmptyFlag = true
        }

        if (!isValidURL(url)) {
            isValidationUrlFlag = true
        }

        if (isValidationEmptyFlag || isValidationUrlFlag ) {
            val dialog = CustomNotifyDialogFragment.newInstance(
                title = getString(R.string.dialog_title_notice),
                msg = failedDialogMessage(),
                showPositive = true,
                showNegative = false
            )
            dialog.show(parentFragmentManager, "ValidateTitleAndURLDialog")

            return@registerAction
        }

        locator?.let {
            // Update
            viewModel.updateLocator(
                id = it.id,
                categoryId = categoryId,
                title = title,
                url = url,
                memo = memo,
                order = it.order,
                createdAt = it.createdAt
            )
        }?: run {
            // Create
            viewModel.insertLocator(
                categoryId = categoryId,
                title = title,
                url = url,
                memo = memo
            )
        }
        closedKeyBoard()
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    /**
     * URLのバリデーション
     */
    private fun isValidURL(url: String): Boolean {
        return URLUtil.isValidUrl(url)
    }

    /**
     * 登録時バリデーションメッセージ
     */
    private fun failedDialogMessage(): String {
        var msg = ""
        if (isValidationEmptyFlag) {
            msg = getString(R.string.dialog_msg_validation_title)
        }
        if (isValidationUrlFlag) {
            if (isValidationEmptyFlag) {
                msg += "\n"
            }
            msg += getString(R.string.dialog_msg_validation_url)
        }
        return msg
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
        fun newInstance(categoryId: Int, locatorId: Int?) =
            LocatorInputFragment().apply {
                arguments = Bundle().apply {
                    putInt(AppArgKey.ARG_CATEGORY_ID_KEY, categoryId)
                    putInt(AppArgKey.ARG_LOCATOR_ID_KEY, locatorId ?: -1)
                }
            }
    }

}