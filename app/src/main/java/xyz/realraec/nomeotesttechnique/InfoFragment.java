package xyz.realraec.nomeotesttechnique;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    private WebView webview;
    private Button switchButton;
    private String[] urlArray;
    private int counter = 0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webview = view.findViewById(R.id.webview);
        switchButton = view.findViewById(R.id.button_switch);
        switchButton.setOnClickListener(button4Listener);
        String url1 = "https://test-pgt-dev.apnl.ws/html";
        String url2 = "https://www.google.com";
        urlArray = new String[]{url1, url2};
        switchButton.performClick();
    }

    View.OnClickListener button4Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            webview.setWebViewClient(new WebViewClient());
            Map<String, String> additionalHeaders = new HashMap<>();
            additionalHeaders.put("X-AP-Key", "uD4Muli8nO6nzkSlsNM3d1Pm");
            additionalHeaders.put("X-AP-DeviceUID", "trial");
            additionalHeaders.put("Accept", "text/html");
            // There seems to be something incompatible between the Android/Chrome WebView and the web service?
            // Or am I missing something?
            // I have added a button to switch between your /html page and another webpage (google)
            // So you can see that I do manage to load the other webpage but not yours
            // The error given is visible in the app: net::ERR_INVALID_SIGNED_EXCHANGE
            // Note that I could still access the html content of your webpage with cURL
            webview.loadUrl(urlArray[counter % 2], additionalHeaders);
            counter++;
        }
    };
}