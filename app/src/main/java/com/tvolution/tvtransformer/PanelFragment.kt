package com.tvolution.tvtransformer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.tvolution.tvtransformer.databinding.FragmentPanelBinding
import com.tvolution.tvtransformer.databinding.PanelItemBinding


class PanelFragment : Fragment() {

    companion object{
        fun getInstance():PanelFragment{
            return PanelFragment()
        }
    }

    private val FORTY_DP: Int = 34 //TODO: make 40dp
    lateinit var binding: FragmentPanelBinding

    val panelItems = listOf(
        PanelItem("Capture Moments", getDrawableById(R.drawable.ic_camera),{}),
        PanelItem("Shop", getDrawableById(R.drawable.ic_shop),{}),
        PanelItem("Kya hai ye?", getDrawableById(R.drawable.ic_lightbulb),{}),
        PanelItem("Watch Party", getDrawableById(R.drawable.ic_party),{}),
        PanelItem("Link Phone", getDrawableById(R.drawable.ic_link_phone),{}),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPanelBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.panelItems.removeAllViews()
        populatePanel(panelItems)
        (binding.root as LinearLayout).get(0).requestFocus()
    }

    var textViewReference: TextView? = null
    fun populatePanel(panelItems: List<PanelItem>){
        var pos = 0
        panelItems.forEach{
            val item = PanelItemBinding.inflate(layoutInflater)
            item.panelItemIcon.setImageDrawable(it.iconDrawable)
            item.root.setOnClickListener { v->it.onClick(v) }
            item.root.setOnFocusChangeListener{ v,hasFocus->
                if(hasFocus) {
                    item.root.isSelected = true
                    val layoutParams =
                        FrameLayout.LayoutParams(FORTY_DP, WRAP_CONTENT, Gravity.BOTTOM)
                    val startMargin  = item.root.width*pos + getDp(12)
                    layoutParams.marginStart = startMargin// position*width + padding -> x*35dp + 12dp
                    val textView = TextView(context)
                    /* <TextView
                        android:layout_width="40dp"
                        android:layout_height="wrap_content"
                        android:layout_marginStart="152dp"
                        android:layout_gravity="bottom"
                        android:gravity="center"
                        android:textColor="#FFFFFF"
                        android:textSize="9sp"
                        android:text="Capture Moments"/>
                        */
                    textView.text = it.name
                    binding.panelItems.addView(textView, layoutParams)
                    textViewReference = textView
                }else{
                    item.root.isSelected = false
                    textViewReference?.let { view->
                        binding.panelItems.removeView(view)
                    }
                }
            }
            binding.panelItems.addView(item.root)
            pos+=1
        }
    }

    private fun getDp(i: Int): Byte {
        return 3
    }

    fun getDrawableById(id:Int): Drawable? {
        return ResourcesCompat.getDrawable(requireContext().resources, id, null)
    }
    data class PanelItem(val name:String, val iconDrawable: Drawable?, val onClick:(v: View)->Unit )

}