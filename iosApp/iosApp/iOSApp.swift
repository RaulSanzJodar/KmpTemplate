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
                // Enlaces de autenticación (confirmar correo, recuperar contraseña, login social):
                // llegan con el esquema de URL declarado en Info.plist (CFBundleURLTypes).
                .onOpenURL { url in
                    MainViewControllerKt.onOpenUrl(url: url)
                }
        }
    }
}
