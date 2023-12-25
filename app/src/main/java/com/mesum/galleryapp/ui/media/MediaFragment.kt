package com.mesum.galleryapp.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.mesum.galleryapp.databinding.FragmentMediaBinding
import com.mesum.galleryapp.common.GridSpacingItemDecoration
import com.mesum.galleryapp.ui.media.adapter.MediaAdapter
import com.mesum.galleryapp.ui.media.viewmodel.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var selectedAlbumId: String
    private lateinit var selectedAlbumName: String

    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            selectedAlbumId = MediaFragmentArgs.fromBundle(it).albumId ?: ""
            selectedAlbumName = MediaFragmentArgs.fromBundle(it).albumName ?: ""
        }
        setupToolbar(selectedAlbumName)
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
        binding.mediaRecyclerView.apply {
            val spanCount = 2
            val spacing = 16
            val includeEdge = true
            layoutManager = GridLayoutManager(context, spanCount)
             addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
            mediaAdapter = MediaAdapter()
            binding.mediaRecyclerView.adapter = mediaAdapter
        }

    }
    private fun loadMedia() {
        viewModel.getMedia(selectedAlbumId)
    }
    private fun setupToolbar(selectedAlbumName: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = selectedAlbumName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
