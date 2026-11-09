package rsanzjo.entreteclasytinta.template.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rsanzjo.entreteclasytinta.template.common.platform.platformModule
import rsanzjo.entreteclasytinta.template.data.datasource.SessionManager
import rsanzjo.entreteclasytinta.template.data.datasource.api.AppApi
import rsanzjo.entreteclasytinta.template.data.datasource.api.client.createHttpClient
import rsanzjo.entreteclasytinta.template.data.repository.impl.AuthRepositoryImpl
import rsanzjo.entreteclasytinta.template.data.repository.impl.ProfileRepositoryImpl
import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository
import rsanzjo.entreteclasytinta.template.domain.repository.ProfileRepository
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.impl.AuthUseCaseImpl
import rsanzjo.entreteclasytinta.template.domain.useCase.impl.ProfileUseCaseImpl
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginViewModel

private val dataModule = module {
    single { SessionManager(storage = get()) }
    single { createHttpClient(sessionManager = get()) }
    single { AppApi(client = get()) }

    factory<AuthRepository> { AuthRepositoryImpl(api = get(), sessionManager = get()) }
    factory<ProfileRepository> { ProfileRepositoryImpl(api = get()) }
}

private val domainModule = module {
    factory<AuthUseCase> { AuthUseCaseImpl(authRepository = get()) }
    factory<ProfileUseCase> { ProfileUseCaseImpl(repository = get()) }
}

private val presentationModule = module {
    viewModelOf(::LoginViewModel)
}

val appModules = listOf(platformModule, dataModule, domainModule, presentationModule)

/** Arranca Koin con los módulos de la app; [config] permite a cada plataforma añadir su contexto. */
fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModules)
    }
}
