package com.example.repositoryg_4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

object DataStoreManager
{
    lateinit var dataStore: DataStore<Preferences>
}

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.theme_spinner)
        val options = resources.getStringArray(R.array.theme_options)
        val arrAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        arrAdapter.also { spinner.adapter = it }
        val textInput = findViewById<TextInputEditText>(R.id.txt_input)
        val saveButton = findViewById<Button>(R.id.btn_saveText)
        val goToSecond = findViewById<Button>(R.id.btn_goTo2nd)
        goToSecond.setOnClickListener{
            startActivity(Intent(this, MainActivity2::class.java))
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position)
                {
                    0 -> { // Default
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    1 -> { // Light
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    2 -> { // Dark
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
        }
        spinner.setSelection(0)
        // Save the input value
        saveButton.setOnClickListener {
            lifecycleScope.launch {
                val input = textInput.text.toString()
                DataStoreManager.dataStore.edit { preferences ->
                    preferences[stringPreferencesKey("input_text")] = input
                }
            }
        }
        // Read the value from DataStore
        lifecycleScope.launch {
            DataStoreManager.dataStore.data.collect { preferences ->
                val savedText = preferences[stringPreferencesKey("input_text")] ?: ""
                textInput.setText(savedText)
            }
        }
    }
}