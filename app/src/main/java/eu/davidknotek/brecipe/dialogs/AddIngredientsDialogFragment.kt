package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import eu.davidknotek.brecipe.databinding.FragmentAddIngredientsDialogBinding
import eu.davidknotek.brecipe.util.setDialogDimension
import eu.davidknotek.brecipe.viewmodels.SharedViewModel


class AddIngredientsDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddIngredientsDialogBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
        binding.ingredientsEditText.setText(sharedViewModel.recipeIngredients.value)
        setDialogDimension(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnClickListener {
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            sharedViewModel.recipeIngredients.value = binding.ingredientsEditText.text.toString().trim()
            dismiss()
        }
    }
}