package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        // 이 화면에서 필요한 작업 수행

        val btn1 = findViewById<Button>(R.id.barcode_search)
        btn1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, BarcodeActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }

        val btn2 = findViewById<Button>(R.id.sound_search)
        btn2.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, VoiceSearchActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }

        val btn3 = findViewById<Button>(R.id.memo)
        btn3.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, MemoMenuActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }
    }
}