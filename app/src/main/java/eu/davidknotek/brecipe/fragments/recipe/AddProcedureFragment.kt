package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.databinding.FragmentAddProcedureBinding

class AddProcedureFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddProcedureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProcedureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}