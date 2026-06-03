package com.mazpiss.skripsi.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.mazpiss.skripsi.data.repository.AuthRepositoryImpl
import com.mazpiss.skripsi.data.repository.KosakataRepositoryImpl
import com.mazpiss.skripsi.data.repository.MateriRepositoryImpl
import com.mazpiss.skripsi.data.repository.ProgressRepositoryImpl
import com.mazpiss.skripsi.data.repository.QuizRepositoryImpl
import com.mazpiss.skripsi.domain.repository.AuthRepository
import com.mazpiss.skripsi.domain.repository.KosakataRepository
import com.mazpiss.skripsi.domain.repository.MateriRepository
import com.mazpiss.skripsi.domain.repository.ProgressRepository
import com.mazpiss.skripsi.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindKosakataRepository(impl: KosakataRepositoryImpl): KosakataRepository

    @Binds
    @Singleton
    abstract fun bindMateriRepository(impl: MateriRepositoryImpl): MateriRepository

    @Binds
    @Singleton
    abstract fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(impl: ProgressRepositoryImpl): ProgressRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
