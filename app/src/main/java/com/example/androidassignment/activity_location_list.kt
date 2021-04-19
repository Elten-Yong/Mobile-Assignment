package com.example.androidassignment

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_online_banking.*
import java.util.*


class activity_location_list : AppCompatActivity() {

    var lv: ListView? = null
    var customAdapter: CustomAdapter? = null

    val state = arrayOf("Kuala Lumpur", "Selangor", "Johor", "Melaka")

    var listItemArrayList: ArrayList<ListItem>? = null

    val region = arrayOf(
        "Setapak",
        "Puchong",
        "Cheras",
        "Bukit Bintang",
        "Damansara",
        "Klang",
        "Banting",
        "Kajang",
        "Sunagi Tiram",
        "Bandar Johor Bahru",
        "Jelutong",
        "Plentong",
        "Batu Berendam",
        "Tangga Batu",
        "Bukit Katil",
        "Pringgit"
    )

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)


        lv = findViewById(R.id.listView) as ListView

        listItemArrayList = ArrayList()
        populateList()

        customAdapter = CustomAdapter(this, listItemArrayList!!)
        lv!!.setAdapter(customAdapter)


        listView.setOnItemClickListener(){ adapterView, view, position, id ->
            if (position != 0 && (position != 5) && (position != 10) && (position != 15)) { //header position
                //val itemAtPos = adapterView.getItemAtPosition(position)
                //val region = adapterView.getItemAtPosition(position).toString()
                //Toast.makeText(this, " $region is currently offline", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,activity_location_maps::class.java)
                var location = position
                intent.putExtra("key", location)
                startActivity(intent)
            }
        }
    }

    interface ListItem {
        fun isHeader(): Boolean
        fun getName(): kotlin.String
    }

    private fun populateList() {

        var headerdone = 0
        var childdone = 0

        for (i in 0..19) {

            if (i == 0 || (i == 5) || (i == 10) || (i == 15)) { //header position
                val titleModel = HeaderModel()
                titleModel.setheader(state[headerdone])
                listItemArrayList!!.add(titleModel)
                headerdone = headerdone + 1
            } else {
                val childModel = ChildModel()
                childModel.setChild(region[childdone])
                listItemArrayList!!.add(childModel)
                childdone = childdone + 1
            }
        }

    }
}