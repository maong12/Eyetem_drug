package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eyetem_drug.databinding.ActivityBarcodeBinding
import com.example.eyetem_drug.databinding.ActivityBasicInformationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.EnumSet.range

class BasicInformationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityBasicInformationBinding
    private lateinit var database: DatabaseReference
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBasicInformationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //db
        database = Firebase.database("https://eyetemdrug-default-rtdb.firebaseio.com/").reference

        // 이 화면에서 필요한 작업 수행
        val drugname = intent.getStringExtra("dname")
        val kdcode  = intent.getStringExtra("kdcode")
        val textView1 = findViewById<TextView>(R.id.drug_name)
        

        //약 기본정보 db에서 읽어오기
        var currentInfo: Any?

        database.child("data").child("$kdcode").get().addOnSuccessListener {
            currentInfo = it.value
            Log.d(TAG, "Got value $currentInfo")

            //문자열 슬라이싱
            var str : String = "$currentInfo".substring(1,"$currentInfo".length-1)
            str = str.replace("=",": ")
            str = str.replace(", ","\n")

            //텍스트 뷰에 추가
            mBinding.explain.setText(str)


        }.addOnFailureListener{
            Log.d(TAG, "Error getting data", it)
        }

        //텍스트뷰에 약 이름
        textView1.setText(drugname)
    }
}