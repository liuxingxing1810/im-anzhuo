package com.aurora.wave.di;

import com.aurora.wave.data.dao.UserDao;
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
public final class DatabaseModule_ProvideUserDaoFactory implements Factory<UserDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideUserDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public UserDao get() {
    return provideUserDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideUserDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideUserDaoFactory(databaseProvider);
  }

  public static UserDao provideUserDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideUserDao(database));
  }
}
