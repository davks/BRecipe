package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentSearchRecipesBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.SearchRecipeAdapter
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class SearchRecipesFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {
    private lateinit var binding: FragmentSearchRecipesBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val searchRecipeAdapter: SearchRecipeAdapter by lazy { SearchRecipeAdapter(recipeViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRecipesBinding.inflate(layoutInflater, container, false)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // RecyclerView
        binding.recipesRecyclerView.adapter = searchRecipeAdapter
        binding.recipesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchRecipes(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchRecipes(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_recipes_menu, menu)

        // Search View
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = false
        searchView?.queryHint = "Search..."
        searchView?.isFocusable = true
        searchView?.isIconified = false
        searchView?.requestFocusFromTouch()
        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun setObservers() {
        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            sharedViewModel.checkIfDatabaseIsEmpty(recipes)
            searchRecipeAdapter.addRecipes(recipes)
        }
    }

    private fun searchRecipes(query: String) {
        recipeViewModel.searchRecipes("%$query%").observe(viewLifecycleOwner) { list ->
            list?.let {
                searchRecipeAdapter.addRecipes(it)
            }
        }
    }
}