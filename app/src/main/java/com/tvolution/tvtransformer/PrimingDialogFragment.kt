package com.tvolution.tvtransformer

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.tvolution.tvtransformer.databinding.DialogPrimingFragmentBinding

class PrimingDialogFragment : DialogFragment() {

    private lateinit var binding: DialogPrimingFragmentBinding
    private lateinit var viewModel: PlayerViewModel

    private val focusChangeListner by lazy {
        View.OnFocusChangeListener { v, isFocused ->
            when (v.id) {
                R.id.tv_leave_for_now, R.id.tv_stay_continue -> (v as Button).setTypeface(if (isFocused) Typeface.DEFAULT_BOLD else Typeface.DEFAULT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPrimingFragmentBinding.inflate(inflater, container, false)
        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            it.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PlayerViewModel::class.java)
        binding.tvLeaveForNow.setOnClickListener { requireActivity().finish() }
        binding.tvStayContinue.setOnClickListener { dismissAllowingStateLoss() }
//        binding.tvLeaveForNow.setOnFocusChangeListener(focusChangeListner)
//        binding.tvStayContinue.setOnFocusChangeListener(focusChangeListner)
//        binding.tvLeaveForNow.requestFocus()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnKeyListener { dialogInterface, keyCode, keyEvent ->
            // getAction to make sure this doesn't double fire
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                true
            } else false
        }
        return dialog
    }

    companion object {
        const val TAG = "PrimingDialogFragment"

        fun newInstance(): PrimingDialogFragment {
            val frag = PrimingDialogFragment()
            frag.isCancelable = false
            val args = Bundle()
            frag.setArguments(args)
            return frag
        }

    }

}