package aj.dev.event.di

import aj.dev.event.data.repository.EventRepository
import aj.dev.event.view.checkin.vm.EventsCheckinMethods
import aj.dev.event.view.detail.vm.EventsDetailMethods
import aj.dev.event.view.list.vm.EventsListMethods
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {

    @Provides
    fun eventListRepository(repository: EventRepository): EventsListMethods = repository

    @Provides
    fun eventDetailRepository(repository: EventRepository): EventsDetailMethods = repository

    @Provides
    fun eventCheckinRepository(repository: EventRepository): EventsCheckinMethods = repository
}