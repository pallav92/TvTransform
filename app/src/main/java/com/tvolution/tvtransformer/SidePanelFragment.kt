package com.tvolution.tvtransformer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tvolution.tvtransformer.databinding.FragmentSidePanelBinding

class SidePanelFragment : Fragment() {

    lateinit var binding: FragmentSidePanelBinding

    companion object{
        val MOMENT_URL = "MOMENT_URL"
        fun getInstance(imageUrl: String):SidePanelFragment{
            val sidePanelFragment = SidePanelFragment()
            Bundle().apply {
                putString(MOMENT_URL, imageUrl)
                sidePanelFragment.arguments = this
            }
            return sidePanelFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSidePanelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(requireContext()).load(requireArguments().getString(MOMENT_URL)).into(binding.sidePanelImage)
    }




}