package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AllMemoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_memo_list)
        // 이 화면에서 필요한 작업 수행

        val textView = findViewById<TextView>(R.id.drug_name)
        textView.setText("전체 메모 보기")

        // 여기부터 리스트 만들기
        val drugNames: ArrayList<String> = ArrayList()

        // VoiceMemoListActivity에서 전달한 데이터 가져오기
        val folder = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        if (folder != null) {
            val files = folder.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        drugNames.add(file.name)
                    }
                }
            } else {
                Log.e("File Error", "No files found in the directory")
            }
        } else {
            Log.e("Directory Error", "Directory does not exist")
        }

        val unitsGroup = findViewById<LinearLayout>(R.id.units_group)

        // drugNames 리스트에 있는 약 이름을 화면에 추가
        for (drug in drugNames) {
            val button = Button(this)
            button.text = drug
            button.setOnClickListener {
                val intent = Intent(this, AllMemoVoiceActivity::class.java)
                intent.putExtra("drug_Name", drug)
                startActivity(intent)
            }
            unitsGroup.addView(button)
        }
    }
}