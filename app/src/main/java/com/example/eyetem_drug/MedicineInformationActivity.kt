package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MedicineInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.medicine_detail)
        // 이 화면에서 필요한 작업 수행

        // 음성 메모 화면으로 이동
        val bt1 = findViewById<Button>(R.id.sound_memo)
        val drugname = intent.getStringExtra("dname")
        val kdcode = intent.getStringExtra("kdcode")
        val textView = findViewById<TextView>(R.id.textView)

        bt1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, VoiceMemoListActivity::class.java)
            intent.putExtra("dname","$drugname")
            intent.putExtra("kdcode","$kdcode")
            startActivity(intent) // 새로운 액티비티로 이동
        }

        val bt2 = findViewById<Button>(R.id.drug_basic_information)


        bt2.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, BasicInformationActivity::class.java)
            intent.putExtra("dname","$drugname")
            intent.putExtra("kdcode","$kdcode")
            startActivity(intent) // 새로운 액티비티로 이동
        }

        //텍스트뷰에 약 이름
        textView.setText("$drugname")

        //뒤로가서 다시 찍기
        val bt3 = findViewById<Button>(R.id.back)
        bt3.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, BarcodeActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }
    }
}