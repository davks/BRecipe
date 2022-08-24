package eu.davidknotek.brecipe.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.databinding.FragmentEditCategoryBinding

class EditCategoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}