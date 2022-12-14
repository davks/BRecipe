package eu.davidknotek.brecipe.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import eu.davidknotek.brecipe.databinding.FragmentAddProcedureDialogBinding
import eu.davidknotek.brecipe.util.setDialogDimension
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class AddProcedureDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddProcedureDialogBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProcedureDialogBinding.inflate(layoutInflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.procedureEditText.setText(sharedViewModel.recipeProcedure.value)
        setDialogDimension(dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnClickListener {
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            sharedViewModel.recipeProcedure.value = binding.procedureEditText.text.toString().trim()
            dismiss()
        }
    }
}