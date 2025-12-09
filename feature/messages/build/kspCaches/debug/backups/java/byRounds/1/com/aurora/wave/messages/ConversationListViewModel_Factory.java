package com.aurora.wave.messages;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ConversationListViewModel_Factory implements Factory<ConversationListViewModel> {
  @Override
  public ConversationListViewModel get() {
    return newInstance();
  }

  public static ConversationListViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ConversationListViewModel newInstance() {
    return new ConversationListViewModel();
  }

  private static final class InstanceHolder {
    private static final ConversationListViewModel_Factory INSTANCE = new ConversationListViewModel_Factory();
  }
}
