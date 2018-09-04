package com.alokomkar.scrapbook

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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
        mWordCountAdapter = WordCountAdapter( mContentList, mWordCountViewModel!!.mCount )
        rvContent.adapter = mWordCountAdapter

        refreshLayout.setOnRefreshListener {
            fab.callOnClick()
        }

        mWordCountViewModel?.fetchParsedData()?.observe(this, this )

        fab.setOnClickListener {
            if( validate() ) {
                refreshLayout.isRefreshing = true
                mWordCountViewModel?.loadTask( etUrl.text.toString().trim() )
            }
        }

        etCount.addTextChangedListener( object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                if( etCount.text.toString().trim().isNotEmpty() ) {
                    val count = etCount.text.toString().trim().toInt()
                    if( count > 0 ) {
                        mWordCountViewModel!!.mCount = count
                        mWordCountAdapter.mCount = count
                        mWordCountAdapter.notifyDataSetChanged()
                    }
                    else {
                        mWordCountViewModel!!.mCount = -1
                        mWordCountAdapter.mCount = -1
                        mWordCountAdapter.notifyDataSetChanged()
                    }
                }
                else {
                    mWordCountViewModel!!.mCount = -1
                    mWordCountAdapter.mCount = -1
                    mWordCountAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun validate(): Boolean {
        if( etUrl.text.toString().trim().isEmpty() ) {
            etUrl.requestFocus()
            Snackbar.make( etUrl, R.string.error_missing_url, Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onChanged(t: List<WordCount>?) {
        if( t != null ) {
            mContentList.clear()
            mContentList.addAll(t)
            mWordCountAdapter.notifyItemRangeChanged(0, mContentList.size )
        }
        if( refreshLayout != null ) refreshLayout.isRefreshing = false
    }

    fun toggleFilter() {
        mWordCountViewModel?.toggleFilter( !mWordCountViewModel?.mIsFiltered!! )
        fab.callOnClick()
    }
}
