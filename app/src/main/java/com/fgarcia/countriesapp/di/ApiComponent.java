package com.fgarcia.countriesapp.di;

import com.fgarcia.countriesapp.model.CountriesService;
import com.fgarcia.countriesapp.viewmodel.ListViewModel;

import dagger.Component;

@Component(modules = {ApiModule.class})
public interface ApiComponent {
    void inject(CountriesService service);
    void inject(ListViewModel listViewModel);
}
