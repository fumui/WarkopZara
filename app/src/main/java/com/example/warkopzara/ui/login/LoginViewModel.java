package com.example.warkopzara.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.warkopzara.Config;
import com.example.warkopzara.data.model.LoggedInUser;
import com.example.warkopzara.R;
import com.example.warkopzara.providers.MyUrlRequestCallback;
import com.google.gson.JsonObject;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class LoginViewModel extends ViewModel {
    private static final String TAG  = "LoginViewModel";
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    LoginViewModel() {

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(Context context, String username, String password) {
        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.addProperty("username", username);
        requestBodyJson.addProperty("password", password);
        String jsonBody = requestBodyJson.toString();

        CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        MyUrlRequestCallback requestCallback = new MyUrlRequestCallback(result -> {
            try {
                JSONObject data = new JSONObject(result.toString());
                String responseBody = data.getString("body");
                int statusCode = data.getInt("statusCode");
                if (statusCode == 200) { //Some kind of success
                    JSONObject body = new JSONObject(responseBody);
                    int id = body.getJSONObject("data").getInt("id");
                    String fullName = body.getJSONObject("data").getString("fullname");
                    String token = body.getString("token");
                    loginResult.postValue(new LoginResult(new LoggedInUser(String.valueOf(id), fullName, token)));
                } else {
                    loginResult.postValue(new LoginResult(R.string.login_failed));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        UrlRequest.Builder requestBuilder = cronetEngine
                .newUrlRequestBuilder(
                        Config.BE_URL+"/api/v1/user/login",
                        requestCallback,
                        executor
                )
                .setHttpMethod("POST")
                .addHeader("Content-Type","application/json")
                .setUploadDataProvider(UploadDataProviders.create(jsonBody.getBytes()),executor);

        UrlRequest request = requestBuilder.build();
        request.start();

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return !username.trim().isEmpty();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }
}