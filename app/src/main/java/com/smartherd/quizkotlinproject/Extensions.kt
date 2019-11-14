package com.smartherd.quizkotlinproject


import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

fun Context.showToast(message:String,duration: Int=Toast.LENGTH_SHORT){
    Toast.makeText(this,message,duration).show()
}


class GeneralUser(){
    companion object UserId{
        var user_id:String=""
        fun setUserId(id:String){
            user_id =id
        }
    }
}
class ExamSource {
    companion object Source{
        var source:Int=0
        fun set(src:Int){
            source =src
        }
    }
}
class MoveUpward{
    companion object FAB{
        @SuppressLint("StaticFieldLeak")
        lateinit var moveFAB:View
        fun setFAB(v:View) {
            moveFAB = v
        }
    }
}
