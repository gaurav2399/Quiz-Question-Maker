package com.smartherd.quizkotlinproject.Activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.quizkotlinproject.*
import com.smartherd.quizkotlinproject.Adapters.ExamAdapter
import com.smartherd.quizkotlinproject.Modals.ExamModal
import kotlinx.android.synthetic.main.activity_exam_list.*

class ExamList : AppCompatActivity(){

    var db= FirebaseFirestore.getInstance()
    var query1: Query? = null
    var adapter: ExamAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_list)

        /**** ACTION BAR SETUP *****/
        setSupportActionBar(examListToolbar as Toolbar?)
        val actionBar=supportActionBar
        actionBar?.setTitle("Exam List")

        val viewFAB=findViewById<View>(R.id.chutiyapsa)
        MoveUpward.setFAB(viewFAB)

        val user_id= GeneralUser.user_id


        /******** FIRESTORE HANDLING **********/
        val examsCollection=db.collection("Question Makers").document(user_id)
            .collection("Exams")
        query1=examsCollection
        val options = FirestoreRecyclerOptions.Builder<ExamModal>().setQuery(query1 as CollectionReference)
        { snapshot ->
            snapshot.toObject(ExamModal::class.java)!!.also {
                it.id = snapshot.id
            }
        }.build()

        /*********** SETTING RECYCLER VIEW **************/
        val exmList=findViewById<RecyclerView>(R.id.examList)
        exmList.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        exmList.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapter= ExamAdapter(this, options)
        exmList.adapter=adapter

        Log.e("user_id examlist",user_id)
        addExam.setOnClickListener {
            val intent = Intent(this, AddExam::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exam_list_menu,menu)
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.filter->{
                val v=findViewById<View>(R.id.chutiyapsa)
                Snackbar.make(v,"Working Proper",Snackbar.LENGTH_SHORT).show()
                showToast("Under Progress")
            }
            R.id.search->{
               val intent=Intent(this,SearchExam::class.java)
                startActivity(intent)
            }

            /****************** METHOD TO LOGOUT ****************/

            R.id.logout->{
                val intent=Intent(this,MainActivity::class.java)
                ExamSource.set(1)
                startActivity(intent)
            }
            else->showToast("baba g ka thullu!")
        }
        return true
    }

    override fun onBackPressed() {

        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle("Are you sure to exit?")
        alertDialog.setIcon(R.drawable.alert_warning)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Yes", DialogInterface.OnClickListener{ _, _->
            super.onBackPressed()
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        })
        alertDialog.setNegativeButton("No",DialogInterface.OnClickListener{dialogInterface, _ -> dialogInterface.cancel() })
        alertDialog.show()
    }

    override fun onStart() {
        Log.e("on start examList","reach here")
        super.onStart()
        adapter?.startListening()

    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

}
