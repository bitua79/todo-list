package com.example.todolist.core.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

inline fun <T> Flow<T>.collectOnFragment(fragment: Fragment, crossinline onCollect: (T) -> Unit) {
    fragment.lifecycleScope.launchWhenStarted {
        this@collectOnFragment.collectLatest {
            onCollect(it)
        }
    }
}

inline fun <T> Flow<T>.collectOnActivity(
    activity: AppCompatActivity,
    crossinline onCollect: (T) -> Unit
) {
    activity.lifecycleScope.launchWhenStarted {
        this@collectOnActivity.collectLatest {
            onCollect(it)
        }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.transparent(){
    alpha = 0F
}

fun View.unTransparent(){
    alpha = 1F
}

fun View.touchDelegate() {
    val parent = parent as View
    parent.post {
        val r = Rect()
        this.getHitRect(r)
        r.top -= 23
        r.left -= 24
        r.bottom += 32
        r.right += 24
        parent.touchDelegate = TouchDelegate(r, this)
    }
}

fun View.hideKeyboard() {
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@ColorInt
fun Fragment.getColorFromResource(@ColorRes colorResId: Int) =
    ResourcesCompat.getColor(resources, colorResId, null)

@ColorInt
fun Context.getColorFromResource(@ColorRes colorResId: Int) =
    ResourcesCompat.getColor(resources, colorResId, null)

fun TextInputLayout.dismissError() {
    if(this.error != null) {
        isErrorEnabled = false
        error = null
    }
}
