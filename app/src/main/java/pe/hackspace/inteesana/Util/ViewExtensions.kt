package pe.hackspace.libertrash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

val View.ctx: Context
    get() = context

fun Context.toast(text:CharSequence, length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,text,length).show()
}

fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

inline fun <reified T : Activity> Activity.navigate(id: String) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("id", id)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.navigate() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Context> Context.navigate() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}


