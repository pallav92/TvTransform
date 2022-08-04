package com.tvolution.tvtransformer

import android.graphics.Color
import android.graphics.Color.WHITE
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
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
        val panelItems = listOf(
            PanelItem("Capture Moment", getDrawableById(R.drawable.ic_camera),{
                (requireActivity() as PlayerActivity).captureMoment()
                (requireActivity() as PlayerActivity).setTopPanelState(PlayerActivity.TopPanelState.Hidden)
            }),
            PanelItem("Shop This", getDrawableById(R.drawable.ic_shop),{}),
            PanelItem("Did you know?", getDrawableById(R.drawable.ic_lightbulb),{}),
            PanelItem("Watch Party", getDrawableById(R.drawable.ic_party),{}),
            PanelItem("Link Phone", getDrawableById(R.drawable.ic_link_phone),{
                (requireActivity() as PlayerActivity).setSidePanelState(PlayerActivity.SidePanelState.ShowLink)
                (requireActivity() as PlayerActivity).setTopPanelState(PlayerActivity.TopPanelState.Hidden)
            }),
        )
        populatePanel(panelItems)
        binding.panelItems[0].requestFocus()
    }


    fun getDp(dip: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    )

    var textViewReference: TextView? = null
    fun populatePanel(panelItems: List<PanelItem>){

        panelItems.forEach{
            val item = PanelItemBinding.inflate(layoutInflater)
            val rootLayoutParams = FrameLayout.LayoutParams(getDp(35f).toInt(),getDp(35f).toInt())
            rootLayoutParams.marginStart = getDp(6f).toInt()
            rootLayoutParams.marginEnd = getDp(6f).toInt()
            rootLayoutParams.topMargin = getDp(8f).toInt()
            rootLayoutParams.bottomMargin = getDp(8f).toInt()

            item.panelItemIcon.setImageDrawable(it.iconDrawable)
            item.root.setOnClickListener { v->it.onClick(v) }
            item.root.setOnFocusChangeListener{ v,hasFocus->
                if(hasFocus) {
                    item.root.isSelected = true
                    item.panelItemIcon.setColorFilter(Color.parseColor("#FF7EDB"), android.graphics.PorterDuff.Mode.SRC_IN);
                    val layoutParams =
                        FrameLayout.LayoutParams(getDp(42f).toInt(), WRAP_CONTENT, Gravity.BOTTOM)
                    var pos = 0
                    var flag = false
                    binding.panelItems.forEach { childView->
                        if(childView==v)
                            flag=true
                        if(!flag)
                            pos+=1
                    }
                    val startMargin  = (item.root.width + getDp(12f).toInt())*pos + getDp(6f)
                    layoutParams.marginStart = startMargin.toInt()// position*width + padding -> x*35dp + 12dp
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
                    textView.apply {
                        text = it.name
                        this.setTextColor(Color.WHITE)
                        textSize = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            5f,
                            resources.displayMetrics
                        )
                        maxLines = 2
                        gravity = Gravity.CENTER
                    }
                    binding.root.addView(textView, layoutParams)
                    textViewReference = textView
                }else{
                    item.root.isSelected = false
                    item.panelItemIcon.setColorFilter(WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
                    textViewReference?.let { view->
                        binding.root.removeView(view)
                    }
                }
            }
            binding.panelItems.addView(item.root, rootLayoutParams)
        }
    }



    fun getDrawableById(id:Int): Drawable? {
        return ResourcesCompat.getDrawable(requireContext().resources, id, null)
    }
    data class PanelItem(val name:String, val iconDrawable: Drawable?, val onClick:(v: View)->Unit )

}