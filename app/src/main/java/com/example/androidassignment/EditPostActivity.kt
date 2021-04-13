package com.example.androidassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)
        supportActionBar?.title = "Add Post"
    }
}