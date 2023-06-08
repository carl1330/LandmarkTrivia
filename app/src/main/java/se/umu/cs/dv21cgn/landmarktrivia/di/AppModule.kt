package se.umu.cs.dv21cgn.landmarktrivia.di

import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import se.umu.cs.dv21cgn.landmarktrivia.data.TriviaCardListImpl
import se.umu.cs.dv21cgn.landmarktrivia.data.TriviaCardListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlacesClient(app: Application): PlacesClient {
        return Places.createClient(app)
    }

    @Provides
    @Singleton
    fun provideTriviaCardListRepository(client: PlacesClient) : TriviaCardListRepository {
        return TriviaCardListImpl(client)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(app: Application) : FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

}