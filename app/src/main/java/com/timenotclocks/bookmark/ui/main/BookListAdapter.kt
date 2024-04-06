/*
* 2021 Fenimore Love
* */

package com.timenotclocks.bookmark.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.timenotclocks.bookmark.BookViewActivity
import com.timenotclocks.bookmark.EXTRA_ID
import com.timenotclocks.bookmark.R
import com.timenotclocks.bookmark.database.*


const val LOG_BOOK_ADAPTER = "BookAdapter"
const val EXTRA_BOOK = "Bookintent"

class BookListAdapter : ListAdapter<Book, BookListAdapter.BookViewHolder>(BOOKS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

        return BookViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)
        holder.itemView.setOnClickListener {
            Log.i("BookId", "Loading book ${current.bookId}")
            val intent = Intent(it.context, BookViewActivity::class.java).apply {
                putExtra(EXTRA_ID, current.bookId)
            }
            it.context.startActivity(intent)
        }
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.book_list_main_view)
        private val authorView: TextView = itemView.findViewById(R.id.book_list_sub_view)
        private val coverView: ImageView = itemView.findViewById(R.id.book_list_cover_view)
        private val yearView: TextView = itemView.findViewById(R.id.book_list_caption_view_1)
        // private val dateView: TextView = itemView.findViewById(R.id.book_list_caption_view_2)
        // private val shelfView: TextView = itemView.findViewById(R.id.book_list_caption_view_3)
        private val ratingView: RatingBar = itemView.findViewById(R.id.book_list_rating_bar)
        private val bookmarkView: ImageView = itemView.findViewById(R.id.book_list_bookmark)

        fun bind(book: Book?) {
            book?.let { b ->
                titleView.text = b.titleString()
                authorView.text = "by ${b.authorString()}"
                b.yearString()?.let { yearView.text = it }
                b.cover("L")?.let {
                    ImageLoader().load(itemView.context, it, b.titleString(), coverView)
                }

                if (b.bookmark) {
                    bookmarkView.setImageResource(R.drawable.ic_baseline_bookmark_32)
                } else {
                    bookmarkView.setImageResource(R.drawable.ic_baseline_bookmark_empty_32)
                }
                when (b.shelf) {
                    ShelfType.ReadShelf.shelf -> {
                        b.rating?.let {
                            ratingView.visibility = View.VISIBLE
                            ratingView.rating = it.toFloat()
                        }
                    }
                    // shelfView.text = "Currently Reading"
                    // shelfView.visibility = View.VISIBLE
                    else -> {
                    }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): BookViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.book_view_list_item, parent, false)
                return BookViewHolder(view)
            }
        }
    }

    companion object {
        private val BOOKS_COMPARATOR = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}
