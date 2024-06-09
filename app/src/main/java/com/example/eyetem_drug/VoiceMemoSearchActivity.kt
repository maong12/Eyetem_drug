package com.example.eyetem_drug

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class VoiceMemoSearchActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognitionListener: RecognitionListener
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_memo_search)
        // 이 화면에서 필요한 작업 수행

        //권한 체크
        requestPermission()

        val bt1 = findViewById<Button>(R.id.back)
        bt1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, MemoMenuActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }

        var intent2 = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent2.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        setListener()

        val bt2 = findViewById<ImageButton>(R.id.mike)
        bt2.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(intent)

        }
    }

    // 권한 설정 메소드
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // 거부해도 계속 노출됨. ("다시 묻지 않음" 체크 시 노출 안됨.)
            // 허용은 한 번만 승인되면 그 다음부터 자동으로 허용됨.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO), 0
            )
        }
    }

    private fun setListener() {
        recognitionListener = object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(applicationContext, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {
                var message: String

                when (error) {
                    SpeechRecognizer.ERROR_AUDIO ->
                        message = "오디오 에러"

                    SpeechRecognizer.ERROR_CLIENT ->
                        message = "클라이언트 에러"

                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                        message = "퍼미션 없음"

                    SpeechRecognizer.ERROR_NETWORK ->
                        message = "네트워크 에러"

                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        message = "네트워크 타임아웃"

                    SpeechRecognizer.ERROR_NO_MATCH ->
                        message = "찾을 수 없음"

                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                        message = "RECOGNIZER가 바쁨"

                    SpeechRecognizer.ERROR_SERVER ->
                        message = "서버가 이상함"

                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                        message = "말하는 시간초과"

                    else ->
                        message = "알 수 없는 오류"
                }
                Toast.makeText(applicationContext, "에러 발생 $message", Toast.LENGTH_SHORT).show()
            }


            override fun onResults(results: Bundle?) {
                var matches: ArrayList<String> =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>

                // 다음 액티비티로 문자열 넘기기

                // 액티비티 이동

                val intent5 = Intent(applicationContext, VoiceMemoSearchListActivity::class.java)
                intent5.putExtra("memo_name", matches)
                startActivity(intent5)

                Log.d(TAG, "음성입력받은 문자열:$matches")

            }

            override fun onPartialResults(partialResults: Bundle?) {

            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        }

    }

}