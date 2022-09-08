package eu.davidknotek.brecipe.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.davidknotek.brecipe.databinding.FragmentViewpagerIngredientsBinding
import eu.davidknotek.brecipe.fragments.adapters.IngredientsAdapter
import eu.davidknotek.brecipe.viewmodels.IngredientsAndProceduresViewModel

class ViewpagerIngredientsFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerIngredientsBinding
    private val ingredientsAndProceduresViewModel: IngredientsAndProceduresViewModel by activityViewModels()
    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter(ingredientsAndProceduresViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerIngredientsBinding.inflate(layoutInflater, container, false)

        // Adapter
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Show all ingredients
        ingredientsAndProceduresViewModel.allIngredients.observe(viewLifecycleOwner) {
            ingredientsAdapter.setIngredients(it)
        }

        return binding.root
    }
}