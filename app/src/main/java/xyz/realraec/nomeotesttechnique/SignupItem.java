package xyz.realraec.nomeotesttechnique;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class SignupItem {

    private final String name;
    private final String email;
    private final String phone;
    private final Bitmap picture;

    public SignupItem(String name, String email, String phone, Bitmap picture) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        // Even though the picture is optional, always included (can be null)
        this.picture = picture;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPicture() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @NotNull
    @Override
    public String toString() {
        // Creating a JSONObject whose toString method is in JSON syntax
        JSONObject temp = new JSONObject();
        try {
            temp.put("name", this.name);
            temp.put("email", this.email);
            temp.put("phone", this.phone);
            if (picture != null) {
                temp.put("picture", getPicture());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp.toString();
    }
}
