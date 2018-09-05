package com.alokomkar.scrapbook

import android.app.Application
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WordCountViewModelTest {

    private lateinit var mWordCountViewModel : WordCountViewModel

    @Mock private lateinit var mWordCountRepository: WordCountRepository
    @Mock private lateinit var mApplication: Application

    @Before fun setupWordCountViewModel() {
        //Initialize mocks
        MockitoAnnotations.initMocks( this )
        //Initialize VM
        mWordCountViewModel = Mockito.spy(WordCountViewModel(mApplication))
    }

    @Test
    fun verifyUrlParse() {
        mWordCountViewModel.loadTask("https://viacom18.com")


    }

}