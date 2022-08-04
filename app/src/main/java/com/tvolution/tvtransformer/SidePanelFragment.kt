package com.tvolution.tvtransformer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        Glide.with(requireContext()).asGif().addListener(object :RequestListener<GifDrawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                (requireActivity() as PlayerActivity).setTopPanelState(PlayerActivity.TopPanelState.Hidden)
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource!!.start()
                resource.setLoopCount(GifDrawable.LOOP_FOREVER)

                return false
            }

        }).load(requireArguments().getString(MOMENT_URL)).into(binding.sidePanelImage)

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