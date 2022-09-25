package eu.davidknotek.brecipe.fragments.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Ingredient
import eu.davidknotek.brecipe.databinding.RowIngredientBinding
import eu.davidknotek.brecipe.util.isBold
import eu.davidknotek.brecipe.viewmodels.IngredientsAndProceduresViewModel

class IngredientsAdapter(private val ingredientsAndProceduresViewModel: IngredientsAndProceduresViewModel): RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {
    private var ingredients = emptyList<Ingredient>()

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
        val ingredient = ingredients[position]
        var item = ingredient.item

        // Item is bold
        if (isBold(ingredient.item)) {
            holder.binding.ingredientNameTextview.setTypeface(null, Typeface.BOLD)
            holder.binding.ingredientNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
            item = item.substring(1, item.length - 1)
        } else {
            holder.binding.ingredientNameTextview.setTypeface(null, Typeface.NORMAL)
            holder.binding.ingredientNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_dark))
        }
        holder.binding.ingredientNameTextview.text = item

        // Item is done
        if (ingredient.done) {
            holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box)
            holder.binding.ingredientNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_light))
            holder.binding.ingredientNameTextview.paintFlags = holder.binding.ingredientNameTextview.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
            holder.binding.ingredientNameTextview.paintFlags = holder.binding.ingredientNameTextview.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // When change ingredient to done or opposite
        holder.binding.rowIngredient.setOnClickListener {
            if (ingredient.done) {
                holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
                ingredient.done = false
            } else {
                holder.binding.ingredientDoneImageView.setImageResource(R.drawable.ic_check_box)
                ingredient.done = true
            }
            ingredientsAndProceduresViewModel.updateIngredient(ingredient)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIngredients(ingredients: List<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}