package com.mobiledev.facebooklogin

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.security.MessageDigest
import java.util.*


class MainActivity : AppCompatActivity() {

    private var facebooklogin: Button? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        callbackManager = CallbackManager.Factory.create();
        facebooklogin = findViewById<Button>(R.id.facebook_login)
        facebooklogin!!.setOnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            val accessToken = AccessToken.getCurrentAccessToken()
                            val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
                                println("===================JSON Object" + `object`)
                                //Intializing each parameters avaible in graph API
                                var id = ""
                                var name = ""
                                var email = ""
                                var gender = ""
                                var url = ""
                                try {
                                    if (`object`.has("id")) {
                                        id = `object`.getString("id")
                                    }
                                    if (`object`.has("name")) {
                                        name = `object`.getString("name")
                                    }
                                    if (`object`.has("email")) {
                                        email = `object`.getString("email")
                                    }
                                    if (`object`.has("gender")) {
                                        gender = `object`.getString("gender")
                                    }
                                    if (`object`.has("picture")) {
                                        url = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                                    }
                                    val intent = Intent(this@MainActivity, ProfileActivity::class.java);

                                    var bundle = Bundle()
                                    bundle.putString("name", name)
                                    bundle.putString("email", email)
                                    bundle.putString("gender", gender)
                                    bundle.putString("url", url)
                                    intent.putExtras(bundle)
                                    startActivity(intent);

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            val parameters = Bundle()
                            parameters.putString("fields", "id,name,link,email,picture,gender, birthday")
                            request.parameters = parameters
                            request.executeAsync()
                        }

                        override fun onCancel() {
                            Log.d("MainActivity", "Facebook onCancel.")
                        }

                        override fun onError(error: FacebookException) {
                            Log.d("MainActivity", "Facebook onError.")
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun generateHashKey() {
        val info: PackageInfo
        try {
            info = packageManager.getPackageInfo("com.mobiledev.facebooklogin", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
