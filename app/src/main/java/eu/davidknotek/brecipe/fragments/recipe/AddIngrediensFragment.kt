package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.databinding.FragmentAddIngrediensBinding

class AddIngrediensFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddIngrediensBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddIngrediensBinding.inflate(layoutInflater, container,false)
        return binding.root
    }
}