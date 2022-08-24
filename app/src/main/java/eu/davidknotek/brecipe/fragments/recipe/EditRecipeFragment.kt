package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.brecipe.databinding.FragmentEditRecipeBinding

class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}