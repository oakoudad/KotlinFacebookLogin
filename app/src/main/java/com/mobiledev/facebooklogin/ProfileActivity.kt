package com.mobiledev.facebooklogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Manu on 11/5/2017.
 */
class ProfileActivity: AppCompatActivity() {

    private var nameTV: TextView? = null
    private var emailTV: TextView? = null
    private var genderTV: TextView? = null
    private var profileIV: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTV = findViewById<TextView>(R.id.nameTV)
        emailTV = findViewById<TextView>(R.id.emailTV)
        genderTV = findViewById<TextView>(R.id.genderTV)
        profileIV = findViewById<ImageView>(R.id.imageViewProfile)

        var name : String = intent.getStringExtra("name");
        var email : String = intent.getStringExtra("email");
        var gender : String = intent.getStringExtra("gender");
        var url : String = intent.getStringExtra("url");
        //Log.e("Prefile", name + " "+ email+"  "+ gender+" "+ url)

        nameTV!!.text = name
        emailTV!!.text = email
        genderTV!!.text = gender
        profileIV?.loadUrl(url)


    }
    fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }
}