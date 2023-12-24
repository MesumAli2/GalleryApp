package com.mesum.galleryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesum.galleryapp.R
import com.mesum.galleryapp.databinding.FragmentMediaBinding
import com.mesum.galleryapp.ui.adapter.MediaAdapter
import com.mesum.galleryapp.ui.viewmodel.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var selectedAlbumId: String
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            selectedAlbumId = MediaFragmentArgs.fromBundle(it).albumId ?: ""
        }
        setupRecyclerView()
        loadMedia()
        observeMedia()
    }

    private fun observeMedia() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { photos ->
                    mediaAdapter.setItems(photos)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val spanCount = 2 // Adjust this value based on your needs and screen size
        val layoutManager = GridLayoutManager(context, spanCount)
        binding.mediaRecyclerView.layoutManager = layoutManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.mediaRecyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacingInPixels, true))
        mediaAdapter = MediaAdapter() // Initialize with empty list
        binding.mediaRecyclerView.adapter = mediaAdapter
    }

    private fun loadMedia() {
        // Load media items for the selected album and update adapter
        viewModel.getMedia(selectedAlbumId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
