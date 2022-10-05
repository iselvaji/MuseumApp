package com.qardio.museum.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.qardio.museum.R
import com.qardio.museum.databinding.FragmentArtListBinding
import com.qardio.museum.model.ArtObjects
import com.qardio.museum.model.NetworkStatusState
import com.qardio.museum.view.adapter.ArtListAdapter
import com.qardio.museum.view.adapter.ArtLoadStateAdapter
import com.qardio.museum.viewmodel.ArtListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Art list fragment of the application, which load the list of arts
 */

@AndroidEntryPoint
class ArtListFragment : Fragment() {

    @Inject
    lateinit var listAdapter: ArtListAdapter

    private var binding: FragmentArtListBinding? = null
    private val viewModel by viewModels<ArtListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        fetchData()
    }

    private fun setupObservers() {
        viewModel.networkState.asLiveData().observe(this) { state ->
            when(state) {
                NetworkStatusState.NetworkStatusConnected -> {
                    fetchData()
                }
                else -> {
                    context?.let {
                        showNetworkErrorUI()
                    }
                }
            }
        }
    }

    private fun setupUI() {

        binding?.recylerViewArts?.adapter = listAdapter
        binding?.recylerViewArts?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter.withLoadStateHeaderAndFooter(
                header = ArtLoadStateAdapter { listAdapter.retry() },
                footer = ArtLoadStateAdapter { listAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        // call back to get the selected(clicked) item from the list
        listAdapter.onItemClick = { selectedItem, _ ->
            navigateToDetailsScreen(selectedItem)
        }
    }

    // fetch the data to be displayed
    private fun fetchData() {
        Log.d("isDeviceOnline ", viewModel.isDeviceOnline().toString())

        lifecycleScope.launch {
            viewModel.fetchArts().collectLatest {
                listAdapter.submitData(it)
            }
        }
    }

    // navigate to art details screen
    private fun navigateToDetailsScreen(selectedItem : ArtObjects) {
        val directions = ArtListFragmentDirections.actionListToDetailsView(selectedItem)
        findNavController().navigate(directions)
    }

    // display the network unvailable message in the UI
    private fun showNetworkErrorUI() {
        binding?.root?.let {
            Snackbar.make(it, getString(R.string.err_network),Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}