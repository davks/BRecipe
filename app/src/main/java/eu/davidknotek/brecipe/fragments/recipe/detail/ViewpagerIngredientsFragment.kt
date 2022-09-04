package eu.davidknotek.brecipe.fragments.recipe.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.brecipe.databinding.FragmentViewpagerDetailBinding
import eu.davidknotek.brecipe.databinding.FragmentViewpagerIngredientsBinding

class ViewpagerIngredientsFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerIngredientsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerIngredientsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}