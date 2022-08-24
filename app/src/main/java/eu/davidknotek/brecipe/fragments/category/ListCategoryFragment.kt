package eu.davidknotek.brecipe.fragments.category

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.FragmentListCategoryBinding

class ListCategoryFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentListCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCategoryBinding.inflate(layoutInflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.addCategoryActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
        }

        binding.listCategoryConstraintLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listCategoryFragment_to_listRecipesFragment)
        }

        return binding.root
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
}