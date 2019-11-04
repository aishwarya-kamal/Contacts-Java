package com.platinumstudio.contacts;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platinumstudio.contacts.adapter.ContactListAdapter;
import com.platinumstudio.contacts.mydetails.MyDetailsActivity;
import com.platinumstudio.contacts.newcontact.NewContactActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity  {

    private FloatingActionButton fab;
    private List<Contact> contactList;

    private ContactViewModel mContactViewModel;

    public static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;
    private ContactListAdapter mContactListAdapter;
    private ContactRepository mRepository;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mContactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        mContactViewModel.setmRepository(mRepository);

        mContactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {

            @Override
            public void onChanged(List<Contact> contacts) {
                mContactListAdapter.setContacts(contacts);
            }
        });

        fab = findViewById(R.id.fab);

        mRecyclerView = findViewById(R.id.recycler_view);

        contactList = new ArrayList<>();
        mContactListAdapter = new ContactListAdapter(contactList, this);

        mRepository = new ContactRepository(getApplication());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mContactListAdapter);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactListActivity.this, NewContactActivity.class);
                startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            Contact contact = new Contact(
                    data.getStringExtra(NewContactActivity.EXTRA_CONTACT_NAME),
                    data.getLongExtra(NewContactActivity.EXTRA_CONTACT_NUMBER, 0),
                    data.getStringExtra(NewContactActivity.EXTRA_CONTACT_EMAIL),
                    data.getStringExtra(NewContactActivity.EXTRA_CONTACT_PROFILE_PICTURE));

            mContactViewModel.insert(contact);

        } else {

            Toast.makeText(
                    getApplicationContext(),
                    "Not Saved! Please add all data",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setMaxWidth(Integer.MAX_VALUE);

        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);

        search(searchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_my_details: {

                Intent intentOpenMyDetailsActivity = new Intent(this,
                        MyDetailsActivity.class);
                startActivity(intentOpenMyDetailsActivity);

                break;
            }

            case R.id.action_search: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // When query is submitted filter the recycler view
                mContactListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mContactListAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}