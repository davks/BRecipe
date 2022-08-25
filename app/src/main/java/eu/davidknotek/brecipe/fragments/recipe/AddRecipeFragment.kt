package eu.davidknotek.brecipe.fragments.recipe

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.databinding.DialogAddIngredientsBinding
import eu.davidknotek.brecipe.databinding.DialogAddNoteBinding
import eu.davidknotek.brecipe.databinding.DialogAddProcedureBinding
import eu.davidknotek.brecipe.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private var ingredientsText = ""
    private var procedureText = ""
    private var noteText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.addIngredientsImageView.setOnClickListener {
//            showIngredientsDialog()
            findNavController().navigate(R.id.action_addRecipeFragment_to_addIngredientsDialogFragment)
        }

        binding.addProcedureImageView.setOnClickListener {
            showProcedureDialog()
        }

        binding.addNoteImageView.setOnClickListener {
            showNoteDialog()
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
    }

    private fun showProcedureDialog() {
        val dialog = Dialog(requireActivity())
        val dialogBinding = DialogAddProcedureBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.procedureEditText.setText(procedureText)

        dialogBinding.closeImageView.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.saveImageView.setOnClickListener {
            dialog.dismiss()
            procedureText = dialogBinding.procedureEditText.text.toString()
            binding.procedureTextView.text = procedureText
        }
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun showIngredientsDialog() {
        val dialog = Dialog(requireActivity())
        val dialogBinding = DialogAddIngredientsBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.ingredientsEditText.setText(ingredientsText)

        dialogBinding.closeImageView.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.saveImageView.setOnClickListener {
            dialog.dismiss()
            ingredientsText = dialogBinding.ingredientsEditText.text.toString()
            binding.ingredientsTextView.text = ingredientsText
        }
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun showNoteDialog() {
        val dialog = Dialog(requireActivity())
        val dialogBinding = DialogAddNoteBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.noteEditText.setText(noteText)

        dialogBinding.closeImageView.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.saveImageView.setOnClickListener {
            dialog.dismiss()
            noteText = dialogBinding.noteEditText.text.toString()
            binding.noteTextView.text = noteText
        }
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }



}