package com.example.mithackathon.InternalFun

import android.content.Context
import android.provider.Settings.Global.putString
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
internal fun saveUserToPref(context: Context,uid: String, name: String, type: String){
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    prefs.edit().apply {
        putString("uid", uid)
        putString("name", name)
        putString("type", type)
        apply()
    }
}

internal fun getUserInfoFromPrefs(context: Context): UserInfo {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    return UserInfo(
        uid = prefs.getString("uid", "") ?: "",
        name = prefs.getString("name", "") ?: "",
        type = prefs.getString("type", "") ?: "",
        email = Firebase.auth.currentUser?.email ?: ""
    )
}
data class UserInfo(
    val uid: String,
    val name: String,
    val type: String,
    val email: String
)

//internal fun getUserInfoFromPrefs(context: Context): Triple<String, String, String> {
//    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//    val uid = prefs.getString("uid", "") ?: ""
//    val name = prefs.getString("name", "") ?: ""
//    val type = prefs.getString("type", "") ?: ""
//    return Triple(uid, name, type)
//}