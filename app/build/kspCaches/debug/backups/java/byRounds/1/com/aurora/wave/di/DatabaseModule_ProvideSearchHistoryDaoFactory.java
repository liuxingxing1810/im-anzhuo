package com.aurora.wave.di;

import com.aurora.wave.data.dao.SearchHistoryDao;
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
public final class DatabaseModule_ProvideSearchHistoryDaoFactory implements Factory<SearchHistoryDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideSearchHistoryDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SearchHistoryDao get() {
    return provideSearchHistoryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSearchHistoryDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSearchHistoryDaoFactory(databaseProvider);
  }

  public static SearchHistoryDao provideSearchHistoryDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSearchHistoryDao(database));
  }
}
