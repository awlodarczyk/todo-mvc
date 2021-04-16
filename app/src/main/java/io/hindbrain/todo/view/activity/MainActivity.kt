package io.hindbrain.todo.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import io.hindbrain.todo.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        this.navController = findNavController(
            this,
            R.id.nav_host_fragment
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_items -> {
                if (navController.currentDestination?.id != R.id.ItemsFragment)
                    navController.navigate(R.id.action_to_ItemsFragment)
                return true
            }
            R.id.action_archived -> {
                if (navController.currentDestination?.id != R.id.ArchivedItemsFragment)
                    navController.navigate(R.id.action_to_ArchivedItemsFragment)
                return true
            }
            R.id.action_activity -> {
                val intent = Intent(this,SecondActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}