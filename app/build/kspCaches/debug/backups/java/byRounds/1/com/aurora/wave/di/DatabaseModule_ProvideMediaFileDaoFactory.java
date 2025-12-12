package com.aurora.wave.di;

import com.aurora.wave.data.dao.MediaFileDao;
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
public final class DatabaseModule_ProvideMediaFileDaoFactory implements Factory<MediaFileDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideMediaFileDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MediaFileDao get() {
    return provideMediaFileDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMediaFileDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMediaFileDaoFactory(databaseProvider);
  }

  public static MediaFileDao provideMediaFileDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMediaFileDao(database));
  }
}
