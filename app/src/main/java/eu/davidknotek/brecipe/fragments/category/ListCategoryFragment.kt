package eu.davidknotek.brecipe.fragments.category

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
import eu.davidknotek.brecipe.util.SwipeGesture
import eu.davidknotek.brecipe.fragments.category.adapters.ListCategoryAdapter
import eu.davidknotek.brecipe.fragments.recipe.ShowRecipesBy
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class ListCategoryFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentListCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val listCategoryAdapter: ListCategoryAdapter by lazy { ListCategoryAdapter() }

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

    private fun setObservers() {
        categoryViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            sharedViewModel.checkIfDatabaseIsEmpty(categories)
            listCategoryAdapter.addCategories(categories)
            binding.categoriesRecyclerView.scheduleLayoutAnimation()  // animace
        }

        // Refresh category list after close the category edit dialog
        sharedViewModel.refreshCategory.observe(viewLifecycleOwner) {
            if (it) {
                listCategoryAdapter.refreshCategories()
                sharedViewModel.refreshCategory.value = false
            }
        }

        // When empty, database show an image on fragment
        sharedViewModel.isEmptyDatabase.observe(viewLifecycleOwner) {

        }
    }

    private fun setListeners() {
        binding.addCategoryFloatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.category_list_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.settings -> {
                //
                true
            }
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

    private fun searchRecipe() {
        findNavController().navigate(R.id.action_listCategoryFragment_to_searchRecipesFragment)
    }

    private fun showFavorite() {
        val bundle = bundleOf(SharedViewModel.SHOW_RECIPES_BY to ShowRecipesBy.FAVORITES)
        findNavController().navigate(R.id.action_listCategoryFragment_to_listRecipesFragment, bundle)
    }

    /**
     * Swipe left and right. Swipe left - delete category and swipe right - show edit dialog.
     */
    private fun swipeToGesture(recyclerView: RecyclerView) {
        val swipetToGestureCallback = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val category = listCategoryAdapter.categories[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        categoryViewModel.deleteCategory(category)
                        listCategoryAdapter.notifyItemRemoved(position)
                        restoreDeletedData(viewHolder.itemView, category)
                    }
                    ItemTouchHelper.RIGHT -> {
                        val bundle = bundleOf(SharedViewModel.CATEGORY to category)
                        findNavController().navigate(R.id.action_listCategoryFragment_to_editCategoryFragment, bundle)
                    }
                }
            }
        }
        // We need the connection to RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipetToGestureCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Show snackbar to restore deleted category.
     */
    private fun restoreDeletedData(view: View, deletedItem: Category) {
        val snackbar = Snackbar.make(
            view, "Deleted: ${deletedItem.name}",
            Snackbar.LENGTH_LONG
        )
        snackbar.addCallback(object: Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                //Change category name in reciepe
            }
        })
        snackbar.setAction("Undo") {
            categoryViewModel.insertCategory(deletedItem)
        }

        snackbar.show()
    }
}