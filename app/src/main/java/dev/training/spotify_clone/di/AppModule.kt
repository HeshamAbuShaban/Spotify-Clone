package dev.training.spotify_clone.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.training.spotify_clone.R
import dev.training.spotify_clone.adapters.SwipeMusicAdapter
import dev.training.spotify_clone.exoplayer.MusicServiceConnection
import dev.training.spotify_clone.ui.viewmodels.factory.FactoryParameterImpl
import dev.training.spotify_clone.ui.viewmodels.factory.MainViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // will live as long as the <?> dose
object AppModule {

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context,
    ): MusicServiceConnection = MusicServiceConnection(context)

    @Singleton
    @Provides
    fun provideSwipeMusicAdapter() = SwipeMusicAdapter()

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext
        context: Context,
    ) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_app_launcher_foreground)
                .error(R.drawable.ic_music)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )


    @Singleton
    @Provides
    fun provideMainViewModelFactory(
        @ApplicationContext
        context: Context,
    ) = MainViewModelFactory(FactoryParameterImpl(context))

}