package com.example.eyetem_drug

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

class AllMemoVoiceActivity : AppCompatActivity() {
    // 화면에 나타날 변수
    private lateinit var audioRecordImageBtn: ImageButton

    // 오디오 파일 및 녹음 관련 변수
    private val recordPermission = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 21
    private var mediaRecorder: MediaRecorder? = null

    private var audioFileName: String = ""
    private var output: String = ""

    private var isRecording = false
    private var audioUri: Uri? = null

    // 오디오 재생 관련 변수
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var playIcon: ImageView

    // 리사이클러뷰
    private lateinit var audioAdapter: AudioListActivity
    private lateinit var audioList: ArrayList<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_memo_voice)

        // 약 이름 가져오기
        val drugname = intent.getStringExtra("drug_Name")
        val textView1 = findViewById<TextView>(R.id.drug_name)

        // 텍스트뷰에 약 이름 설정
        textView1.text = drugname

        /*// DrugListActivity로 전환하는 Intent를 생성하고 drugname을 추가
        val intent = Intent(this, AllMemoListActivity::class.java)
        intent.putExtra("drug_name", drugname)
        startActivity(intent)*/

        // 초기화 함수 호출
        init()
    }

    // XML 변수 초기화 및 리사이클러뷰 설정
    private fun init() {

        // UI 요소 초기화
        audioRecordImageBtn = findViewById(R.id.audioRecordImageBtn)

        // 녹음 버튼 클릭시 동작 설정
        audioRecordImageBtn.setOnClickListener {

            // 녹음 중인지 확인하고 녹음 시작 또는 종료
            if (isRecording) {
                isRecording = false
                audioRecordImageBtn.setImageResource(R.drawable.start_audio)
                stopRecording()
            } else {
                if (checkAudioPermission()) {
                    isRecording = true
                    audioRecordImageBtn.setImageResource(R.drawable.stop_audio)
                    startRecording()
                }
            }
        }

        val audioRecyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        audioList = ArrayList()
        audioAdapter = AudioListActivity(this, audioList)
        audioRecyclerView.adapter = audioAdapter
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        audioRecyclerView.layoutManager = mLayoutManager

        audioAdapter.setOnItemClickListener(object : AudioListActivity.OnIconClickListener {
            override fun onItemClick(view: View, position: Int) {
                val uri = audioList[position] // 클릭한 아이템의 Uri 가져오기
                if (isPlaying) {
                    if (playIcon === view) {
                        stopAudio()
                    } else {
                        stopAudio()
                        playIcon = view as ImageView
                        playAudio(uri) // 수정된 playAudio 함수 호출
                    }
                } else {
                    playIcon = view as ImageView
                    playAudio(uri) // 수정된 playAudio 함수 호출
                }
            }
        })

        /*기존의 녹음 파일 불러오기*/
        val drugNameTextView = findViewById<TextView>(R.id.drug_name)
        val drugName = drugNameTextView.text.toString()

        val folder = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val drugFolder = File(folder, drugName)

        if (drugFolder.exists()) {
            val files = drugFolder.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        val uri = FileProvider.getUriForFile(this, "com.example.eyetem_drug.fileprovider", file)
                        audioList.add(uri)
                    }
                }
                audioAdapter.notifyDataSetChanged()
            } else {
                Log.e("File Error", "No files found in the directory")
            }
        } else {
            Log.e("Directory Error", "Directory does not exist")
        }
    }

    // 오디오 파일 권한 체크
    private fun checkAudioPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                applicationContext,
                recordPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(recordPermission), PERMISSION_CODE)
            false
        }
    }


    // 녹음 시작
    private fun startRecording() {

        // 이전 녹음 중인 미디어 레코더 해제
        mediaRecorder?.release()
        mediaRecorder = null

        // 약 이름을 텍스트 뷰에서 가져오기
        val drugNameTextView = findViewById<TextView>(R.id.drug_name)
        val drugName = drugNameTextView.text.toString()

        /*        // 녹음된 파일을 저장할 폴더 경로 설정
                val appFolderPath = getExternalFilesDir(null)?.absolutePath + "/MyRecordingFolder"
                val myRecordingFolder = File(appFolderPath)*/

        // 녹음된 파일을 저장할 폴더 경로 설정
        val folder = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        // 약 이름으로 폴더 생성
        val drugFolder = File(folder, drugName)

        // 폴더가 없으면 생성
        if (!drugFolder.exists()) {
            drugFolder.mkdirs()
        }


        // 녹음 파일의 고유한 이름 설정
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        /*audioFileName = "$drugFolder/RecordExample_$timeStamp.mp3"*/

        // 약 이름을 UTF-8로 인코딩하여 파일명에 추가
        val encodedDrugName = URLEncoder.encode(drugName, "UTF-8")
        audioFileName = "${encodedDrugName}_$timeStamp.mp3"
        val outputFile = File(drugFolder, audioFileName) // 녹음 파일 객체 생성
        output = outputFile.absolutePath // 녹음 파일 경로 설정

        // 미디어 레코더 설정 및 녹음 시작
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(output)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare() // 녹음 준비
            start() // 녹음 시작
        }
    }


    // 녹음 종료
    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null

        // 리소스 해제
        val recordedFile = File(output)
        if (recordedFile.exists()) {

            // 비동기 처리(추가)
            CoroutineScope(Dispatchers.IO).launch {
                val uri = Uri.fromFile(recordedFile)
                withContext(Dispatchers.Main) {
                    updateAudioList(uri) // 녹음이 끝날 때마다 파일 목록 업데이트
                }
            }
            /*audioUri = Uri.fromFile(recordedFile)
            audioList.add(audioUri!!)
            audioAdapter.notifyDataSetChanged()*/
        }
        /*audioUri = Uri.parse(output)
        audioList.add(audioUri!!)
        audioAdapter.notifyDataSetChanged()*/
    }

    // 녹음 파일 재생
    private fun playAudio(uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            reset() // 추가
            setDataSource(applicationContext, uri)
            prepare()
            start()
        }
        playIcon.setImageResource(R.drawable.play_stop)
        isPlaying = true

        mediaPlayer?.setOnCompletionListener {
            stopAudio()
        }
    }

    // 녹음 파일 목록 업데이트 (추가)
    private fun updateAudioList(uri: Uri) {
        audioList.add(uri)
        audioAdapter.notifyDataSetChanged()
    }

    // 녹음 파일 중지
    private fun stopAudio() {
        playIcon.setImageResource(R.drawable.play_start)
        isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.release() // 여기부터 추가
        mediaPlayer = null // MediaPlayer 해제
    }
}