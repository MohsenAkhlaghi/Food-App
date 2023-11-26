package com.example.foodapp.domain.repository

import com.example.foodapp.data.datasource.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

//این انوتیشن میاد ریپازیتوری رو وصل میکنه به چرخه حیات اکتیویتی و در نتیجه کانفیگوریشن چنج (چرخش صفجه) رو میتونه هندل کنه
@ActivityRetainedScoped
class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
//    val remote = remoteDataSource

}