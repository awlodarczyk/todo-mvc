package io.hindbrain.todo.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.google.android.material.button.MaterialButton
import io.hindbrain.todo.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<MaterialButton>(R.id.button).setOnClickListener {
            onBackPressed()
        }

    }

}