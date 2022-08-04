package com.tvolution.tvtransformer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tvolution.tvtransformer.databinding.FragmentNudgeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NudgeFragment : Fragment() {

    companion object{

        const val NO_TIMER = 2
        const val TITLE="title"
        const val ICON="icon"
        const val DURATION="duration"
        fun getInstance(title:String, iconId: Int, duration:Int): NudgeFragment{
            val fragment = NudgeFragment()
            Bundle().apply {
                putString(TITLE, title)
                putInt(ICON, iconId)
                putInt(DURATION, duration)
                fragment.arguments = this
            }
            return fragment
        }
    }


    lateinit var binding: FragmentNudgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNudgeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val time = arguments!!.getInt(DURATION)
        if(time== NO_TIMER)
            binding.pollsViewTimerFl.visibility = View.INVISIBLE
        else
            binding.pollsViewTimerFl.visibility = View.VISIBLE
        binding.include.panelItemIcon.setColorFilter(Color.parseColor("#FF7EDB"), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.nudgeTitle.text = arguments!!.getString(TITLE)
        binding.include.panelItemIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, arguments!!.getInt(ICON), null))
        binding.include.root.isSelected = true
        lifecycleScope.launch {
            var timeElapsed = 0
            binding.pollsViewTimerPb.max = time
            while(time>timeElapsed){
                binding.pollsViewTimeTv.text = (time - timeElapsed).toString()
                binding.pollsViewTimerPb.progress=time - timeElapsed
                timeElapsed+=1
                delay(1000)
            }
            (requireActivity() as PlayerActivity).hideNudge()
        }
    }
}