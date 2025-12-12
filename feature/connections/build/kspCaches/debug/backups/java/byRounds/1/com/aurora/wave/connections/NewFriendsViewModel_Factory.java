package com.aurora.wave.connections;

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
public final class NewFriendsViewModel_Factory implements Factory<NewFriendsViewModel> {
  @Override
  public NewFriendsViewModel get() {
    return newInstance();
  }

  public static NewFriendsViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NewFriendsViewModel newInstance() {
    return new NewFriendsViewModel();
  }

  private static final class InstanceHolder {
    private static final NewFriendsViewModel_Factory INSTANCE = new NewFriendsViewModel_Factory();
  }
}
