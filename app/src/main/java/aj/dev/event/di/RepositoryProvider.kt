package aj.dev.event.di

import aj.dev.event.data.repository.EventRepository
import aj.dev.event.vm.EventsMethods
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {

    @Provides
    fun eventRepository(repository: EventRepository): EventsMethods = repository
}