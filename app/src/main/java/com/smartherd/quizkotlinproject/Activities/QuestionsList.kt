package com.smartherd.quizkotlinproject.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.smartherd.quizkotlinproject.Adapters.QuestionAdapter
import com.smartherd.quizkotlinproject.GeneralUser
import com.smartherd.quizkotlinproject.Modals.QuestionModal
import com.smartherd.quizkotlinproject.R
import com.smartherd.quizkotlinproject.showToast
import kotlinx.android.synthetic.main.activity_questions_list.*

class QuestionsList : AppCompatActivity() {

    var db= FirebaseFirestore.getInstance()
    var query1: Query? = null
    lateinit var id:String
    lateinit var exam_id:String
    var adapter: QuestionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_list)
        setSupportActionBar(questionListToolbar as Toolbar?)
        val actionBar=supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Log.e("enter in question","list yes..")
        exam_id=intent.getStringExtra("examId")
        id= GeneralUser.user_id
        Log.e("user id questionlist",id)
        Log.e("exam id questionlist",exam_id)
        actionBar?.title = "Question List"

        db.collection("Question Makers").document(id)
            .collection("Exams").document(exam_id).get().addOnSuccessListener {
                totalQuestions.text = it.get("totalQuestion").toString()
            }

        val questionMakersCollection=db.collection("Question Makers").document(id)
            .collection("Exams").document(exam_id).collection("Questions")
        query1=questionMakersCollection
        val qstnList=findViewById<RecyclerView>(R.id.questionList)
        val options = FirestoreRecyclerOptions.Builder<QuestionModal>().setQuery(query1 as CollectionReference, QuestionModal::class.java).build()
        qstnList.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        adapter= QuestionAdapter(this, options)
        qstnList.adapter=adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.questionl_list_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId=item?.itemId
        when(itemId){
            R.id.addExamIcon->{
                val intent= Intent(this,AddQuestion::class.java)
                Log.e("user id", id)
                Log.e("exam id",exam_id)
                intent.putExtra("exam_id", exam_id)
                startActivity(intent)
            }
            android.R.id.home->{
                finish()
                val intent=Intent(this,ExamList::class.java)
                startActivity(intent)
            }
            else->showToast("baba g ka thullu")
        }
        return true
    }

    override fun onStart() {
        Log.e("on start questionList","reach here")
        super.onStart()
        adapter?.startListening()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val intent=Intent(this,ExamList::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}
