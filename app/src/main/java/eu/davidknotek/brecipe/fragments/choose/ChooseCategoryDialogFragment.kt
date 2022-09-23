package eu.davidknotek.brecipe.fragments.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentChooseCategoryDialogBinding
import eu.davidknotek.brecipe.fragments.adapters.DialogListCategoryAdapter
import eu.davidknotek.brecipe.util.setDialogDimension
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

/**
 * Fragment, which will display a list of categories from which we can select one.
 * The selected category is recorded via the DialogListCategoryAdapter.
 */
class ChooseCategoryDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChooseCategoryDialogBinding
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        const val MESSAGE = "message"
        const val CATEGORY_ID = "category_id"
        const val TITLE = "title"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseCategoryDialogBinding.inflate(layoutInflater, container, false)
        isCancelable = false

        setMessage()
        setTitle()
        showCategories()
        setObservers()

        // Close a dialog
        binding.closeImageView.setOnClickListener {
            sharedViewModel.refreshCategory.value = true
            dismiss()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setDialogDimension(dialog)
    }

    private fun setTitle() {
        val title = arguments?.getString(TITLE) ?: getString(R.string.choose_category)
        binding.titleTextView.text = title
    }

    private fun setMessage() {
        val message = arguments?.getString(MESSAGE) ?: ""
        if (message.isEmpty()) {
            binding.messageTextView.visibility = View.GONE
        } else {
            binding.messageTextView.visibility = View.VISIBLE
            binding.messageTextView.text = message
        }
    }

    /**
     * We can show all categories, or all categories except the deleted one
     */
    private fun showCategories() {
        val categoryId = arguments?.getInt(CATEGORY_ID)?:0

        if (categoryId == 0) {
            findAllCategories()
        } else {
            findCategories(categoryId)
        }
    }

    private fun setObservers() {
        // Close a dialog if is selected a category
        sharedViewModel.isSelectedCategory.observe(viewLifecycleOwner) { isSelectedCategory ->
            if (isSelectedCategory) {
                sharedViewModel.isSelectedCategory.value = false
                dismiss()
            }
        }

        // When empty, database show an image with no items
        sharedViewModel.isEmptyCategory.observe(viewLifecycleOwner) {
            if (it) {
                binding.noItemsLinearLayout.visibility = View.VISIBLE
            } else {
                binding.noItemsLinearLayout.visibility = View.GONE
            }
        }
    }

    /**
     * When editing a recipe, we need to display all categories.
     */
    private fun findAllCategories() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            sharedViewModel.checkIfCategoriesIsEmpty(categories)
            val dialogListCategoryAdapter = DialogListCategoryAdapter { selectedCategory ->
                sharedViewModel.selectedCategory.value = selectedCategory
                sharedViewModel.isSelectedCategory.value = true
            }
            setRecyclerView(dialogListCategoryAdapter, categories)
        }
    }

    /**
     * If we try to remove a category, we will display the categories except the one that will be removed.
     */
    private fun findCategories(categoryId: Int) {
        categoryViewModel.getCategories(categoryId).observe(viewLifecycleOwner) { categories ->
            sharedViewModel.checkIfCategoriesIsEmpty(categories)
            val dialogListCategoryAdapter = DialogListCategoryAdapter { selectedCategory ->
                sharedViewModel.deleteCategory.value = selectedCategory
                sharedViewModel.isSelectedCategory.value = true
            }
            setRecyclerView(dialogListCategoryAdapter, categories)
        }
    }

    private fun setRecyclerView(
        adapter: DialogListCategoryAdapter,
        categories: List<Category>
    ) {
        binding.categoriesRecyclerView.adapter = adapter
        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        adapter.addCategories(categories)
    }


}