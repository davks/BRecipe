package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.databinding.FragmentChooseCategoryDialogBinding
import eu.davidknotek.brecipe.fragments.adapters.DialogListCategoryAdapter
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel

class ChooseCategoryDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChooseCategoryDialogBinding
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val dialogListCategoryAdapter: DialogListCategoryAdapter by lazy { DialogListCategoryAdapter(categoryViewModel) }

    companion object {
        const val MESSAGE = "message"
        const val CATEGORY_ID = "category_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseCategoryDialogBinding.inflate(layoutInflater, container, false)
        isCancelable = false

        // Get Message from bundle
        val message = arguments?.getString(MESSAGE)
        val categoryId = arguments?.getInt(CATEGORY_ID)?:0
        binding.messageTextView.text = message

        // RecyclerView
        binding.categoriesRecyclerView.adapter = dialogListCategoryAdapter
        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

        setObservers(categoryId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnClickListener {
            categoryViewModel.refreshCategory.value = true
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        setDialogDimension()
    }

    /**
     * We can show all categories, or all categories except the selected one
     */
    private fun setObservers(categoryId: Int) {
        // category list
        if (categoryId == 0) {
            categoryViewModel.allCategories.observe(viewLifecycleOwner) {
                dialogListCategoryAdapter.addCategories(it)
            }
        } else {
            categoryViewModel.getAllCategories(categoryId).observe(viewLifecycleOwner) {
                dialogListCategoryAdapter.addCategories(it)
            }
        }

        categoryViewModel.selectedCategory.observe(viewLifecycleOwner) {
            dismiss()
        }
    }

    private fun setDialogDimension() {
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
}