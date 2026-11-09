package rsanzjo.entreteclasytinta.template

import android.app.Application
import org.koin.android.ext.koin.androidContext
import rsanzjo.entreteclasytinta.template.di.initKoin

class TemplateApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@TemplateApp) }
    }
}
