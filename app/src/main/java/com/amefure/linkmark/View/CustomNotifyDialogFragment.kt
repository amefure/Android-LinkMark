package com.amefure.linkmark.View

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.amefure.linkmark.Model.Key.AppArgKey
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Locator.LocatorAdapter


class CustomNotifyDialogFragment : DialogFragment() {

    private var title: String = getString(R.string.dialog_title_notice)
    private var msg: String = ""

    companion object {
        @JvmStatic
        public fun newInstance(title: String, msg: String) =
            CustomNotifyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(AppArgKey.ARG_DIALOG_TITLE_KEY, title)
                    putString(AppArgKey.ARG_DIALOG_MSG_KEY, msg)
                }
            }
    }

    private var positiveListner: onPositiveButtonTappedListner? = null
    private var negativeListner: onNegativeButtonTappedListner? = null

    interface onPositiveButtonTappedListner {
        fun onTapped()
    }


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(AppArgKey.ARG_DIALOG_TITLE_KEY).toString()
            msg = it.getString(AppArgKey.ARG_DIALOG_MSG_KEY).toString()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(this.requireContext())
        val inflater = this.requireActivity().layoutInflater
        val dialog = inflater.inflate(R.layout.fragment_custom_notify_dialog,null)

        val button: Button = dialog.findViewById(R.id.positive_button)
        val title: TextView = dialog.findViewById(R.id.dialog_title)
        title.text = this.title
        val msg: TextView = dialog.findViewById(R.id.dialog_msg)
        msg.text = this.msg
        button.setOnClickListener {
            positiveListner?.onTapped()
            dismiss()
        }
        builder.setView(dialog)
        return builder.create()
    }

}