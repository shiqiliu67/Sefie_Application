package com.shiqiliu.selfieapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.shiqiliu.selfieapp.R
import com.shiqiliu.selfieapp.models.User
import kotlinx.android.synthetic.main.activity_add_selfie.*
import kotlinx.android.synthetic.main.activity_regsiter.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class AddSelfieActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CAMERA_CODE = 100
    private val REQUEST_GALLERY_CODE = 110
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    //var list :ArrayList<Uri> = ArrayList()
    var list :ArrayList<String> = ArrayList()
    lateinit var auth: FirebaseAuth
     var uid : String? = null
    var uri: Uri? = null

    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_selfie)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
         uid = auth.uid
        databaseReference = firebaseDatabase.getReference("/users/$uid")

        firebaseStorage = FirebaseStorage.getInstance()
        val fileName = UUID.randomUUID().toString()
        storageReference = firebaseStorage.getReference("/images/$fileName")

        init()
    }

    private fun init() {
        button_camera.setOnClickListener(this)
        button_gallery.setOnClickListener(this)
        button_upload_image.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            //dexter open carmera
            button_camera -> {
                requestSinglePermission()
                openCamera()
            }
            //dexter open gallery
            button_gallery -> {
                requestGalleryPermission()
                openGalleryForImage()
            }
            //database store image
            button_upload_image -> {
                uploadImageToFirebaseStorage()
            }
        }
    }


    private fun openCamera() {
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA_CODE)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun requestSinglePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }
                }).check()
    }

    private fun requestGalleryPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                        p1?.continuePermissionRequest()
                    }
                }).check()
    }
lateinit var bmp:Bitmap

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if requestcode = camera
        if (requestCode == REQUEST_CAMERA_CODE) {
             bmp = data?.extras?.get("data") as Bitmap//data type to receive the data from camera
            Log.d("abc", "camera bmp:$bmp")
            image_view_show_photo.setImageBitmap(bmp)
            uri = imageUri(applicationContext, bmp)
            Log.d("abc", "camera uri:$uri")

        }
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK) {
            uri = data?.data
            Log.d("abc", "gallery uri:$uri")
            image_view_show_photo.setImageURI(data?.data)
        }
    }


    private fun imageUri(context: Context?, bmp: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(context?.getContentResolver(), bmp, "Title", null)
        return Uri.parse(path)
    }

//find uri in camera

    //upload
    private fun uploadImageToFirebaseStorage() {
        //from gallery ->putFile()
        if (uri == null) {
            Log.d("abc", "uri is null")
        }
        storageReference.putFile(uri!!)
                .addOnSuccessListener {
        Log.d("abc", "upload successful image is ${it.metadata?.path}")
        Toast.makeText(applicationContext, "Upload Successful", Toast.LENGTH_SHORT).show()
        var email = auth.currentUser.email
         var name = auth.currentUser.displayName
        //download uri
         storageReference.downloadUrl.addOnSuccessListener {
             Log.d("abc", "Download path uri $it")
         //    getOriginList()
             Log.d("abc", "orginial list:$list")
             list?.add(uri!!.toString())
            Log.d("abc", "update list:$list")
             var user = User(uid, email, name, list)

             saveUserToFirebaseDatabase(user)
         }
         }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
    }

//    private fun getOriginList() {
//        databaseReference.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (data in snapshot.children){
//                    if (data.getValue(User::class.java)?.uid == auth.currentUser.uid){
//                        var temp = data.getValue(User::class.java)?.imageUri!!
//                        list = temp
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }

    private fun saveUserToFirebaseDatabase(user:User) {
//        databaseReference.child("imageUri").setValue(list)
//                .addOnSuccessListener {
//                    Log.d("abc", "Save to the database")
//                    Toast.makeText(applicationContext, "Successful save", Toast.LENGTH_SHORT).show()
//                }
        //insert user data
       // var user = User(uid,email,null,null)
        databaseReference.setValue(user)
            .addOnSuccessListener {
                Log.d("abc", "Save to the database")
                Toast.makeText(applicationContext, "Successful save", Toast.LENGTH_SHORT).show()
            }
    }


}