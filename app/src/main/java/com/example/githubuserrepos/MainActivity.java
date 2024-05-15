package com.example.githubuserrepos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText mLoginEdit;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEdit = (EditText)findViewById(R.id.loginEdit);
        mTextView = (TextView)findViewById(R.id.textView);
    }

    public void onClick(View view) {
        String login = mLoginEdit.getText().toString();

        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);

        final Call<List<Repos>> call = gitHubService.getRepos(login);

        call.enqueue(new Callback<List<Repos>>() {
            @Override
            public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                if (response.isSuccessful()) {
                    mTextView.setText(response.body().toString() + "\n");
                    for (int i = 0; i < response.body().size(); i++) {
                        mTextView.append(response.body().get(i).getName() + "\n");
                    }
                } else {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Repos>> call, Throwable t) {
                mTextView.setText("Что-то пошло не так: " + t.getMessage());
            }
        });
    }
}