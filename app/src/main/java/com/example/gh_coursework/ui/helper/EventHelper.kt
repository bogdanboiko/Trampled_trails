package com.example.gh_coursework.ui.helper

import android.os.SystemClock
import android.view.MotionEvent

fun createOnMapClickEvent(center: Pair<Float, Float>): Pair<MotionEvent, MotionEvent> {
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis() + 10
    val downAction = MotionEvent.obtain(
        downTime, eventTime, MotionEvent.ACTION_DOWN,
        center.first , center.second, 0
    )
    val upAction = MotionEvent.obtain(
        downTime, eventTime, MotionEvent.ACTION_UP,
        center.first , center.second, 0
    )

    return Pair(downAction, upAction)
}