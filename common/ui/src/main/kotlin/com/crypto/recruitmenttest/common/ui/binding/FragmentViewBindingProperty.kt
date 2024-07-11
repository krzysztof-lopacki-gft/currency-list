package com.crypto.recruitmenttest.common.ui.binding

import android.view.View
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingProperty<T : ViewBinding>(
    private val viewBinder: ViewBinder<T>,
) : ReadOnlyProperty<Fragment, T> {
    private var viewBinding: T? = null

    @UiThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewBinding?.let { return it }
        val view = thisRef.requireView()
        thisRef.viewLifecycleOwner.lifecycle.addObserver(BindingLifecycleObserver())
        return viewBinder.bind(view).also { vb -> viewBinding = vb }
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            viewBinding = null
        }
    }
}

fun interface ViewBinder<T : ViewBinding> {
    fun bind(view: View): T
}

/**
 * Create new [ViewBinding] associated with the [Fragment][this]
 */
@Suppress("unused")
fun <T : ViewBinding> Fragment.viewBinding(vb: ViewBinder<T>): ReadOnlyProperty<Fragment, T> = FragmentViewBindingProperty(vb)
