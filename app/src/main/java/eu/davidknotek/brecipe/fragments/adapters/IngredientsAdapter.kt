package eu.davidknotek.brecipe.fragments.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.CurrentIngredient
import eu.davidknotek.brecipe.databinding.RowIngredientBinding
import eu.davidknotek.brecipe.viewmodels.IngredientsViewModel

class IngredientsAdapter(private val ingredientsViewModel: IngredientsViewModel): RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {
    private var ingredients = emptyList<CurrentIngredient>()

    class MyViewHolder(val binding: RowIngredientBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RowIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredients[position]
        holder.binding.ingredientNameTextview.text = currentIngredient.item
        if (currentIngredient.done) {
            holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box)
            holder.binding.ingredientNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_light))
            holder.binding.ingredientNameTextview.paintFlags = holder.binding.ingredientNameTextview.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
            holder.binding.ingredientNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_dark))
            holder.binding.ingredientNameTextview.paintFlags = holder.binding.ingredientNameTextview.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.binding.rowIngredient.setOnClickListener {
            if (currentIngredient.done) {
                holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
                currentIngredient.done = false
            } else {
                holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box)
                currentIngredient.done = true
            }
            ingredientsViewModel.updateIngredient(currentIngredient)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIngredients(ingredients: List<CurrentIngredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}