package com.example.eyetem_drug

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.util.Log

class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.toolbar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        // 툴바에 메뉴를 인플레이트한 후에 버튼들을 가져오도록 변경합니다.
        toolbar.inflateMenu(R.menu.toolbar_menu)

        // 툴바의 자식 요소들을 가져오는 방식을 변경하여 버튼을 참조합니다.
        val viewGroup = toolbar.getChildAt(0) as? LinearLayout
        val goBackButton = viewGroup?.findViewById<ImageButton>(R.id.action_goback)
        val homeButton = viewGroup?.findViewById<ImageButton>(R.id.action_home)

        goBackButton?.setOnClickListener {
            onBackPressed()
        }

        homeButton?.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}