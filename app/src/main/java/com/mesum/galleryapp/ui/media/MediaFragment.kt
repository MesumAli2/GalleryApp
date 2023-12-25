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
        val spanCount = 2 // Adjust this value based on your needs and screen size
        val layoutManager = GridLayoutManager(context, spanCount)
        binding.mediaRecyclerView.layoutManager = layoutManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.mediaRecyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacingInPixels, true))
        mediaAdapter = MediaAdapter() // Initialize with empty list
        binding.mediaRecyclerView.adapter = mediaAdapter

        // Ensure the RecyclerView's layout behavior is set for scrolling
        val layoutParams = binding.mediaRecyclerView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
    }


    private fun loadMedia() {
        // Load media items for the selected album and update adapter
        viewModel.getMedia(selectedAlbumId)
    }
    private fun setupToolbar(selectedAlbumName: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = selectedAlbumName // Set action bar title
            setDisplayHomeAsUpEnabled(true) // Enable the Up button
            setDisplayShowHomeEnabled(true) // Show the Up button

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
