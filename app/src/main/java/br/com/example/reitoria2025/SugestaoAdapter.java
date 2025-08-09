package br.com.example.reitoria2025;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoViewHolder> {

    private List<Sugestao> sugestoes;

    public SugestaoAdapter(List<Sugestao> sugestoes) {
        this.sugestoes = sugestoes;
    }

    @NonNull
    @Override
    public SugestaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugestao, parent, false);
        return new SugestaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugestaoViewHolder holder, int position) {
        Sugestao sugestao = sugestoes.get(position);
        if (sugestao.isAnonimo()) {
            holder.textViewNome.setText("Anônimo");
        } else {
            holder.textViewNome.setText(sugestao.getNome());
        }
        holder.textViewTexto.setText(sugestao.getTexto());
        holder.textViewCategoria.setText("Categoria: " + sugestao.getCategoria());
    }

    @Override
    public int getItemCount() {
        return sugestoes.size();
    }

    static class SugestaoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewTexto, textViewCategoria;

        public SugestaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.nomeTextView);
            textViewTexto = itemView.findViewById(R.id.textoTextView);
            textViewCategoria = itemView.findViewById(R.id.categoriaTextView);
        }
    }
}

