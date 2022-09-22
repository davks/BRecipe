package eu.davidknotek.brecipe.fragments.list

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentListCategoryBinding
import eu.davidknotek.brecipe.dialogs.ChooseCategoryDialogFragment
import eu.davidknotek.brecipe.util.SwipeGesture
import eu.davidknotek.brecipe.fragments.adapters.ListCategoryAdapter
import eu.davidknotek.brecipe.fragments.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

/**
 * Home fragment that displays list of categories.
 */
class ListCategoryFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentListCategoryBinding
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val listCategoryAdapter: ListCategoryAdapter by lazy { ListCategoryAdapter() }

    private var oldCategory: Category? = null
    private var oldCategoryId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCategoryBinding.inflate(layoutInflater, container, false)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // RecyclerView
        binding.categoriesRecyclerView.adapter = listCategoryAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeToGesture(binding.categoriesRecyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.category_list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.favorite -> {
                showFavorite()
                true
            }
            R.id.searchRecipe -> {
                searchRecipe()
                true
            }
            else -> false
        }
    }

    private fun setListeners() {
        binding.addCategoryFloatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
        }
    }

    private fun setObservers() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            sharedViewModel.checkIfDatabaseIsEmpty(categories)
            listCategoryAdapter.addCategories(categories)
            binding.categoriesRecyclerView.scheduleLayoutAnimation()  // animation
        }

        // Refresh category list after close the category edit dialog
        sharedViewModel.refreshCategory.observe(viewLifecycleOwner) {
            if (it) {
                listCategoryAdapter.refreshCategories()
                sharedViewModel.refreshCategory.value = false
            }
        }

        // When empty, database show an image with no items
        sharedViewModel.isEmptyDatabase.observe(viewLifecycleOwner) {
            if (it) {
                binding.noItemsLinearLayout.visibility = View.VISIBLE
            } else {
                binding.noItemsLinearLayout.visibility = View.GONE
            }
        }

        // If we select a category from the chooseCategoryDialog, we remove the old category and transfer the recipes to the new category.
        sharedViewModel.recipeCategory.observe(viewLifecycleOwner) { selectedCategory ->
            selectedCategory?.let { newCategory ->
                recipeViewModel.changeRecipeCategory(oldCategoryId, newCategory.id)
                oldCategory?.let {
                    categoryViewModel.deleteCategory(it)
                }
                listCategoryAdapter.refreshCategories()
                sharedViewModel.recipeCategory.value = null
            }
        }
    }

    private fun searchRecipe() {
        findNavController().navigate(R.id.action_listCategoryFragment_to_searchRecipesFragment)
    }

    private fun showFavorite() {
        val bundle = bundleOf(DetailRecipeFragment.SHOW_RECIPES_BY to UsedRecipesBy.FAVORITES)
        findNavController().navigate(R.id.action_listCategoryFragment_to_listRecipesFragment, bundle)
    }

    /**
     * Swipe left and right. Swipe left - delete category and swipe right - show edit dialog.
     */
    private fun swipeToGesture(recyclerView: RecyclerView) {
        val swipeToGestureCallback = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val category = listCategoryAdapter.categories[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deleteCategory(category, position, viewHolder)
                        oldCategoryId = category.id
                        oldCategory = category
                    }
                    ItemTouchHelper.RIGHT -> {
                        val bundle = bundleOf(DetailRecipeFragment.CATEGORY to category)
                        findNavController().navigate(R.id.action_listCategoryFragment_to_editCategoryFragment, bundle)
                    }
                }
            }
        }
        // We need the connection to RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipeToGestureCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * If the number of categories is greater than zero, a dialog box will appear listing the categories
     * where the recipes should be moved to. Otherwise the category will be deleted directly.
     */
    private fun deleteCategory(category: Category, position: Int, viewHolder: RecyclerView.ViewHolder) {
        category.numberOfRecipes?.let { numberOfRecipes ->
            if (numberOfRecipes > 0) {
                val bundle = bundleOf(
                    ChooseCategoryDialogFragment.MESSAGE to "Select the category where the recipes will be moved.",
                    ChooseCategoryDialogFragment.CATEGORY_ID to category.id,
                    ChooseCategoryDialogFragment.TITLE to "Delete the category")
                findNavController().navigate(R.id.action_listCategoryFragment_to_chooseCategoryDialogFragment, bundle)
            } else {
                categoryViewModel.deleteCategory(category)
                listCategoryAdapter.notifyItemRemoved(position)
                restoreDeletedData(viewHolder.itemView, category)
            }
        }
    }

    /**
     * Show snack to restore deleted category.
     */
    private fun restoreDeletedData(view: View, deletedItem: Category) {
        val snack = Snackbar.make(
            view, "Deleted: ${deletedItem.name}",
            Snackbar.LENGTH_LONG
        )
        snack.addCallback(object: Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
            }
        })
        snack.setAction("Undo") {
            categoryViewModel.insertCategory(deletedItem)
        }

        snack.show()
    }
}