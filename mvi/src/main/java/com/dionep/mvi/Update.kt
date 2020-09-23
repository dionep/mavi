package com.dionep.mvi

/**
 * Created by Damir Rakhimulin on 23.09.2020.
 * damirpq1@gmail.com
 * tg: dima2828
 */

data class Update<State, Cmd>(
    val state: State? = null,
    val cmd: Cmd? = null
) {

    companion object {
        fun <State, Cmd> state(state: State) =
            Update<State, Cmd>(state = state, cmd = null)

        fun <State, Cmd> cmd(cmd: Cmd) =
            Update<State, Cmd>(cmd = cmd, state = null)
    }
}
