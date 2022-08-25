package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import eu.davidknotek.brecipe.databinding.FragmentAddProcedureDialogBinding

class AddProcedureDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddProcedureDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProcedureDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}