package com.aurora.wave.di;

import com.aurora.wave.data.dao.ReactionDao;
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
public final class DatabaseModule_ProvideReactionDaoFactory implements Factory<ReactionDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideReactionDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ReactionDao get() {
    return provideReactionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideReactionDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideReactionDaoFactory(databaseProvider);
  }

  public static ReactionDao provideReactionDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideReactionDao(database));
  }
}
