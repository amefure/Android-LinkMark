package com.amefure.linkmark.View.Utility

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

class ClipOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val margin = dp2Px(10f, view.context).toInt()
        outline.setRoundRect(
            margin, margin, (view.width - margin), (view.height - margin),
            dp2Px(10f, view.context)
        )
    }

    private fun dp2Px(dp: Float, context: Context): Float {
        val metrics = context.resources.displayMetrics
        return dp * metrics.density
    }
}