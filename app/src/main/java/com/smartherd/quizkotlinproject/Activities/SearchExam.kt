package com.smartherd.quizkotlinproject.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.quizkotlinproject.Adapters.ExamAdapter
import com.smartherd.quizkotlinproject.GeneralUser
import com.smartherd.quizkotlinproject.Modals.ExamModal
import com.smartherd.quizkotlinproject.R
import kotlinx.android.synthetic.main.activity_exam_list.*
import kotlinx.android.synthetic.main.activity_exam_list.searchExam
import kotlinx.android.synthetic.main.activity_search_exam.*

class SearchExam : AppCompatActivity() {

    var db= FirebaseFirestore.getInstance()
    var adapter: ExamAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_exam)
        val examsSearchCollection=db.collection("Question Makers").document(GeneralUser.user_id)
            .collection("Exams")
        //setOnKeyListener for handling key events
        val srchExam=findViewById<AppCompatEditText>(R.id.searchExam)
        srchExam.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                adapter?.startListening()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val value=searchExam.text.toString()
                Log.e("value typed ",value)
                val query=examsSearchCollection
                val options = FirestoreRecyclerOptions.Builder<ExamModal>().setQuery(query.whereGreaterThanOrEqualTo("title",value)) { snapshot ->
                    snapshot.toObject(ExamModal::class.java)!!.also {
                        it.id = snapshot.id
                    }
                }.build()
                val srch=findViewById<RecyclerView>(R.id.searchSuggest)
                srch.layoutManager= LinearLayoutManager(this@SearchExam,RecyclerView.VERTICAL,false)
                //searchSuggest.addItemDecoration(DividerItemDecoration(this@SearchExam, DividerItemDecoration.VERTICAL))
                // searchSuggest.itemAnimator=null
                adapter= ExamAdapter(this@SearchExam, options)
                srch.adapter=adapter
                adapter!!.startListening()
            }
        })

    }


}
