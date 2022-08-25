package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import eu.davidknotek.brecipe.databinding.FragmentAddIngredientsDialogBinding


class AddIngredientsDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddIngredientsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddIngredientsDialogBinding.inflate(layoutInflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setDialogDimension()
    }

    private fun setDialogDimension() {
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnClickListener {
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            dismiss()
        }
    }
}