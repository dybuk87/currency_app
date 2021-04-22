package pl.duch.dybuk87.banksample.components.shimmer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.ext.obtainStyle


class ShimmerView : View {

    var color = 0
    var alphaVal = 0.8f
    private val p = Paint()

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        color = ContextCompat.getColor(context, R.color.content_text)
        val resId = R.styleable.ShimmerView_shimmerColor
        context.obtainStyle(attrs, R.styleable.ShimmerView) {
            if (it.hasValue(resId)) {
                val str = it.getString(resId)!!
                color = if (str.startsWith("#") || str.startsWith("@")) {
                    val col = it.getColorStateList(resId)!!
                    col.defaultColor
                } else {
                    val enumIndex = str.toInt() // no enum yet
                    color
                }
            }

            alphaVal = it.getFloat(R.styleable.ShimmerView_shimmerAlpha, 0.8f)
        }
    }


    override fun onDraw(canvas: Canvas) {
        var globalTimePassed = System.currentTimeMillis() - startTime
        globalTimePassed = (globalTimePassed).toLong() // speed

        if (globalTimePassed > 1000) {
            invert = !invert
            startTime = System.currentTimeMillis()
            globalTimePassed = 0
        }

        val animPercent = globalTimePassed / 1000.0f

        val gradient = RadialGradient(
            5.0f * width.toFloat() - 5.0f * animPercent * width, (height / 2).toFloat(),
            (width * 5.0).toFloat(),
            if (invert) color else Color.WHITE,
            if (!invert) color else Color.WHITE,
            Shader.TileMode.CLAMP
        )


        p.isDither = true
        p.shader = gradient
        p.alpha = (alphaVal * 255f).toInt()

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), p)

        postDelayed({ invalidate() }, 16)
    }

    companion object {
        var invert = false
        internal var startTime = System.currentTimeMillis()
    }
}
