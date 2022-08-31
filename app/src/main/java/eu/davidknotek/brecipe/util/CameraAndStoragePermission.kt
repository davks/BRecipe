package eu.davidknotek.brecipe.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class CameraAndStoragePermission(val fragment: Fragment) {
    var image: Bitmap? = null
    private var imageView: ImageView? = null

    private val cameraPermissionRequest =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(fragment.context, "Denied, you can change it in settings", Toast.LENGTH_SHORT).show()
            }
        }

    private val readPermissionRequest =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGalleryWindow()
            } else {
                Toast.makeText(fragment.context, "Denied, you can change it in settings", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val resultCameraLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val myIntent: Intent? = result.data
                val thumbNail: Bitmap = myIntent?.extras?.get("data") as Bitmap
                imageView?.setImageBitmap(thumbNail)
                image = thumbNail
            }
        }

    private val resultGalleryLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageView?.setImageURI(result.data?.data)
            }
        }

    fun checkPermissionsCamera(imageView: ImageView) {
        this.imageView = imageView
        when {
            // 1. Permissions accepted
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            // 2. Permissions denied
            ActivityCompat.shouldShowRequestPermissionRationale(
                fragment.requireActivity(),
                Manifest.permission.CAMERA
            ) -> {
                showRationaleDialog(
                    "Permission is required for Camera access",
                    "Camera cannot be use because Camera access is denied.") {
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }
            }
            // 3. Ask for permissions
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    fun checkPermissionGallery() {
        when {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGalleryWindow()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                fragment.requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showRationaleDialog(
                    "Read file permission is required",
                    "Multimedia window cannot be use because access is denied.") {
                    readPermissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else -> {
                readPermissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    /**
     * Save an image to storage
     */
    fun saveImageToStorage(): Uri {
        var imgFile = fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        imgFile = File(imgFile, "${UUID.randomUUID()}.jpg")

        try {
            val fout = FileOutputStream(imgFile)
            image?.compress(Bitmap.CompressFormat.JPEG, 85, fout)
            fout.flush()
            fout.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imgFile.toUri()
    }

    fun deleteOldImageFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun copyImageToStorage(uri: Uri?): Uri? {
        if (uri != null) {
            var imgFile = fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            imgFile = File(imgFile, "${UUID.randomUUID()}.jpg")
            val imgSource = File(getPath(uri)!!)
            imgSource.copyTo(imgFile)
            return imgFile.toUri()
        }

        return null
    }

    /**
     * Convert Content Uri to File Uri
     */
    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = fragment.requireContext().contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex!!)
        cursor?.close()
        return path
    }

    /**
     * Show dialog with message about denied permissions
     */
    private fun showRationaleDialog(title: String, message: String, launchPermission: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(fragment.requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("ok") { dialog, _ ->
                dialog.dismiss()
                launchPermission()
            }
            .setNegativeButton("cancel") {_, _ ->}

        builder.create().show()
    }

    /**
     * Open camera
     */
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultCameraLauncher.launch(intent)
    }

    /**
     * Open galery
     */
    private fun openGalleryWindow() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultGalleryLauncher.launch(intent)
    }


}