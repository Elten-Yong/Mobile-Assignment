package com.example.androidassignment

class ChildModel : activity_location_list.ListItem {

    private lateinit var name: String

    override
    fun isHeader () : Boolean {
        return false
    }

    override
    fun getName(): String {
        return name
    }

    fun setChild(child: String) {
        this.name = child
    }
}