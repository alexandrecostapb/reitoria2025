package br.com.example.reitoria2025;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        ImageView adicionarSugestaoButton = findViewById(R.id.adicionarSugestaoButton);
        ImageView sugestoesButton = findViewById(R.id.sugestoesButton);
        ImageView logoutButton = findViewById(R.id.logoutButton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences sharedPreferences = getSharedPreferences("app-config", MODE_PRIVATE);
        String nomeUsuarioLogado = sharedPreferences.getString("nome", "Sem nome");
        String email = sharedPreferences.getString("email", "Sem email");

        adicionarSugestaoButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.activity_adicionar_sugestao_dialog, null);

            EditText nomeEditText = view.findViewById(R.id.nomeEditText);
            EditText sugestaoEditText = view.findViewById(R.id.sugestaoEditText);
            CheckBox anonimoCheckBox = view.findViewById(R.id.anonimoCheckBox);
            Spinner categoriaSpinner = view.findViewById(R.id.categoriasSpinner);

            //carrega valores do spinner a partir do xml
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    MainActivity.this,
                    R.array.categoriasSpinner,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriaSpinner.setAdapter(adapter);

            nomeEditText.setText(nomeUsuarioLogado);
            nomeEditText.setEnabled(false);

            new AlertDialog.Builder(MainActivity.this)
                    .setView(view)
                    .setPositiveButton("Salvar", (dialog, which) -> {
                        String texto = sugestaoEditText.getText().toString().trim();
                        boolean anonimo = anonimoCheckBox.isChecked();
                        String nomeParaSalvar = anonimo ? "" : nomeUsuarioLogado;
                        String categoriaSelecionada = categoriaSpinner.getSelectedItem().toString();

                        if (!texto.isEmpty() && !categoriaSelecionada.equals("Selecione uma categoria")) {
                            Sugestao sugestao = new Sugestao(nomeParaSalvar, texto, anonimo, categoriaSelecionada);

                            FirebaseFirestore.getInstance()
                                    .collection("sugestoes")
                                    .add(sugestao)
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(MainActivity.this, "Sugestão salva!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MainActivity.this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                                    //.addOnFailureListener(e ->
                                        //Toast.makeText(MainActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(MainActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        sugestoesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListaDeSugestoesActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(v -> logout());

    }

    private void logout() {
        // Firebase Sign-out
        mAuth.signOut();

        // Google Sign-out
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Vai para tela de login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}