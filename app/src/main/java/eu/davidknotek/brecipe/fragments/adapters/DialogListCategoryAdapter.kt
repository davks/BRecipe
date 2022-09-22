package eu.davidknotek.brecipe.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.RowCategoryDialogBinding
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

/**
 * Adapter for RecyclerView in ChooseCategoryDialogFragment.
 * Display the categories, from which we select one
 */
class DialogListCategoryAdapter(private val sharedViewModel: SharedViewModel): RecyclerView.Adapter<DialogListCategoryAdapter.MyViewHolder>() {
    private var categories = emptyList<Category>()

    class MyViewHolder(val binding: RowCategoryDialogBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowCategoryDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.binding.categoryNameTextView.text = currentCategory.name
        if (currentCategory.imageUrl.isEmpty()) {
            holder.binding.categoryPictureImageView.setImageResource(R.drawable.no_image_available_small)
        } else {
            holder.binding.categoryPictureImageView.setImageURI(currentCategory.imageUrl.toUri())
        }

        // If we click on a category, we save it and inform that we have already selected the category (we need close the chooseCategoryDialog)
        holder.binding.categoryCargView.setOnClickListener {
            sharedViewModel.recipeCategory.value = currentCategory
            sharedViewModel.isSelectedCategory.value = true
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addCategories(categories: List<Category>) {
        this.categories = categories
        refreshCategories()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshCategories() {
        notifyDataSetChanged()
    }
}