package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MemoMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_menu)
        // 이 화면에서 필요한 작업 수행

        val bt1 = findViewById<Button>(R.id.all_memo)
        bt1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, AllMemoListActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }
        val bt2 = findViewById<Button>(R.id.see_voice_memo)
        bt2.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, VoiceMemoSearchActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }
    }
}