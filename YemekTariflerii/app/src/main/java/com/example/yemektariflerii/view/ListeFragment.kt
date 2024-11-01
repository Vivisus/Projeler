 package com.example.yemektariflerii.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.yemektariflerii.adapter.TarifAdapter
import com.example.yemektariflerii.databinding.FragmentListeBinding
import com.example.yemektariflerii.model.Tarif
import com.example.yemektariflerii.roomdb.TarifDao
import com.example.yemektariflerii.roomdb.TarifDataBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


 class ListeFragment : Fragment() {
    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!
     private lateinit var db : TarifDataBase
     private lateinit var tarifDao: TarifDao
     private val mDisposable = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),TarifDataBase::class.java,"Tarifler").build()
        tarifDao = db.TarifDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yeniEkleButon.setOnClickListener { eklemeButonu(it) }
        binding.tarifRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

     private fun verileriAl(){
         mDisposable.add(
             tarifDao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
         )
     }

     private fun handleRespond(tarif :List<Tarif>){
         val adapter = TarifAdapter(tarif)
         binding.tarifRecycler.adapter  =adapter
     }

     fun eklemeButonu(view: View){
         val action = ListeFragmentDirections.actionListeFragmentToTarifFragment(bilgi = "",id = 0)
         Navigation.findNavController(view).navigate(action)
     }

     override fun onDestroyView() {
         super.onDestroyView()
         _binding = null
         mDisposable.clear()
     }
}