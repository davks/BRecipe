package eu.davidknotek.brecipe.fragments.edit

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.RecipeAndCategory
import eu.davidknotek.brecipe.databinding.FragmentEditRecipeBinding
import eu.davidknotek.brecipe.dialogs.ChooseCategoryDialogFragment
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.util.CameraAndStoragePermission
import eu.davidknotek.brecipe.util.strToInt
import eu.davidknotek.brecipe.util.verifyRecipe
import eu.davidknotek.brecipe.viewmodels.IngredientsAndProceduresViewModel
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

/**
 * Edit a recipe. We can change the recipe name, procedure, ingredients etc...
 */
class EditRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentEditRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val ingredientsAndProceduresViewModel: IngredientsAndProceduresViewModel by activityViewModels()
    private var recipeAndCategory: RecipeAndCategory? = null
    private lateinit var cameraAndStoragePermission: CameraAndStoragePermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(layoutInflater, container, false)

        recipeAndCategory = requireArguments().getParcelable(DetailRecipeFragment.RECIPE_AND_CATEGORY)

        // We need permission to camera
        cameraAndStoragePermission = CameraAndStoragePermission(this)

        setRecipe()
        setListeners()
        setObservers()

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_edit_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.delete -> {
                deleteRecipe()
                true
            }
            R.id.save -> {
                saveRecipe()
                true
            }
            else -> false
        }
    }

    private fun setRecipe() {
        binding.recipeNameEditText.setText(recipeAndCategory?.recipe?.name)
        binding.preparationEditText.setText(recipeAndCategory?.recipe?.preparation.toString())
        binding.cookingEditText.setText(recipeAndCategory?.recipe?.cooking.toString())
        binding.yieldEditText.setText(recipeAndCategory?.recipe?.yield.toString())
        binding.categoryTextView.text = recipeAndCategory?.category?.name?:""
        setRating(recipeAndCategory?.recipe?.rating?: 0)

        if (recipeAndCategory?.recipe?.imageUrl == "") {
            binding.recipeImageView.setImageResource(R.drawable.no_image_available)
        } else {
            binding.recipeImageView.setImageURI(recipeAndCategory?.recipe?.imageUrl?.toUri())
        }

        sharedViewModel.recipeCategory.value = recipeAndCategory?.category
        sharedViewModel.recipeNote.value = recipeAndCategory?.recipe?.note?:""
        sharedViewModel.recipeIngredients.value = recipeAndCategory?.recipe?.ingredients?:""
        sharedViewModel.recipeProcedure.value = recipeAndCategory?.recipe?.procedure?:""
    }

    private fun saveRecipe() {
        recipeAndCategory?.let { recipeAndCategory ->
            recipeAndCategory.recipe.name = binding.recipeNameEditText.text.toString()
            recipeAndCategory.recipe.preparation = strToInt(binding.preparationEditText.text.toString())
            recipeAndCategory.recipe.cooking = strToInt(binding.cookingEditText.text.toString())
            recipeAndCategory.recipe.yield = strToInt(binding.yieldEditText.text.toString())

            if (verifyRecipe(recipeAndCategory.recipe)) {
                if (cameraAndStoragePermission.image != null) {
                    val uri = cameraAndStoragePermission.saveImageToStorage()
                    recipeAndCategory.recipe.imageUrl = uri.path.toString()
                }
                recipeViewModel.updateRecipe(recipeAndCategory.recipe)

                ingredientsAndProceduresViewModel.setNewIngredientsAndProcedure(recipeAndCategory.recipe)
                val bundle = bundleOf(DetailRecipeFragment.RECIPE_AND_CATEGORY to recipeAndCategory)
                findNavController().navigate(R.id.action_editRecipeFragment_to_detailRecipeFragment, bundle)
            } else {
                Toast.makeText(requireContext(), getString(R.string.fill_in_all), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteRecipe() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialog.setTitle("Delete recipe: ${recipeAndCategory?.recipe?.name}")
        dialog.setMessage("Do you want to delete this recipe: ${recipeAndCategory?.recipe?.name}?")
        dialog.setIcon(R.drawable.ic_warning)
        dialog.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            recipeAndCategory?.let {
                recipeViewModel.deleteRecipe(it.recipe)
                findNavController().navigate(R.id.action_editRecipeFragment_to_listCategoryFragment)
            }
            dialogInterface.dismiss()
        }
        dialog.show()
    }

    private fun setListeners() {
        binding.addIngredientsImageView.setOnClickListener {
            findNavController().navigate(R.id.action_editRecipeFragment_to_addIngredientsDialogFragment)
        }

        binding.addProcedureImageView.setOnClickListener {
            findNavController().navigate(R.id.action_editRecipeFragment_to_addProcedureDialogFragment)
        }

        binding.addNoteImageView.setOnClickListener {
            findNavController().navigate(R.id.action_editRecipeFragment_to_addNoteDialogFragment)
        }

        binding.imageFromCameraImageView.setOnClickListener {
            cameraAndStoragePermission.checkPermissionsCamera(binding.recipeImageView)
        }

        binding.addCategoryImageView.setOnClickListener {
            val bundle = bundleOf(
                ChooseCategoryDialogFragment.TITLE to "Choose the category")
            findNavController().navigate(R.id.action_editRecipeFragment_to_chooseCategoryDialogFragment, bundle)
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

        recipeAndCategory?.recipe?.rating = stars
    }

    private fun setObservers() {
        // Set ingredients
        sharedViewModel.recipeIngredients.observe(viewLifecycleOwner) { ingredients ->
            recipeAndCategory?.recipe?.ingredients = ingredients
            binding.ingredientsTextView.text = sharedViewModel.getIngredientsMessage(ingredients)
        }

        // Set procedure
        sharedViewModel.recipeProcedure.observe(viewLifecycleOwner) { procedure ->
            recipeAndCategory?.recipe?.procedure = procedure
            binding.procedureTextView.text = sharedViewModel.getProcedureMessage(procedure)

        }

        // Set note
        sharedViewModel.recipeNote.observe(viewLifecycleOwner) { note ->
            recipeAndCategory?.recipe?.note = note
            binding.noteTextView.text = sharedViewModel.geNoteMessage(note)
        }

        // Set category
        sharedViewModel.recipeCategory.observe(viewLifecycleOwner) { selectedCategory ->
            selectedCategory?.let { category ->
                recipeAndCategory?.recipe?.idCategory = category.id
                binding.categoryTextView.text = category.name
            }
        }
    }
}