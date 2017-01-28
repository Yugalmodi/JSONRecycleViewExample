package com.techpalle.jsonviewpagerexample;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
    Button button;
    RecyclerView recyclerView;
    MyTask myTask;
    MyAdapter myAdapter;
    ArrayList<Contacts> arrayList;

    public class MyTask extends AsyncTask<String, Void, String> {
        URL myUrl;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        InputStreamReader streamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder builder = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                myUrl = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                inputStream = httpURLConnection.getInputStream();
                streamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(streamReader);
                line = bufferedReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                return builder.toString();
            }
            catch (MalformedURLException e) {
                Log.e("MyError", "MalFOrmed");
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.e("MyError", "IO");
                e.printStackTrace();
            }
            catch (SecurityException e) {
                Log.e("MyError", "Security");
                e.printStackTrace();
            }

            Log.e("MyError", "WRONG");
            return "Some Thing Went Wrong";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Log.e("MyError", s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray k = jsonObject.getJSONArray("contacts");
                for (int i = 0; i<k.length(); i++){
                    JSONObject m = k.getJSONObject(i);
                    String name = m.getString("name");
                    String email = m.getString("email");
                    JSONObject phone = m.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    Contacts contacts = new Contacts();
                    contacts.setSno(i+1);
                    contacts.setName(name);
                    contacts.setEmail(email);
                    contacts.setMobile(mobile);
                    arrayList.add(contacts);
                }
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("B34", "JSON PARSING ERROR");
            }
            super.onPostExecute(s);
        }
    }

    public boolean checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null || networkInfo.isConnected() == false){
            return false;
        }
        return true;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.row, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Contacts contacts = arrayList.get(position);
            holder.textViewNo.setText(""+contacts.getSno()+".");
            holder.textViewName.setText(contacts.getName());
            holder.textViewEmail.setText(contacts.getEmail());
            holder.textViewMobile.setText(contacts.getMobile());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewNo, textViewName, textViewEmail, textViewMobile;
            public ViewHolder(View itemView) {
                super(itemView);
                textViewNo = (TextView) itemView.findViewById(R.id.text_view_no);
                textViewName = (TextView) itemView.findViewById(R.id.text_view_name);
                textViewEmail = (TextView) itemView.findViewById(R.id.text_view_email);
                textViewMobile = (TextView) itemView.findViewById(R.id.text_view_mobile);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_two, container, false);
        button = (Button) v.findViewById(R.id.button);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        myTask = new MyTask();
        arrayList= new ArrayList<Contacts>();
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()){
                    if(myTask.getStatus() == AsyncTask.Status.RUNNING || myTask.getStatus() == AsyncTask.Status.FINISHED){
                        Toast.makeText(getActivity(), "Already Running, Please Wait......", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myTask.execute("http://api.androidhive.info/contacts/");
                }
                else {
                    Toast.makeText(getActivity(), "PLEASE Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
