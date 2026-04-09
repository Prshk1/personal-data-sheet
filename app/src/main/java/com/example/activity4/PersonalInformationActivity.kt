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

    private val filename = "personal_info.txt"

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
        setupCitizenshipToggle()
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

    private fun setupCitizenshipToggle() {
        val rgCitizenship = findViewById<RadioGroup>(R.id.rgCitizenship) ?: return
        val llDualDetails = findViewById<android.widget.LinearLayout>(R.id.llDualCitizenshipDetails) ?: return
        rgCitizenship.setOnCheckedChangeListener { _, checkedId ->
            llDualDetails.visibility = if (checkedId == R.id.rbDualCitizenship) View.VISIBLE else View.GONE
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
            getSharedPreferences("PDS_DATA", MODE_PRIVATE).edit {
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
                    val hn  = findViewById<EditText>(R.id.etResHouseNo)?.text?.toString() ?: ""
                    val st  = findViewById<EditText>(R.id.etResStreet)?.text?.toString() ?: ""
                    val sub = findViewById<EditText>(R.id.etResSubdivision)?.text?.toString() ?: ""
                    val brgy= findViewById<EditText>(R.id.etResBarangay)?.text?.toString() ?: ""
                    val cty = findViewById<EditText>(R.id.etResCity)?.text?.toString() ?: ""
                    val prv = findViewById<EditText>(R.id.etResProvince)?.text?.toString() ?: ""
                    val zip = findViewById<EditText>(R.id.etResZipCode)?.text?.toString() ?: ""
                    putString("resHouseNo", hn)
                    putString("resStreet", st)
                    putString("resSubdivision", sub)
                    putString("resBarangay", brgy)
                    putString("resCity", cty)
                    putString("resProvince", prv)
                    putString("resAddress", listOf(hn, st, sub, brgy, cty, prv, zip).filter { it.isNotBlank() }.joinToString(", "))
                }

                val permHn  = findViewById<EditText>(R.id.etPermHouseNo)?.text?.toString() ?: ""
                val permSt  = findViewById<EditText>(R.id.etPermStreet)?.text?.toString() ?: ""
                val permSub = findViewById<EditText>(R.id.etPermSubdivision)?.text?.toString() ?: ""
                val permBrgy= findViewById<EditText>(R.id.etPermBarangay)?.text?.toString() ?: ""
                val permCty = findViewById<EditText>(R.id.etPermCity)?.text?.toString() ?: ""
                val permPrv = findViewById<EditText>(R.id.etPermProvince)?.text?.toString() ?: ""
                val permZip = findViewById<EditText>(R.id.etPermZipCode)?.text?.toString() ?: ""
                putString("permHouseNo", permHn)
                putString("permStreet", permSt)
                putString("permSubdivision", permSub)
                putString("permBarangay", permBrgy)
                putString("permCity", permCty)
                putString("permProvince", permPrv)
                putString("permZipCode", permZip)
                putString("permAddress", listOf(permHn, permSt, permSub, permBrgy, permCty, permPrv, permZip).filter { it.isNotBlank() }.joinToString(", "))

                putString("telephone", findViewById<EditText>(R.id.etTelephone)?.text?.toString() ?: "")
                putString("mobile", findViewById<EditText>(R.id.etMobile)?.text?.toString() ?: "")
                putString("email", findViewById<EditText>(R.id.etEmail)?.text?.toString() ?: "")
                putString("zipCode", findViewById<EditText>(R.id.etResZipCode)?.text?.toString() ?: "")
            }

            // Save to local file storage
            saveData(firstName, lastName)
        }
        return isValid
    }

    fun saveData(name: EditText, gender: EditText) {
        val first_text = name.text.toString()
        val second_text = gender.text.toString()

        val saveData = first_text + "," + second_text

        openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(saveData.toByteArray())
        }

        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("PDS_DATA", MODE_PRIVATE)

        // Name
        findViewById<EditText>(R.id.etFirstName).setText(prefs.getString("firstName", ""))
        findViewById<EditText>(R.id.etSurname).setText(prefs.getString("lastName", ""))
        findViewById<EditText>(R.id.etMiddleName).setText(prefs.getString("middleName", ""))
        findViewById<EditText>(R.id.etNameExtension).setText(prefs.getString("nameExtension", ""))

        // Demographics
        findViewById<EditText>(R.id.etBirthDate).setText(prefs.getString("dateOfBirth", ""))
        findViewById<EditText>(R.id.etBirthPlace)?.setText(prefs.getString("placeOfBirth", ""))
        findViewById<EditText>(R.id.etHeight)?.setText(prefs.getString("height", ""))
        findViewById<EditText>(R.id.etWeight)?.setText(prefs.getString("weight", ""))

        // Sex radio
        val savedSex = prefs.getString("sex", "")
        if (!savedSex.isNullOrEmpty()) {
            val rgSex = findViewById<RadioGroup>(R.id.rgSex)
            for (i in 0 until (rgSex?.childCount ?: 0)) {
                val v = rgSex?.getChildAt(i)
                if (v is RadioButton && v.text.toString() == savedSex) { v.isChecked = true; break }
            }
        }

        // Civil status radio (handles nested LinearLayouts)
        val savedCivilStatus = prefs.getString("civilStatus", "")
        if (!savedCivilStatus.isNullOrEmpty()) {
            val rgCivilStatus = findViewById<RadioGroup>(R.id.rgCivilStatus)
            fun checkRadioIn(group: android.view.ViewGroup): Boolean {
                for (i in 0 until group.childCount) {
                    val v = group.getChildAt(i)
                    if (v is RadioButton && v.text.toString() == savedCivilStatus) { v.isChecked = true; return true }
                    if (v is android.view.ViewGroup) { if (checkRadioIn(v)) return true }
                }
                return false
            }
            rgCivilStatus?.let { checkRadioIn(it) }
        }

        // Blood type spinner
        val savedBloodType = prefs.getString("bloodType", "")
        val spinner = findViewById<Spinner>(R.id.bloodTypeSpinner)
        if (spinner != null && !savedBloodType.isNullOrEmpty()) {
            val idx = (spinner.adapter as? ArrayAdapter<*>)?.let { adapter ->
                (0 until adapter.count).firstOrNull { adapter.getItem(it).toString() == savedBloodType }
            } ?: -1
            if (idx >= 0) spinner.setSelection(idx)
        }

        // Citizenship radio
        val savedCitizenship = prefs.getString("citizenship", "")
        if (!savedCitizenship.isNullOrEmpty()) {
            val rgCitizenship = findViewById<RadioGroup>(R.id.rgCitizenship)
            for (i in 0 until (rgCitizenship?.childCount ?: 0)) {
                val v = rgCitizenship?.getChildAt(i)
                if (v is RadioButton && v.text.toString() == savedCitizenship) {
                    v.isChecked = true
                    if (v.id == R.id.rbDualCitizenship) {
                        findViewById<android.widget.LinearLayout>(R.id.llDualCitizenshipDetails)?.visibility = View.VISIBLE
                    }
                    break
                }
            }
        }

        // Dual citizenship details
        val savedDualType = prefs.getString("dualType", "")
        if (!savedDualType.isNullOrEmpty()) {
            val rgDualType = findViewById<RadioGroup>(R.id.rgDualType)
            for (i in 0 until (rgDualType?.childCount ?: 0)) {
                val v = rgDualType?.getChildAt(i)
                if (v is RadioButton && v.text.toString() == savedDualType) { v.isChecked = true; break }
            }
        }
        findViewById<EditText>(R.id.etCountry)?.setText(prefs.getString("dualCountry", ""))

        // Government IDs
        findViewById<EditText>(R.id.etGsis)?.setText(prefs.getString("gsisId", ""))
        findViewById<EditText>(R.id.etPagibig)?.setText(prefs.getString("pagibigId", ""))
        findViewById<EditText>(R.id.etPhilhealth)?.setText(prefs.getString("philhealthId", ""))
        findViewById<EditText>(R.id.etSss)?.setText(prefs.getString("sssId", ""))
        findViewById<EditText>(R.id.etTin)?.setText(prefs.getString("tinId", ""))
        findViewById<EditText>(R.id.etAgencyNo)?.setText(prefs.getString("agencyId", ""))

        // Addresses
        findViewById<EditText>(R.id.etResHouseNo)?.setText(prefs.getString("resHouseNo", ""))
        findViewById<EditText>(R.id.etResStreet)?.setText(prefs.getString("resStreet", ""))
        findViewById<EditText>(R.id.etResSubdivision)?.setText(prefs.getString("resSubdivision", ""))
        findViewById<EditText>(R.id.etResBarangay)?.setText(prefs.getString("resBarangay", ""))
        findViewById<EditText>(R.id.etResCity)?.setText(prefs.getString("resCity", ""))
        findViewById<EditText>(R.id.etResProvince)?.setText(prefs.getString("resProvince", ""))
        findViewById<EditText>(R.id.etResZipCode)?.setText(prefs.getString("zipCode", ""))
        findViewById<EditText>(R.id.etPermHouseNo)?.setText(prefs.getString("permHouseNo", ""))
        findViewById<EditText>(R.id.etPermStreet)?.setText(prefs.getString("permStreet", ""))
        findViewById<EditText>(R.id.etPermSubdivision)?.setText(prefs.getString("permSubdivision", ""))
        findViewById<EditText>(R.id.etPermBarangay)?.setText(prefs.getString("permBarangay", ""))
        findViewById<EditText>(R.id.etPermCity)?.setText(prefs.getString("permCity", ""))
        findViewById<EditText>(R.id.etPermProvince)?.setText(prefs.getString("permProvince", ""))
        findViewById<EditText>(R.id.etPermZipCode)?.setText(prefs.getString("permZipCode", ""))

        // Contact
        findViewById<EditText>(R.id.etTelephone)?.setText(prefs.getString("telephone", ""))
        findViewById<EditText>(R.id.etMobile)?.setText(prefs.getString("mobile", ""))
        findViewById<EditText>(R.id.etEmail)?.setText(prefs.getString("email", ""))
    }
}
