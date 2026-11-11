package rsanzjo.entreteclasytinta.template.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rsanzjo.entreteclasytinta.template.common.platform.platformModule
import rsanzjo.entreteclasytinta.template.data.datasource.supabase.createAppSupabaseClient
import rsanzjo.entreteclasytinta.template.data.repository.impl.AuthRepositoryImpl
import rsanzjo.entreteclasytinta.template.data.repository.impl.ProfileRepositoryImpl
import rsanzjo.entreteclasytinta.template.domain.repository.AuthRepository
import rsanzjo.entreteclasytinta.template.domain.repository.ProfileRepository
import rsanzjo.entreteclasytinta.template.domain.useCase.AuthUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.ProfileUseCase
import rsanzjo.entreteclasytinta.template.domain.useCase.impl.AuthUseCaseImpl
import rsanzjo.entreteclasytinta.template.domain.useCase.impl.ProfileUseCaseImpl
import rsanzjo.entreteclasytinta.template.presentation.AppViewModel
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.forgotpassword.ForgotPasswordViewModel
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.login.LoginViewModel
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.newpassword.NewPasswordViewModel
import rsanzjo.entreteclasytinta.template.presentation.feature.auth.register.RegisterViewModel
import rsanzjo.entreteclasytinta.template.presentation.feature.main.home.HomeViewModel

private val dataModule = module {
    single { createAppSupabaseClient(storage = get()) }

    // `single`: guarda estado (si el cierre de sesión lo pidió el usuario) entre llamadas.
    single<AuthRepository> { AuthRepositoryImpl(supabase = get()) }
    factory<ProfileRepository> { ProfileRepositoryImpl(supabase = get()) }
}

private val domainModule = module {
    factory<AuthUseCase> { AuthUseCaseImpl(authRepository = get()) }
    factory<ProfileUseCase> { ProfileUseCaseImpl(repository = get()) }
}

private val presentationModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::NewPasswordViewModel)
    viewModelOf(::HomeViewModel)
}

val appModules = listOf(platformModule, dataModule, domainModule, presentationModule)

/** Arranca Koin con los módulos de la app; [config] permite a cada plataforma añadir su contexto. */
fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModules)
    }
}
