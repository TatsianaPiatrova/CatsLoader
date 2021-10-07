package com.example.catsloader.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.catsloader.R
import com.example.catsloader.databinding.RecyclerviewItemBinding
import com.example.catsloader.model.Cat

class RecyclerViewAdapter(private val list: ArrayList<Cat>, private val context: Context):
                            RecyclerView.Adapter<RecyclerViewAdapter.ListViewHolder>() {
    private lateinit var binding: RecyclerviewItemBinding
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding =
            RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val url = list[position].url
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop())
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility = View.GONE
                    binding.img.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progress.visibility = View.GONE
                    binding.img.visibility = View.VISIBLE
                    return false
                }
            })
            .apply(requestOptions)
            .error(R.drawable.ic_launcher_background)
            .into(holder.img)

        if (listener != null) {
            holder.itemView.setOnClickListener {
                listener!!.onCatClick(url)
            }
        }
    }

    fun addData(list: ArrayList<Cat>) {
        val size = this.list.size
        this.list.addAll(list)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    inner class ListViewHolder(itemView: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val img: ImageView = itemView.img
    }

    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}

interface OnItemClickListener {
    fun onCatClick(url: String?)
}
