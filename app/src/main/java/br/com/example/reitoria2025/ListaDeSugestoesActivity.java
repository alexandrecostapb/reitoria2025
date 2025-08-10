package br.com.example.reitoria2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaDeSugestoesActivity extends AppCompatActivity {

    private RecyclerView sugestoesRecyclerView;
    private SugestaoAdapter adapter;
    private List<Sugestao> sugestoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_de_sugestoes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView voltarButton = findViewById(R.id.voltarButton);
        sugestoesRecyclerView = findViewById(R.id.sugestoesRecyclerView);
        sugestoesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sugestoes = new ArrayList<>();
        adapter = new SugestaoAdapter(sugestoes);
        sugestoesRecyclerView.setAdapter(adapter);

        carregarSugestoesDoFirestore();

        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retirei o comando de "abertura" da página principal (que ocorria por meio de um Intent) e deixei ele só fechando a página atual (a da lista de sugestões, no caso).
                // Fiz essa alteração pois agora, na MainActivity, a mesma não é mais fechada ao ir para a lista de sugestões, fazendo o usuário volltar para ela apenas fechando a tela atual, em vez de fazer isso e ter que abrir outra tela.
                finish();
            }
        });
    }

    private void carregarSugestoesDoFirestore() {
        FirebaseFirestore.getInstance()
                .collection("sugestoes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    sugestoes.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Sugestao sugestao = doc.toObject(Sugestao.class);
                        sugestoes.add(sugestao);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao carregar sugestões", Toast.LENGTH_SHORT).show());
    }
}
