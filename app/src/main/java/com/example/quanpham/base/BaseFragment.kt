package com.example.quanpham.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected lateinit var binding: T
    private lateinit var callback: OnBackPressedCallback
//    val firestore = FirebaseFirestore.getInstance()
//    val database = FirebaseDatabase.getInstance()
//    val storage = FirebaseStorage.getInstance()
    open fun handlerBackPressed(){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handlerBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getBinding(inflater, container)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }



    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?) :T
    abstract fun initView()

    fun addFragment(fragment: Fragment){
        (requireActivity() as BaseActivity<*>).addFragment(fragment)
    }

    fun replaceFullViewFragment(fragment: Fragment, addToBackStack: Boolean){
        (requireActivity()  as BaseActivity<*>).replaceFragment(fragment, android.R.id.content, addToBackStack)
    }
    fun replaceFragment(fragment: Fragment) {
        (requireActivity()  as BaseActivity<*>).replaceFragment(fragment)
    }
    open fun closeFragment(fragment: Fragment) {
        (requireActivity() as BaseActivity<*>).handleBackPress()
    }

    fun addAndRemoveCurrentFragment(currentFragment : Fragment, newFragment : Fragment, addToBackStack: Boolean = false) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.remove(currentFragment)
        transaction.add(android.R.id.content, newFragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    protected open fun hideKeyboard() {
        if (activity != null) {
            (activity as BaseActivity<*>?)?.hideKeyboard()
        }
    }

    protected open fun showKeyboard(view: View?) {
        (requireActivity() as BaseActivity<*>?)?.showKeyboard(view)
    }

    protected fun setColorStatusBar(idColor : Int){
        if(activity != null){
            (activity as BaseActivity<*>).window.statusBarColor = ContextCompat.getColor(requireContext(), idColor)
        }
    }

    protected fun getResultListener(requestKey : String, callback : (requestKey : String, bundle : Bundle) -> Unit){
        parentFragmentManager.setFragmentResultListener(requestKey, this
        ) { key, result ->
            callback(key, result)
        }
    }

    protected fun setFragmentResult(requestKey: String, resultBundle : Bundle){
        requireActivity().supportFragmentManager.setFragmentResult(requestKey, resultBundle)
    }
    fun isAPI33OrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun goToSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
        isGoToSetting = true
    }


    companion object{
        var isGoToSetting = false
        var isAdsRewardShowing =  false
        fun <F : Fragment> newInstance(fragment: Class<F>, args: Bundle? = null): F {
            val f = fragment.newInstance()
            args?.let {
                f.arguments = it
            }
            return f
        }

    }
}
