package com.aktv.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class AkIntroView @JvmOverloads constructor(c: Context, a: AttributeSet? = null) : View(c, a) {
    var onReveal: (() -> Unit)? = null
    var onDone: (() -> Unit)? = null
    var wordmark: Typeface? = null
    private val logo = BitmapFactory.decodeResource(resources, R.drawable.ic_mark)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val rect = RectF()
    private var progress = 0f
    private var finished = false
    private val anim = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000; interpolator = DecelerateInterpolator()
        addUpdateListener { progress = it.animatedValue as Float; invalidate() }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) { finishIntro() }
        })
    }
    fun start() { if (!anim.isStarted) anim.start() }
    fun skip() { anim.end() }
    private fun finishIntro() {
        if (finished) return
        finished = true; onReveal?.invoke(); onDone?.invoke()
    }
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Accent.ABYSS)
        val size = minOf(width * 0.35f, height * 0.5f) * (0.95f + 0.05f * progress)
        rect.set((width-size)/2, (height-size)/2, (width+size)/2, (height+size)/2)
        paint.alpha = (255 * (progress * 4).coerceAtMost(1f)).toInt()
        canvas.drawBitmap(logo, null, rect, paint)
    }
    override fun onDetachedFromWindow() { anim.cancel(); super.onDetachedFromWindow() }
}
