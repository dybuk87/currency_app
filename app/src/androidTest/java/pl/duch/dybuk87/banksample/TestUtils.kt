package pl.duch.dybuk87.banksample

import android.R.attr.resource
import android.graphics.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import io.mockk.MockKAnnotations
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import java.lang.Math.abs
import java.util.*


class InstMockkitoRule : MethodRule {
    override fun apply(base: Statement, method: FrameworkMethod?, target: Any): Statement =
        object : Statement() {
            override fun evaluate() {
                MockKAnnotations.init(target)
                base.evaluate()
            }
        }

}

fun withDrawable(@DrawableRes id: Int, targetColor: Int = 0x000000) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("ImageView with drawable same as drawable with id $id")
    }

    fun dump(bitmap: Bitmap) {
        for (y in 0 until bitmap.height) {
            var line = ""
            for (x in 0 until bitmap.width) {
                line += bitmap.getPixel(x, y).toString() + " "
            }
            Log.i("L$y", line)
        }
    }

    fun tintImage(bitmap: Bitmap, color: Int): Bitmap {
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        val bitmapResult = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapResult)
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint)
        return bitmapResult
    }

    override fun matchesSafely(view: View): Boolean {
        val context = view.context
        val expectedBitmap = tintImage(context.getDrawable(id)?.toBitmap()!!, targetColor)
//        dump(expectedBitmap)
//
//        if (view is ImageView) {
//            dump(view.drawable.toBitmap())
//        }

        return view is ImageView && sameAs(view.drawable.toBitmap(), expectedBitmap)//view.drawable.toBitmap().sameAs(expectedBitmap)
    }


    fun sameAs(A: Bitmap, B: Bitmap): Boolean {

        // Different types of image
        if (A.config != B.config) return false

        // Different sizes
        if (A.width != B.width) return false
        if (A.height != B.height) return false

        // Allocate arrays - OK because at worst we have 3 bytes + Alpha (?)
        val w = A.width
        val h = A.height
        val argbA = IntArray(w * h)
        val argbB = IntArray(w * h)
        A.getPixels(argbA, 0, w, 0, 0, w, h)
        B.getPixels(argbB, 0, w, 0, 0, w, h)

        var invalid = 0
        val th = 20
        for (y in 0 until h) {
            for(x in 0 until w) {
                val pixA = argbA[x + y * w] and 0xFFFFFF
                val pixB = argbB[x + y * h] and 0xFFFFFF

                val rd = ((pixA shr 16) and 0xFF) - ((pixB shr 16) and 0xFF)
                val gd = ((pixA shr 8) and 0xFF) - ((pixB shr 8) and 0xFF)
                val bd = (pixA and 0xFF) - (pixB and 0xFF)

                val dc = abs(rd) + abs(gd) + abs(bd)
                if (dc > th * 3)  {
                    invalid++
                }
            }
        }
        val percent = 100 - (100 * invalid / (w*h))
        Log.i("COMPARE", "valid $percent%")

        return percent > 90
    }

}