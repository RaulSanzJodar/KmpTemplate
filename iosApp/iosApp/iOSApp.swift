import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        // Arranca Koin (inyección de dependencias del módulo compartido) una sola vez.
        MainViewControllerKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
