package com.smartherd.quizkotlinproject.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.smartherd.quizkotlinproject.GeneralUser
import com.smartherd.quizkotlinproject.R
import com.smartherd.quizkotlinproject.showToast
import kotlinx.android.synthetic.main.activity_add_question.*

class AddQuestion : AppCompatActivity() {

    var db= FirebaseFirestore.getInstance()
    var answerValue:String="1"
    var optionList= arrayOf(1,2,3,4)
    var exam_id:String?=null
    var questionsAdded = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        setSupportActionBar(addQuestionToolbar as Toolbar?)
        val progressDialog = ProgressDialog(this)
        val actionBar=supportActionBar
        actionBar?.title = "Add Question"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle=intent.extras
        Log.e("enter in add question","yes...")
        val id= GeneralUser.user_id
        exam_id=bundle?.getString("exam_id")
        Log.e("user id adddquestion",id)
        Log.e("exam id addquestion",exam_id)
        val qstn=findViewById<TextInputLayout>(R.id.question)
        val questionsCollection=db.collection("Question Makers")
            .document(id).collection("Exams").document(exam_id!!).collection("Questions")

            saveQuestion.setOnClickListener {
                if(qstn.editText?.text.toString().isNotEmpty()&&option1.text.toString().isNotEmpty()&&option2.text.toString().isNotEmpty()
                    &&option3.text.toString().isNotEmpty()&&option4.text.toString().isNotEmpty()) {
                    progressDialog.setMessage("Adding Question, Please Wait...")
                    progressDialog.show()
                val questionMap = mapOf(
                    "question" to qstn.editText?.text.toString(),
                    "option1" to option1.text.toString(),
                    "option2" to option2.text.toString(),
                    "option3" to option3.text.toString(),
                    "option4" to option4.text.toString(),
                    "correctAnswer" to answerValue
                )

                questionsCollection.add(questionMap)
                    .addOnSuccessListener {
                        val ques_id = it.id
                        Log.e("question id", ques_id)
                        qstn.editText?.setText("")
                        option1.setText("")
                        option2.setText("")
                        option3.setText("")
                        option4.setText("")
                        optionSpinner.setSelection(0)
                        questionsAdded++
                        Log.e("questions added",questionsAdded.toString())
                        //showToast("Total questions saved now $questionsAdded")
                        progressDialog.dismiss()
                        showToast("Question added successfully.")
                    }.addOnFailureListener {
                        showToast("Some error has occured")
                    }
            }else{
                    showToast("empty fields")
                }
        }

        doneQuestion.setOnClickListener {
            val intent = Intent(this, QuestionsList::class.java)
            intent.putExtra("examId", "$exam_id")
            var presentQuestions = 0
            db.collection("Question Makers")
                .document(id).collection("Exams").document(exam_id!!).get().addOnSuccessListener {
                    if(it!=null){
                        presentQuestions = it.get("totalQuestion").toString().toInt()
                        Log.e("presetQuestions",presentQuestions.toString())
                        //showToast("present questions are $presentQuestions")
                        val map = mapOf("totalQuestion" to presentQuestions + questionsAdded)
                        db.collection("Question Makers")
                            .document(id).collection("Exams").document(exam_id!!).update(map).addOnSuccessListener {
                                //showToast("total questions are ${presentQuestions + questionsAdded}")
                                startActivity(intent)
                                //showToast("Total Questions Updated.")
                            }.addOnFailureListener {
                                showToast("Error in total Questions Updated.")
                            }
                    }else{
                        Log.e("snapshot is empty","null")
                    }

                }.addOnFailureListener {
                    Log.e("can't get the total","questions becoz " + it.message)
                }
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, optionList)
        optionSpinner.adapter=arrayAdapter
        optionSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                showToast("marks : " + optionList[0].toString())
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                answerValue = optionList[p2].toString()
            }
        }
    }


}
