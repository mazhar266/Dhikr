package fi.mazhar.dhikr.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import fi.mazhar.dhikr.DhikrRepository
import fi.mazhar.dhikr.SystemClock
import platform.UIKit.UIViewController

/**
 * The single Compose host controller. [IosPlatform] presents document pickers
 * from it; the lookup is deferred to click time so the controller can finish
 * constructing first.
 */
private var rootViewController: UIViewController? = null

/** Entry point consumed by the Swift shell in iosApp/. */
fun MainViewController(): UIViewController {
    val viewController = ComposeUIViewController {
        val platform = remember { IosPlatform { rootViewController!! } }
        CompositionLocalProvider(LocalAppPlatform provides platform) {
            DhikrApp(AppHolder.model)
        }
    }
    rootViewController = viewController
    return viewController
}

/** Process-wide holder so the model survives SwiftUI re-creation of ContentView. */
private object AppHolder {
    val model: AppModel by lazy {
        val repo = DhikrRepository(IosDhikrStore(), SystemClock())
        repo.load()
        AppModel(repo)
    }
}
