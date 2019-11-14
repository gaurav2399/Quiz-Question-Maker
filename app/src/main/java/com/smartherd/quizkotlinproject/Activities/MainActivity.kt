package com.smartherd.quizkotlinproject.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.quizkotlinproject.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.style_toast_layout.*
import kotlinx.android.synthetic.main.style_toast_layout.view.*
import java.lang.NullPointerException

open class MainActivity : AppCompatActivity() {



    lateinit var sharedPreferences: SharedPreferences
    var online:Boolean=false
    var user_id:String=""
    lateinit var db:FirebaseFirestore
    lateinit var app:FirebaseApp

    override fun onCreate(savedInstanceState: Bundle?) {
        app = try {
            Log.e("no need of initilaisng","yes")
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            //Firebase not initialized automatically, do it manually
            Log.e("firebasenotinitialising","do manually")
            FirebaseApp.initializeApp(this)!!
        }

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this)
        val editor=sharedPreferences.edit()


        if(ExamSource.source==1) {
            editor.putString("userId", "null")
            editor.putBoolean("online", false)
            editor.apply()
        }

        online=sharedPreferences.getBoolean("online",false)
        user_id=sharedPreferences.getString("userId","null")!!

        super.onCreate(savedInstanceState)

        try {
            db = FirebaseFirestore.getInstance(app)
            Log.e("chl ja bro","please")
        }catch (e:NullPointerException){
            Log.e("teri bhn ka","----")
            FirebaseFirestore.getInstance(app)
        }
        val questionMakersCollection=db.collection("Question Makers")
        Log.e("online",online.toString())
        Log.e("user_id",user_id)
        if(!online)
            setContentView(R.layout.activity_main)
        else{
           val intent= Intent(this,ExamList::class.java)
            GeneralUser.setUserId(user_id)
            startActivity(intent)
        }

        login?.setOnClickListener {
            val username=username.text.toString()
            val password=password.text.toString()
            FirebaseApp.initializeApp(this)
            questionMakersCollection
                .whereEqualTo("username",username).whereEqualTo("password",password).get()
                .addOnSuccessListener { documentSnapshots ->
                    if(documentSnapshots.isEmpty)
                        showToast("some error has occured")
                    else {

                        showToast("Logged in.",Toast.LENGTH_LONG)
                        /************ Style toast ********/
                        /*val layoutInflater = LayoutInflater.from(this)
                        val view = layoutInflater.inflate(R.layout.style_toast_layout,toast_root)
                        view.toast_text.text="Logged in."
                        view.toast_icon.setImageResource(R.drawable.ic_done)
                        val toast=Toast(this)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.view = view
                        toast.show()*/

                        for (documentSnapshot in documentSnapshots){
                            val id=documentSnapshot.id
                            GeneralUser.setUserId(id)
                            editor.putString("userId",id)
                            editor.putBoolean("online",true)
                            editor.apply()
                            Log.e("user_id general user", GeneralUser.user_id)
                            val intent=Intent(this,ExamList::class.java)
                            intent.putExtra("login",true)
                            startActivity(intent)
                        }
                    }
                }.addOnFailureListener{exception ->
                    Log.e("exception",exception.toString())
                    showToast("Error has occured", Toast.LENGTH_LONG)
                }

        }
    }
}
