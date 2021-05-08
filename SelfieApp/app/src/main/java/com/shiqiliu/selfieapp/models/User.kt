package com.shiqiliu.selfieapp.models

import android.net.Uri
import java.io.Serializable

data class User(
    var uid: String? = null,
    var email: String? = null,
    var name: String? = null,
    var imageUri: ArrayList<String>? = null,
    //var imageUri: ArrayList<Uri>? = null
) : Serializable {
    companion object {
        const val COLL_NAME = "users"
    }
}
