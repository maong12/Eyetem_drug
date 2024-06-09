package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// 바코드
import android.speech.tts.TextToSpeech
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import java.util.Locale
import com.example.eyetem_drug.databinding.ActivityBarcodeBinding
open class TextToSpeech

class BarcodeActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var mBinding: ActivityBarcodeBinding
    private lateinit var database: DatabaseReference

    private var tts: TextToSpeech? = null
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBarcodeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        database = Firebase.database("https://eyetemdrug-default-rtdb.firebaseio.com/").reference
        val db = Firebase.firestore
        tts = TextToSpeech(this, this)

        // 이 화면에서 필요한 작업 수행
        val bt1 = findViewById<Button>(R.id.back)
        bt1.setOnClickListener {
            // 버튼 클릭 시 수행할 작업
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent) // 새로운 액티비티로 이동
        }

        //mBinding.barcode_search.setOnClickListener{
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES )
            integrator.setPrompt("의약품 패키지를 스캔해 주세요.")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()

    }

    // 바코드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // QR 코드를 찍은 결과를 변수에 담는다.
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "QR 코드 체크")

        //결과가 있으면
        if (result != null) {
            // 컨텐츠가 없으면
            if (result.contents == null) {
                //토스트를 띄운다.
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
            // 컨텐츠가 있으면
            else {
                //토스트를 띄운다.
                //Toast.makeText(this, "scanned" + result.contents, Toast.LENGTH_LONG).show()
                Log.d(TAG, "QR 코드 URL:${result.contents}")

                //웹뷰 설정
                // mBinding.webView.settings.javaScriptEnabled = true
                //mBinding.webView.webViewClient = WebViewClient()
//
//                //웹뷰를 띄운다.
                //mBinding.webView.loadUrl(result.contents)

                var res: String = result.contents

                if(result.contents.length > 13){
                    res = result.contents.substring(result.contents.length-13)
                }


                searchDName("$res")
            }
            // 결과가 없으면
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun searchDName(KDcode : String) {

        var currentDrug: Any?
        var result : String = ""
        //1회 읽어오기
        database.child("data").child(KDcode).child("이름").get().addOnSuccessListener {
                currentDrug = it.value
                Log.d(TAG, "Got value $currentDrug")
                //Toast.makeText(applicationContext, "$currentDrug".substring(4), Toast.LENGTH_SHORT).show()
                speakOut("$currentDrug")



                //액티비티 이동
                intent = Intent(this, MedicineInformationActivity::class.java)
                intent.putExtra("dname","$currentDrug")
                //표준코드 정보 보내기
                intent.putExtra("kdcode","$KDcode")
                startActivity(intent)

            }.addOnFailureListener{
                Log.d(TAG, "Error getting data", it)
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
    fun speakOut(text : String) {
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