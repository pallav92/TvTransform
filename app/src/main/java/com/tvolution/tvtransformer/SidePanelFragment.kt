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

    enum class Type{
        Moment, Link
    }
    companion object{
        val MOMENT_URL = "MOMENT_URL"
        val TYPE="type"
        fun getInstance(imageUrl: String, type: Type):SidePanelFragment{
            val sidePanelFragment = SidePanelFragment()
            Bundle().apply {
                putString(MOMENT_URL, imageUrl)
                putString(TYPE, type.name)
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
        val type = Type.valueOf(requireArguments().getString(TYPE).toString())
        when(type){
            Type.Moment -> {
                binding.sidePanelMoment.visibility = View.VISIBLE
                binding.sidePanelLink.visibility = View.GONE
            }
            Type.Link -> {
                binding.sidePanelMoment.visibility = View.GONE
                binding.sidePanelLink.visibility = View.VISIBLE
            }
        }
    }




}