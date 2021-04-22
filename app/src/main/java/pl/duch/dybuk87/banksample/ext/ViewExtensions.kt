package pl.duch.dybuk87.banksample.ext

import android.app.Application
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView

fun Context.obtainStyle(attrs: AttributeSet?, id: IntArray, onStyle: (TypedArray) -> Unit) {
    val a = this.theme.obtainStyledAttributes(attrs, id, 0, 0)

    onStyle(a)

    a.recycle()
}

fun <T> TypedArray.handleTextColor(view: T, resId: Int) where T : TextView {
    if (this.hasValue(resId)) {
        val str = this.getString(resId)!!
        if (str.startsWith("#") || str.startsWith("@")) {
            val col = this.getColorStateList(resId)!!
            view.setTextColor(col.defaultColor)
        } else {
            val colEnumIndex = str.toInt() // not used
            //view.setTextColor(col)
        }
    }
}

fun View.dpToPx(dp: Int): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics)

fun Application.dpToPx(dp: Int): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics)

fun Context.dpToPx(dp: Int): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics)