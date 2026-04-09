package com.example.activity4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit

class EducationalBackgroundActivity : AppCompatActivity() {

    private val filename = "educational_background.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educational_background)

        setupButtons()
        loadSavedData()
    }

    private fun setupButtons() {
        findViewById<Toolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }

        findViewById<Button>(R.id.backButton)?.setOnClickListener { finish() }

        findViewById<Button>(R.id.finishButton)?.setOnClickListener {
            if (validateAndSave()) {
                val intent = Intent(this, ViewSavedFormActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateAndSave(): Boolean {
        val elemSchool = findViewById<EditText>(R.id.elemSchool)
        val secSchool  = findViewById<EditText>(R.id.secSchool)

        var isValid = true
        if (elemSchool?.text?.isBlank() == true) { elemSchool.error = "Required"; isValid = false }
        if (secSchool?.text?.isBlank()  == true) { secSchool.error  = "Required"; isValid = false }

        if (isValid) {
            getSharedPreferences("PDS_DATA", MODE_PRIVATE).edit {
                // Elementary
                putString("elemSchool", elemSchool?.text?.toString() ?: "")
                putString("elemCourse", findViewById<EditText>(R.id.elemCourse)?.text?.toString() ?: "")
                putString("elemFrom",   findViewById<EditText>(R.id.elemFrom)?.text?.toString()   ?: "")
                putString("elemTo",     findViewById<EditText>(R.id.elemTo)?.text?.toString()     ?: "")
                putString("elemLevel",  findViewById<EditText>(R.id.elemLevel)?.text?.toString()  ?: "")
                putString("elemGrad",   findViewById<EditText>(R.id.elemGrad)?.text?.toString()   ?: "")
                putString("elemHonors", findViewById<EditText>(R.id.elemHonors)?.text?.toString() ?: "")

                // Secondary
                putString("secSchool",  secSchool?.text?.toString() ?: "")
                putString("secCourse",  findViewById<EditText>(R.id.secCourse)?.text?.toString()  ?: "")
                putString("secFrom",    findViewById<EditText>(R.id.secFrom)?.text?.toString()    ?: "")
                putString("secTo",      findViewById<EditText>(R.id.secTo)?.text?.toString()      ?: "")
                putString("secLevel",   findViewById<EditText>(R.id.secLevel)?.text?.toString()   ?: "")
                putString("secGrad",    findViewById<EditText>(R.id.secGrad)?.text?.toString()    ?: "")
                putString("secHonors",  findViewById<EditText>(R.id.secHonors)?.text?.toString()  ?: "")

                // Vocational
                putString("vocSchool",  findViewById<EditText>(R.id.vocSchool)?.text?.toString()  ?: "")
                putString("vocCourse",  findViewById<EditText>(R.id.vocCourse)?.text?.toString()  ?: "")
                putString("vocFrom",    findViewById<EditText>(R.id.vocFrom)?.text?.toString()    ?: "")
                putString("vocTo",      findViewById<EditText>(R.id.vocTo)?.text?.toString()      ?: "")
                putString("vocLevel",   findViewById<EditText>(R.id.vocLevel)?.text?.toString()   ?: "")
                putString("vocGrad",    findViewById<EditText>(R.id.vocGrad)?.text?.toString()    ?: "")
                putString("vocHonors",  findViewById<EditText>(R.id.vocHonors)?.text?.toString()  ?: "")

                // College
                putString("colSchool",  findViewById<EditText>(R.id.collSchool)?.text?.toString() ?: "")
                putString("colCourse",  findViewById<EditText>(R.id.collCourse)?.text?.toString() ?: "")
                putString("colFrom",    findViewById<EditText>(R.id.collFrom)?.text?.toString()   ?: "")
                putString("colTo",      findViewById<EditText>(R.id.collTo)?.text?.toString()     ?: "")
                putString("colLevel",   findViewById<EditText>(R.id.collLevel)?.text?.toString()  ?: "")
                putString("colGrad",    findViewById<EditText>(R.id.collGrad)?.text?.toString()   ?: "")
                putString("colHonors",  findViewById<EditText>(R.id.collHonors)?.text?.toString() ?: "")

                // Graduate
                putString("gradSchool", findViewById<EditText>(R.id.gradSchool)?.text?.toString() ?: "")
                putString("gradCourse", findViewById<EditText>(R.id.gradCourse)?.text?.toString() ?: "")
                putString("gradFrom",   findViewById<EditText>(R.id.gradFrom)?.text?.toString()   ?: "")
                putString("gradTo",     findViewById<EditText>(R.id.gradTo)?.text?.toString()     ?: "")
                putString("gradLevel",  findViewById<EditText>(R.id.gradLevel)?.text?.toString()  ?: "")
                putString("gradGrad",   findViewById<EditText>(R.id.gradGrad)?.text?.toString()   ?: "")
                putString("gradHonors", findViewById<EditText>(R.id.gradHonors)?.text?.toString() ?: "")
            }

            // Save key fields to internal file storage using the required saveData pattern
            saveData(elemSchool!!, secSchool!!)
        }
        return isValid
    }

    /**
     * Saves name (elementary school) and gender (secondary school) to internal file storage.
     * The comma-separated string is written to [filename] in private app storage.
     */
    fun saveData(name: EditText, gender: EditText) {
        val first_text  = name.text.toString()
        val second_text = gender.text.toString()

        val saveData = first_text + "," + second_text

        openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(saveData.toByteArray())
        }

        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("PDS_DATA", MODE_PRIVATE)
        findViewById<EditText>(R.id.elemSchool)?.setText(prefs.getString("elemSchool", ""))
        findViewById<EditText>(R.id.elemCourse)?.setText(prefs.getString("elemCourse", ""))
        findViewById<EditText>(R.id.elemFrom)?.setText(prefs.getString("elemFrom", ""))
        findViewById<EditText>(R.id.elemTo)?.setText(prefs.getString("elemTo", ""))
        findViewById<EditText>(R.id.elemLevel)?.setText(prefs.getString("elemLevel", ""))
        findViewById<EditText>(R.id.elemGrad)?.setText(prefs.getString("elemGrad", ""))
        findViewById<EditText>(R.id.elemHonors)?.setText(prefs.getString("elemHonors", ""))

        findViewById<EditText>(R.id.secSchool)?.setText(prefs.getString("secSchool", ""))
        findViewById<EditText>(R.id.secCourse)?.setText(prefs.getString("secCourse", ""))
        findViewById<EditText>(R.id.secFrom)?.setText(prefs.getString("secFrom", ""))
        findViewById<EditText>(R.id.secTo)?.setText(prefs.getString("secTo", ""))
        findViewById<EditText>(R.id.secLevel)?.setText(prefs.getString("secLevel", ""))
        findViewById<EditText>(R.id.secGrad)?.setText(prefs.getString("secGrad", ""))
        findViewById<EditText>(R.id.secHonors)?.setText(prefs.getString("secHonors", ""))

        findViewById<EditText>(R.id.collSchool)?.setText(prefs.getString("colSchool", ""))
        findViewById<EditText>(R.id.collCourse)?.setText(prefs.getString("colCourse", ""))
        findViewById<EditText>(R.id.collFrom)?.setText(prefs.getString("colFrom", ""))
        findViewById<EditText>(R.id.collTo)?.setText(prefs.getString("colTo", ""))
        findViewById<EditText>(R.id.collLevel)?.setText(prefs.getString("colLevel", ""))
        findViewById<EditText>(R.id.collGrad)?.setText(prefs.getString("colGrad", ""))
        findViewById<EditText>(R.id.collHonors)?.setText(prefs.getString("colHonors", ""))

        findViewById<EditText>(R.id.vocSchool)?.setText(prefs.getString("vocSchool", ""))
        findViewById<EditText>(R.id.vocCourse)?.setText(prefs.getString("vocCourse", ""))
        findViewById<EditText>(R.id.vocFrom)?.setText(prefs.getString("vocFrom", ""))
        findViewById<EditText>(R.id.vocTo)?.setText(prefs.getString("vocTo", ""))
        findViewById<EditText>(R.id.vocLevel)?.setText(prefs.getString("vocLevel", ""))
        findViewById<EditText>(R.id.vocGrad)?.setText(prefs.getString("vocGrad", ""))
        findViewById<EditText>(R.id.vocHonors)?.setText(prefs.getString("vocHonors", ""))

        findViewById<EditText>(R.id.gradSchool)?.setText(prefs.getString("gradSchool", ""))
        findViewById<EditText>(R.id.gradCourse)?.setText(prefs.getString("gradCourse", ""))
        findViewById<EditText>(R.id.gradFrom)?.setText(prefs.getString("gradFrom", ""))
        findViewById<EditText>(R.id.gradTo)?.setText(prefs.getString("gradTo", ""))
        findViewById<EditText>(R.id.gradLevel)?.setText(prefs.getString("gradLevel", ""))
        findViewById<EditText>(R.id.gradGrad)?.setText(prefs.getString("gradGrad", ""))
        findViewById<EditText>(R.id.gradHonors)?.setText(prefs.getString("gradHonors", ""))
    }
}
