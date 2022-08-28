package com.bsstandard.piece.view.fragment.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.widget.utils.LogUtil

/**
 *packageName    : com.bsstandard.piece.view.fragment.navigation
 * fileName       : KeepStateNavigator
 * author         : piecejhm
 * date           : 2022/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/24        piecejhm       최초 생성
 */


@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        val tag = destination.id.toString()

        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment


        // primaryNavigationFragment 가 존재하면 기존 primaryFragment hide 처리 (재생성 방지)
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)      // 최초로 생성되는 fragment
        LogUtil.logE("최초 생성 Fragment : $fragment")
        if (fragment == null) {
            val className = destination.className
            LogUtil.logE("className : $className" )
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)    // add로 fragment 최초 생성 (add)
            LogUtil.logE("add 최초 생성 fragment : $containerId + $fragment")

        } else {
            transaction.show(fragment)  // 이미 생성되어 있던 fragment 라면 show
            LogUtil.logE("이미 생성되어있던 fragment : $fragment")
        }

        transaction.setPrimaryNavigationFragment(fragment) // destination fragment 를 primary 로 설정

        // transaction 관련 fragment 상태 변경 최적화
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}