package com.amefure.linkmark.View.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R

class CustomNotifyDialogFragment : DialogFragment() {

    /**
     * 引数で受け取るデータ
     */
    private var title: String = ""
    private var msg: String = ""
    private var showPositive: Boolean = true
    private var showNegative: Boolean = true

    /**
     * 引数で受け取る処理
     */
    private var positiveListner: onPositiveButtonTappedListner? = null
    private var negativeListner: onNegativeButtonTappedListner? = null

    /**
     * ポジティブボタンアクション
     */
    interface onPositiveButtonTappedListner {
        fun onTapped()
    }

    /**
     * ネガティブボタンアクション
     */
    interface onNegativeButtonTappedListner {
        fun onTapped()
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnButtonTappedListner(positiveListner: onPositiveButtonTappedListner? = null, negativeListner: onNegativeButtonTappedListner? = null) {
        this.positiveListner = positiveListner
        this.negativeListner = negativeListner
    }

    /**
     * インスタンス化メソッド
     */
    companion object {
        @JvmStatic
        public fun newInstance(title: String, msg: String, showPositive: Boolean = true, showNegative: Boolean = true) =
            CustomNotifyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(AppArgKey.ARG_DIALOG_TITLE_KEY, title)
                    putString(AppArgKey.ARG_DIALOG_MSG_KEY, msg)
                    putBoolean(AppArgKey.ARG_DIALOG_SHOW_POSITIVE_KEY, showPositive)
                    putBoolean(AppArgKey.ARG_DIALOG_SHOW_NEGATIVE_KEY, showNegative)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(AppArgKey.ARG_DIALOG_TITLE_KEY).toString()
            msg = it.getString(AppArgKey.ARG_DIALOG_MSG_KEY).toString()
            showPositive = it.getBoolean(AppArgKey.ARG_DIALOG_SHOW_POSITIVE_KEY)
            showNegative = it.getBoolean(AppArgKey.ARG_DIALOG_SHOW_NEGATIVE_KEY)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(this.requireContext())
        val inflater = this.requireActivity().layoutInflater
        val dialog = inflater.inflate(R.layout.fragment_custom_notify_dialog,null)

        val title: TextView = dialog.findViewById(R.id.dialog_title)
        val msg: TextView = dialog.findViewById(R.id.dialog_msg)
        val positiveButton: Button = dialog.findViewById(R.id.positive_button)
        val negativeButton: Button = dialog.findViewById(R.id.negative_button)

        if (!showPositive) {
            positiveButton.visibility = View.GONE
        }

        if (!showNegative) {
            negativeButton.visibility = View.GONE
        }

        title.text = this.title
        msg.text = this.msg

        // ポジティブボタンアクション実装
        positiveButton.setOnClickListener {
            positiveListner?.onTapped()
            dismiss()
        }

        // ネガティブボタンアクション実装
        negativeButton.setOnClickListener {
            negativeListner?.onTapped()
            dismiss()
        }

        builder.setView(dialog)
        return builder.create()
    }
}