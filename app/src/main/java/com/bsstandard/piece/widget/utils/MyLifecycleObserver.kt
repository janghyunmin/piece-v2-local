package com.bsstandard.piece.widget.utils

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 *packageName    : com.bsstandard.piece.widget.utils
 * fileName       : MyLifecycleObserver
 * author         : piecejhm
 * date           : 2022/11/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/11/02        piecejhm       최초 생성
 */

// DefaultLifecycleObserver 인터페이스 구현
class MyLifecycleObserver: DefaultLifecycleObserver {
    // 필요한 메서드를 구현
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onCreate")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onResume")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onStart")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onDestroy")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d(owner.toString(), "In LifecycleObserver - onStop")
    }
}