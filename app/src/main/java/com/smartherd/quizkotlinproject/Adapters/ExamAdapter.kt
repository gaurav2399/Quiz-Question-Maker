package com.smartherd.quizkotlinproject.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.smartherd.quizkotlinproject.Activities.QuestionsList
import com.smartherd.quizkotlinproject.GeneralUser.UserId.user_id
import com.smartherd.quizkotlinproject.Modals.ExamModal
import com.smartherd.quizkotlinproject.MoveUpward
import com.smartherd.quizkotlinproject.R
import kotlinx.android.synthetic.main.exam_item.view.*

class ExamAdapter(val context:Context,var options:FirestoreRecyclerOptions<ExamModal>)
    :FirestoreRecyclerAdapter<ExamModal, ExamAdapter.ExamViewHolder>(options){
    var db= FirebaseFirestore.getInstance()
    var deleteReference=db.collection("Question Makers").document(user_id).collection("Exams")
    init {
        Log.e("exam list adapter ","reach here")
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ExamViewHolder {
        Log.e("adapterCreateView EXAM","reach here")
        val view=LayoutInflater.from(context).inflate(R.layout.exam_item,p0,false)
        return ExamViewHolder(view)

    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int, model: ExamModal) {
        Log.e("adapterBindView EXAM","reach here")
        holder.setTitle(model.title)

        holder.itemView.setOnLongClickListener {
            val builder=AlertDialog.Builder(context)
            builder.setTitle("Are you really want to delete exam-${model.title}?")
            builder.setCancelable(false)
            builder.setIcon(R.drawable.alert_warning)
            builder.setPositiveButton("Delete",DialogInterface.OnClickListener{_,_->
                deleteReference.document(model.id).collection("Questions").get().addOnSuccessListener { documentSnapshots->
                    for(documentSnapshot in documentSnapshots){
                        documentSnapshot.reference.delete()
                    }
                }.addOnSuccessListener { deleteReference.document(model.id).delete() }
                    .addOnSuccessListener {
                        Snackbar.make(MoveUpward.moveFAB,"Exam successfully deleted.",Snackbar.LENGTH_SHORT).show() }
            })
            builder.setNegativeButton("Cancel",DialogInterface.OnClickListener{dialogInterface, _ -> dialogInterface.dismiss() })
            builder.show()
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            val intent =Intent(context, QuestionsList::class.java)
            intent.putExtra("examId",model.id)
            context.startActivity(intent)
        }
    }
    override fun onError(e: FirebaseFirestoreException) {
        Log.e("error", e.message)
    }

    inner class ExamViewHolder(item : View): RecyclerView.ViewHolder(item){
        init {
            Log.e("reach here","please")
        }
        fun setTitle(title:String){
            itemView.examTitle.text=title
        }
    }

}