package com.example.credhub;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Vector;

import static com.example.credhub.SDMWebRepo.WS_METHOD_EXPORT;
import static com.example.credhub.SDMWebRepo.WS_METHOD_IMPORT;
import static com.example.credhub.SDMWebRepo.WS_METHOD_LIST;
import static com.example.credhub.SDMWebRepo.WS_NAMESPACE;
import static com.example.credhub.SDMWebRepo.androidHttpTransport;
import static com.example.credhub.SDMWebRepo.headerList_basic_auth;

public class RepositoryTask extends AsyncTask<String[], Void, Void> {
    protected Void doInBackground(String[]... params) {
        String[] data = params[0];
        Vector<SoapPrimitive> listIds = new Vector<>();

        // Selecting diverse operations depending on the parameters we receive
        if (data[0].equals("export")) {
            headerList_basic_auth = null;
            androidHttpTransport = new HttpTransportSE("http://10.0.2.2/SDM/WebRepo?wsdl");
            SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_EXPORT);
            PropertyInfo propId = new PropertyInfo();
            propId.name = "arg0";
            propId.setValue(data[1]);
            propId.type = PropertyInfo.STRING_CLASS;
            request.addProperty(propId);
            PropertyInfo propUser = new PropertyInfo();
            propUser.name = "arg1";
            propUser.setValue(data[2]);
            propUser.type = PropertyInfo.STRING_CLASS;
            request.addProperty(propUser);
            PropertyInfo propPass = new PropertyInfo();
            propPass.name = "arg2";
            propPass.setValue(data[3]);
            propPass.type = PropertyInfo.STRING_CLASS;
            request.addProperty(propPass);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("\"" + WS_NAMESPACE + WS_METHOD_EXPORT + "\"", envelope, headerList_basic_auth);
                System.out.println("Export result: " + envelope.getResponse().toString());
                //Toast.makeText(ShowPasswordExportRegistry.this,"Export result: " + envelope.getResponse().toString(),Toast.LENGTH_LONG).show();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data[0].equals("retrieve_list")) {
            headerList_basic_auth = null;
            androidHttpTransport = new HttpTransportSE("http://10.0.2.2/SDM/WebRepo?wsdl");
            SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_LIST);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("\"" + WS_NAMESPACE + WS_METHOD_LIST + "\"", envelope, headerList_basic_auth);
                if(envelope.getResponse() instanceof Vector) // 2+ elements
                    listIds.addAll((Vector<SoapPrimitive>) envelope.getResponse());
                else if(envelope.getResponse() instanceof SoapPrimitive) // 1 element
                    listIds.add((SoapPrimitive)envelope.getResponse());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("List of records stored on the repo: ");
            for(int i = 0; i < listIds.size(); i++)
            {
                if (!listIds.get(i).toString().equals("null")) GlobalClass.repositoryItems.add(listIds.get(i).toString());
                System.out.println("- " + listIds.get(i).toString());
            }
            System.out.println();
        } else if (data[0].equals("import")) {
            SoapObject request = new SoapObject(WS_NAMESPACE, WS_METHOD_IMPORT);
            PropertyInfo propId = new PropertyInfo();
            propId.name = "arg0";
            propId.setValue(GlobalClass.selectedItem);
            propId.type = PropertyInfo.STRING_CLASS;
            request.addProperty(propId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            try {
                androidHttpTransport.call("\"" + WS_NAMESPACE + WS_METHOD_IMPORT + "\"", envelope, headerList_basic_auth);
                Vector<SoapPrimitive> importedRecord = (Vector<SoapPrimitive>)envelope.getResponse();
                if(importedRecord.size() == 3)
                {
                    System.out.println("Record imported successfully: ");
                    System.out.println("ID: " + importedRecord.get(0));
                    System.out.println("Username: " + importedRecord.get(1));
                    System.out.println("Password: " + importedRecord.get(2));

                    String[] projection = {
                            Database.DatabaseEntry.COLUMN_NAME_1
                    };
                    String selection = Database.DatabaseEntry.COLUMN_NAME_1 + " = ?";
                    String[] selectionArgs = { GlobalClass.selectedItem };
                    String sortOrder = Database.DatabaseEntry.COLUMN_NAME_1 + " DESC";
                    Cursor cursor = GlobalClass.db.query(
                            Database.DatabaseEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                    ContentValues values = new ContentValues();

                    // We need to check for a possible insert or update
                    if (cursor.getCount() > 0) {
                        values.put(Database.DatabaseEntry.COLUMN_NAME_1, "" + importedRecord.get(0));
                        values.put(Database.DatabaseEntry.COLUMN_NAME_2, "" + importedRecord.get(1));
                        values.put(Database.DatabaseEntry.COLUMN_NAME_3, "" + importedRecord.get(2));
                        String select = Database.DatabaseEntry.COLUMN_NAME_1 + " LIKE ?";
                        String[] selectArgs = { GlobalClass.selectedItem };
                        GlobalClass.db.update(
                                Database.DatabaseEntry.TABLE_NAME,
                                values,
                                select,
                                selectArgs
                        );
                        cursor.close();
                    } else {
                        values.put(Database.DatabaseEntry.COLUMN_NAME_1, "" + importedRecord.get(0));
                        values.put(Database.DatabaseEntry.COLUMN_NAME_2, "" + importedRecord.get(1));
                        values.put(Database.DatabaseEntry.COLUMN_NAME_3, "" + importedRecord.get(2));
                        GlobalClass.db.insert(Database.DatabaseEntry.TABLE_NAME, null, values);
                    }
                }
                else
                    System.out.println("Import error - " + importedRecord.get(0));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
