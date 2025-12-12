package com.aurora.wave.di;

import com.aurora.wave.data.dao.DraftDao;
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
public final class DatabaseModule_ProvideDraftDaoFactory implements Factory<DraftDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideDraftDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DraftDao get() {
    return provideDraftDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDraftDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDraftDaoFactory(databaseProvider);
  }

  public static DraftDao provideDraftDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDraftDao(database));
  }
}
