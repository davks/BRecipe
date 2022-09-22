package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import eu.davidknotek.brecipe.databinding.FragmentAddNoteDialogBinding
import eu.davidknotek.brecipe.util.setDialogDimension
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class AddNoteDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddNoteDialogBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteDialogBinding.inflate(layoutInflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnClickListener {
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            sharedViewModel.recipeNote.value = binding.noteEditText.text.toString()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.noteEditText.setText(sharedViewModel.recipeNote.value)
        setDialogDimension(dialog)
    }
}