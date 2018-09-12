package com.alokomkar.scrapbook

import android.app.Application
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WordCountViewModelTest {

    private lateinit var mWordCountViewModel : WordCountViewModel

    @Mock private lateinit var mDataAPI: DataAPI
    @Mock private lateinit var mApplication: Application

    @Before fun setupWordCountViewModel() {
        //Initialize mocks
        MockitoAnnotations.initMocks( this )
        //Initialize VM
        mWordCountViewModel = WordCountViewModel(Mockito.mock(Application::class.java))
    }

    @Test
    fun verifyUrlParse() {
        mWordCountViewModel.loadTask("https://viacom18.com")
        verify( mDataAPI ).fetchParsedData()
    }

}