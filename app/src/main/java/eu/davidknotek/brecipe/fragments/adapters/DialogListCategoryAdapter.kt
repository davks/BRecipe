package eu.davidknotek.brecipe.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.RowCategoryDialogBinding
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel

class DialogListCategoryAdapter(private val categoryViewModel: CategoryViewModel): RecyclerView.Adapter<DialogListCategoryAdapter.MyViewHolder>() {
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

        holder.binding.categoryCargView.setOnClickListener {
            categoryViewModel.selectedCategory.value = currentCategory
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun addCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}