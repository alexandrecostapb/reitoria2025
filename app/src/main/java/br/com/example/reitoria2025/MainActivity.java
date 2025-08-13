package br.com.example.reitoria2025;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_CAMPUS = "campus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#008744"));
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        TextView saudacaoTextView = findViewById(R.id.saudacaoTextView);
        TextView infoCampusTextView = findViewById(R.id.infoCampusTextView);


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

        saudacaoTextView.setText("Olá, " + nomeUsuarioLogado + "!");

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String campusSalvo = prefs.getString(KEY_CAMPUS, "");
        if (!campusSalvo.isEmpty()) {
            infoCampusTextView.setText("Campus: " + campusSalvo);
        } else {
            infoCampusTextView.setText("Campus: informe seu campus");
        }

        infoCampusTextView.setOnClickListener(v -> {
            // Infla o layout do diálogo com o Spinner e o EditText
            View dialogView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.activity_informar_campus, null);
            Spinner campusSpinner = dialogView.findViewById(R.id.campus2Spinner);
            EditText outroEditTextDialog = dialogView.findViewById(R.id.outro2EditText);

            // Configura o Spinner
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    MainActivity.this,
                    R.array.campusSpinner, // lista no strings.xml
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            campusSpinner.setAdapter(adapter);

            // Mostra/oculta o campo "Outro"
            campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();
                    if (selected.equals("Outro")) {
                        outroEditTextDialog.setVisibility(View.VISIBLE);
                    } else {
                        outroEditTextDialog.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });

            // Cria e exibe o diálogo
            new AlertDialog.Builder(MainActivity.this)
                    .setView(dialogView)
                    .setPositiveButton("Salvar", (dialog, which) -> {
                        String campusSelecionado = campusSpinner.getSelectedItem().toString();

                        // Se for "Outro", pega o texto digitado
                        if (campusSelecionado.equals("Outro")) {
                            String outroDigitado = outroEditTextDialog.getText().toString().trim();
                            if (!outroDigitado.isEmpty()) {
                                campusSelecionado = outroDigitado;
                            }
                        }

                        // Atualiza o texto da TextView
                        infoCampusTextView.setText("Campus: " + campusSelecionado);

                        // Salva no SharedPreferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(KEY_CAMPUS, campusSelecionado);
                        editor.apply();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        adicionarSugestaoButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.activity_adicionar_sugestao_dialog, null);

            EditText nomeEditText = view.findViewById(R.id.nomeEditText);
            EditText sugestaoEditText = view.findViewById(R.id.sugestaoEditText);
            CheckBox anonimoCheckBox = view.findViewById(R.id.anonimoCheckBox);
            Spinner categoriaSpinner = view.findViewById(R.id.categoriasSpinner);


            // Carrega valores do spinner de categorias a partir do xml
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
                            Map<String, Object> sugestao = new HashMap<>();
                            sugestao.put("nome", nomeParaSalvar);
                            sugestao.put("texto", texto);
                            sugestao.put("email", email);
                            sugestao.put("anonimo", anonimo);
                            sugestao.put("categoria", categoriaSelecionada);
                            sugestao.put("campus", "Campus " + campusSalvo);
                            sugestao.put("dataInsercao", FieldValue.serverTimestamp()); // salva data/hora do servidor

                            FirebaseFirestore.getInstance()
                                    .collection("sugestoes")
                                    .add(sugestao)
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(MainActivity.this, "Sugestão salva!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MainActivity.this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
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