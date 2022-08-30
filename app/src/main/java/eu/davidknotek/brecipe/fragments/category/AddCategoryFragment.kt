package eu.davidknotek.brecipe.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentAddCategoryBinding
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel

class AddCategoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.cancelImageView.setOnClickListener {
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            if (saveCategory()) {
                dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_category_name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCategory(): Boolean {
        val name = binding.categoryNameEditText.text.toString()
        return if (name.isNotEmpty()) {
            categoryViewModel.insertCategory(Category(0, name, ""))
            true
        } else {
            false
        }
    }
}