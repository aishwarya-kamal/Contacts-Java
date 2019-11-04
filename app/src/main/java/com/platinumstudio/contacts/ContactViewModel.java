package com.platinumstudio.contacts;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class ContactViewModel extends AndroidViewModel {

    private ContactRepository mRepository;
    private ContactRepository mContactRepository;

    private LiveData<List<Contact>> mAllContacts;

    public void setmRepository(ContactRepository mRepository) {
        this.mRepository = mRepository;
    }


    public ContactViewModel(Application application){
        super(application);
        mContactRepository = new ContactRepository(application);
        mAllContacts = mContactRepository.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts(){
        return mAllContacts;
    }

    public void insert(Contact contact){
        mContactRepository.insert(contact);
    }

}
