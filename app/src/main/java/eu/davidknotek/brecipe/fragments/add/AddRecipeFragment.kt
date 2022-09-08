package eu.davidknotek.brecipe.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.databinding.FragmentAddRecipeBinding
import eu.davidknotek.brecipe.fragments.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.util.CameraAndStoragePermission
import eu.davidknotek.brecipe.util.strToInt
import eu.davidknotek.brecipe.util.verifyRecipe
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class AddRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAddRecipeBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var cameraAndStoragePermission: CameraAndStoragePermission
    private var category: Category? = null
    private lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(layoutInflater, container, false)
        category = arguments?.getParcelable(DetailRecipeFragment.CATEGORY)
        recipe = Recipe(0, category?.id?: 0, "", 0, 0, 0, false, 0, "", "", "", "")
        cameraAndStoragePermission = CameraAndStoragePermission(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menu in ToolBar
        val menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Set category
        binding.categoryTextView.text = category?.name

        emptyRecipe()
        setListeners()
        setObservers()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_add_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveRecipe -> {
                saveRecipe()
                true
            }
            else -> false
        }
    }

    private fun emptyRecipe() {
        sharedViewModel.recipeIngredients.value = ""
        sharedViewModel.recipeProcedure.value = ""
        sharedViewModel.recipeNote.value = ""
    }

    private fun setListeners() {
        binding.addIngredientsImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addIngredientsDialogFragment)
        }

        binding.addProcedureImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addProcedureDialogFragment)
        }

        binding.addNoteImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addNoteDialogFragment)
        }

        binding.imageFromCameraImageView.setOnClickListener {
            cameraAndStoragePermission.checkPermissionsCamera(binding.recipeImageView)
        }

        binding.starOneImageView.setOnClickListener {
            setRating(1)
        }

        binding.starTwoImageView.setOnClickListener {
            setRating(2)
        }

        binding.starThreeImageView.setOnClickListener {
            setRating(3)
        }

        binding.starFourImageView.setOnClickListener {
            setRating(4)
        }

        binding.starFiveImageView.setOnClickListener {
            setRating(5)
        }
    }

    private fun setObservers() {
        // Set ingredients od recipe
        sharedViewModel.recipeIngredients.observe(viewLifecycleOwner) { ingredients ->
            recipe.ingredients = ingredients
            binding.ingredientsTextView.text = sharedViewModel.getIngredientsMessage(ingredients)
        }

        // Set procedure of recipe
        sharedViewModel.recipeProcedure.observe(viewLifecycleOwner) { procedure ->
            recipe.procedure = procedure
            binding.procedureTextView.text = sharedViewModel.getProcedureMessage(procedure)

        }

        // Set note of recipe
        sharedViewModel.recipeNote.observe(viewLifecycleOwner) { note ->
            recipe.note = note
            binding.noteTextView.text = sharedViewModel.geNoteMessage(note)
        }
    }

    private fun setRating(stars: Int) {
        binding.starOneImageView.setImageResource(R.drawable.ic_star_border)
        binding.starTwoImageView.setImageResource(R.drawable.ic_star_border)
        binding.starThreeImageView.setImageResource(R.drawable.ic_star_border)
        binding.starFourImageView.setImageResource(R.drawable.ic_star_border)
        binding.starFiveImageView.setImageResource(R.drawable.ic_star_border)

        if (stars >= 1) {
            binding.starOneImageView.setImageResource(R.drawable.ic_star)
        }
        if (stars >= 2) {
            binding.starTwoImageView.setImageResource(R.drawable.ic_star)
        }
        if (stars >= 3) {
            binding.starThreeImageView.setImageResource(R.drawable.ic_star)
        }
        if (stars >= 4) {
            binding.starFourImageView.setImageResource(R.drawable.ic_star)
        }
        if (stars == 5) {
            binding.starFiveImageView.setImageResource(R.drawable.ic_star)
        }

        recipe.rating = stars
    }

    private fun saveRecipe() {
        recipe.name = binding.recipeNameEditText.text.toString()
        recipe.preparation = strToInt(binding.preparationEditText.text.toString())
        recipe.cooking = strToInt(binding.cookingEditText.text.toString())
        recipe.yield = strToInt(binding.yieldEditText.text.toString())

        if (verifyRecipe(recipe)) {
            if (cameraAndStoragePermission.image != null) {
                val uri = cameraAndStoragePermission.saveImageToStorage()
                recipe.imageUrl = uri.path.toString()
            }
            recipeViewModel.insertRecipe(recipe)

            val bundle = bundleOf(DetailRecipeFragment.CATEGORY to category, DetailRecipeFragment.SHOW_RECIPES_BY to UsedRecipesBy.CATEGORY)
            findNavController().navigate(R.id.action_addRecipeFragment_to_listRecipesFragment, bundle)
        } else {
            Toast.makeText(requireContext(), getString(R.string.fill_in_all), Toast.LENGTH_SHORT).show()
        }
    }
}