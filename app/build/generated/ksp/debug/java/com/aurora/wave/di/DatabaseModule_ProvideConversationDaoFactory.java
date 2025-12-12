package com.aurora.wave.di;

import com.aurora.wave.data.dao.ConversationDao;
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
public final class DatabaseModule_ProvideConversationDaoFactory implements Factory<ConversationDao> {
  private final Provider<WaveDatabase> databaseProvider;

  public DatabaseModule_ProvideConversationDaoFactory(Provider<WaveDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ConversationDao get() {
    return provideConversationDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideConversationDaoFactory create(
      Provider<WaveDatabase> databaseProvider) {
    return new DatabaseModule_ProvideConversationDaoFactory(databaseProvider);
  }

  public static ConversationDao provideConversationDao(WaveDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideConversationDao(database));
  }
}
