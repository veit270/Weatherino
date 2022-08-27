package com.veit.app.weatherino.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.rules.RuleChain

@OptIn(ExperimentalCoroutinesApi::class)
open class CoroutinesRuleTest {
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule: RuleChain = RuleChain.emptyRuleChain()
        .around(instantTaskExecutorRule)
        .around(coroutineRule)
}