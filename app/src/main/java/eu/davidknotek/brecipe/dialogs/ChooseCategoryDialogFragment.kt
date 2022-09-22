package eu.davidknotek.brecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.R
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
    private val dialogListCategoryAdapter: DialogListCategoryAdapter by lazy { DialogListCategoryAdapter(sharedViewModel) }

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
        setObservers()

        // RecyclerView
        binding.categoriesRecyclerView.adapter = dialogListCategoryAdapter
        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeImageView.setOnClickListener {
            sharedViewModel.refreshCategory.value = true
            dismiss()
        }
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

    private fun setObservers() {
        val categoryId = arguments?.getInt(CATEGORY_ID)?:0

        // We can show all categories, or all categories except the selected one
        if (categoryId == 0) {
            categoryViewModel.allCategories.observe(viewLifecycleOwner) {
                dialogListCategoryAdapter.addCategories(it)
            }
        } else {
            categoryViewModel.getAllCategories(categoryId).observe(viewLifecycleOwner) {
                dialogListCategoryAdapter.addCategories(it)
            }
        }

        // Close a dialog if is selected a category
        sharedViewModel.isSelectedCategory.observe(viewLifecycleOwner) { isSelectedCategory ->
            if (isSelectedCategory) {
                sharedViewModel.isSelectedCategory.value = false
                dismiss()
            }
        }
    }
}