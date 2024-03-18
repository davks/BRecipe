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
import eu.davidknotek.brecipe.databinding.FragmentAddRecipeBinding
import eu.davidknotek.brecipe.fragments.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.util.CameraAndStoragePermission
import eu.davidknotek.brecipe.util.strToInt
import eu.davidknotek.brecipe.util.verifyRecipe
import eu.davidknotek.brecipe.viewmodels.AddRecipeViewModel
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

/**
 * This we use when we need add a new recipe.
 * We need to know a category and camera permission.
 */
class AddRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentAddRecipeBinding
    private val addRecipeViewModel: AddRecipeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var cameraAndStoragePermission: CameraAndStoragePermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(layoutInflater, container, false)

        // We need permission to camera
        cameraAndStoragePermission = CameraAndStoragePermission(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menu in ToolBar
        val menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
        // In a new recipe we need to know a category
        addRecipeViewModel.setRecipeCategory(arguments?.getParcelable(DetailRecipeFragment.CATEGORY))
    }

    private fun setListeners() {
        binding.addNoteImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addNoteDialogFragment)
        }

        binding.addIngredientsImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addIngredientsDialogFragment)
        }

        binding.addProcedureImageView.setOnClickListener {
            findNavController().navigate(R.id.action_addRecipeFragment_to_addProcedureDialogFragment)
        }

        binding.imageFromCameraImageView.setOnClickListener {
            cameraAndStoragePermission.checkPermissionsCamera(binding.recipeImageView)
        }

        // Set rating (stars)
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
        // Set note
        sharedViewModel.recipeNote.observe(viewLifecycleOwner) { note ->
            addRecipeViewModel.setRecipeNotes(note)
            binding.noteTextView.text = sharedViewModel.geNoteMessage(note)
        }

        // Set ingredients
        sharedViewModel.recipeIngredients.observe(viewLifecycleOwner) { ingredients ->
            addRecipeViewModel.setRecipeIngredients(ingredients)
            binding.ingredientsTextView.text = sharedViewModel.getIngredientsMessage(ingredients)
        }

        // Set procedure
        sharedViewModel.recipeProcedure.observe(viewLifecycleOwner) { procedure ->
            addRecipeViewModel.setRecipeProcedure(procedure)
            binding.procedureTextView.text = sharedViewModel.getProcedureMessage(procedure)

        }

        // Set recipe category
        addRecipeViewModel.category.observe(viewLifecycleOwner) { category ->
            binding.categoryTextView.text = category?.name
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

        addRecipeViewModel.setRecipeRating(stars)
    }

    private fun saveRecipe() {
        addRecipeViewModel.setRecipeName(binding.recipeNameEditText.text.toString())
        addRecipeViewModel.setRecipePreparation(strToInt(binding.preparationEditText.text.toString()))
        addRecipeViewModel.setRecipeCooking(strToInt(binding.cookingEditText.text.toString()))
        addRecipeViewModel.setRecipeYield(strToInt(binding.yieldEditText.text.toString()))

        if (addRecipeViewModel.recipe.value != null) {
            if (verifyRecipe(addRecipeViewModel.recipe.value!!)) {
                if (cameraAndStoragePermission.image != null) {
                    val uri = cameraAndStoragePermission.saveImageToStorage()
                    addRecipeViewModel.setRecipeImageUrl(uri.path.toString())
                }
                recipeViewModel.insertRecipe(addRecipeViewModel.recipe.value!!)

                val bundle = bundleOf(DetailRecipeFragment.CATEGORY to addRecipeViewModel.category.value, DetailRecipeFragment.SHOW_RECIPES_BY to UsedRecipesBy.CATEGORY)
                findNavController().navigate(R.id.action_addRecipeFragment_to_listRecipesFragment, bundle)
            } else {
                Toast.makeText(requireContext(), getString(R.string.fill_in_all), Toast.LENGTH_SHORT).show()
            }
        }
    }
}