package com.example.eyetem_drug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Element
import java.net.URL
import kotlin.coroutines.coroutineContext


var dlist: MutableList<String> = mutableListOf()


class VoiceSearchListActivity : AppCompatActivity() {
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.voice_search_list)
        // 이 화면에서 필요한 작업 수행

        val drugname = intent.getStringExtra("dname_kor")
        val textView = findViewById<TextView>(R.id.drug_name)
        val dlen = "$drugname".length
        val Dname: String = "$drugname".substring(1, dlen - 1)
        //텍스트뷰에 약 이름
        textView.setText(Dname)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //doCrawling("$Dname")

        //API 파라미터
        val serviceKey =
            "serviceKey=FK9B42ejEdTQKE12u5vgn8wWc5DOFFauvf3CpRLLaMbqTRj6XM6XcaENQzc9NLX55L8m8zHJIdlfzYYr9aLdLg%3D%3D"
        val pageNo = "&pageNo=1"
        val numOfRows = "&numOfRows=20"
        val type = "&type=XML"
        val itemName = "&itemName=" + Dname
        val url =
            "apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?" + serviceKey + itemName + pageNo + numOfRows + type

        //Log.d(TAG, "apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?" + serviceKey + itemName + pageNo + numOfRows + type)

        val thread = Thread(NetworkThread("https://$url"))

        dlist= mutableListOf()
        thread.start() // 쓰레드 시작
        thread.join()

        //텍스트뷰에 추가
        //val textView2 = findViewById<TextView>(R.id.drugs)
        //textView2.text = text

        val unitsBtns = findViewById<LinearLayout>(R.id.units_group)

        for(i in dlist){
            val dynamicButton = Button(this)
            dynamicButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dynamicButton.text = i
            dynamicButton.textAlignment = TEXT_ALIGNMENT_TEXT_START
            dynamicButton.setOnClickListener{
                //액티비티 이동
                var intent = Intent(applicationContext, VoiceSearchInformationActivity::class.java)
                intent.putExtra("drug_name",i)
                startActivity(intent)
            }
            unitsBtns.addView(dynamicButton)
        }

    }


    //크롤링 함수(안씀)
//    private fun doCrawling(keyword: String) {
//
//        val siteUrl =
//            "http://connectdi.com/mobile/drug/?pap=search_result&search_keyword_type=all&search_keyword="
//        val doc: Document = Jsoup.connect(siteUrl + keyword).maxBodySize(0).get()
//        var elmt: Elements = doc.select("section")
//        var elmt2: Elements = elmt.select("ul.medList")
//        var elmt3: Elements = elmt2.select("li dt")
//        var dtext: String = elmt3.text()
//
//
//        Log.d(TAG, "This is url :: $siteUrl$keyword)")
//        Log.d(TAG, "This is doc :: $doc")
//        Log.d(TAG, "This is elmt :: $elmt")
//        Log.d(TAG, "This is elmt3 :: $elmt3")
//
//
//        val textView2 = findViewById<TextView>(R.id.drugs)
//
//        //문자열 프로세싱 (druglist는 각 약이름을 담은 리스트입니다)
//        var druglist: MutableList<String> = mutableListOf()
//        for (i in elmt3) {
//            val a = i.getElementsByTag("dt").text()
//            druglist.add("$a")
//            //Log.d(TAG, "This is druglist :: $druglist")
//        }
//
//        //텍스트뷰에 추가
//        textView2.setText("$dtext")
//    }
//}

    //api
    class NetworkThread(
        var url: String
    ) : Runnable {
        val TAG: String = "로그"


        override fun run() {

            try {

                val xml: org.w3c.dom.Document =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
                Log.d(TAG, "url : $url")

                xml.documentElement.normalize()

                //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
                val list: NodeList = xml.getElementsByTagName("item")

                //list.length-1 만큼 얻고자 하는 태그의 정보를 가져온다
                for (i in 0..list.length - 1) {

                    val n: Node = list.item(i)

                    if (n.getNodeType() == Node.ELEMENT_NODE) {

                        val elem = n as Element

                        val map = mutableMapOf<String, String>()


                        for (j in 0..elem.attributes.length - 1) {

                            map.putIfAbsent(
                                elem.attributes.item(j).nodeName,
                                elem.attributes.item(j).nodeValue
                            )
                        }

                        Log.d(TAG, "이름 : ${elem.getElementsByTagName("itemName").item(0).textContent}")

                        dlist.add("${elem.getElementsByTagName("itemName").item(0).textContent}")

                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "오픈API" + e.toString())
                Log.d(TAG, "url : $url")
            }
        }



    }
}