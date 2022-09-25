package eu.davidknotek.brecipe.fragments.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Procedure
import eu.davidknotek.brecipe.databinding.RowProcedureBinding
import eu.davidknotek.brecipe.util.isBold
import eu.davidknotek.brecipe.viewmodels.IngredientsAndProceduresViewModel

/**
 * Adapter for recyclerview in the recipe detail and a list of procedures.
 */
class ProcedureAdapter(private val ingredientsAndProceduresViewModel: IngredientsAndProceduresViewModel):
    RecyclerView.Adapter<ProcedureAdapter.MyViewHolder>() {
    private var procedures = emptyList<Procedure>()

    class MyViewHolder(val binding: RowProcedureBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowProcedureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val procedure = procedures[position]
        var item = procedure.item

         // Item is bold
        if (isBold(item)) {
            holder.binding.procedureNameTextview.setTypeface(null, Typeface.BOLD)
            holder.binding.procedureNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue2))
            item = procedure.item.substring(1, item.length - 1)
        } else {
            holder.binding.procedureNameTextview.setTypeface(null, Typeface.NORMAL)
            holder.binding.procedureNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_dark))
        }
        holder.binding.procedureNameTextview.text = item

        // Item is done
        if (procedure.done) {
            holder.binding.procedureDoneImageView.setImageResource(R.drawable.ic_check_box)
            holder.binding.procedureNameTextview.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_light))
            holder.binding.procedureNameTextview.paintFlags = holder.binding.procedureNameTextview.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.procedureDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
            holder.binding.procedureNameTextview.paintFlags = holder.binding.procedureNameTextview.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // When change ingredient to done or opposite
        holder.binding.rowProcedure.setOnClickListener {
            if (procedure.done) {
                holder.binding.procedureDoneImageView.setImageResource(R.drawable.ic_check_box_outline_blank)
                procedure.done = false
            } else {
                holder.binding.procedureDoneImageView.setImageResource(R.drawable.ic_check_box)
                procedure.done = true
            }
            ingredientsAndProceduresViewModel.updateProcedure(procedure)
        }
    }

    override fun getItemCount(): Int {
        return procedures.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProcedures(procedures: List<Procedure>) {
        this.procedures = procedures
        notifyDataSetChanged()
    }
}