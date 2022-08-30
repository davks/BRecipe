package eu.davidknotek.brecipe.fragments.category.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.RowCategoryBinding
import eu.davidknotek.brecipe.fragments.category.EditCategoryFragment

class ListCategoryAdapter: RecyclerView.Adapter<ListCategoryAdapter.MyViewHolder>() {
    var categories = emptyList<Category>()

    class MyViewHolder(val binding: RowCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentCategory = categories[position]

        holder.binding.categoryNameTextView.text = currentCategory.name
        //holder.binding.categoryImageView.setImageBitmap()

        // Show detail
        holder.binding.rowCategory.setOnClickListener {
            Toast.makeText(context, "Showing a detail", Toast.LENGTH_SHORT).show()
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