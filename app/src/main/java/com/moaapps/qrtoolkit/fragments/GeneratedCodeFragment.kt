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
import com.moaapps.qrtoolkit.databinding.FragmentGeneratedCodeBinding
import com.moaapps.qrtoolkit.modules.QRCode
import com.moaapps.qrtoolkit.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GeneratedCodeFragment : Fragment() {

    private lateinit var binding: FragmentGeneratedCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneratedCodeBinding.inflate(inflater, container, false)

        GlobalScope.launch {
            val historyDao = AppDatabase.getInstance(context!!).historyDao()
            val codes = historyDao.getGeneratedCodes()
            activity?.runOnUiThread {
                if (codes.isEmpty()){
                    binding.noItems.visibility = View.VISIBLE
                }else{
                    val codesList = ArrayList<QRCode>()
                    codesList.addAll(codes)
                    binding.noItems.visibility = View.GONE
                    binding.rvGenerated.layoutManager = LinearLayoutManager(context!!)
                    binding.rvGenerated.adapter = HistoryAdapter(activity as Activity, codesList)
                }
            }
        }

        return binding.root
    }

}