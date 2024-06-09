package com.example.eyetem_drug

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Element


var text = ""

class VoiceSearchInformationActivity : AppCompatActivity() {
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_search_information)

        val drugname = intent.getStringExtra("drug_name")
        val textView = findViewById<TextView>(R.id.drug_name)

        Log.d(TAG, "drugname : $drugname")

        //텍스트뷰에 약 이름
        textView.setText("$drugname")


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        //API 파라미터
        val serviceKey =
            "serviceKey=FK9B42ejEdTQKE12u5vgn8wWc5DOFFauvf3CpRLLaMbqTRj6XM6XcaENQzc9NLX55L8m8zHJIdlfzYYr9aLdLg%3D%3D"
        val pageNo = "&pageNo=1"
        val numOfRows = "&numOfRows=20"
        val type = "&type=XML"
        val itemName = "&itemName=$drugname"
        val url = "apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?" + serviceKey + itemName + pageNo + numOfRows + type

        //Log.d(TAG, "apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?" + serviceKey + itemName + pageNo + numOfRows + type)

        val thread = Thread(NetworkThread("https://$url"))

        text = ""
        thread.start() // 쓰레드 시작
        thread.join()

        //텍스트뷰에 추가
        val textView2 = findViewById<TextView>(R.id.explain)
        textView2.text = text

        }
    }

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

                        //text += "${i + 1}] "


                        Log.d(TAG, "업체명 : ${elem.getElementsByTagName("entpName").item(0).textContent}")

                        text += "업체명: ${elem.getElementsByTagName("entpName").item(0).textContent} \n\n"
                        //dlist.add("${elem.getElementsByTagName("itemName").item(0).textContent}")

                        text += "효능: ${elem.getElementsByTagName("efcyQesitm").item(0).textContent} \n\n"

                        text += "사용법: ${elem.getElementsByTagName("useMethodQesitm").item(0).textContent} \n\n"

                        text += "경고: ${elem.getElementsByTagName("atpnWarnQesitm").item(0).textContent} \n\n"

                        text += "주의사항: ${elem.getElementsByTagName("intrcQesitm").item(0).textContent} \n\n"

                        text += "보관법: ${elem.getElementsByTagName("depositMethodQesitm").item(0).textContent} \n\n"

                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "오픈API" + e.toString())
                Log.d(TAG, "url : $url")
            }
        }
    }

