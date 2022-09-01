package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentListRecipesBinding

class ListRecipesFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private var currentCategory: Category? = null

    companion object {
        const val CATEGORY = "category"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecipesBinding.inflate(layoutInflater, container, false)
        currentCategory = arguments?.getParcelable(CATEGORY)
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title = "Recipes: ${currentCategory?.name}"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        // Add new recipe
        binding.addRecipeActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listRecipesFragment_to_addRecipeFragment)
        }
    }


}