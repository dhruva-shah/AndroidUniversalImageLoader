package indianic.com.universalimageloaderdemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by indianic on 04/02/16.
 */
public class GetContactList  {

    private Context mContext;
    private boolean isError;
    private ArrayList<ContactModel> listContactModel = new ArrayList<ContactModel>();
    private ArrayList<ContactModel> listContactModelDublicate = new ArrayList<ContactModel>();

    public boolean isError() {
        return isError;
    }

    /**
     * @param context
     */
    public GetContactList(Context context) {
        super();
        this.mContext = context;

    }

    /****************************************************************************
     * @purpose:This Method use to get Device PhoneBook ContactsList (id,Name,Photo,phone number)
     ***************************************************************************/

    public void executeService() {
        try {

            isError = false;
            Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            while (phones.moveToNext()) {

                final String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                final String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String firstname = "";
                String lastname = "";
                final String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^0-9]+", "");
                final String image_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                final String photo_id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String email = "";

                if (name.contains(" ")) {
                    List<String> items = Arrays.asList(name.split(" "));
                    lastname = items.get(items.size() - 1);
                    int endIndex = name.lastIndexOf(" ");

                    if (endIndex != -1) {
                        firstname = name.substring(0, endIndex);
                    }

                } else {
                    firstname = name;
                }


                final ContactModel contactModel = new ContactModel();
                contactModel.setId(id);
                contactModel.setFirstname(firstname);
                contactModel.setLastname(lastname);
                contactModel.setPhoneNumber(phoneNumber);
                contactModel.setEmail(email);

                if (image_uri != null && !image_uri.isEmpty()) {
                    contactModel.setImage_uri(image_uri);
                } else {
                    contactModel.setImage_uri("");
                }
                listContactModelDublicate.add(contactModel);
            }

            String prev_section = "";


            for (ContactModel mContactModel : listContactModelDublicate) {
                if (!mContactModel.getPhoneNumber().equalsIgnoreCase(prev_section)) {
                    final ContactModel contactModel = new ContactModel();
                    contactModel.setId(mContactModel.getId());
                    contactModel.setFirstname(mContactModel.getFirstname());
                    contactModel.setLastname(mContactModel.getLastname());

                    contactModel.setPhoneNumber(mContactModel.getPhoneNumber());
                    contactModel.setEmail(mContactModel.getEmail());

                    contactModel.setImage_uri(mContactModel.getImage_uri());

                    listContactModel.add(contactModel);
                    prev_section = mContactModel.getPhoneNumber();
                }

            }

            Log.e("SIZE", "Orignal Name size :-" + listContactModelDublicate.size());
            Log.e("SIZE", "new Name size :-" + listContactModel.size());

        } catch (Exception e) {
            isError = true;
            e.printStackTrace();
        }


    }



    public ArrayList<ContactModel> getListContactModel(){
        return listContactModel;
    }


}
