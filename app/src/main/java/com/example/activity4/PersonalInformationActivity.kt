package com.example.activity4

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import java.util.*

class PersonalInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        setupDatePicker()
        setupButtons()
        loadSavedData()
    }

    private fun setupDatePicker() {
        val etDOB = findViewById<EditText>(R.id.dateOfBirth)
        etDOB.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                etDOB.setText(String.format(Locale.US, "%02d/%02d/%d", month + 1, day, year))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupButtons() {
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.backToMenu).setOnClickListener { finish() }

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            if (validateAndSave()) {
                startActivity(Intent(this, FamilyBackgroundActivity::class.java))
            }
        }
    }

    private fun validateAndSave(): Boolean {
        val firstName = findViewById<EditText>(R.id.firstName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val dob = findViewById<EditText>(R.id.dateOfBirth)

        var isValid = true
        if (firstName.text.isBlank()) { firstName.error = "Required"; isValid = false }
        if (lastName.text.isBlank()) { lastName.error = "Required"; isValid = false }
        if (dob.text.isBlank()) { dob.error = "Required"; isValid = false }

        if (isValid) {
            getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE).edit {
                putString("firstName", firstName.text.toString())
                putString("lastName", lastName.text.toString())
                putString("middleName", findViewById<EditText>(R.id.middleName).text.toString())
                putString("nameExtension", findViewById<Spinner>(R.id.nameExtensionSpinner).selectedItem.toString())
                putString("dateOfBirth", dob.text.toString())
                putString("placeOfBirth", findViewById<EditText>(R.id.placeOfBirth).text.toString())
                
                val sexId = findViewById<RadioGroup>(R.id.sexGroup).checkedRadioButtonId
                putString("sex", if (sexId != -1) findViewById<RadioButton>(sexId).text.toString() else "")
                
                putString("civilStatus", findViewById<Spinner>(R.id.civilStatusSpinner).selectedItem.toString())
                putString("height", findViewById<EditText>(R.id.height).text.toString())
                putString("weight", findViewById<EditText>(R.id.weight).text.toString())
                putString("bloodType", findViewById<Spinner>(R.id.bloodTypeSpinner).selectedItem.toString())
                
                val citizenshipId = findViewById<RadioGroup>(R.id.citizenshipGroup).checkedRadioButtonId
                putString("citizenship", if (citizenshipId != -1) findViewById<RadioButton>(citizenshipId).text.toString() else "")
                
                val dualId = findViewById<RadioGroup>(R.id.dualTypeGroup).checkedRadioButtonId
                putString("dualType", if (dualId != -1) findViewById<RadioButton>(dualId).text.toString() else "")
                putString("dualCountry", findViewById<EditText>(R.id.dualCountry).text.toString())

                putString("gsisId", findViewById<EditText>(R.id.gsisId).text.toString())
                putString("pagibigId", findViewById<EditText>(R.id.pagibigId).text.toString())
                putString("philhealthId", findViewById<EditText>(R.id.philhealthId).text.toString())
                putString("sssId", findViewById<EditText>(R.id.sssId).text.toString())
                putString("tinId", findViewById<EditText>(R.id.tinId).text.toString())
                putString("agencyId", findViewById<EditText>(R.id.agencyEmployeeId).text.toString())

                val resAddress = "${findViewById<EditText>(R.id.resHouseNo).text}, ${findViewById<EditText>(R.id.resStreet).text}, ${findViewById<EditText>(R.id.resSubdivision).text}, ${findViewById<EditText>(R.id.resBarangay).text}, ${findViewById<EditText>(R.id.resCity).text}, ${findViewById<EditText>(R.id.resProvince).text}, ${findViewById<EditText>(R.id.resZipCode).text}"
                putString("resAddress", resAddress)
                
                val permAddress = "${findViewById<EditText>(R.id.permHouseNo).text}, ${findViewById<EditText>(R.id.permStreet).text}, ${findViewById<EditText>(R.id.permSubdivision).text}, ${findViewById<EditText>(R.id.permBarangay).text}, ${findViewById<EditText>(R.id.permCity).text}, ${findViewById<EditText>(R.id.permProvince).text}, ${findViewById<EditText>(R.id.permZipCode).text}"
                putString("permAddress", permAddress)

                putString("telephone", findViewById<EditText>(R.id.telephone).text.toString())
                putString("mobile", findViewById<EditText>(R.id.mobile).text.toString())
                putString("email", findViewById<EditText>(R.id.email).text.toString())
            }
        }
        return isValid
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE)
        findViewById<EditText>(R.id.firstName).setText(prefs.getString("firstName", ""))
        findViewById<EditText>(R.id.lastName).setText(prefs.getString("lastName", ""))
        findViewById<EditText>(R.id.dateOfBirth).setText(prefs.getString("dateOfBirth", ""))
    }
}
