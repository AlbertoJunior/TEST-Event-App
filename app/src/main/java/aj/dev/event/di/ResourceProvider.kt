package aj.dev.event.di

import aj.dev.event.listener.ProviderHandler
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ResourceProvider {
    @Provides
    fun eventListRepository(@ApplicationContext context: Context): ProviderHandler {
        return object : ProviderHandler {
            override fun getString(id: Int): String {
                return context.getString(id)
            }

            override fun getColor(id: Int): Int {
                return ContextCompat.getColor(context, id)
            }

            override fun getDrawable(id: Int): Drawable? {
                return ContextCompat.getDrawable(context, id)
            }
        }
    }
}