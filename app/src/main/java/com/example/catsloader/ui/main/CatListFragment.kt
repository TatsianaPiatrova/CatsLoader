package com.example.catsloader.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.catsloader.*
import com.example.catsloader.databinding.CatListFragmentBinding
import com.example.catsloader.model.Cat
import com.example.catsloader.util.Constants.Companion.API_KEY
import com.example.catsloader.util.Constants.Companion.COLUMN_COUNT
import com.example.catsloader.util.Constants.Companion.PAGES_COUNT
import com.example.catsloader.util.Constants.Companion.SPINNER_DURATION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatListFragment : Fragment() {
    private var _binding: CatListFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModels()

    private var pages = 1
    private val isLastPage = false
    private var isLoading = false
    private var mLayoutManager: GridLayoutManager? = null
    private var list: ArrayList<Cat>? = arrayListOf()
    private var catsAdapter: RecyclerViewAdapter? = null
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View?
        if (savedInstanceState != null) {
            val savedRecyclerLayoutState: Parcelable? = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
            _binding?.recyclerView?.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutState)
            view = binding?.root
        } else {
            _binding = CatListFragmentBinding.inflate(inflater, container, false)
            view = binding?.root
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.layoutManager = createLayoutManager()
        initOptionsMenu()
        setSwipeListener()
        loadMore()
        pagination()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).currentFragment = (activity as MainActivity).contentFragment
        (activity as MainActivity).contentFragment?.let { viewModel.saveCurrentFragment(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.recyclerView?.layoutManager?.onSaveInstanceState()?.let {
            viewModel.saveCatParcelable(
                it
            )
        }
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, binding?.recyclerView?.layoutManager?.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedRecyclerLayoutManager: Parcelable? = savedInstanceState.getParcelable(
                BUNDLE_RECYCLER_LAYOUT)
            binding?.recyclerView?.layoutManager?.onRestoreInstanceState(savedRecyclerLayoutManager)
        } else {
            val getParcelable = viewModel.getCatParcelable
            if (getParcelable != null) {
                binding?.recyclerView?.layoutManager?.onRestoreInstanceState(getParcelable)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initOptionsMenu() {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setSwipeListener() {
        handler = Handler()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            runnable = Runnable {
                loadMore()
                binding?.swipeRefreshLayout?.isRefreshing = false
            }

            handler.postDelayed(
                runnable, SPINNER_DURATION.toLong()
            )
        }
    }

    private fun createLayoutManager(): GridLayoutManager {
        val grid = GridLayoutManager(context, COLUMN_COUNT)
        mLayoutManager = grid
        return grid
    }

    private fun loadMore() {
        val savedList = viewModel.getResponse
        if (savedList.isNullOrEmpty()) {
            val request = RetrofitInstance.buildService(Api::class.java)
            val call = request.getListOfCats(API_KEY, pages, PAGES_COUNT)
            call.enqueue(object : Callback<ArrayList<Cat>> {
                override fun onResponse(
                    call: Call<ArrayList<Cat>>,
                    response: Response<ArrayList<Cat>>
                ) {
                    if (response.isSuccessful) {
                        list = response.body()
                        viewModel.getCatList(list)
                        list?.let { getList(it)
                            setItemClickListener() }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Cat>>, t: Throwable) {
                    Toast.makeText(context, "Make a swipe", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            getList(savedList)
            setItemClickListener()
        }
    }

    private fun loadMore2() {
        val request = RetrofitInstance.buildService(Api::class.java)
        val call = request.getListOfCats(API_KEY, pages++, PAGES_COUNT)
        call.enqueue(object : Callback<ArrayList<Cat>> {
            override fun onResponse(
                call: Call<ArrayList<Cat>>,
                response: Response<ArrayList<Cat>>
            ) {
                if (response.isSuccessful) {
                    isLoading = false
                    list = response.body()
                    viewModel.getCatList(list)
                    list?.let { (binding?.recyclerView?.adapter as RecyclerViewAdapter).addData(it) }
                }
            }

            override fun onFailure(call: Call<ArrayList<Cat>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getList(list: ArrayList<Cat>) {
        catsAdapter = RecyclerViewAdapter(list, requireContext())
        binding?.recyclerView?.adapter = catsAdapter
    }

    private fun pagination() {
        binding?.recyclerView?.addOnScrollListener(object : ScrollPagingListener(mLayoutManager!!) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                Toast.makeText(context, "Paging start", Toast.LENGTH_SHORT).show()
                loadMore2()
            }
        })
    }

    private fun setItemClickListener() {
        catsAdapter?.setListener(object : OnItemClickListener {
            override fun onCatClick(url: String?) {
                if (url != null) {
                    sendDataInterface?.sendUrlData(url)
                }
            }
        })
    }

    fun sendDataToActivity(inter: OnSendDataToActivity) {
        sendDataInterface = inter
    }

    companion object {
        fun newInstance(): CatListFragment {
            return CatListFragment()
        }
        private var sendDataInterface: OnSendDataToActivity? = null
        private const val BUNDLE_RECYCLER_LAYOUT = "recycler.layout"
    }
}