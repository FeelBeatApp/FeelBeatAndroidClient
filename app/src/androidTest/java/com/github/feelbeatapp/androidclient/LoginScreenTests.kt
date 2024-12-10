package com.github.feelbeatapp.androidclient

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.feelbeatapp.androidclient.ui.login.LoginScreen
import com.github.feelbeatapp.androidclient.ui.login.LoginViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Example instrumented test */
@RunWith(AndroidJUnit4::class)
class LoginScreenTests {
    private val loginViewModelMock = mockk<LoginViewModel>(relaxed = true)

    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setupLoginScreen() {
        composeTestRule.setContent { LoginScreen(loginViewModel = loginViewModelMock) }
    }

    @Test
    fun loginScreen_loggingIntoSpotify_triggersOnLoggedIn() {
        composeTestRule.onNodeWithStringId(R.string.login_with_spotify).performClick()

        verify(exactly = 1) { loginViewModelMock.login(any()) }
    }
}
