package com.project.mvvm_booksearch.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.mvvm_booksearch.network.BookListModel
import com.project.mvvm_booksearch.network.RetroInstance
import com.project.mvvm_booksearch.network.RetroService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel: ViewModel() {

    var bookList: MutableLiveData<BookListModel> = MutableLiveData()

    fun getBookListObserver(): MutableLiveData<BookListModel> {
        return bookList
    }

    fun makeApiCall(query: String) {

        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        retroInstance.getBookListFromApi(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getBookListObserverRx())

    }

    private fun getBookListObserverRx(): Observer<BookListModel> {
        return object : Observer<BookListModel> {

            override fun onComplete() {
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onError(e: Throwable) {
                bookList.postValue(null)
            }

            override fun onNext(t: BookListModel) {
                bookList.postValue(t)
            }

            override fun onSubscribe(d: Disposable) {

            }

        }
    }

}