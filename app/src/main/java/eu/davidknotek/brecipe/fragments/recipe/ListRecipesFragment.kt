package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentListRecipesBinding

class ListRecipesFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecipesBinding.inflate(layoutInflater, container, false)

        binding.addRecipeActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listRecipesFragment_to_addRecipeFragment)
        }

        return binding.root
    }


}