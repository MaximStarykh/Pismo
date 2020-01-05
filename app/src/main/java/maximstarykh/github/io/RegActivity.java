package maximstarykh.github.io;

import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) { // юезр не авторизований

            setContentView(R.layout.activity_reg);

            mFirebaseAuth = FirebaseAuth.getInstance();
            emailId = findViewById(R.id.editText);
            password = findViewById(R.id.editText2);
            btnSignUp = findViewById(R.id.button2);
            tvSignIn = findViewById(R.id.textView);

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailId.getText().toString();
                    String pwd = password.getText().toString();
                    if (email.isEmpty()) {
                        emailId.setError("Будь ласка ведіть свою пошту");
                        emailId.requestFocus();
                    } else if (pwd.isEmpty()) {
                        password.setError("Будь ласка ведіть свій пароль");
                        password.requestFocus();
                    }
                    else if (email.isEmpty() && pwd.isEmpty()) {
                        Toast.makeText(RegActivity.this, "Поля пусті!", Toast.LENGTH_SHORT).show();
                    } else if (!(email.isEmpty() && pwd.isEmpty())) {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegActivity.this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegActivity.this, "Щось пішло не так, спробуйте пізніше",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            startActivity(new Intent(RegActivity.this, HomeActivity.class));
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegActivity.this, "Помилка!", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(RegActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });

        } else {
            Intent inte = new Intent(RegActivity.this, HomeActivity.class);
            startActivity(inte);
        }
    }
}
