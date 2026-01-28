package live.mehiz.mpvkt

import android.app.Application
import live.mehiz.mpvkt.di.AppModule
import live.mehiz.mpvkt.di.DatabaseModule
import live.mehiz.mpvkt.di.FileManagerModule
import live.mehiz.mpvkt.di.PreferencesModule
import live.mehiz.mpvkt.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.dsl.koinConfiguration

/**
 * Library entry point for initializing Koin and crash handling.
 * Host apps should call MpvLibrary.initialize(application) in their Application.onCreate().
 */
object MpvLibrary {

  private var initialized = false

  @OptIn(KoinExperimentalAPI::class)
  fun initialize(app: Application) {
    if (initialized) return
    initialized = true

    // Start Koin manually
    startKoin {
      androidContext(app)
      modules(
        AppModule,
        PreferencesModule,
        DatabaseModule,
        FileManagerModule,
        ViewModelModule,
      )
    }
  }
}
