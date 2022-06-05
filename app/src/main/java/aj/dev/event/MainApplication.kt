package aj.dev.event

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApplication : MultiDexApplication()
//{
//    override fun onCreate() {
//        super.onCreate()
//        try {
//            ProviderInstaller.installIfNeeded(applicationContext)
//            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
//            sslContext.init(null, null, null)
//            sslContext.createSSLEngine()
//        } catch (e: GooglePlayServicesRepairableException) {
//            e.printStackTrace()
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyManagementException) {
//            e.printStackTrace()
//        }
//    }
//}