package com.amefure.linkmark.View.AppLock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.amefure.linkmark.R

class AppLockFragment : Fragment() {

    private lateinit var number1: Button
    private lateinit var number2: Button
    private lateinit var number3: Button
    private lateinit var number4: Button
    private lateinit var number5: Button
    private lateinit var number6: Button
    private lateinit var number7: Button
    private lateinit var number8: Button
    private lateinit var number9: Button
    private lateinit var number0: Button
    private lateinit var numberBack: Button


    private lateinit var password1: TextView
    private lateinit var password2: TextView
    private lateinit var password3: TextView
    private lateinit var password4: TextView

    private var password: MutableList<Int> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        number1 = view.findViewById(R.id.number_button1)
        number2 = view.findViewById(R.id.number_button2)
        number3 = view.findViewById(R.id.number_button3)
        number4 = view.findViewById(R.id.number_button4)
        number5 = view.findViewById(R.id.number_button5)
        number6 = view.findViewById(R.id.number_button6)
        number7 = view.findViewById(R.id.number_button7)
        number8 = view.findViewById(R.id.number_button8)
        number9 = view.findViewById(R.id.number_button9)
        number0 = view.findViewById(R.id.number_button0)
        numberBack = view.findViewById(R.id.number_button_back)

        password1 = view.findViewById(R.id.password_label1)
        password2 = view.findViewById(R.id.password_label2)
        password3 = view.findViewById(R.id.password_label3)
        password4 = view.findViewById(R.id.password_label4)

        number1.setOnClickListener {
            clickNumber(1)
        }
        number2.setOnClickListener {
            clickNumber(2)
        }
        number3.setOnClickListener {
            clickNumber(3)
        }
        number4.setOnClickListener {
            clickNumber(4)
        }
        number5.setOnClickListener {
            clickNumber(5)
        }
        number6.setOnClickListener {
            clickNumber(6)
        }
        number7.setOnClickListener {
            clickNumber(7)
        }
        number8.setOnClickListener {
            clickNumber(8)
        }
        number9.setOnClickListener {
            clickNumber(9)
        }
        number0.setOnClickListener {
            clickNumber(0)
        }
        numberBack.setOnClickListener {
            if (password.size != 0) {
                password.removeAt(password.size - 1)
                when(password.size) {
                    0 -> password1.text = getString(R.string.app_lock_password_placeholder)
                    1 -> password2.text = getString(R.string.app_lock_password_placeholder)
                    2 -> password3.text = getString(R.string.app_lock_password_placeholder)
                    3 -> password4.text = getString(R.string.app_lock_password_placeholder)
                }
            }
        }
    }

    private fun clickNumber(num: Int) {
        if (password.size != 4) {
            password.add(num)
            when(password.size) {
                1 -> password1.text = getString(R.string.app_lock_password_fill)
                2 -> password2.text = getString(R.string.app_lock_password_fill)
                3 -> password3.text = getString(R.string.app_lock_password_fill)
                4 -> password4.text = getString(R.string.app_lock_password_fill)
            }
        }
    }
}