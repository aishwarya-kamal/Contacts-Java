package com.platinumstudio.contacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.platinumstudio.contacts.Contact;
import com.platinumstudio.contacts.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>
        implements Filterable {

    private List<Contact> contactList;
    private List<Contact> filteredContactList;

    private Context context;

    public void setContacts(List<Contact> contacts) {
        this.contactList = contacts;
        filteredContactList = contacts;
        notifyDataSetChanged();
    }

    public ContactListAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.filteredContactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        if(filteredContactList != null){

            final Contact currentContact = filteredContactList.get(position);

            holder.name.setText(currentContact.getName());
            holder.number.setText(String.valueOf(currentContact.getNumber()));
            holder.email.setText(currentContact.getEmail());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_account_circle_black_24dp);

            Uri selectedImageURI = Uri.parse(currentContact.getImage());

            Glide.with(holder.imageView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(selectedImageURI)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);

            holder.callImageButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    String uri = "tel:" + currentContact.getNumber();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    context.startActivity(intent);
                }
            });

            holder.emailImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String to = currentContact.getEmail();

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});

                    email.setType("message/rfc822");
                    context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (filteredContactList != null) {
            return filteredContactList.size();
        }
        return 0;
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView email;
        private final TextView number;
        private final ImageView imageView;

        private ImageButton callImageButton;
        private ImageButton emailImageButton;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            number = itemView.findViewById(R.id.number);
            imageView = itemView.findViewById(R.id.image);

            callImageButton = itemView.findViewById(R.id.image_button_call);
            emailImageButton = itemView.findViewById(R.id.image_button_email);

        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredContactList = contactList;

                } else {

                    List<Contact> filteredList = new ArrayList<>();

                    for (Contact contact : contactList) {

                        if (contact.getName().toLowerCase().contains(charString.toLowerCase())
                            || contact.getEmail().toLowerCase().contains(charString.toLowerCase())){

                            filteredList.add(contact);
                        }
                    }
                    filteredContactList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredContactList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filteredContactList = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
