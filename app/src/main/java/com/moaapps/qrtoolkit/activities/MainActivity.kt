package com.moaapps.qrtoolkit.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.moaapps.qrtoolkit.BuildConfig
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.ActivityMainBinding
import com.moaapps.qrtoolkit.fragments.HistoryFragment
import com.moaapps.qrtoolkit.fragments.HomeFragment
import java.util.*


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
    private val appUrl = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
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
                binding.navView.setCheckedItem(item)
            }
            R.id.history -> {
                binding.title.text = getString(R.string.history)
                supportFragmentManager.beginTransaction().replace(R.id.container, HistoryFragment::class.java.newInstance()).commit()
                binding.navView.setCheckedItem(item)
            }
            R.id.share -> {

                val shareText = "${getString(R.string.share_msg)}\n$appUrl"
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
            }
            R.id.rate -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(appUrl)))
            }
            R.id.language -> {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.select_language))
                        .setSingleChoiceItems(R.array.languagesList, -1) { dialog, which ->
                            val codes = resources.getStringArray(R.array.languageCode)
                            val langCode = codes[which]
                            dialog.dismiss()
                            setLocale(langCode)


                        }
                        .show()
            }
            R.id.privacy_policy -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.privacypolicies.com/live/67ba21db-ca86-41f1-ad05-bf2a124ee1c6")))
            }
        }

        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setLocale(languageCode: String?) {
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        getSharedPreferences("PRE", MODE_PRIVATE).edit().putString("lng", languageCode).apply()
        recreate()
    }




}