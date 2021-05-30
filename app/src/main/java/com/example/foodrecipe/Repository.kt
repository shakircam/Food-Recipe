package com.example.foodrecipe

import com.example.foodrecipe.data.local.LocalDataSource
import com.example.foodrecipe.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource) {

    val remote = remoteDataSource
    val local = localDataSource
}