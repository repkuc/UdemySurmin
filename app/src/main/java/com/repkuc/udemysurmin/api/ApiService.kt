package com.repkuc.udemysurmin.api

import com.repkuc.udemysurmin.pojo.CoinInfoListOfData
import com.repkuc.udemysurmin.pojo.CoinPriceInfoRawData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top/totalvolfull")
    fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "33e9aff24a036e97a935bba23c1223772c0a4f1fa83b50767d77bce492066cf5",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): Single<CoinInfoListOfData>

    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "33e9aff24a036e97a935bba23c1223772c0a4f1fa83b50767d77bce492066cf5",
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY,
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String
    ): Single<CoinPriceInfoRawData>


    companion object{

        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_API_KEY = "api_key"

        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"

        private const val CURRENCY = "EUR"

    }

}