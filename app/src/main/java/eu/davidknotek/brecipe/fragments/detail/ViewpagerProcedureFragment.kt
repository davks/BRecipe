package eu.davidknotek.brecipe.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentViewpagerProcedureBinding
import eu.davidknotek.brecipe.fragments.adapters.IngredientsAdapter
import eu.davidknotek.brecipe.fragments.adapters.ProcedureAdapter
import eu.davidknotek.brecipe.viewmodels.IngredientsAndProceduresViewModel

class ViewpagerProcedureFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerProcedureBinding
    private val ingredientsAndProceduresViewModel: IngredientsAndProceduresViewModel by activityViewModels()
    private val procedureAdapter: ProcedureAdapter by lazy { ProcedureAdapter(ingredientsAndProceduresViewModel) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerProcedureBinding.inflate(layoutInflater, container, false)

        // Adapter
        binding.procedureRecyclerView.adapter = procedureAdapter
        binding.procedureRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Show all ingredients
        ingredientsAndProceduresViewModel.allProcedures.observe(viewLifecycleOwner) {
            procedureAdapter.setProcedures(it)
        }

        return binding.root
    }
}