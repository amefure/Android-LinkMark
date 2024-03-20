package com.amefure.linkmark.Model.Config

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.amefure.linkmark.R

enum class AppThemaColor {
    RED {
        override fun color(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, R.color.ex_red)
    },
    YELLOW {
        override fun color(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, R.color.ex_yellow)
    },
    BLUE {
        override fun color(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, R.color.ex_blue)
    },
    GREEN {
        override fun color(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, R.color.ex_green)
    },
    PURPLE {
        override fun color(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, R.color.ex_purple)
    };
    abstract fun color(context: Context): ColorStateList?
}