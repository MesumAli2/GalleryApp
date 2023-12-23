package com.mesum.galleryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesum.galleryapp.R
import com.mesum.galleryapp.databinding.FragmentMediaBinding
import com.mesum.galleryapp.ui.adapter.MediaAdapter
import com.mesum.galleryapp.ui.viewmodel.MediaViewModel


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
    }

    private fun setupRecyclerView() {
        mediaAdapter = MediaAdapter(emptyList()) // Initialize with empty list
        binding.mediaRecyclerView.adapter = mediaAdapter
        binding.mediaRecyclerView.layoutManager = LinearLayoutManager(context)
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
