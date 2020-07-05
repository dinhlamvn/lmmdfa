package vn.dl.lmmdfa.base

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView

abstract class BaseModelViewController<S : BaseViewModel.ViewModelState> {

    private val baseListAdapter: BaseListAdapter = BaseListAdapter()

    private val builds: MutableList<BaseListAdapter.BaseModelView> = mutableListOf()

    private val handlerThread = HandlerThread("buildModelViewThread")
    private val buildModelHandler by lazy {
        handlerThread.start()
        Handler(handlerThread.looper)
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    internal fun requestBuild(state: S) {
        buildModelHandler.post {
            builds.clear()
            buildModelView(state)
            mainHandler.post {
                baseListAdapter.submitChange(ArrayList(builds))
            }
        }
    }

    internal fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = baseListAdapter
    }

    abstract fun buildModelView(state: S)

    protected fun <MV : BaseListAdapter.BaseModelView> MV.addToController() {
        builds.add(this)
    }

    protected fun <MV : BaseListAdapter.BaseModelView> List<MV>.addToController() {
        builds.addAll(this)
    }
}