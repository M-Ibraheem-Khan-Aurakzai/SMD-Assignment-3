package com.example.myapplication

import android.app.Application

class MyApplication : Application() {

    lateinit var dbHelper: DBHelper
        private set

    lateinit var contactViewModel: ContactViewModel
        private set


    override fun onCreate() {
        super.onCreate()

        dbHelper = DBHelper.getInstance(applicationContext)
        contactViewModel = ContactViewModel(dbHelper)
    }
}