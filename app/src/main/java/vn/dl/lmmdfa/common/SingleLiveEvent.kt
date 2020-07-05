package vn.dl.lmmdfa.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val atomicBoolean = AtomicBoolean(false)

    override fun setValue(value: T) {
        atomicBoolean.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { data ->
            if (atomicBoolean.compareAndSet(true, false)) {
                observer.onChanged(data)
            }
        })
    }
}