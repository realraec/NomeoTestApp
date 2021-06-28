package xyz.realraec.nomeotesttechnique;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int PICK_IMAGE = 1;
    private Button completeSignupButton, addPictureButton, removePictureButton;
    private TextView nameTextView, emailTextView, phoneTextView;
    private ImageView imageView;
    private boolean pictureAdded = false;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    // Inside fragment (and not activity), so findViewById only accessible after onCreate/onCreateView
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        // Binding views
        addPictureButton = view.findViewById(R.id.button_add_picture);
        removePictureButton = view.findViewById(R.id.button_remove_picture);
        completeSignupButton = view.findViewById(R.id.button_complete_signup);
        nameTextView = view.findViewById(R.id.editText_name);
        emailTextView = view.findViewById(R.id.editText_email);
        phoneTextView = view.findViewById(R.id.editText_phone);
        imageView = view.findViewById(R.id.imageView);

        // Adding listeners
        addPictureButton.setOnClickListener(addPictureButtonListener);
        removePictureButton.setOnClickListener(removePictureButtonListener);
        completeSignupButton.setOnClickListener(completeSignupButtonListener);

        super.onViewCreated(view, savedInstanceState);
    }


    // Button listeners
    private final View.OnClickListener addPictureButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE);
        }
    };

    private final View.OnClickListener removePictureButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageView.setImageURI(null);
            pictureAdded = false;
        }
    };

    private final View.OnClickListener completeSignupButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
                        String name = nameTextView.getText().toString();
            String email = emailTextView.getText().toString();
            String phone = phoneTextView.getText().toString();
            Bitmap picture;
            if (pictureAdded) {
                imageView.setDrawingCacheEnabled(true);
                picture = imageView.getDrawingCache();
            } else {
                picture = null;
            }

            SignupItem signupItem = new SignupItem(name, email, phone, picture);


            if (!name.equals("") && !email.equals("") && !phone.equals("") && validEmail(email)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpsURLConnection connection = null;
                        try {
                            URL url = new URL("https://test-pgt-dev.apnl.ws/authentication/register");

                            connection = (HttpsURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setRequestProperty("X-AP-Key", "uD4Muli8nO6nzkSlsNM3d1Pm");
                            connection.setRequestProperty("X-AP-DeviceUID", "trial");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("Content-Type", "multipart/form-data");
                            OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                            byte[] temp2 = signupItem.toString().getBytes();
                            out.write(temp2);
                            out.close();

                            Log.i("Exchange JSON", "Sending: " + signupItem.toString());
                            int responseCode = connection.getResponseCode();
                            Log.i("Exchange JSON", "Response code: " + responseCode);

                            InputStream in = new BufferedInputStream(connection.getInputStream());
                            Scanner scanner = new Scanner(in);
                            String temp = scanner.nextLine();
                            JSONObject x = new JSONObject(temp);
                            in.close();
                            Log.i("Exchange JSON", "Receiving: " + x.toString());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        nameTextView.setEnabled(false);
                                        emailTextView.setEnabled(false);
                                        phoneTextView.setEnabled(false);
                                        addPictureButton.setEnabled(false);
                                        removePictureButton.setEnabled(false);
                                        completeSignupButton.setEnabled(false);
                                        imageView.setImageURI(null);
                                        Toast.makeText(v.getContext(), x.getJSONObject("success").getString("message"),
                                                Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) connection.disconnect();
                        }
                    }
                }).start();

            } else {
                Toast.makeText(v.getContext(), R.string.toast_check_info, Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageView.setImageURI(data.getData());
            pictureAdded = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean validEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}