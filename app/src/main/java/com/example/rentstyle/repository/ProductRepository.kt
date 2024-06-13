package com.example.rentstyle.repository

import androidx.paging.*
import com.example.rentstyle.model.data.database.ProductDatabase
import com.example.rentstyle.model.data.local.LocalProduct
import com.example.rentstyle.model.data.network.ProductRemoteMediator
import com.example.rentstyle.model.network.ApiService
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class ProductRepository(
    private val database: ProductDatabase,
    private val apiService: ApiService
) {

    fun getProducts(): Flow<PagingData<LocalProduct>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(apiService, database.productDao()),
            pagingSourceFactory = { database.productDao().getProducts() }
        ).flow
    }
}
