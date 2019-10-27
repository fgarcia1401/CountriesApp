package com.fgarcia.countriesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fgarcia.countriesapp.di.DaggerApiComponent;
import com.fgarcia.countriesapp.model.CountriesService;
import com.fgarcia.countriesapp.model.CountryModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends ViewModel {

    public MutableLiveData<List<CountryModel>> countries = new MutableLiveData<>();
    public MutableLiveData<Boolean> countryLoadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public ListViewModel() {
        super();
        DaggerApiComponent.create().inject(this);
    }

    @Inject
    public CountriesService countriesService;

    private CompositeDisposable disposable = new CompositeDisposable();

    public void reflesh(){
        fetchCountries();
    }

    private void fetchCountries() {
        loading.setValue(true);
        disposable.add(
                countriesService.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CountryModel>>() {
                    @Override
                    public void onSuccess(List<CountryModel> countriesModels) {
                        countries.setValue(countriesModels);
                        countryLoadError.setValue(false);
                        loading.setValue(false);
                    }
                    @Override
                    public void onError(Throwable e) {
                        countryLoadError.setValue(true);
                        loading.setValue(false);
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
