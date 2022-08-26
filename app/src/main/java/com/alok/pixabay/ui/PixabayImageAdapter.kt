package com.alok.pixabay.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alok.pixabay.R
import com.alok.pixabay.contract.ItemClickCallback
import com.alok.pixabay.databinding.ItemPixabayImageBinding
import com.alok.pixabay.model.PixabayImageDetails
import com.bumptech.glide.Glide

class PixabayImageAdapter(
    private val itemClickCallback: ItemClickCallback<PixabayImageDetails>
): RecyclerView.Adapter<PixabayImageAdapter.ViewHolder>() {

    private var pixabayImageList: MutableList<PixabayImageDetails> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPixabayImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (pixabayImageList.isNotEmpty()) {
            val pixabayImageDetails = pixabayImageList[position]
            holder.bind(pixabayImageDetails, position)
        }
    }

    override fun getItemCount() = pixabayImageList.size

    fun setPixabayImageList(pixabayImageData: List<PixabayImageDetails>) {
        val currentLastPosition = if (pixabayImageList.isEmpty()) {
            0
        } else {
            pixabayImageList.size - 1
        }

        pixabayImageList.addAll(pixabayImageData)
        notifyItemRangeInserted(currentLastPosition, pixabayImageData.size)
    }

    fun removeAllData() {
        val listSize = pixabayImageList.size
        pixabayImageList.clear()
        notifyItemRangeRemoved(0, listSize)
    }

    inner class ViewHolder(
        private val binding: ItemPixabayImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(pixabayImageDetails: PixabayImageDetails, position: Int) {
            binding.tvPixabayUserName.text = pixabayImageDetails.user
            binding.tvPixabayTags.text = pixabayImageDetails.tags
            Glide
                .with(binding.ivPixabayImage.context)
                .load(pixabayImageDetails.previewURL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(binding.ivPixabayImage)

            binding.clItemPixabayImage.setOnClickListener{
                itemClickCallback.onItemClicked(pixabayImageDetails, position)
            }
        }
    }
}