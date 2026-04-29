package com.demo.negative

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.negative.service.NegativeScreenService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openNegativeScreen(view: View) {
        // 启动负一屏 Activity（全屏显示）
        val intent = Intent(this, NegativeScreenActivity::class.java)
        startActivity(intent)
    }

    fun stopNegativeScreen(view: View) {
        // 停止后台服务
        val intent = Intent(this, NegativeScreenService::class.java)
        stopService(intent)
        Toast.makeText(this, "已停止", Toast.LENGTH_SHORT).show()
    }
}