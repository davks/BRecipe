package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import eu.davidknotek.brecipe.databinding.FragmentChooseCategoryDialogBinding

class ChooseCategoryDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentChooseCategoryDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseCategoryDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}