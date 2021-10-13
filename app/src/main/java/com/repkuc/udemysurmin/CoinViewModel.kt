package com.repkuc.udemysurmin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.repkuc.udemysurmin.api.ApiFactory
import com.repkuc.udemysurmin.database.AppDatabase
import com.repkuc.udemysurmin.pojo.CoinPriceInfo
import com.repkuc.udemysurmin.pojo.CoinPriceInfoRawData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CoinViewModel(application: Application): AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun loadData(){
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("test_of_loading_data", it.toString())
            },{
                Log.d("test_of_loading_data", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
            coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo>{
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject
        if (jsonObject == null) return result
        var coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet){
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet){
                val priceInfo = Gson().fromJson(currencyJson.getAsJsonObject(currencyKey), CoinPriceInfo::class.java)
                result.add(priceInfo)
            }
        }
        return result
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

