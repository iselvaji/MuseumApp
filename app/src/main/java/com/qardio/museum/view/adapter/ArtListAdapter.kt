package com.qardio.museum.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.qardio.museum.R
import com.qardio.museum.databinding.ListItemArtBinding
import com.qardio.museum.model.ArtObjects
import javax.inject.Inject

/**
 * Paging Adpater with for the list of arts to display by [ArtViewHolder]
 */
class ArtListAdapter @Inject constructor() : PagingDataAdapter<ArtObjects, ArtListAdapter.ArtViewHolder>(ArtDiffCallBack) {

    var onItemClick: ((item: ArtObjects, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemArtBinding.inflate(inflater, parent, false)
        return ArtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    object ArtDiffCallBack : DiffUtil.ItemCallback<ArtObjects>() {
        override fun areItemsTheSame(oldItem: ArtObjects, newItem: ArtObjects): Boolean {
            return oldItem.artId == newItem.artId
        }

        override fun areContentsTheSame(oldItem: ArtObjects, newItem: ArtObjects): Boolean {
            return oldItem == newItem
        }
    }

    inner class ArtViewHolder(private var binding: ListItemArtBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(data: ArtObjects) {
            binding.apply {
                imageArt.load(data.webImage?.url) {
                    crossfade(true)
                    placeholder(R.drawable.ic_loading)
                    error(R.drawable.ic_error)
                }
            }
        }

        override fun onClick(view: View?) {
            if (view != null) {
                getItem(absoluteAdapterPosition)?.let { onItemClick?.invoke(it, view) }
            }
        }
    }
}