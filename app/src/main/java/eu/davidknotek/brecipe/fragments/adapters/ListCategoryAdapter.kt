package eu.davidknotek.brecipe.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.RowCategoryBinding
import eu.davidknotek.brecipe.fragments.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment

/**
 * Adapter for RecyclerView with a list of categories in the home fragment.
 */
class ListCategoryAdapter : RecyclerView.Adapter<ListCategoryAdapter.MyViewHolder>() {
    var categories = emptyList<Category>()

    class MyViewHolder(val binding: RowCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categories[position]

        holder.binding.categoryNameTextView.text = currentCategory.name
        if (currentCategory.imageUrl == "") {
            holder.binding.categoryImageView.setImageResource(R.drawable.no_image_available_small)
        } else {
            holder.binding.categoryImageView.setImageURI(currentCategory.imageUrl.toUri())
        }
        holder.binding.numberOfRecipesTextView.text = currentCategory.numberOfRecipes?.toString()

        // Show recipes from selected category
        holder.binding.rowCategory.setOnClickListener {
            val bundle = bundleOf(
                DetailRecipeFragment.CATEGORY to currentCategory,
                DetailRecipeFragment.SHOW_RECIPES_BY to UsedRecipesBy.CATEGORY
            )
            holder.itemView.findNavController()
                .navigate(R.id.action_listCategoryFragment_to_listRecipesFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun addCategories(categories: List<Category>) {
        this.categories = categories
        refreshCategories()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshCategories() {
        notifyDataSetChanged()
    }

}