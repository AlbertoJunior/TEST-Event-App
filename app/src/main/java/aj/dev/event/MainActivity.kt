package aj.dev.event

import aj.dev.event.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.root.post {
//            navController.graph
//        }
    }

//    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
//        try {
//            ProviderInstaller.installIfNeeded(this)
//        } catch (e: GooglePlayServicesRepairableException) {
//            // Thrown when Google Play Services is not installed, up-to-date, or enabled
//            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
//            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            Log.e("SecurityException", "Google Play Services not available.")
//        }
//    }
}