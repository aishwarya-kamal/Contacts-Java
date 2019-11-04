package com.platinumstudio.contacts;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepository {

    private ContactDao mContactDao;

    private LiveData<List<Contact>> mAllContacts;

    ContactRepository(Application application){

        ContactRoomDatabase contactRoomDatabase = ContactRoomDatabase.getDatabase(application);
        mContactDao = contactRoomDatabase.contactDao();
        mAllContacts = mContactDao.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts() {
        return mAllContacts;
    }


    public void insert (Contact contact) {
        new insertAsyncTask(mContactDao).execute(contact);
    }

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao mContactDao;

        insertAsyncTask(ContactDao dao) {
            mContactDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            mContactDao.insert(params[0]);
            return null;
        }
    }
}
