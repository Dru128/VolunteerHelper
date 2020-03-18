package com.arbonik.helper

import com.google.firebase.database.FirebaseDatabase

object FireDatabase {
    var database : FirebaseDatabase
    init {
        database = FirebaseDatabase.getInstance()
    }
}