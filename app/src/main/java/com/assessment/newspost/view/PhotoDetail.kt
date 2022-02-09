package com.assessment.newspost.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.Matrix
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.assessment.newspost.databinding.ActivityPhotoDetailBinding
import com.assessment.newspost.model.PhotoModel
import com.assessment.newspost.utils.Status
import com.assessment.newspost.utils.getUrl
import com.assessment.newspost.utils.toasShort
import com.assessment.newspost.viewmodel.NewsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import java.lang.StringBuilder

class PhotoDetail : AppCompatActivity() {
    companion object{
        private const val KEY_PHOTO = "photo"
        private const val MIN_ZOOM = 1f
        private const val MAX_ZOOM = 1F
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        fun newIntent(context: Context, idPhoto: Int) : Intent{
            return Intent(context, PhotoDetail::class.java)
                .putExtra(KEY_PHOTO, idPhoto)
                .setFlags(FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
        }
    }
    private lateinit var binding: ActivityPhotoDetailBinding
    private lateinit var viewModel: NewsViewModel
    private var photoId = 0
    private var matrix = Matrix()
    private var savedMatrix = Matrix()
    private var mode = NONE
    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        photoId = intent.getIntExtra(KEY_PHOTO, 0)
        setContentView(binding.root)
        setupViewModel()
        getPhotoData { photoModel ->
            setPhoto(photoModel)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touch = View.OnTouchListener { v, motion ->
        val view = v as ImageView
        view.scaleType = ImageView.ScaleType.MATRIX
        var scale : Float? = null
        dumpEvent(motion)

        when(motion.action and MotionEvent.ACTION_MASK){
            MotionEvent.ACTION_DOWN -> {
                matrix.set(view.imageMatrix)
                savedMatrix.set(matrix)
                start.set(motion.getX(), motion.getY())
                mode = DRAG
            }
            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(motion)
                if (oldDist > 5f){
                    savedMatrix.set(matrix)
                    midPoint(mid, motion)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG){
                    matrix.set(savedMatrix)
                    matrix.postTranslate(motion.getX() - start.x, motion.getY() - start.y)
                } else if (mode == ZOOM){
                    var newDist = spacing(motion)
                    if (newDist > 5f){
                        matrix.set(savedMatrix)
                        scale = newDist / oldDist
                        matrix.postScale(scale, scale, mid.x, mid.y)
                    }
                }
            }
        }
        view.imageMatrix = matrix
        return@OnTouchListener true
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    private fun dumpEvent(event: MotionEvent) {
        val names = arrayOf(
            "DOWN",
            "UP",
            "MOVE",
            "CANCEL",
            "OUTSIDE",
            "POINTER_DOWN",
            "POINTER_UP",
            "7?",
            "8?",
            "9?"
        )
        val sb = StringBuilder()
        val action = event.action
        val actionCode = action and MotionEvent.ACTION_MASK
        sb.append("event ACTION_").append(names[actionCode])
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action shr MotionEvent.ACTION_POINTER_ID_SHIFT)
            sb.append(")")
        }
        sb.append("[")
        for (i in 0 until event.pointerCount) {
            sb.append("#").append(i)
            sb.append("(pid ").append(event.getPointerId(i))
            sb.append(")=").append(event.getX(i).toInt())
            sb.append(",").append(event.getY(i).toInt())
            if (i + 1 < event.pointerCount) sb.append(";")
        }
        sb.append("]")
    }
    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this)[NewsViewModel::class.java]
        viewModel.getPhotoDetail(photoId)
    }

    private fun getPhotoData(callbacks:(PhotoModel) -> Unit){
        viewModel.getPhoto().observe(this, Observer {
            when(it.status){
                Status.LOADING -> toasShort("Loading")
                Status.SUCCES -> it.data?.let { photo ->
                    callbacks(photo)
                }
                Status.ERROR -> toasShort("Terjadi Kesalahan")
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPhoto(photoModel: PhotoModel){
        Glide.with(applicationContext)
            .load(getUrl(photoModel.url.toString()))
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(binding.imageView)

        binding.tvPhotoTitle.text = photoModel.title?.uppercase()
        binding.imageView.setOnTouchListener(touch)
    }
}