package com.example.yemektariflerii.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.yemektariflerii.databinding.FragmentTarifBinding
import com.example.yemektariflerii.model.Tarif
import com.example.yemektariflerii.roomdb.TarifDao
import com.example.yemektariflerii.roomdb.TarifDataBase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.io.ByteArrayOutputStream
import kotlin.math.max


class TarifFragment : Fragment() {
    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var secilenGorsel : Uri?= null
    private var secilenBitmap : Bitmap?= null
    private lateinit var db : TarifDataBase
    private lateinit var tarifDao: TarifDao
    private val mDisposable = CompositeDisposable()
    private var secilenTarif : Tarif? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        db = Room.databaseBuilder(requireContext(),TarifDataBase::class.java,"Tarifler").build()
        tarifDao = db.TarifDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTarifBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yemekView.setOnClickListener { gorsel(it) }
        binding.silButon.setOnClickListener { sil(it) }
        binding.kayitButon.setOnClickListener { kaydet(it) }

        arguments?.let {
            val bilgi = TarifFragmentArgs.fromBundle(it).bilgi
            if (bilgi == "yeni"){
                secilenTarif = null
                binding.silButon.isEnabled = false
                binding.kayitButon.isEnabled = true
                binding.yemekismiText.setText("")
                binding.malzemeText.setText("")
            } else {
                binding.silButon.isEnabled = true
                binding.kayitButon.isEnabled = false
                val id = TarifFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    tarifDao.findById(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                )
            }
        }
    }
    private fun handleRespond(tarif :Tarif){
        binding.yemekismiText.setText(tarif.isim)
        binding.malzemeText.setText(tarif.malzeme)
        val bitmap = BitmapFactory.decodeByteArray(tarif.gorsel,0, tarif.gorsel.size)
        binding.yemekView.setImageBitmap(bitmap)
        secilenTarif = tarif
    }
    fun kaydet(view: View){
        val isim = binding.yemekismiText.text.toString()
        val malzeme = binding.malzemeText.text.toString()
        if (secilenBitmap != null){
            val kucukBitmap = kucukBitmap(secilenBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,54,outputStream)
            val byteDizisi = outputStream.toByteArray()

            val tarif = Tarif(isim,malzeme,byteDizisi)

            mDisposable.add(tarifDao.insert(tarif)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe (this:: handleRespondForInsert)
            )


        }

    }
    private fun handleRespondForInsert(){
        val action = TarifFragmentDirections.actionTarifFragmentToListeFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    fun sil(view: View){
    if (secilenTarif != null){
        mDisposable.add(tarifDao.delete(tarif = secilenTarif!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleRespondForInsert))
    }
    }
    fun gorsel(view: View){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view,"Galeri iznine ihtiyacımız var!",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin ver", View.OnClickListener { permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES) }
                    ).show()
                } else{
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            }
        }else{
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view,"Galeri iznine ihtiyacımız var!",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin ver", View.OnClickListener { permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
                    ).show()
                } else{
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            }
        }


    }
    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    secilenGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.yemekView.setImageBitmap(secilenBitmap)

                        }else {
                            secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel)
                            binding.yemekView.setImageBitmap(secilenBitmap)
                        }
                    } catch (e : Exception){
                        println(e.localizedMessage)
                    }



                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            } else {
                Toast.makeText(requireContext(),"Görsel yüklenemedi!",Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun kucukBitmap(kullaniciBitmapi: Bitmap, maxBoyut : Int) : Bitmap {
        var width = kullaniciBitmapi.width
        var height= kullaniciBitmapi.height
        val bitmapOrani : Double = width.toDouble() / height.toDouble()

        if (bitmapOrani >  1){
            width = maxBoyut
            val kisaltilmisYukseklik = width/height
            height = kisaltilmisYukseklik.toInt()
        }else{
            height = maxBoyut
            val kisaltilmisGenislik = height*bitmapOrani
            width = kisaltilmisGenislik.toInt()
        }

        return Bitmap.createScaledBitmap(kullaniciBitmapi,width,height,true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}