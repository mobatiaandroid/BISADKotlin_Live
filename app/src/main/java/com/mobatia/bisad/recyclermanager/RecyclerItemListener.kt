package com.mobatia.bisad.recyclermanager

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener



class RecyclerItemListener(
    ctx: Context?,
    rv: RecyclerView,
    listener: RecyclerItemListener.RecyclerTouchListener
) :
    OnItemTouchListener {
    private val listener: RecyclerItemListener.RecyclerTouchListener
    private val gd: GestureDetector

    interface RecyclerTouchListener {
        fun onClickItem(v: View?, position: Int)
        fun onLongClickItem(v: View?, position: Int)
    }

    init {
        this.listener = listener
        gd = GestureDetector(ctx, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                // We find the view
                val v = rv.findChildViewUnder(e.x, e.y)
                // Notify the even
                listener.onLongClickItem(v, rv.getChildAdapterPosition(v!!))
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val v = rv.findChildViewUnder(e.x, e.y)
                // Notify the even
                listener.onClickItem(v, rv.getChildAdapterPosition(v!!))
                return true
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        return child != null && gd.onTouchEvent(e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}

