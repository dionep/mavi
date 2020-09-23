package com.dionep.mavi.ui

import com.dionep.mavi.global.MviFragment
import com.dionep.mavi.R
import com.dionep.mvi.Feature
import com.dionep.mvi.SideEffect
import com.dionep.mvi.Update
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Damir Rakhimulin on 23.09.2020.
 * damirpq1@gmail.com
 * tg: dima2828
 */

class MainFragment : MviFragment<MainFragment.State, MainFragment.News>() {

    override val layoutRes: Int?
        get() = R.layout.fragment_main

    override val feature by lazy {
        Feature<State, Cmd, Msg, News>(
            initialState = State(),
            initialMessages = setOf(Msg.Init),
            reducer = { msg, state ->
                when (msg) {
                    Msg.Init -> Update(cmd = Cmd.Get)
                    is Msg.SetList -> Update(state.copy(list = msg.list))
                }
            },
            commandHandler = {
                when (it) {
                    is Cmd.Get -> Observable.just(
                        SideEffect<Msg, News>(
                            msg = Msg.SetList(listOf("1", "2", "3")),
                            news = News.Success
                        )
                    )
                }
            }
        )
    }

    data class State(
        val list: List<String> = listOf()
    )

    sealed class Cmd {
        object Get : Cmd()
    }
    sealed class Msg {
        object Init : Msg()
        data class SetList(val list: List<String>) : Msg()
    }

    sealed class News {
        object Success : News()
    }

    override fun renderState(state: State) {
        println("render state: $state")
    }

    override fun renderNews(news: News) {
        println("render news: $news" )
    }

}