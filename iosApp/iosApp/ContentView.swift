import SwiftUI
import Shared

/// Aloja la UI de Compose Multiplatform definida en el módulo `shared`.
struct ComposeView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {

    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
