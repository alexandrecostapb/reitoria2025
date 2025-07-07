package br.com.example.reitoria2025;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseApp.initializeApp(this);

        EditText nomeEditText = findViewById(R.id.nomeEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText senhaEditText = findViewById(R.id.senhaEditText);
        Button loginButton = findViewById(R.id.loginButton);

        SharedPreferences preferences = getSharedPreferences("dados", MODE_PRIVATE);
        String nomeSalvo = preferences.getString("nome", "");
        nomeEditText.setText(nomeSalvo);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String senha = senhaEditText.getText().toString().trim();

                if (!nome.equals("") && !email.equals("") && !senha.equals("")) {
                    auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> { //para login
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login bem-sucedido: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("nome", nome);
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //se o login falhar, tenta cadastrar
                            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(createTask -> {
                                if (createTask.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Usuário criado: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("nome", nome);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Erro: " + createTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}