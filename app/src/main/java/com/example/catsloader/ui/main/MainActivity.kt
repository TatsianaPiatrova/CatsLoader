package com.example.catsloader.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.catsloader.OnSendDataToActivity
import com.example.catsloader.R
import com.example.catsloader.util.Constants.Companion.CONTENT_TAG
import com.example.catsloader.util.Constants.Companion.FULL_PIC_TAG

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _contentFragment: CatListFragment? = null
    val contentFragment get() = _contentFragment

    private var _catFragment: CatFragment? = null
    val catFragment get() = _catFragment

    var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val restoreFullPicFragment = supportFragmentManager.findFragmentByTag(FULL_PIC_TAG)
        val restoreContentFragment = supportFragmentManager.findFragmentByTag(CONTENT_TAG)
        if (restoreFullPicFragment != null) {
            restoreContentFragment?.let { attachFragment(it, CONTENT_TAG) }
            attachFragment(restoreFullPicFragment, FULL_PIC_TAG)
            viewModel.saveCurrentFragment(restoreFullPicFragment)
        } else {
            contentFragment?.let { viewModel.saveCurrentFragment(it) }
            showContentFragment()
        }
        openFullScreenPic()
    }

    private fun showContentFragment() {
        _contentFragment = CatListFragment.newInstance()
        contentFragment?.let { attachFragment(it, CONTENT_TAG) }
    }

    private fun openFullScreenPic() {
        contentFragment?.sendDataToActivity(object : OnSendDataToActivity {
            override fun sendUrlData(url: String?) {
                _catFragment = url?.let { CatFragment.newInstance(it) }
                catFragment?.let { attachFragmentWithAnimation(it) }
            }
        })
    }

    private fun attachFragment(fragment: Fragment, tag: String) {
        currentFragment = fragment
        currentFragment?.let { viewModel.saveCurrentFragment(it) }
        viewModel.saveFragmentManager(supportFragmentManager)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commit()
    }

    private fun attachFragmentWithAnimation(fragment: Fragment) {
        currentFragment = fragment
        currentFragment?.let { viewModel.saveCurrentFragment(it) }
        if (supportFragmentManager.isDestroyed) {
            val f = viewModel.getFragmentManager
            f?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.card_flip_enter,
                    R.anim.card_flip_exit
                )
                ?.replace(R.id.container, fragment, FULL_PIC_TAG)
                ?.addToBackStack(null)
                ?.commit()
        } else {
            viewModel.saveFragmentManager(supportFragmentManager)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.card_flip_enter,
                    R.anim.card_flip_exit
                )
                .replace(R.id.container, fragment, FULL_PIC_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        val curFragment = viewModel.getCurrentFragment
        if (curFragment != null || contentFragment != null) {
            if (curFragment == contentFragment) {
                viewModel.saveCatParcelable(null)
                viewModel.clearList()
                finishAndRemoveTask()
            }
            else super.onBackPressed()
        } else super.onBackPressed()
    }

}
