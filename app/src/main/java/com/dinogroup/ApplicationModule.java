package com.dinogroup;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;

import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.dinogroup.analytics.AnalyticsModule;
import com.dinogroup.android.AndroidModule;
import com.dinogroup.environment.Environment;
import com.dinogroup.environment.EnvironmentModule;
import com.dinogroup.repository.JsonSharedPreferencesRepository;
import com.dinogroup.repository.PresenterSharedRepository;
import com.dinogroup.service.ApiService;
import com.dinogroup.service.StubApiService;
import com.dinogroup.util.gson.GsonModule;

import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(
  includes = {
    AndroidModule.class,
    AnalyticsModule.class,
    EnvironmentModule.class,
    GsonModule.class
  },
  library = true
)
public class ApplicationModule {
  private Application application;

  ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  android.app.Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  Bus providesBus() {
    return new Bus(); // my name is Otto and I love to get blotto
  }

  @Provides
  @Singleton
  JsonSharedPreferencesRepository provideSharedPreferenceRepository(GsonBuilder gsonBuilder, SharedPreferences sharedPreferences) {
    return new JsonSharedPreferencesRepository(gsonBuilder, sharedPreferences);
  }

  @Provides
  @Singleton
  PresenterSharedRepository providePresenterSharedRepository() {
    return new PresenterSharedRepository();
  }

  @Provides
  @Singleton
  ApiService provideApiService(Environment environment) {
    if (environment.getName().equals("Local")) {
      return new StubApiService();
    } else {
      RestAdapter restAdapter = new RestAdapter.Builder()
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setEndpoint(environment.getApiHost() + environment.getApiBasePath())
        .build();
      return restAdapter.create(ApiService.class);
    }
  }

  @Provides
  @Singleton
  Validator provideValidator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }
}
