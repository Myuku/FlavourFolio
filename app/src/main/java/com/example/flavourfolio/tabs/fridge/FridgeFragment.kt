package com.example.flavourfolio.tabs.fridge

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.IMPORTANT_FOR_AUTOFILL_NO
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flavourfolio.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile


class FridgeFragment : Fragment() {

    companion object {
        fun newInstance() = FridgeFragment()
    }

    private lateinit var viewModel: FridgeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fridge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FridgeViewModel::class.java)

        val csvFileName = "fridge.csv"
        val csvFile = File(requireActivity().getExternalFilesDir(null), csvFileName)
        csvFile.createNewFile()

        val tableLayout: TableLayout = view.findViewById(R.id.fridge_table)

        // read from csv and add table rows
        fun readCsv(inputStream: InputStream) {
            val reader = inputStream.bufferedReader()
            var line = reader.readLine()
            while (!line.isNullOrEmpty()) {
                addTableRow(tableLayout, line)
                line = reader.readLine()
            }
            reader.close()
        }
        readCsv(FileInputStream(csvFile))

        // write to csv when any EditText widget is changed
        val editTextList = ArrayList<EditText>()
        val tableRowList = ArrayList<TableRow>()
        fun updateElementLists() {
            for (i in 0 ..<tableLayout.childCount) {
                val tableRow = tableLayout.getChildAt(i)
                if (tableRow is TableRow) {
                    tableRowList.add(tableRow)
                    for (j in 0..<tableRow.childCount) {
                        val child = tableRow.getChildAt(j)
                        if (child is EditText) {
                            editTextList.add(child)
                        }
                    }
                }
            }

            for (editText in editTextList) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        updateCsv(editTextList, csvFile)
                    }
                })
            }
        }
        updateElementLists()

        val addRowBtn = view.findViewById<Button>(R.id.fridge_add_row_button)
        addRowBtn.setOnClickListener {
            FileOutputStream(csvFile, true).writeCsv("", "", "")
            addTableRow(tableLayout, null)
            editTextList.clear()
            tableRowList.clear()
            updateElementLists()
        }

        val delRowBtn = view.findViewById<Button>(R.id.fridge_del_row_button)
        delRowBtn.setOnClickListener {
            if (tableRowList.size > 1) {
                tableLayout.removeView(tableRowList[tableRowList.size - 1])
                tableRowList.removeLast()
                viewModel.changeColor()

                val f = RandomAccessFile(csvFile, "rw")
                var b: Byte = 0
                var length = f.length() - 1
                while (b != 10.toByte() && length > 0) {
                    length -= 1
                    f.seek(length)
                    b = f.readByte()
                }
                if (length == 0L) {
                    f.setLength(0)
                } else {
                    f.setLength(length + 1)
                }
                f.close()
            } else {
                Toast.makeText(requireActivity(), "No Rows to Delete!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setEditTextAttributes(editText: EditText, hint: String, text: String?) {
        editText.layoutParams = TableRow.LayoutParams(
            0, TableRow.LayoutParams.WRAP_CONTENT, 0f)
        editText.setPadding(5)
        editText.setBackgroundColor(Color.TRANSPARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO
        }
        editText.hint = hint
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        if (text != null) {
            editText.setText(text)
        }
    }

    private fun addTableRow(tableLayout: TableLayout, line: String?) {
        val tableRow = TableRow(requireActivity())
        val editText1 = EditText(requireActivity())
        val borderEnd1 = View(requireActivity())
        val editText2 = EditText(requireActivity())
        val borderEnd2 = View(requireActivity())
        val editText3 = EditText(requireActivity())

        borderEnd1.setBackgroundColor(Color.WHITE)
        borderEnd1.layoutParams = TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT)
        borderEnd2.setBackgroundColor(Color.WHITE)
        borderEnd2.layoutParams = TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT)

        if (!line.isNullOrEmpty()) {
            val data = line.split(',')
            val ingredient = data[0].trim()
            val unit = data[1].trim()
            val quantity = data[2].trim()
            setEditTextAttributes(editText1, "eg. Milk", ingredient)
            setEditTextAttributes(editText2, "eg. Gallons", unit)
            setEditTextAttributes(editText3, "eg. 11", quantity)
        } else {
            setEditTextAttributes(editText1, "eg. Milk", null)
            setEditTextAttributes(editText2, "eg. Gallons", null)
            setEditTextAttributes(editText3, "eg. 11", null)
        }
        if (viewModel.color.value != null) {
            tableRow.setBackgroundResource(viewModel.color.value!!)
            viewModel.changeColor()
        }

        tableRow.addView(editText1)
        tableRow.addView(borderEnd1)
        tableRow.addView(editText2)
        tableRow.addView(borderEnd2)
        tableRow.addView(editText3)
        tableLayout.addView(tableRow)
    }

    private fun OutputStream.writeCsv(ingredient: String, unit: String, quantity: String) {
        val writer = bufferedWriter()
        writer.write("$ingredient, $unit, $quantity")
        writer.newLine()
        writer.flush()
    }

    private fun updateCsv(editTextList: ArrayList<EditText>, csvFile: File) {
        FileOutputStream(csvFile).writeCsv(
            editTextList[0].text.toString(),
            editTextList[1].text.toString(),
            editTextList[2].text.toString()
        )
        for (i in 3 ..<editTextList.size step 3) {
            FileOutputStream(csvFile, true).writeCsv(
                editTextList[i].text.toString(),
                editTextList[i + 1].text.toString(),
                editTextList[i + 2].text.toString()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.color.value == R.color.very_light_pink) {
            viewModel.changeColor()
        }
    }

}