package com.dionep.mvi

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class Feature<State, Cmd, Msg, News> (
    initialState: State,
    initialMessages: Set<Msg> = setOf(),
    private val reducer: (Msg, State) -> Update<State, Cmd>,
    private val commandHandler: (Cmd) -> Observable<SideEffect<Msg, News>>
) {

    private val compositeDisposable = CompositeDisposable()

    private val stateSubject = BehaviorSubject.create<State>().apply {
        onNext(initialState)
    }
    private val newsSubject = PublishSubject.create<News>()
    private val msgSubject = BehaviorSubject.create<Msg>()
    private val cmdSubject = BehaviorSubject.create<Cmd>()

    init {
        compositeDisposable.addAll(
            cmdSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { cmd ->
                    commandHandler.invoke(cmd)
                        .subscribeOn(Schedulers.io())
                        .subscribe { (msg, news) ->
                            if (msg != null) msgSubject.onNext(msg)
                            if (news != null) newsSubject.onNext(news)
                        }
                },
            msgSubject
                .doOnSubscribe {
                    initialMessages.forEach { msgSubject.onNext(it) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { msg ->
                    println("msg ${Thread.currentThread()}")
                    val (state, cmd) = reducer.invoke(msg, stateSubject.value)
                    if (state != null) stateSubject.onNext(state)
                    if (cmd != null) cmdSubject.onNext(cmd)
                }
        )
    }

    fun getCurrentState(): State = stateSubject.value

    fun accept(msg: Msg) {
        msgSubject.onNext(msg)
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    val state: Observable<State>
        get() = stateSubject.subscribeOn(Schedulers.io())

    val news: Observable<News>
        get() = newsSubject.subscribeOn(Schedulers.io())

}