package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() , TextToSpeech.OnInitListener{

    private var tts: TextToSpeech? = null
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)

        val btn1 = findViewById<Button>(R.id.start)
        btn1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }

        val btn2 = findViewById<Button>(R.id.listen)
        btn2.setOnClickListener {
            // 설명하기 버튼 클릭시 나올 tts(제가 대충 작성했는데 얼마든지 더 자세하게 수정해주셔도 됩니다!)
            speakOut("이 앱은 바코드를 스캔하면 의약품 이름을 알려줍니다. 그 외에 음성메모를 남기거나 약품에 대한 설명을 들으실 수도 있습니다.")
        }


    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.KOREAN)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG,"The Language specified is not supported!")
            } else {
                Log.d(TAG,"가능!")
            }

        } else {
            Log.e(TAG, "Initilization Failed!")
        }
    }
    public fun speakOut(text : String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        tts!!.playSilentUtterance(750,TextToSpeech.QUEUE_ADD,null) // deley시간 설정
    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}