package eu.davidknotek.brecipe.fragments.recipe.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.databinding.RowRecipeBinding
import eu.davidknotek.brecipe.fragments.recipe.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.recipe.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel

class ListRecipeAdapter(
    private val recipeViewModel: RecipeViewModel
) : RecyclerView.Adapter<ListRecipeAdapter.MyViewHolder>() {
    private var recipes = emptyList<Recipe>()

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
        val currentRecipe = recipes[position]
        val time = "${currentRecipe.cooking + currentRecipe.preparation}"
        holder.binding.nameRecipeTextView.text = currentRecipe.name
        holder.binding.timeCookingTextView.text = "Time: ${time}min"

        if (currentRecipe.imageUrl == "") {
            holder.binding.recipeImageView.setImageResource(R.drawable.no_image_available_small)
        } else {
            holder.binding.recipeImageView.setImageURI(currentRecipe.imageUrl.toUri())
        }

        if (currentRecipe.heart) {
            holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
        }

        // Heart - favorite recipe
        holder.binding.favoriteImageView.setOnClickListener {
            if (currentRecipe.heart) {
                holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
                currentRecipe.heart = false
            } else {
                holder.binding.favoriteImageView.setImageResource(R.drawable.ic_favorite)
                currentRecipe.heart = true
            }
            recipeViewModel.updateRecipe(currentRecipe)
        }

        // Recipe detail, we need send current recipe
        holder.binding.recipeCard.setOnClickListener {
            val bundle = bundleOf(DetailRecipeFragment.RECIPE to currentRecipe)
            holder.itemView.findNavController()
                .navigate(R.id.action_listRecipesFragment_to_detailRecipeFragment, bundle)

        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    private fun openDetail(holder: ListRecipeAdapter.MyViewHolder) {

    }
}