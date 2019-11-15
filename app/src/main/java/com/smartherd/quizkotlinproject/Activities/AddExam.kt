package com.smartherd.quizkotlinproject.Activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.quizkotlinproject.GeneralUser
import com.smartherd.quizkotlinproject.MoveUpward
import com.smartherd.quizkotlinproject.R
import com.smartherd.quizkotlinproject.showToast
import kotlinx.android.synthetic.main.activity_add_exam.*
import kotlinx.android.synthetic.main.timer_set_alert.view.*

class AddExam : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var id: String? = null
    var marks= arrayOf(1,2,3,5,10)
    var marksSet:String="0"
    var totalQues:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exam)
        setSupportActionBar(addExamToolbar as Toolbar?)
        val actionBar = supportActionBar
        actionBar?.title = "Add Exam"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val user_id = GeneralUser.user_id
        Log.e("user_id", user_id)

        val examCollection = db.collection("Question Makers")
            .document(user_id).collection("Exams")
        val exmTitle=findViewById<TextInputLayout>(R.id.examTitle)
        saveExam.setOnClickListener { it ->
            if(examTitle.editText?.text.toString().isNotEmpty()) {
                Log.e("title to be set",examTitle.editText?.text.toString())
                val map = hashMapOf(
                    "title" to exmTitle.editText?.text.toString(),
                    "marksPer" to marksSet,
                    "totalQuestion" to totalQues,
                    "timerValue" to timerValue.text.toString()
                )
                var id: String?
                examCollection.add(map)
                    .addOnSuccessListener {
                        showToast("exam added")
                        id = it.id
                        Log.e("id exam questionList", id)
                        val intent = Intent(this, QuestionsList::class.java)
                        intent.putExtra("examId", "$id")
                        startActivity(intent)
                    }.addOnFailureListener { e ->
                        Log.e("exception", e.toString())
                    }
            }else{
                Snackbar.make(it,"Title must be added.",Snackbar.LENGTH_LONG).show()
            }

        }

        var hr_text:String;var min_text:String
        pickTimer.setOnClickListener {
            val pickerDialog= Dialog(this)
            pickerDialog.setTitle("Timer value for whole exam")
            pickerDialog.setContentView(R.layout.timer_set_alert)
            val hr_picker: NumberPicker = with(pickerDialog) { findViewById<View>(R.id.hour_picker) } as NumberPicker
            val min_picker:NumberPicker= with(pickerDialog) { findViewById<View>(R.id.minute_picker) } as NumberPicker
            val cancel_timer: TextView = with(pickerDialog) { findViewById<View>(R.id.cancelTimer) } as TextView
            val add_timer:TextView = with(pickerDialog) { findViewById<View>(R.id.addTimer) } as TextView
            hr_picker.minValue=0
            hr_picker.maxValue=3
            hr_picker.wrapSelectorWheel=true
            min_picker.minValue=0
            min_picker.maxValue=59
            min_picker.wrapSelectorWheel=true
            pickerDialog.setCancelable(false)
            cancel_timer.setOnClickListener {
                pickerDialog.dismiss()
            }
            add_timer.setOnClickListener {


                val hr_value=hr_picker.value
                val min_value=min_picker.value


                hr_text = if(hr_value<10)
                    "0$hr_value"
                else
                    hr_value.toString()


                min_text = if(min_value<10)
                    "0$min_value"
                else
                    min_value.toString()



                val setText= "$hr_text : $min_text"
                timerValue.text=setText
                pickerDialog.dismiss()
            }
            pickerDialog.show()
        }



        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marks)
        marksList.adapter=arrayAdapter
        marksList.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                showToast("marks : " +marks[0].toString())
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                marksSet=marks[p2].toString()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }else -> {
                true
            }
        }

    }

}
