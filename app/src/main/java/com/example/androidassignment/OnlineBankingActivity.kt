package com.example.androidassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_online_banking.*

class OnlineBankingActivity : AppCompatActivity() {
    val bankName = arrayOf<String>("Maybank","Public Bank","Ambank","CIMB Bank","Citibank","Heong Leong Bank","OCBC Bank","RHB Bank")
    val imageId = arrayOf<Int>(
        R.drawable.maybank_logo,R.drawable.public_bank_logo,
        R.drawable.ambank_logo,R.drawable.cimb_logo,
        R.drawable.citibank_logo,R.drawable.heong_leong_logo,
        R.drawable.ocbc_logo,R.drawable.rhb_logo,
    )
    //lateinit var binding: ActivityOnlineBankingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_banking)

        val myListAdapter = ListAdapterBank(this,bankName,imageId)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(this, " $itemAtPos is currently offline", Toast.LENGTH_SHORT).show()
        }
    }
}