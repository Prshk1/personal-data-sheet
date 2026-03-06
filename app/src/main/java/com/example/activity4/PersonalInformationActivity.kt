package com.example.activity4

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class PersonalInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personal_information)
        
        findViewById<ConstraintLayout>(R.id.personal_info_root)?.let { v ->
            ViewCompat.setOnApplyWindowInsetsListener(v) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        setupSpinners()
        setupDatePicker()
        setupButtons()
        setupAddressCheckbox()
        loadSavedData()
    }

    private fun setupSpinners() {
        val bloodTypeSpinner = findViewById<Spinner>(R.id.bloodTypeSpinner)
        if (bloodTypeSpinner != null) {
            // Using a custom adapter to ensure text color is visible
            val options = resources.getStringArray(R.array.blood_type_options)
            val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getView(position, convertView, parent)
                    (v as? TextView)?.setTextColor(ContextCompat.getColor(context, R.color.black))
                    return v
                }
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val v = super.getDropDownView(position, convertView, parent)
                    (v as? TextView)?.setTextColor(ContextCompat.getColor(context, R.color.black))
                    return v
                }
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bloodTypeSpinner.adapter = adapter
        }
    }

    private fun setupDatePicker() {
        val etDOB = findViewById<EditText>(R.id.etBirthDate)
        etDOB.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                etDOB.setText(String.format(Locale.US, "%02d/%02d/%d", month + 1, day, year))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupButtons() {
        // Toolbar back button
        findViewById<Toolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        
        // Alternative back button if it exists as ImageView
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (validateAndSave()) {
                startActivity(Intent(this, FamilyBackgroundActivity::class.java))
            }
        }

        findViewById<Button>(R.id.btnBackToMenu)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun setupAddressCheckbox() {
        val cbSameAddress = findViewById<CheckBox>(R.id.cbSameAddress)
        cbSameAddress?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                findViewById<EditText>(R.id.etPermHouseNo)?.setText(findViewById<EditText>(R.id.etResHouseNo)?.text)
                findViewById<EditText>(R.id.etPermStreet)?.setText(findViewById<EditText>(R.id.etResStreet)?.text)
                findViewById<EditText>(R.id.etPermSubdivision)?.setText(findViewById<EditText>(R.id.etResSubdivision)?.text)
                findViewById<EditText>(R.id.etPermBarangay)?.setText(findViewById<EditText>(R.id.etResBarangay)?.text)
                findViewById<EditText>(R.id.etPermCity)?.setText(findViewById<EditText>(R.id.etResCity)?.text)
                findViewById<EditText>(R.id.etPermProvince)?.setText(findViewById<EditText>(R.id.etResProvince)?.text)
                findViewById<EditText>(R.id.etPermZipCode)?.setText(findViewById<EditText>(R.id.etResZipCode)?.text)
            }
        }
    }

    private fun validateAndSave(): Boolean {
        val firstName = findViewById<EditText>(R.id.etFirstName)
        val lastName = findViewById<EditText>(R.id.etSurname)
        val dob = findViewById<EditText>(R.id.etBirthDate)

        var isValid = true
        if (firstName.text.isBlank()) { firstName.error = "Required"; isValid = false }
        if (lastName.text.isBlank()) { lastName.error = "Required"; isValid = false }
        if (dob.text.isBlank()) { dob.error = "Required"; isValid = false }

        if (isValid) {
            getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE).edit {
                putString("firstName", firstName.text.toString())
                putString("lastName", lastName.text.toString())
                putString("middleName", findViewById<EditText>(R.id.etMiddleName).text.toString())
                putString("nameExtension", findViewById<EditText>(R.id.etNameExtension).text.toString())
                putString("dateOfBirth", dob.text.toString())
                putString("placeOfBirth", findViewById<EditText>(R.id.etBirthPlace).text.toString())
                
                val rgSex = findViewById<RadioGroup>(R.id.rgSex)
                val sexId = rgSex?.checkedRadioButtonId ?: -1
                putString("sex", if (sexId != -1) findViewById<RadioButton>(sexId).text.toString() else "")
                
                val rgCivilStatus = findViewById<RadioGroup>(R.id.rgCivilStatus)
                val civilStatusId = rgCivilStatus?.checkedRadioButtonId ?: -1
                putString("civilStatus", if (civilStatusId != -1) findViewById<RadioButton>(civilStatusId).text.toString() else "")

                putString("height", findViewById<EditText>(R.id.etHeight)?.text?.toString() ?: "")
                putString("weight", findViewById<EditText>(R.id.etWeight)?.text?.toString() ?: "")
                putString("bloodType", findViewById<Spinner>(R.id.bloodTypeSpinner)?.selectedItem?.toString() ?: "")
                
                val rgCitizenship = findViewById<RadioGroup>(R.id.rgCitizenship)
                val citizenshipId = rgCitizenship?.checkedRadioButtonId ?: -1
                putString("citizenship", if (citizenshipId != -1) findViewById<RadioButton>(citizenshipId).text.toString() else "")
                
                val rgDualType = findViewById<RadioGroup>(R.id.rgDualType)
                val dualId = rgDualType?.checkedRadioButtonId ?: -1
                putString("dualType", if (dualId != -1) findViewById<RadioButton>(dualId).text.toString() else "")
                putString("dualCountry", findViewById<EditText>(R.id.etCountry)?.text?.toString() ?: "")

                putString("gsisId", findViewById<EditText>(R.id.etGsis)?.text?.toString() ?: "")
                putString("pagibigId", findViewById<EditText>(R.id.etPagibig)?.text?.toString() ?: "")
                putString("philhealthId", findViewById<EditText>(R.id.etPhilhealth)?.text?.toString() ?: "")
                putString("sssId", findViewById<EditText>(R.id.etSss)?.text?.toString() ?: "")
                putString("tinId", findViewById<EditText>(R.id.etTin)?.text?.toString() ?: "")
                putString("agencyId", findViewById<EditText>(R.id.etAgencyNo)?.text?.toString() ?: "")

                val resAddressField = findViewById<EditText>(R.id.etResAddress)
                if (resAddressField != null) {
                    putString("resAddress", resAddressField.text.toString())
                } else {
                    val resAddress = "${findViewById<EditText>(R.id.etResHouseNo)?.text}, ${findViewById<EditText>(R.id.etResStreet)?.text}, ${findViewById<EditText>(R.id.etResSubdivision)?.text}, ${findViewById<EditText>(R.id.etResBarangay)?.text}, ${findViewById<EditText>(R.id.etResCity)?.text}, ${findViewById<EditText>(R.id.etResProvince)?.text}, ${findViewById<EditText>(R.id.etResZipCode)?.text}"
                    putString("resAddress", resAddress)
                }
                
                val permAddress = "${findViewById<EditText>(R.id.etPermHouseNo)?.text}, ${findViewById<EditText>(R.id.etPermStreet)?.text}, ${findViewById<EditText>(R.id.etPermSubdivision)?.text}, ${findViewById<EditText>(R.id.etPermBarangay)?.text}, ${findViewById<EditText>(R.id.etPermCity)?.text}, ${findViewById<EditText>(R.id.etPermProvince)?.text}, ${findViewById<EditText>(R.id.etPermZipCode)?.text}"
                putString("permAddress", permAddress)

                putString("telephone", findViewById<EditText>(R.id.etTelephone)?.text?.toString() ?: "")
                putString("mobile", findViewById<EditText>(R.id.etMobile)?.text?.toString() ?: "")
                putString("email", findViewById<EditText>(R.id.etEmail)?.text?.toString() ?: "")
                putString("zipCode", findViewById<EditText>(R.id.etResZipCode)?.text?.toString() ?: "")
            }
        }
        return isValid
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("PDS_DATA", Context.MODE_PRIVATE)
        findViewById<EditText>(R.id.etFirstName).setText(prefs.getString("firstName", ""))
        findViewById<EditText>(R.id.etSurname).setText(prefs.getString("lastName", ""))
        findViewById<EditText>(R.id.etBirthDate).setText(prefs.getString("dateOfBirth", ""))
        findViewById<EditText>(R.id.etResAddress)?.setText(prefs.getString("resAddress", ""))
        findViewById<EditText>(R.id.etResZipCode)?.setText(prefs.getString("zipCode", ""))
        
        // Load RadioGroup selection for civil status
        val savedCivilStatus = prefs.getString("civilStatus", "")
        if (savedCivilStatus != null && savedCivilStatus.isNotEmpty()) {
            val rgCivilStatus = findViewById<RadioGroup>(R.id.rgCivilStatus)
            for (i in 0 until rgCivilStatus.childCount) {
                val view = rgCivilStatus.getChildAt(i)
                if (view is RadioButton && view.text.toString() == savedCivilStatus) {
                    view.isChecked = true
                    break
                } else if (view is LinearLayout) {
                    // Check nested radio buttons in LinearLayouts
                    for (j in 0 until view.childCount) {
                        val innerView = view.getChildAt(j)
                        if (innerView is RadioButton && innerView.text.toString() == savedCivilStatus) {
                            innerView.isChecked = true
                            break
                        }
                    }
                }
            }
        }
    }
}
