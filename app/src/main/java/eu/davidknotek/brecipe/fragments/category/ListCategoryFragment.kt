package eu.davidknotek.brecipe.fragments.category

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentListCategoryBinding
import eu.davidknotek.brecipe.fragments.category.adapters.ListCategoryAdapter
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel

class ListCategoryFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentListCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val listCategoryAdapter: ListCategoryAdapter by lazy { ListCategoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCategoryBinding.inflate(layoutInflater, container, false)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // CategoryAdapter
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
            categoryViewModel.checkIfDatabaseIsEmpty(categories)
            listCategoryAdapter.addCategories(categories)
            binding.categoriesRecyclerView.scheduleLayoutAnimation()  // animace
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
            else -> false
        }
    }

    private fun swipeToGesture(recyclerView: RecyclerView) {
        val swipetToGestureCallback = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val category = listCategoryAdapter.categories[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        categoryViewModel.deleteCategory(category)
                        listCategoryAdapter.notifyItemRemoved(position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        val bundle = bundleOf(EditCategoryFragment.CATEGORY to category)
                        findNavController().navigate(R.id.action_listCategoryFragment_to_editCategoryFragment, bundle)
                        listCategoryAdapter.refreshCategories()
                    }
                }
            }
        }
        // Connect to RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipetToGestureCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}