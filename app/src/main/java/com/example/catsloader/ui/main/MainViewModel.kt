package com.example.catsloader.ui.main

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.catsloader.model.Cat

class MainViewModel : ViewModel() {
    
    val getResponse: ArrayList<Cat> get() = savedList

    fun getCatList(list: ArrayList<Cat>?) {
        if (list != null) {
            savedList.addAll(list)
        }
    }

    fun clearList() {
        getResponse.clear()
    }

    val getCurrentFragment: Fragment? get() = currentFragment

    fun saveCurrentFragment(fragment: Fragment) {
        currentFragment = fragment
    }

    val getFragmentManager: FragmentManager? get() = fManager

    fun saveFragmentManager(fragmentM: FragmentManager) {
        fManager = fragmentM
    }

    val getCatParcelable: Parcelable? get() = catParcelable

    fun saveCatParcelable(parcelableCat: Parcelable?) {
        catParcelable = parcelableCat
    }

    companion object{
        private var savedList = arrayListOf<Cat>()
        private var currentFragment: Fragment? = null
        private var fManager: FragmentManager? = null
        private var catParcelable: Parcelable? = null
    }
}