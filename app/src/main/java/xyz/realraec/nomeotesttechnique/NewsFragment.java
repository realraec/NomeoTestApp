package xyz.realraec.nomeotesttechnique;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button reloadButton;
    private RecyclerView recyclerView;
    private NewsItemAdapter newsItemAdapter;
    private ArrayList<NewsItem> newsList;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reloadButton = view.findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(button3Listener);
        reloadButton.performClick();
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    View.OnClickListener button3Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String temp = "https://test-pgt-dev.apnl.ws/events";


            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(temp);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("X-AP-Key", "uD4Muli8nO6nzkSlsNM3d1Pm");
                        connection.setRequestProperty("X-AP-DeviceUID", "trial");
                        connection.setRequestProperty("Accept", "application/json");
                        InputStream inputStream = new BufferedInputStream(connection.getInputStream());

                        int responseCode = connection.getResponseCode();
                        Log.i("Exchange JSON", "Response code: " + responseCode);


                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        Scanner scanner = new Scanner(in);
                        String temp = scanner.nextLine();
                        JSONArray jsonArray = new JSONArray(temp);
                        Log.i("Exchange JSON", "Receiving " + jsonArray.toString());
                        in.close();
                        inputStream.close();


                        newsList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NewsItem newsItem = new NewsItem((JSONObject) jsonArray.get(i));
                            newsList.add(newsItem);
                        }
                        Collections.sort(newsList);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                newsItemAdapter = new NewsItemAdapter(getContext(), newsList);
                                recyclerView.setAdapter(newsItemAdapter);
                            }
                        });


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }
    };

}