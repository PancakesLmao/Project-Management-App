package com.example.projectmanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanagement.databinding.ActivityMainBinding
import com.example.projectmanagement.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_notification, R.id.nav_projects
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        if (username != null) {
            mainViewModel.setUsername(username)
        }
        if (email != null) {
            mainViewModel.setEmail(email)
        }
        val headerView: View = navView.getHeaderView(0)
        val userNameTextView: TextView = headerView.findViewById(R.id.userNameTextView)
        mainViewModel.username.observe(this) { updatedUsername ->
            userNameTextView.text = updatedUsername
        }
        val userEmailTextView: TextView = headerView.findViewById(R.id.userEmailTextView)
        mainViewModel.email.observe(this) { updatedEmail ->
            userEmailTextView.text = updatedEmail
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout_button -> {
//                    Close navigation drawer
                    drawerLayout.closeDrawers()
//                    Call logout function
                    mainViewModel.logout()
                    true
                }
                else -> false
            }
        }

        mainViewModel.logoutResult.observe(this) { isLoggedOut ->
            if (isLoggedOut) {
                val intent = Intent(this, LoginActivity::class.java)
//                Check report Appendix for more details
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}