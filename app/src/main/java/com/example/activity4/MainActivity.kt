package com.example.activity4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.personalInfoButton).setOnClickListener {
            startActivity(Intent(this, PersonalInformationActivity::class.java))
        }

        findViewById<Button>(R.id.familyBackgroundButton).setOnClickListener {
            startActivity(Intent(this, FamilyBackgroundActivity::class.java))
        }

        findViewById<Button>(R.id.educationalBackgroundButton).setOnClickListener {
            startActivity(Intent(this, EducationalBackgroundActivity::class.java))
        }

        findViewById<Button>(R.id.viewSavedFormButton).setOnClickListener {
            startActivity(Intent(this, ViewSavedFormActivity::class.java))
        }

        findViewById<Button>(R.id.aboutButton).setOnClickListener {
            Toast.makeText(this, "Personal Data Sheet v1.0", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.resetFormButton).setOnClickListener {
            getSharedPreferences("PDS_DATA", MODE_PRIVATE).edit {
                clear()
            }
            Toast.makeText(this, "Form Data Reset", Toast.LENGTH_SHORT).show()
        }
    }
}
