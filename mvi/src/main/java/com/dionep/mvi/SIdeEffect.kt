package com.dionep.mvi

/**
 * Created by Damir Rakhimulin on 23.09.2020.
 * damirpq1@gmail.com
 * tg: dima2828
 */

data class SideEffect<out Msg, out News>(
    val msg: Msg? = null,
    val news: News? = null
) {

    companion object {
        fun <Msg, News> msg(msg: Msg) =
            SideEffect<Msg, News>(msg = msg, news = null)

        fun <Msg, News> news(news: News) =
            SideEffect<Msg, News>(msg = null, news = news)
    }
}