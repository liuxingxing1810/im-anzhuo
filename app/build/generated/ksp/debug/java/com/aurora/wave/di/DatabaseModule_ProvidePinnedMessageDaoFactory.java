package com.aurora.wave.di;

import com.aurora.wave.data.dao.PinnedMessageDao;
import com.aurora.wave.data.database.WaveDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvidePinnedMessageDaoFactory implements Factory<PinnedMessageDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvidePinnedMessageDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PinnedMessageDao get() {
    return providePinnedMessageDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePinnedMessageDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePinnedMessageDaoFactory(databaseProvider);
  }

  public static PinnedMessageDao providePinnedMessageDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePinnedMessageDao(database));
  }
}
