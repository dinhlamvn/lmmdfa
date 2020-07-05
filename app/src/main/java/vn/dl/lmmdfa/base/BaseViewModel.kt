package vn.dl.lmmdfa.base

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import vn.dl.lmmdfa.common.SingleLiveEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

abstract class BaseViewModel<S : BaseViewModel.ViewModelState>(viewModelState: S) : ViewModel() {

    interface ViewModelState

    private val compositeDisposable = CompositeDisposable()

    private val stateOperatorQueue: Queue<() -> Unit> = ConcurrentLinkedQueue<() -> Unit>()

    private val stateRunnable = object : Runnable {
        override fun run() {
            if (pendingSetState) {
                stateHandler.post(this)
                return
            }
            val block = stateOperatorQueue.poll()
            block?.invoke()
            if (stateOperatorQueue.isNotEmpty()) {
                stateHandler.post(this)
            }
        }
    }

    @Volatile
    private var pendingSetState: Boolean = false

    private val stateHandlerThread = HandlerThread("getStateThread")
    private val stateHandler by lazy {
        stateHandlerThread.start()
        Handler(stateHandlerThread.looper)
    }

    private val subject = BehaviorSubject.create<S>()

    init {
        subject.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                _viewModelState.setValue(state)
                pendingSetState = false
            }
            .disposeOnClear()
    }

    private val _viewModelState = SingleLiveEvent<S>()
    val viewModelState: LiveData<S>
        get() = _viewModelState

    init {
        _viewModelState.setValue(viewModelState)
    }

    protected fun Disposable.disposeOnClear(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun <T> Observable<T>.execute(block: S.(T) -> S): Disposable {
        return subscribe { value ->
            val currentState = viewModelState.value ?: return@subscribe
            val newState = block.invoke(currentState, value)
            setState(newState)
        }.disposeOnClear()
    }

    private fun setState(newState: S) {
        val setStateBlock = {
            subject.onNext(newState)
            pendingSetState = true
        }
        stateOperatorQueue.add(setStateBlock)
        triggerOperator()
    }

    @Synchronized
    protected fun setState(block: S.() -> S) {
        val setStateBlock = {
            val state =
                viewModelState.value ?: throw IllegalAccessException("ViewModelState is null.")
            subject.onNext(block.invoke(state))
            pendingSetState = true
        }
        stateOperatorQueue.add(setStateBlock)
        triggerOperator()
    }

    @Synchronized
    protected fun getState(block: (S) -> Unit) {
        val getStateBlock = {
            val state =
                viewModelState.value ?: throw IllegalAccessException("ViewModelState is null.")
            block.invoke(state)
        }
        stateOperatorQueue.add(getStateBlock)
        triggerOperator()
    }

    @Synchronized
    private fun triggerOperator() {
        stateHandler.removeCallbacks(stateRunnable)
        stateHandler.post(stateRunnable)
    }
}