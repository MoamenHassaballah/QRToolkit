package com.moaapps.qrtoolkit.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityMainBinding
import com.moaapps.qrtoolkit.fragments.HistoryFragment
import com.moaapps.qrtoolkit.fragments.HomeFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(starter)
        }
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.drawer_open, R.string.drawer_close)
        binding.drawer.addDrawerListener(toggle)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setCheckedItem(R.id.home)
        binding.navView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).addToBackStack(null).commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                binding.title.text = getString(R.string.home)
                supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
            }
            R.id.history -> {
                binding.title.text = getString(R.string.history)
                supportFragmentManager.beginTransaction().replace(R.id.container, HistoryFragment::class.java.newInstance()).commit()
            }
        }
        binding.navView.setCheckedItem(item)
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

}