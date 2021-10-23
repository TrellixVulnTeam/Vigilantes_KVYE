package edu.umich.Vigilantes

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String, short: Boolean = true) {
    Toast.makeText(this, message, if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}
fun Context.dp2px(dp: Float): Int {
    return Math.ceil((dp * resources.displayMetrics.density).toDouble()).toInt()
}