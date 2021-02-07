package com.moaapps.qrtoolkit.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.moaapps.qrtoolkit.R
import com.moaapps.qrtoolkit.adapters.HistoryAdapter
import com.moaapps.qrtoolkit.databinding.FragmentScannedCodesBinding
import com.moaapps.qrtoolkit.modules.QRCode
import com.moaapps.qrtoolkit.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScannedCodesFragment : Fragment() {

    private lateinit var binding: FragmentScannedCodesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentScannedCodesBinding.inflate(inflater, container, false)
        GlobalScope.launch {
            val historyDao = AppDatabase.getInstance(context!!).historyDao()
            val codes = historyDao.getScannedCodes()
            activity?.runOnUiThread {
                if (codes.isEmpty()){
                    binding.noItems.visibility = View.VISIBLE
                }else{
                    val codesList = ArrayList<QRCode>()
                    codesList.addAll(codes)
                    binding.noItems.visibility = View.GONE
                    binding.rvScanned.layoutManager = LinearLayoutManager(context!!)
                    binding.rvScanned.adapter = HistoryAdapter(activity as Activity, codesList)
                }
            }
        }
        return binding.root
    }

}