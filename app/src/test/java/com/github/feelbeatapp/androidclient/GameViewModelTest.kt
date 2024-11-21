package com.github.feelbeatapp.androidclient

import com.github.feelbeatapp.androidclient.rules.MainDispatcherRule
import com.github.feelbeatapp.androidclient.ui.game.GameViewModel
import com.github.feelbeatapp.androidclient.utils.FakeNetworkAgent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

const val TEST_TEXT = "Very simple text"

/**
 * Example simple unit test
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    @Test
    fun gameViewModel_receiveMessage_updatesState() = runTest {
        val fakeNetworkAgent = FakeNetworkAgent()
        val vm = GameViewModel(fakeNetworkAgent)

        advanceUntilIdle()
        fakeNetworkAgent.incoming.emit(TEST_TEXT)

        assertEquals(TEST_TEXT, vm.state.first().textInput)
    }

    @Test
    fun gameViewModel_onTextInput_sendMessage() = runTest {
        val fakeNetworkAgent = FakeNetworkAgent()
        val vm = GameViewModel(fakeNetworkAgent)

        advanceUntilIdle()
        vm.setText(TEST_TEXT)
        advanceUntilIdle()

        assertEquals(1, fakeNetworkAgent.sentMessages.size)
        assertEquals(TEST_TEXT, fakeNetworkAgent.sentMessages.first())
    }
}
