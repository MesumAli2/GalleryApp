package com.mesum.galleryapp.ui.Albums

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mesum.galleryapp.R
import com.mesum.galleryapp.common.Constant
import com.mesum.galleryapp.common.Constant.readExternal
import com.mesum.galleryapp.common.GridSpacingItemDecoration
import com.mesum.galleryapp.databinding.FragmentAlbumsBinding
import com.mesum.galleryapp.ui.Albums.adapter.AlbumAdapter
import com.mesum.galleryapp.ui.Albums.viewmodel.AlbumsViewModel


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumsViewModel by viewModels()
    private val albumAdapter = AlbumAdapter{ id, albumName ->
        navigateToMediaFragment(id, albumName) }

    private val videoImagesPermission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissionMap->
        if (permissionMap.all { it.value }){
            viewModel.loadAlbums()
        }
    }
    private val readExternalPermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
        if (isGranted){
            viewModel.loadAlbums()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()
        observeAlbums()
        setupToolbar()


    }

    private fun setupRecyclerView() {
        binding.albumsRecyclerView.apply {
            val spanCount = 2
            val spacing = 16
            val includeEdge = true
             layoutManager = GridLayoutManager(context, spanCount)
             addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
            adapter = albumAdapter
        }
    }

    private fun observeAlbums() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.albums.collect{ albums->
                    albumAdapter.submitList(albums)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermissions(){
        //checks the API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val notGrantedPermissions=Constant.permissions.filterNot { permission->
                ContextCompat.checkSelfPermission(requireActivity(),permission) == PackageManager.PERMISSION_GRANTED
            }
            if (notGrantedPermissions.isNotEmpty()){
                //checks if permission was previously denied and return a boolean value
                val showRationale=notGrantedPermissions.any { permission->
                    shouldShowRequestPermissionRationale(permission)
                }
                if (showRationale) AlertDialog.Builder(requireActivity())
                    .setTitle(getString(R.string.storage_permission))
                    .setMessage(getString(R.string.message))
                    .setNegativeButton(getString(R.string.cancel)){ dialog, _->
                        Toast.makeText(activity,
                            getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.ok)){ _, _->
                        videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
                    }
                    .show() else videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
            }
        }else{
            if (ContextCompat.checkSelfPermission(requireActivity(),Constant.readExternal) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Constant.readExternal)){
                    AlertDialog.Builder(requireActivity())
                        .setTitle(getString(R.string.storage_permission))
                        .setMessage(getString(R.string.message))
                        .setNegativeButton(getString(R.string.cancel)){ dialog, _->
                            Toast.makeText(activity,
                                getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .setPositiveButton(getString(R.string.ok)){_,_->
                            readExternalPermission.launch(readExternal)
                        }
                        .show()
                }else{
                    readExternalPermission.launch(readExternal)
                }
            }
        }
    }

    private fun navigateToMediaFragment(albumId: String, albumName: String) {
        val action = AlbumsFragmentDirections.actionAlbumsFragmentToMediaFragment(albumId, albumName)
        findNavController().navigate(action)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).supportActionBar?.apply { title = getString(R.string.title) }
    }
}
