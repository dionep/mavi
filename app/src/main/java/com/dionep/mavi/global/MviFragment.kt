package com.dionep.mavi.global

import com.dionep.mvi.Feature
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Damir Rakhimulin on 23.09.2020.
 * damirpq1@gmail.com
 * tg: dima2828
 */

abstract class MviFragment<State, News> : BaseFragment() {

    abstract val feature: Feature<State, *, *, News>
    val compositeDisposable = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        compositeDisposable.addAll(
            feature.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    println("State Thread: ${Thread.currentThread()}")
                    renderState(it)
                },
            feature.news
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    println("News Thread: ${Thread.currentThread()}")
                    renderNews(it) }
        )
    }

    abstract fun renderState(state: State)
    abstract fun renderNews(news: News)

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        feature.dispose()
        super.onDestroy()
    }

}