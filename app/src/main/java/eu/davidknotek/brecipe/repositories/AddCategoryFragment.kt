package eu.davidknotek.brecipe.repositories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.davidknotek.brecipe.databinding.FragmentAddCategoryBinding

class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}