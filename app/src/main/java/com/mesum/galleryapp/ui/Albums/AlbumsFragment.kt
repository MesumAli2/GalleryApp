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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mesum.galleryapp.common.Constant
import com.mesum.galleryapp.common.Constant.readExternal
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
    private val albumAdapter = AlbumAdapter{ navigateToMediaFragment(it) }

    private val videoImagesPermission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissionMap->
        if (permissionMap.all { it.value }){
            Toast.makeText(activity, "Media permissions granted", Toast.LENGTH_SHORT).show()
            viewModel.loadAlbums()
        }else{
            Toast.makeText(activity, "Media permissions not granted!", Toast.LENGTH_SHORT).show()
        }
    }
    //register a permissions activity launcher for a single permission
    private val readExternalPermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
        if (isGranted){
            Toast.makeText(activity, "Read external storage permission granted", Toast.LENGTH_SHORT).show()
            viewModel.loadAlbums()

        }else{
            Toast.makeText(activity, "Read external storage permission denied!", Toast.LENGTH_SHORT).show()
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


    }

    private fun setupRecyclerView() {
        binding.albumsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = albumAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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
            //filter permissions array in order to get permissions that have not been granted
            val notGrantedPermissions=Constant.permissions.filterNot { permission->
                ContextCompat.checkSelfPermission(requireActivity(),permission) == PackageManager.PERMISSION_GRANTED
            }
            if (notGrantedPermissions.isNotEmpty()){
                //checks if permission was previously denied and return a boolean value
                val showRationale=notGrantedPermissions.any { permission->
                    shouldShowRequestPermissionRationale(permission)
                }
                if (showRationale){
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Storage Permission")
                        .setMessage("Storage permission is needed in order to show images and videos")
                        .setNegativeButton("Cancel"){dialog,_->
                            Toast.makeText(activity, "Read media storage permission denied!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .setPositiveButton("OK"){_,_->
                            videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
                        }
                        .show()
                }else{
                    videoImagesPermission.launch(notGrantedPermissions.toTypedArray())
                }
            }else{
                Toast.makeText(activity, "Read media storage permission granted", Toast.LENGTH_SHORT).show()
            }
        }else{
            if (ContextCompat.checkSelfPermission(requireActivity(),Constant.readExternal) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "Read external storage permission granted", Toast.LENGTH_SHORT).show()
            }else{
                if (shouldShowRequestPermissionRationale(Constant.readExternal)){
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Storage Permission")
                        .setMessage("Storage permission is needed in order to show images and video")
                        .setNegativeButton("Cancel"){dialog,_->
                            Toast.makeText(activity, "Read external storage permission denied!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .setPositiveButton("OK"){_,_->
                            readExternalPermission.launch(readExternal)
                        }
                        .show()
                }else{
                    readExternalPermission.launch(readExternal)
                }
            }
        }
    }

    private fun navigateToMediaFragment(albumId: String) {
        // Navigation logic to MediaFragment
        val action = AlbumsFragmentDirections.actionAlbumsFragmentToMediaFragment(albumId)
        findNavController().navigate(action)
    }
}
