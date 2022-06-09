package com.example.trampled_trails.ui.helper

import android.os.SystemClock
import android.view.MotionEvent

fun createOnMapClickEvent(center: Pair<Int, Int>): Pair<MotionEvent, MotionEvent> {
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis() + 10
    val downAction = MotionEvent.obtain(
        downTime, eventTime, MotionEvent.ACTION_DOWN,
        center.first.toFloat(), center.second.toFloat(), 0
    )
    val upAction = MotionEvent.obtain(
        downTime, eventTime, MotionEvent.ACTION_UP,
        center.first.toFloat(), center.second.toFloat(), 0
    )

    return Pair(downAction, upAction)
}