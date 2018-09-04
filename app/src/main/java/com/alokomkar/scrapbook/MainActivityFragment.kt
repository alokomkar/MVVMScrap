package com.alokomkar.scrapbook

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), Observer<List<WordCount>> {


    private val mContentList = ArrayList<WordCount>()
    private lateinit var mWordCountAdapter : WordCountAdapter
    private var mWordCountViewModel: WordCountViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWordCountViewModel = activity?.let { ViewModelProviders.of(it).get( WordCountViewModel::class.java ) }

        rvContent.layoutManager = LinearLayoutManager( context )
        mWordCountAdapter = WordCountAdapter( mContentList )
        rvContent.adapter = mWordCountAdapter

        refreshLayout.setOnRefreshListener {
            fab.callOnClick()
        }

        fetchData()

        fab.setOnClickListener {
            refreshLayout.isRefreshing = true
            mWordCountViewModel?.loadTask()
        }
    }

    private fun fetchData() {
        mWordCountViewModel?.fetchParsedData()?.observe(this, this )
    }

    override fun onChanged(t: List<WordCount>?) {
        if( t != null ) {
            mContentList.addAll(t)
            mWordCountAdapter.notifyItemRangeChanged(0, mContentList.size )
        }
        if( refreshLayout != null ) refreshLayout.isRefreshing = false
    }
}
