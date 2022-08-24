package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.brecipe.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}