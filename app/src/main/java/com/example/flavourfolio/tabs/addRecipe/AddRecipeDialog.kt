package com.example.flavourfolio.tabs.addRecipe


import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.flavourfolio.R
import com.example.flavourfolio.enums.RecipeType
import com.google.android.material.imageview.ShapeableImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.math.floor


class AddRecipeDialog : AppCompatDialogFragment() {

    private lateinit var galleryResult: ActivityResultLauncher<Intent>
    private var imgBitmap: ByteArray? = null
    private lateinit var imageView: ShapeableImageView
    private lateinit var etRecipeName: EditText
    private lateinit var spnrRecipeType: Spinner
    private lateinit var recipeName: String
    private var recipeType: Int = -1

    companion object {
        const val NAME_KEY = "NewRecipeName"
        const val TYPE_KEY = "NewRecipeType"
        const val IMAGE_KEY = "NewImageType"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_recipe_prompt, null)

        etRecipeName = view.findViewById(R.id.etRecipeName)
        spnrRecipeType = view.findViewById(R.id.spnrRecipeType)
        spnrRecipeType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            RecipeType.values()
        )

        galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data

                val iStream: InputStream = context?.contentResolver?.openInputStream(data?.data!!)!!
                val inputData: ByteArray = getBytes(iStream)!!
                imgBitmap = inputData

                val bm = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(data?.data!!))
                val img = Bitmap.createBitmap(bm,0,0, bm.width,bm.height, Matrix(), true)

                val currentBitmapWidth = img.width
                val currentBitmapHeight = img.height
                val ivWidth = 80

                val newHeight =
                    floor(currentBitmapHeight.toDouble() *
                            (ivWidth.toDouble() / currentBitmapWidth.toDouble())).toInt()

                val scaledImg = Bitmap.createScaledBitmap(img, ivWidth, newHeight, true)

                imageView.setImageBitmap(scaledImg)
            }
        }

        val dialog = builder
            .setView(view)
            .setNeutralButton("Add Image") { _, _ -> }
            .setTitle("Add New Recipe")
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Create") { _, _ ->
                recipeName = etRecipeName.text.toString()
                recipeType = spnrRecipeType.selectedItemPosition

                val intent = Intent(requireContext(), AddRecipeActivity::class.java)
                intent.putExtra(NAME_KEY, recipeName)
                intent.putExtra(TYPE_KEY, recipeType)
                intent.putExtra(IMAGE_KEY, imgBitmap)
                startActivity(intent)
            }
            .create()
        dialog.setOnShowListener { dialog ->
            val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)
            button.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryResult.launch(intent)
                imageView = dialog.findViewById(R.id.add_recipe_image)!!
            }
        }
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
        etRecipeName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = etRecipeName.text.toString().isNotEmpty()
            }
        })
        return dialog
    }
    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }






}
