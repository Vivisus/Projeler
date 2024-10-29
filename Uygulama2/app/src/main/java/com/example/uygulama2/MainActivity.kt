package com.example.uygulama2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uygulama2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var superKahramanListesi: ArrayList<SuperKahraman>
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
        var ironman = SuperKahraman("Ironman", "Mucit", R.drawable.ironman)
        var thor = SuperKahraman("Thor","Prens",R.drawable.thor)
        var doctor = SuperKahraman("Doctor Strange", "Doktor",R.drawable.doctorstrange)

        superKahramanListesi = arrayListOf(ironman, thor, doctor)
        val adapter = SuperKahramanAdapter(superKahramanListesi)
        binding.superKahramanView.layoutManager = LinearLayoutManager(this)
        binding.superKahramanView.adapter = adapter

    }
}