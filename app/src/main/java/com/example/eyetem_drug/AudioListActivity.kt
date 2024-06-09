    package com.example.eyetem_drug

    import android.content.Context
    import android.net.Uri
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageButton
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import java.io.File
    import java.net.URLDecoder

    class AudioListActivity(private val context: Context, private val dataModels: ArrayList<Uri>) :
        RecyclerView.Adapter<AudioListActivity.MyViewHolder>() {

        // 아이템 클릭 리스너 인터페이스
        private var listener: OnIconClickListener? = null

        // 아이템 클릭 리스너를 설정하기 위한 인터페이스 정의
        interface OnIconClickListener {
            fun onItemClick(view: View, position: Int)
        }

        fun setOnItemClickListener(listener: OnIconClickListener) {
            this.listener = listener
        }

        // 아이템 개수 반환
        override fun getItemCount(): Int {
            // 데이터 리스트의 크기를 전달해주어야 함
            return dataModels.size
        }

        // 뷰홀더 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            // itemview를 inflate하여 새로운 뷰 홀더 생성
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.audio_list, parent, false)

            // 생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
            return MyViewHolder(view)
        }

        // 뷰홀더에 데이터 바인딩
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // 파일 이름을 가져와서 뷰에 설정
            val uri = dataModels[position]
            val fileName = File(uri.path!!).name
            // holder.audioTitle.text = file.name

            // UTF-8로 인코딩된 파일명을 디코딩하여 표시(추가)
            val decodedFileName = URLDecoder.decode(fileName, "UTF-8")
            holder.audioTitle.text = decodedFileName
        }

        // 뷰홀더 클래스 정의
        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            // 아이콘과 텍스트뷰를 위한 변수 선언
            var audioBtn: ImageButton = itemView.findViewById(R.id.playButton)
            var audioTitle: TextView = itemView.findViewById(R.id.audioTitle)

            init {
                // 아이콘 클릭 시 동작 설정
                audioBtn.setOnClickListener { view ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener!!.onItemClick(view, pos)
                        }
                    }
                }
            }
        }
    }