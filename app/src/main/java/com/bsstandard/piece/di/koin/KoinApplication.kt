//package com.bsstandard.piece.di.koin
//import android.app.Application
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import org.koin.android.BuildConfig
//import org.koin.android.ext.koin.androidContext
//import org.koin.android.ext.koin.androidFileProperties
//import org.koin.android.ext.koin.androidLogger
//import org.koin.core.context.startKoin
//import org.koin.core.logger.Level
//
///**
// *packageName    : com.bsstandard.piece.di
// * fileName       : MyApplication
// * author         : piecejhm
// * date           : 2022/04/27
// * description    : DI - Koin Component ( 추후 로직 개선시 사용 )
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2022/04/27        piecejhm       최초 생성
// */
//
//
//class KoinApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        startKoin {
//            if (BuildConfig.DEBUG) {
//                androidLogger()  // Koin 로그를 Android Log에 남길 수 있다.
//            } else {
//                androidLogger(Level.NONE)
//            }
//            androidContext(this@KoinApplication) // context 의 인스턴스를 MyApplication에 전달해준다.
//            androidFileProperties() // assets/koin.properties 파일에서 프로퍼티를 가져옴
//            modules(
//               // NetworkModule,          // 네트워크 모듈
//               // ViewModelModule        // 해당되는 뷰모델 모듈
//               // RepositoryModule,       // 필요 기능 모아놓은 모듈
//               // UseCaseModule           // 사용자 케이스 모듈
//            )
//        }
//    }
//
//
//}
//
//fun View.hide() {
//    this.visibility = View.GONE
//}
//
//fun View.show() {
//    this.visibility = View.VISIBLE
//}
//
//fun ViewGroup.inflateLayout(layoutId: Int, attachToRoot: Boolean = false): View {
//    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
//}