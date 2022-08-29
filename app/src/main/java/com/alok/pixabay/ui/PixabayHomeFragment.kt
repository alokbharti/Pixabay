package com.alok.pixabay.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alok.pixabay.R
import com.alok.pixabay.contract.ItemClickCallback
import com.alok.pixabay.databinding.FragmentPixabayHomeBinding
import com.alok.pixabay.model.PixabayImageDetails
import com.alok.pixabay.ui.PixabayImageDetailsFragment.Companion.PIXABAY_IMAGE_DETAILS
import com.alok.pixabay.utillity.Constants.DEFAULT_QUERY
import com.alok.pixabay.utillity.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PixabayHomeFragment: Fragment(), ItemClickCallback<PixabayImageDetails> {

    private var _binding: FragmentPixabayHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PixabayViewModel by activityViewModels()
    private lateinit var adapter: PixabayImageAdapter
    private var page: Int = 1
    private var currentQuery = DEFAULT_QUERY //default

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPixabayHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PixabayImageAdapter(this)
        binding.rvImages.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvImages.layoutManager = linearLayoutManager

        viewModel.apiLoaderLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbLoader.visibility = View.VISIBLE
            } else {
                binding.pbLoader.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()){
                if (adapter.itemCount < 1){
                    binding.llError.visibility = View.VISIBLE //show error if it's a first api call
                }
            } else {
                binding.llError.visibility = View.GONE
            }
        }

        viewModel.pixabayImageDataData.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.tvQueryText.text = getString(
                    R.string.result_query_text,
                    viewModel.getLastSearchedQuery()
                )
                adapter.setPixabayImageList(it)
                if (currentQuery != viewModel.getLastSearchedQuery()) {
                    Toast.makeText(context, R.string.internet_error, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.getPixabayImageData(currentQuery, page)

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enteredText = s.toString()
                if (enteredText.isNotEmpty()) {
                    page = 1 //reset page
                    currentQuery = enteredText
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                adapter.removeAllData()
                viewModel.getPixabayImageData(currentQuery, page)
                context?.let {
                    Util.hideKeyboard(it, binding.root)
                }
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }

        //handling PAGING, this will make the api call when user has only 4 items left to scroll
        //here 4 is chosen to seamlessly show data to users
        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (totalItemCount <= lastVisibleItemPosition + 4) {
                    page++
                    viewModel.getPixabayImageData(currentQuery, page)
                }
            }
        })

        binding.btnRetry.setOnClickListener {
            viewModel.getPixabayImageData(currentQuery, page)
        }

    }

    override fun onItemClicked(value: PixabayImageDetails, position: Int) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(R.string.see_more_detail)
                setPositiveButton(R.string.ok) { dialog, id ->
                    val bundle = Bundle().apply {
                        putSerializable(PIXABAY_IMAGE_DETAILS, value)
                    }
                    findNavController().navigate(
                        R.id.action_pixabay_home_to_pixabay_image_details,
                        bundle
                    )
                }
                setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}