package com.example.myapplication1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*binding.button.setOnClickListener {
            println("Butona tıklandı")
         */

        /*
        val image = findViewById<ImageView>(R.id.imageView)
        val yazi = findViewById<TextView>(R.id.textView)
         */
        println("onCreate çalıştırıldı")
    }

    override fun onStart() {
        super.onStart()
        println("start çalıştırıldı")
    }

    override fun onResume() {
        super.onResume()
        println("resume çalıştırıldı")
    }

    override fun onPause() {
        super.onPause()
        println("pause çalıştırıldı")
    }

    override fun onStop() {
        super.onStop()
        println("stop çalıştırıldı")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun giris(view: View){
        val intent  =Intent(this, SecondActivitive::class.java)
        startActivity(intent)
    }
    fun girisYap(view: View){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Hata")
        alert.setMessage("Giriş yapılamadı!")
        alert.setPositiveButton("Tamam", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(this@MainActivity, "Giriş yapılamadı", Toast.LENGTH_LONG).show()
            }

        })
    }


}