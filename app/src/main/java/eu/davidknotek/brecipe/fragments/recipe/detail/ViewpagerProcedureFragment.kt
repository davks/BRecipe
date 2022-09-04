package eu.davidknotek.brecipe.fragments.recipe.detail

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentViewpagerProcedureBinding

class ViewpagerProcedureFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerProcedureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerProcedureBinding.inflate(layoutInflater, container, false)
        val procedure = arguments?.getString(DetailRecipeFragment.PROCEDURE)
        procedure?.let {
            binding.procedureTextView.text = procedure
        }
        return binding.root
    }
}