package com.moaapps.qrtoolkit.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.moaapps.qrtoolkit.activities.GenerateQrActivity
import com.moaapps.qrtoolkit.activities.ResultActivity
import com.moaapps.qrtoolkit.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    companion object{
        private const val TAG = "HomeFragment"
    }
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.scanCode.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Scan a barcode")
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }

        binding.generateCode.setOnClickListener { GenerateQrActivity.start(context!!) }
        return binding.root;
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if(result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onActivityResult: ${result.contents}, ${result.formatName}")
                ResultActivity.start(context!!, result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}