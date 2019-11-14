package com.smartherd.quizkotlinproject.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.smartherd.quizkotlinproject.Modals.QuestionModal
import com.smartherd.quizkotlinproject.R
import kotlinx.android.synthetic.main.question_item.view.*


class QuestionAdapter(val context:Context,options:FirestoreRecyclerOptions<QuestionModal>)
    :FirestoreRecyclerAdapter<QuestionModal, QuestionAdapter.MyViewHolder>(options){
    init {
        Log.e("adapter","reach here")
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        Log.e("adapter create view","reach here")
        val view=LayoutInflater.from(context).inflate(R.layout.question_item,p0,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: QuestionModal) {
        Log.e("adapter bind view","reach here")
        with(holder) {
            with(itemView) {
                questionId.text = model.question
                option1id.text = model.option1
                option2id.text = model.option2
                option3id.text = model.option3
                option4id.text = model.option4
            }
        }
    }



    inner class MyViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!),LayoutContainer{
        init {
            Log.e("MyViewHOlder","reach here")
        }
    }
}