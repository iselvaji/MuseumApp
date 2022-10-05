package com.qardio.museum.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.qardio.museum.R
import com.qardio.museum.databinding.FragmentArtDetailsBinding
import com.qardio.museum.model.ArtObjects
import dagger.hilt.android.AndroidEntryPoint

/**
 * Art details fragment of the application, which display the art details like art name, image, creator
 */

@AndroidEntryPoint
class ArtDetailsFragment : Fragment() {

    private var binding: FragmentArtDetailsBinding? = null
    private val arguments: ArtDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        displayDetails(arguments.art)
    }

    // display the art details like name, image, creator
    private fun displayDetails(art : ArtObjects) {
        art.let { art ->
            binding?.apply {

                imgViewArt.load(art.webImage?.url) {
                    crossfade(true)
                    placeholder(R.drawable.ic_loading)
                    error(R.drawable.ic_error)
                }

                txtTitle.text = art.title
                txtLongTitle.text = art.longTitle
                txtCreator.text = art.principalOrFirstMaker
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}