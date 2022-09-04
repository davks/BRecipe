package eu.davidknotek.brecipe.fragments.recipe.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.databinding.FragmentViewpagerDetailBinding

class ViewpagerDetailFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerDetailBinding.inflate(layoutInflater, container,false)
        val recipe = requireArguments().getParcelable<Recipe>(DetailRecipeFragment.RECIPE_AND_CATEGORY)
        recipe?.let {
            setDetailRecipe(recipe)
        }

        return binding.root
    }

    private fun setDetailRecipe(recipe: Recipe) {
        binding.recipeNameTextView.text = recipe.name
        binding.preparationTextView.text = getString(R.string.min, recipe.preparation)
        binding.cookingTimeTextView.text = getString(R.string.min, recipe.cooking)
        binding.yieldTextView.text = getString(R.string.yield, recipe.yield)
        binding.ratingTextView.text = recipe.rating.toString()
        binding.noteTextView.text = recipe.note
        if (recipe.imageUrl == "") {
            binding.recipeImageView.setImageResource(R.drawable.no_image_available)
        } else {
            binding.recipeImageView.setImageURI(recipe.imageUrl.toUri())
        }
    }
}