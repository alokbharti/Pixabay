package com.alok.pixabay.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alok.pixabay.R
import com.alok.pixabay.databinding.FragmentPixabayImageDetailsBinding
import com.alok.pixabay.model.PixabayImageDetails
import com.bumptech.glide.Glide

class PixabayImageDetailsFragment: Fragment() {

    companion object{
        const val PIXABAY_IMAGE_DETAILS = "pixabayImageDetails"
    }

    private var _binding: FragmentPixabayImageDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPixabayImageDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pixabayImageDetails = arguments?.getSerializable(
            PIXABAY_IMAGE_DETAILS
        ) as PixabayImageDetails?

        pixabayImageDetails?.let {
            binding.tvPixabayTags.text = it.tags
            binding.tvPixabayUserName.text = it.user
            binding.tvNumberOfLikes.text = it.likes.toString()
            binding.tvNumberOfComments.text = it.comments.toString()
            binding.tvNumberOfDownloads.text = it.downloads.toString()

            context?.let { ctx ->
                Glide
                    .with(ctx)
                    .load(it.largeImageURL)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(binding.ivPixabayLargeImage)
            }

        } ?: kotlin.run {
            //null case, go back - invalid
            Toast.makeText(
                context,
                getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}