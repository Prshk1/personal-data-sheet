package com.example.activity4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit

class FamilyBackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_background)

        setupButtons()
        loadSavedData()
    }

    private fun setupButtons() {
        findViewById<Toolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        
        // Use safe call for btnBack in case it's not in the layout
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }
        
        findViewById<Button>(R.id.backButton)?.setOnClickListener { finish() }

        findViewById<Button>(R.id.nextButton)?.setOnClickListener {
            if (validateAndSave()) {
                startActivity(Intent(this, EducationalBackgroundActivity::class.java))
            }
        }
    }

    private fun validateAndSave(): Boolean {
        val fatherFirstName = findViewById<EditText>(R.id.fatherFirstName)
        val motherSurname = findViewById<EditText>(R.id.motherSurname)

        var isValid = true
        if (fatherFirstName?.text?.isBlank() == true) { fatherFirstName.error = "Required"; isValid = false }
        if (motherSurname?.text?.isBlank() == true) { motherSurname.error = "Required"; isValid = false }

        if (isValid) {
            getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE).edit {
                // Spouse
                putString("spouseLastName", findViewById<EditText>(R.id.spouseSurname)?.text?.toString() ?: "")
                putString("spouseFirstName", findViewById<EditText>(R.id.spouseFirstName)?.text?.toString() ?: "")
                putString("spouseMiddleName", findViewById<EditText>(R.id.spouseMiddleName)?.text?.toString() ?: "")
                putString("spouseExtension", findViewById<EditText>(R.id.spouseExtension)?.text?.toString() ?: "")
                putString("spouseOccupation", findViewById<EditText>(R.id.spouseOccupation)?.text?.toString() ?: "")
                putString("spouseEmployer", findViewById<EditText>(R.id.spouseEmployer)?.text?.toString() ?: "")
                putString("spouseBusinessAddress", findViewById<EditText>(R.id.spouseBusinessAddress)?.text?.toString() ?: "")
                putString("spouseTelephone", findViewById<EditText>(R.id.spouseTelephone)?.text?.toString() ?: "")

                // Children
                putString("childrenNames", findViewById<EditText>(R.id.childrenNames)?.text?.toString() ?: "")
                putString("childrenBirthdates", findViewById<EditText>(R.id.childrenBirthdates)?.text?.toString() ?: "")

                // Father
                putString("fatherLastName", findViewById<EditText>(R.id.fatherSurname)?.text?.toString() ?: "")
                putString("fatherFirstName", fatherFirstName?.text?.toString() ?: "")
                putString("fatherMiddleName", findViewById<EditText>(R.id.fatherMiddleName)?.text?.toString() ?: "")
                putString("fatherExtension", findViewById<EditText>(R.id.fatherExtension)?.text?.toString() ?: "")

                // Mother
                putString("motherLastName", motherSurname?.text?.toString() ?: "")
                putString("motherFirstName", findViewById<EditText>(R.id.motherFirstName)?.text?.toString() ?: "")
                putString("motherMiddleName", findViewById<EditText>(R.id.motherMiddleName)?.text?.toString() ?: "")
            }
        }
        return isValid
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE)
        findViewById<EditText>(R.id.fatherFirstName)?.setText(prefs.getString("fatherFirstName", ""))
        findViewById<EditText>(R.id.motherSurname)?.setText(prefs.getString("motherLastName", ""))
        
        // Loading other fields as well for better UX
        findViewById<EditText>(R.id.spouseSurname)?.setText(prefs.getString("spouseLastName", ""))
        findViewById<EditText>(R.id.spouseFirstName)?.setText(prefs.getString("spouseFirstName", ""))
        findViewById<EditText>(R.id.spouseMiddleName)?.setText(prefs.getString("spouseMiddleName", ""))
        findViewById<EditText>(R.id.spouseExtension)?.setText(prefs.getString("spouseExtension", ""))
        findViewById<EditText>(R.id.spouseOccupation)?.setText(prefs.getString("spouseOccupation", ""))
        findViewById<EditText>(R.id.spouseEmployer)?.setText(prefs.getString("spouseEmployer", ""))
        findViewById<EditText>(R.id.spouseBusinessAddress)?.setText(prefs.getString("spouseBusinessAddress", ""))
        findViewById<EditText>(R.id.spouseTelephone)?.setText(prefs.getString("spouseTelephone", ""))
        
        findViewById<EditText>(R.id.childrenNames)?.setText(prefs.getString("childrenNames", ""))
        findViewById<EditText>(R.id.childrenBirthdates)?.setText(prefs.getString("childrenBirthdates", ""))
        
        findViewById<EditText>(R.id.fatherSurname)?.setText(prefs.getString("fatherLastName", ""))
        findViewById<EditText>(R.id.fatherMiddleName)?.setText(prefs.getString("fatherMiddleName", ""))
        findViewById<EditText>(R.id.fatherExtension)?.setText(prefs.getString("fatherExtension", ""))
        
        findViewById<EditText>(R.id.motherFirstName)?.setText(prefs.getString("motherFirstName", ""))
        findViewById<EditText>(R.id.motherMiddleName)?.setText(prefs.getString("motherMiddleName", ""))
    }
}
