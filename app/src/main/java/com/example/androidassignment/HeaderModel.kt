package com.example.androidassignment

class HeaderModel : activity_location_list.ListItem {

    private lateinit var name : String

    fun setheader(header: String) {
        this.name = header
    }

    override
    fun isHeader () : Boolean {
        return true
    }

    override
    fun getName(): String {
        return name
    }
}