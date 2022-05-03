//package com.buysellstandards.presentation.base.view.activity
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.annotation.LayoutRes
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//
///**
// * @author jhm
// * @since 2022/03/29
// * @commnet 공통 BaseActivity Class
// **/
//
//
//abstract class BaseActivity<B : ViewDataBinding>(
//    @LayoutRes val layoutId : Int
//) : AppCompatActivity() {
//    lateinit var binding: B
//    private val compositeDisabled = CompositeDisposable()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, layoutId)
//        binding.lifecycleOwner = this
//    }
//
//    protected fun showToast(msg: String) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        compositeDisabled.clear()
//    }
//
//}