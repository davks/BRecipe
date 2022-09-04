package eu.davidknotek.brecipe.fragments.recipe.adapters

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.RecipeAndCategory
import eu.davidknotek.brecipe.databinding.RowRecipeBinding
import eu.davidknotek.brecipe.fragments.recipe.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel

class SearchRecipeAdapter(
    private val recipeViewModel: RecipeViewModel
) : RecyclerView.Adapter<SearchRecipeAdapter.MyViewHolder>() {
    private var recipes = emptyList<RecipeAndCategory>()

    class MyViewHolder(val binding: RowRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipeWithCategory = recipes[position]
        val time = "${currentRecipeWithCategory.recipe.cooking + currentRecipeWithCategory.recipe.preparation}"
        Log.d(TAG, "onBindViewHolder: $time")
        holder.binding.nameRecipeTextView.text = currentRecipeWithCategory.recipe.name
        holder.binding.timeCookingTextView.text = "Time: ${time}min"

        if (currentRecipeWithCategory.recipe.imageUrl == "") {
            holder.binding.recipeImageView.setImageResource(R.drawable.no_image_available_small)
        } else {
            holder.binding.recipeImageView.setImageURI(currentRecipeWithCategory.recipe.imageUrl.toUri())
        }

        if (currentRecipeWithCategory.recipe.heart) {
            holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
        }

        // Heart - favorite recipe
        holder.binding.favoriteImageView.setOnClickListener {
            if (currentRecipeWithCategory.recipe.heart) {
                holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
                currentRecipeWithCategory.recipe.heart = false
            } else {
                holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite)
                currentRecipeWithCategory.recipe.heart = true
            }
            recipeViewModel.updateRecipe(currentRecipeWithCategory.recipe)
        }

        // Recipe detail, we need send current recipe
        holder.binding.recipeCard.setOnClickListener {
            val bundle = bundleOf(
                DetailRecipeFragment.RECIPE_AND_CATEGORY to currentRecipeWithCategory
            )
            holder.itemView.findNavController()
                .navigate(R.id.action_searchRecipesFragment_to_detailRecipeFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRecipes(recipes: List<RecipeAndCategory>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    private fun openDetail(holder: SearchRecipeAdapter.MyViewHolder) {

    }
}