package eu.davidknotek.brecipe.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentEditCategoryBinding
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.util.CameraAndStoragePermission
import eu.davidknotek.brecipe.viewmodels.CategoryViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class EditCategoryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentEditCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var currentCategory: Category? = null
    private lateinit var cameraAndStoragePermission: CameraAndStoragePermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCategoryBinding.inflate(layoutInflater, container, false)
        isCancelable = false
        cameraAndStoragePermission = CameraAndStoragePermission(this)
        currentCategory = requireArguments().getParcelable(DetailRecipeFragment.CATEGORY)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setCategoryFragment()
    }

    private fun setListeners() {
        binding.cancelImageView.setOnClickListener {
            sharedViewModel.refreshCategory.value = true
            dismiss()
        }

        binding.saveImageView.setOnClickListener {
            if (saveCategory()) {
                dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_category_name, Toast.LENGTH_SHORT).show()
            }
        }

        // Show camera
        binding.cameraButton.setOnClickListener {
            cameraAndStoragePermission.checkPermissionsCamera(binding.categoryImageView)
        }
    }

    private fun saveCategory(): Boolean {
        val name = binding.categoryNameEditText.text.toString()
        return if (name.isNotEmpty() && currentCategory != null) {
            categoryViewModel.updateCategory(getCategoryFragment())
            true
        } else {
            false
        }
    }

    private fun setCategoryFragment() {
        binding.categoryNameEditText.setText(currentCategory?.name)
        if (currentCategory?.imageUrl == "") {
            binding.categoryImageView.setImageResource(R.drawable.no_image_available)
        } else {
            binding.categoryImageView.setImageURI(currentCategory?.imageUrl?.toUri())
        }
    }

    private fun getCategoryFragment(): Category {
        val categoryName = binding.categoryNameEditText.text.toString()
        var categoryImageUrl = currentCategory?.imageUrl.toString()

        // Save a new image to the storage
        if (cameraAndStoragePermission.image != null) {
            val uri = cameraAndStoragePermission.saveImageToStorage()
            cameraAndStoragePermission.deleteOldImageFile(categoryImageUrl)
            categoryImageUrl = uri.path.toString()
        }

        return Category(currentCategory?.id!!, categoryName, categoryImageUrl)
    }
}