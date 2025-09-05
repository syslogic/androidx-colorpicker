package io.syslogic.demo.colorpicker

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import io.syslogic.demo.colorpicker.activity.ComposeActivity

import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Compose Activity Test Case
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4::class)
@Deprecated("kept for reference")
class TestComposeActivity : TestSuite() {

    @get:Rule
    val testRule: AndroidComposeTestRule<ActivityScenarioRule<ComposeActivity>, ComposeActivity> =
        createAndroidComposeRule(ComposeActivity::class.java)
}
