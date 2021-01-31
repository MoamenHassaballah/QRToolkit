package com.moaapps.qrtoolkit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var mood ="scanned"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.swScanned.setOnClickListener { switchToScanned() }
        binding.swGenerated.setOnClickListener { switchToGenerated() }
        childFragmentManager.beginTransaction().replace(R.id.container, ScannedCodesFragment()).commit()
        return binding.root
    }

    private fun switchToScanned(){
        if (mood != "scanned"){
            binding.switcher.animate().x(0f).setDuration(300).start()
            binding.swScanned.setTextColor(ContextCompat.getColor(context!!,R.color.white))
            binding.swGenerated.setTextColor(ContextCompat.getColor(context!!,R.color.black))
            childFragmentManager.beginTransaction().replace(R.id.container, ScannedCodesFragment()).commit()
            mood = "scanned"
        }
    }


    private fun switchToGenerated(){
        if (mood != "generated"){
            binding.switcher.animate().x(binding.switcher.layoutParams.width.toFloat()).setDuration(300).start()
            binding.swScanned.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            binding.swGenerated.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            childFragmentManager.beginTransaction().replace(R.id.container, GeneratedCodeFragment()).commit()
            mood = "generated"
        }
    }

}