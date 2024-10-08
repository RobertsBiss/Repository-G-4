package com.example.repositoryg_4

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<Button>(R.id.btn_back)
        val textStorage = findViewById<TextView>(R.id.txt_storage)
        val readTextStorageButton = findViewById<Button>(R.id.btn_readStorage)
        backButton.setOnClickListener {
            finish()
        }

        readTextStorageButton.setOnClickListener {
            lifecycleScope.launch {
                DataStoreManager.dataStore.data.collect { preferences ->
                    val savedText = preferences[stringPreferencesKey("input_text")] ?: ""
                    if (savedText.isNotEmpty())
                    {
                        textStorage.text = savedText
                    }
                    else
                    {
                        Toast.makeText(this@MainActivity2, "No text stored", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}