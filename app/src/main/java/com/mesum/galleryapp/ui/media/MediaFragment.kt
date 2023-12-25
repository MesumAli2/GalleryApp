package com.mesum.galleryapp.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar.NavigationMode
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.mesum.galleryapp.R
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


        Toast.makeText(activity, selectedAlbumName, Toast.LENGTH_SHORT).show()
        // Set up the toolbar
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
            val spanCount = 2 // Number of columns in the grid
            val spacing = 16 // Spacing in pixels
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp() // Handle the back action
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
