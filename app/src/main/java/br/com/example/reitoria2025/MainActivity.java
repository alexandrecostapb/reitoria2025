package br.com.example.reitoria2025;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button adicionarSugestaoButton = findViewById(R.id.adicionarSugestaoButton);
        Button sugestoesButton = findViewById(R.id.sugestoesButton);

        adicionarSugestaoButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.activity_adicionar_sugestao_dialog, null);

            EditText nomeEditText = view.findViewById(R.id.nomeEditText);
            EditText sugestaoEditText = view.findViewById(R.id.sugestaoEditText);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Nova Sugestão")
                    .setView(view)
                    .setPositiveButton("Salvar", (dialog, which) -> {
                        String nome = nomeEditText.getText().toString().trim();
                        String texto = sugestaoEditText.getText().toString().trim();

                        if (!nome.isEmpty() && !texto.isEmpty()) {
                            Sugestao sugestao = new Sugestao(nome, texto);

                            FirebaseFirestore.getInstance()
                                    .collection("sugestoes")
                                    .add(sugestao)
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(MainActivity.this, "Sugestão salva!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(MainActivity.this, "Erro ao salvar!", Toast.LENGTH_SHORT).show());
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
                finish();
            }
        });
    }
}