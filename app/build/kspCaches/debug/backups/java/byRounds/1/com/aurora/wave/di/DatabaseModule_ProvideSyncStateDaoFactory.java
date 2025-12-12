package com.aurora.wave.di;

import com.aurora.wave.data.dao.SyncStateDao;
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
public final class DatabaseModule_ProvideSyncStateDaoFactory implements Factory<SyncStateDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideSyncStateDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SyncStateDao get() {
    return provideSyncStateDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSyncStateDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSyncStateDaoFactory(databaseProvider);
  }

  public static SyncStateDao provideSyncStateDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSyncStateDao(database));
  }
}
