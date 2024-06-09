package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VoiceMemoSearchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_memo_search_list)
        // 이 화면에서 필요한 작업 수행

        // 음성으로 검색한 약 이름이 위에 나오게
        val memoName = intent.getStringArrayListExtra("memo_name")
        val textView = findViewById<TextView>(R.id.drug_name)

        /*val dlen = "$memoName".length
        val Mname: String = "$memoName".substring(1, dlen - 1)

        // 텍스트뷰에 memo 이름
        textView.setText(Mname)*/

        // null 체크 및 배열 요소 확인
        if (memoName != null && memoName.isNotEmpty()) {
            val dlen = memoName[0].length
            val Mname: String = memoName[0]
            // 텍스트뷰에 memo 이름

            textView.text = Mname

            val folders = getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.listFiles()
            val matchingFolders = mutableListOf<String>()

            folders?.forEach { folder ->
                memoName.forEach { searchText ->
                    if (folder.name.contains(searchText, ignoreCase = true)) {
                        matchingFolders.add(folder.name)
                    }
                }
            }

            // UI에 결과를 표시하는 부분에 대한 코드 예시
            val unitsGroup = findViewById<LinearLayout>(R.id.units_group)

            // matchingFolders 리스트에 있는 약 이름을 화면에 추가
            for (drug in matchingFolders) {
                val button = Button(this)
                button.text = drug
                button.setOnClickListener {
                    val intent = Intent(this, AllMemoVoiceActivity::class.java)
                    intent.putExtra("drug_Name", drug)
                    startActivity(intent)
                }
                unitsGroup.addView(button)
            }
        } else {
            textView.text = "No search results found."
        }
    }
}