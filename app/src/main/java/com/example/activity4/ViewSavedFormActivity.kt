package com.example.activity4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class ViewSavedFormActivity : AppCompatActivity() {

    private val filenamePersonal     = "personal_info.txt"
    private val filenameFamily       = "family_background.txt"
    private val filenameEducational  = "educational_background.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_saved_form)

        setupButtons()
        displaySavedData()
        displayFileSummaries()
    }

    private fun setupButtons() {
        findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

        findViewById<Button>(R.id.btnMainMenu).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnEditForm).setOnClickListener {
            startActivity(Intent(this, PersonalInformationActivity::class.java))
        }
    }

    /** Reads a saved file and returns the raw content, or null if not found. */
    private fun readFile(name: String): String? = try {
        openFileInput(name).use { fis ->
            BufferedReader(InputStreamReader(fis)).readText()
        }
    } catch (_: Exception) { null }

    /**
     * Reads all 3 section files saved by each activity's saveData() and
     * updates the corresponding summary banners in the view.
     */
    private fun displayFileSummaries() {
        // Personal Information banner
        val tvPersonal = findViewById<TextView>(R.id.viewFileSummaryPersonal)
        val personalContent = readFile(filenamePersonal)
        if (tvPersonal != null) {
            if (personalContent != null) {
                val parts     = personalContent.split(",")
                val firstName = parts.getOrElse(0) { "" }.trim()
                val lastName  = parts.getOrElse(1) { "" }.trim()
                tvPersonal.text = getString(R.string.view_file_personal_format, firstName, lastName)
            } else {
                tvPersonal.text = getString(R.string.view_file_not_saved)
            }
        }

        // Family Background banner
        val tvFamily = findViewById<TextView>(R.id.viewFileSummaryFamily)
        val familyContent = readFile(filenameFamily)
        if (tvFamily != null) {
            if (familyContent != null) {
                val parts           = familyContent.split(",")
                val fatherFirstName = parts.getOrElse(0) { "" }.trim()
                val motherSurname   = parts.getOrElse(1) { "" }.trim()
                tvFamily.text = getString(R.string.view_file_family_format, fatherFirstName, motherSurname)
            } else {
                tvFamily.text = getString(R.string.view_file_not_saved)
            }
        }

        // Educational Background banner
        val tvEducational = findViewById<TextView>(R.id.viewFileSummaryEducational)
        val educationalContent = readFile(filenameEducational)
        if (tvEducational != null) {
            if (educationalContent != null) {
                val parts      = educationalContent.split(",")
                val elemSchool = parts.getOrElse(0) { "" }.trim()
                val secSchool  = parts.getOrElse(1) { "" }.trim()
                tvEducational.text = getString(R.string.view_file_educational_format, elemSchool, secSchool)
            } else {
                tvEducational.text = getString(R.string.view_file_not_saved)
            }
        }
    }

    private fun displaySavedData() {
        val prefs = getSharedPreferences("PDS_DATA", MODE_PRIVATE)
        val notAvailable = getString(R.string.view_not_available)

        // I. Personal Information
        val firstName  = prefs.getString("firstName",      "") ?: ""
        val middleName = prefs.getString("middleName",     "") ?: ""
        val lastName   = prefs.getString("lastName",       "") ?: ""
        val nameExt    = prefs.getString("nameExtension",  "") ?: ""
        val fullName   = "$firstName $middleName $lastName $nameExt".trim()
        findViewById<TextView>(R.id.viewFullName).text = fullName.ifEmpty { notAvailable }

        findViewById<TextView>(R.id.viewDOB).text = prefs.getString("dateOfBirth",  notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewPOB).text = prefs.getString("placeOfBirth", notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewSex).text = prefs.getString("sex",          notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewCivilStatus).text = prefs.getString("civilStatus", notAvailable) ?: notAvailable

        val citizenship = prefs.getString("citizenship", getString(R.string.filipino)) ?: getString(R.string.filipino)
        findViewById<TextView>(R.id.viewCitizenship).text = citizenship
        if (citizenship == getString(R.string.dual_citizenship)) {
            val type    = prefs.getString("dualType",    "") ?: ""
            val country = prefs.getString("dualCountry", "") ?: ""
            findViewById<TextView>(R.id.viewDualCitizenshipDetails).text = getString(R.string.view_dual_citizenship_format, type, country)
        } else {
            findViewById<TextView>(R.id.viewDualCitizenshipDetails).text = notAvailable
        }

        val height = prefs.getString("height", "0") ?: "0"
        val weight = prefs.getString("weight", "0") ?: "0"
        findViewById<TextView>(R.id.viewPhysique).text   = getString(R.string.view_physique_format, height, weight)
        findViewById<TextView>(R.id.viewBloodType).text  = prefs.getString("bloodType", notAvailable) ?: notAvailable

        // Government IDs
        findViewById<TextView>(R.id.viewGSIS).text       = getString(R.string.view_gsis_format,       prefs.getString("gsisId",       notAvailable))
        findViewById<TextView>(R.id.viewPAGIBIG).text    = getString(R.string.view_pagibig_format,     prefs.getString("pagibigId",    notAvailable))
        findViewById<TextView>(R.id.viewPhilHealth).text = getString(R.string.view_philhealth_format,  prefs.getString("philhealthId", notAvailable))
        findViewById<TextView>(R.id.viewSSS).text        = getString(R.string.view_sss_format,         prefs.getString("sssId",        notAvailable))
        findViewById<TextView>(R.id.viewTIN).text        = getString(R.string.view_tin_format,         prefs.getString("tinId",        notAvailable))
        findViewById<TextView>(R.id.viewAgencyID).text   = getString(R.string.view_agency_format,      prefs.getString("agencyId",     notAvailable))

        // Addresses
        findViewById<TextView>(R.id.viewResAddress).text  = prefs.getString("resAddress",  notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewPermAddress).text = prefs.getString("permAddress", notAvailable) ?: notAvailable

        // Contact
        findViewById<TextView>(R.id.viewTelephone).text = getString(R.string.view_telephone_format, prefs.getString("telephone", notAvailable))
        findViewById<TextView>(R.id.viewMobile).text    = getString(R.string.view_mobile_format,    prefs.getString("mobile",    notAvailable))
        findViewById<TextView>(R.id.viewEmail).text     = getString(R.string.view_email_format,     prefs.getString("email",     notAvailable))

        // II. Family Background
        val spouseFirstName  = prefs.getString("spouseFirstName",  "") ?: ""
        val spouseMiddleName = prefs.getString("spouseMiddleName", "") ?: ""
        val spouseLastName   = prefs.getString("spouseLastName",   "") ?: ""
        val spouseExt        = prefs.getString("spouseExtension",  "") ?: ""
        val spouseName = "$spouseFirstName $spouseMiddleName $spouseLastName $spouseExt".trim()
        findViewById<TextView>(R.id.viewSpouseName).text = spouseName.ifEmpty { notAvailable }

        val spouseOccupation = prefs.getString("spouseOccupation",      notAvailable) ?: notAvailable
        val spouseEmployer   = prefs.getString("spouseEmployer",        notAvailable) ?: notAvailable
        val spouseAddress    = prefs.getString("spouseBusinessAddress",  notAvailable) ?: notAvailable
        val spousePhone      = prefs.getString("spouseTelephone",        notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewSpouseWork).text    = getString(R.string.view_combined_format, spouseOccupation, spouseEmployer)
        findViewById<TextView>(R.id.viewSpouseContact).text = getString(R.string.view_combined_format, spouseAddress, spousePhone)

        val fFirstName  = prefs.getString("fatherFirstName",  "") ?: ""
        val fMiddleName = prefs.getString("fatherMiddleName", "") ?: ""
        val fLastName   = prefs.getString("fatherLastName",   "") ?: ""
        val fExt        = prefs.getString("fatherExtension",  "") ?: ""
        findViewById<TextView>(R.id.viewFatherName).text = "$fFirstName $fMiddleName $fLastName $fExt".trim()

        val mFirstName  = prefs.getString("motherFirstName",  "") ?: ""
        val mMiddleName = prefs.getString("motherMiddleName", "") ?: ""
        val mLastName   = prefs.getString("motherLastName",   "") ?: ""
        findViewById<TextView>(R.id.viewMotherName).text = "$mFirstName $mMiddleName $mLastName".trim()

        val children           = prefs.getString("childrenNames",      notAvailable) ?: notAvailable
        val childrenBirthdates = prefs.getString("childrenBirthdates", "")           ?: ""
        findViewById<TextView>(R.id.viewChildrenList).text = if (childrenBirthdates.isNotEmpty()) {
            getString(R.string.view_children_format, children, childrenBirthdates)
        } else { children }

        // III. Educational Background
        val elemSchool = prefs.getString("elemSchool", notAvailable) ?: notAvailable
        val elemFrom   = prefs.getString("elemFrom",   "") ?: ""
        val elemTo     = prefs.getString("elemTo",     "") ?: ""
        val elemGrad   = prefs.getString("elemGrad",   "") ?: ""
        findViewById<TextView>(R.id.viewElemDetails).text = getString(R.string.view_edu_details_format, elemSchool, elemFrom, elemTo, elemGrad)

        val secSchool = prefs.getString("secSchool", notAvailable) ?: notAvailable
        val secFrom   = prefs.getString("secFrom",   "") ?: ""
        val secTo     = prefs.getString("secTo",     "") ?: ""
        val secGrad   = prefs.getString("secGrad",   "") ?: ""
        findViewById<TextView>(R.id.viewSecDetails).text = getString(R.string.view_edu_details_format, secSchool, secFrom, secTo, secGrad)

        val vocSchool = prefs.getString("vocSchool", notAvailable) ?: notAvailable
        val vocCourse = prefs.getString("vocCourse", "") ?: ""
        val vocFrom   = prefs.getString("vocFrom",   "") ?: ""
        val vocTo     = prefs.getString("vocTo",     "") ?: ""
        findViewById<TextView>(R.id.viewVocDetails).text = getString(R.string.view_edu_course_period_format, vocSchool, vocCourse, vocFrom, vocTo)

        val colSchool = prefs.getString("colSchool", notAvailable) ?: notAvailable
        val colCourse = prefs.getString("colCourse", "") ?: ""
        val colFrom   = prefs.getString("colFrom",   "") ?: ""
        val colTo     = prefs.getString("colTo",     "") ?: ""
        val colHonors = prefs.getString("colHonors", "") ?: ""
        findViewById<TextView>(R.id.viewColDetails).text = getString(R.string.view_edu_full_format, colSchool, colCourse, colFrom, colTo, colHonors)

        val gradSchool = prefs.getString("gradSchool", notAvailable) ?: notAvailable
        val gradCourse = prefs.getString("gradCourse", "") ?: ""
        findViewById<TextView>(R.id.viewGradDetails).text = getString(R.string.view_combined_format, gradSchool, gradCourse)

        // Verification
        findViewById<TextView>(R.id.viewSignature).text = prefs.getString("signature", notAvailable) ?: notAvailable
        findViewById<TextView>(R.id.viewDate).text      = prefs.getString("date",      notAvailable) ?: notAvailable
    }
}
