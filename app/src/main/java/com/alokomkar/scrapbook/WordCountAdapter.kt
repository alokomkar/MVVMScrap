package com.alokomkar.scrapbook

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

@SuppressLint("InflateParams")
class WordCountAdapter( private var contentList: ArrayList<WordCount>, var mCount: Int ) : RecyclerView.Adapter<WordCountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_word_count, null))

    override fun getItemCount(): Int
            = if( mCount > 0 ) mCount else contentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bindData(contentList[position])


    inner class ViewHolder( itemView : View) : RecyclerView.ViewHolder( itemView )  {

        private val tvWord = itemView.findViewById<TextView>(R.id.tvWord)
        private val tvCount = itemView.findViewById<TextView>(R.id.tvCount)

        fun bindData( wordCount: WordCount ) {
            tvWord.text = wordCount.word
            tvCount.text = wordCount.count.toString()
        }

    }
}