package com.example.photoshop_phr

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.EmbossMaskFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar;

@Suppress("deprecation")
class MainActivity : AppCompatActivity() {
    lateinit var graphicView: MyGraphicView

    companion object {
        var sX = 1f
        var sY = 1f
        var angle = 0f
        var satur = 1f
        var blur = false
        var embos = false
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Mini Photoshop"

        val pictureLayout = findViewById<LinearLayout>(R.id.pictureLayout)
        graphicView = MyGraphicView(this)
        pictureLayout.addView(graphicView)

        registerForContextMenu(graphicView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu1, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ibZoomin -> {
                sX = sX + 0.2f
                sY = sY + 0.2f
                graphicView.invalidate()
            }
            R.id.ibZoomout -> {
                sX = sX - 0.2f
                sY = sY - 0.2f
                graphicView.invalidate()
            }
            R.id.ibRotate -> {
                angle = angle + 20
                graphicView.invalidate()
            }
            R.id.ibBright -> {
                satur = satur + 0.2f
                graphicView.invalidate()
            }
            R.id.ibDark -> {
                satur = satur - 0.2f
                graphicView.invalidate()
            }
            R.id.ibGray -> {
                if (satur == 0f)
                    satur = 1f
                else
                    satur = 0f
                graphicView.invalidate()
            }
        }
        return super.onContextItemSelected(item)
    }

    public class MyGraphicView(context: Context) : View(context) {

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val cenX = this.width / 2
            val cenY = this.height / 2
            canvas.scale(sX, sY, cenX.toFloat(), cenY.toFloat())
            canvas.rotate(angle, cenX.toFloat(), cenY.toFloat())

            val paint = Paint()
            val cm = ColorMatrix()
            cm.setSaturation(satur)

            paint.colorFilter = ColorMatrixColorFilter(cm)

            if (blur == true) {
                val bMask: BlurMaskFilter
                bMask = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
                paint.maskFilter = bMask
            }

            if (embos == true) {
                val eMask: EmbossMaskFilter
                eMask = EmbossMaskFilter(floatArrayOf(3f, 3f, 3f), 0.5f, 5f,30f)
                paint.maskFilter = eMask
            }

            val picture = BitmapFactory.decodeResource(resources,
                R.drawable.lena256)
            val picX = (this.width - picture.width) / 2f
            val picY = (this.height - picture.height) / 2f
            canvas.drawBitmap(picture, picX, picY, paint)

            picture.recycle()
        }
    }
}
