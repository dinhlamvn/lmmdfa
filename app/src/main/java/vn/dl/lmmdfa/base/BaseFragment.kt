package vn.dl.lmmdfa.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<S : BaseViewModel.ViewModelState> : Fragment() {

    companion object {
        protected const val KEY_ARGS: String = "fragment_args"
    }

    private var rootView: View? = null

    @LayoutRes
    abstract fun layoutRes(): Int

    @UiThread
    abstract fun postAfterViewCreated(view: View)

    abstract fun onViewModelStateChanged(state: S)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return rootView ?: inflater.inflate(layoutRes(), container, false).also { view ->
            rootView = view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAfterViewCreated(view)
    }

    protected inline fun <reified T : BaseViewModel<S>> fragmentViewModel(
        noinline factory: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<T> {
        return lazy {
            val viewModelFactory = factory?.invoke()

            val viewModel = if (viewModelFactory == null) {
                ViewModelProvider(this).get(T::class.java)
            } else {
                ViewModelProvider(requireActivity(), viewModelFactory).get(T::class.java)
            }

            viewModel.viewModelState.observe(this, Observer { newState ->
                onViewModelStateChanged(newState)
            })

            viewModel
        }
    }
}