package com.example.aluasistencias;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<Record> recordList;
    private Context context;

    // Constructor
    public RecordAdapter(List<Record> recordList, Context context) {
        this.recordList = recordList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada ítem del RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        // Obtener el registro de la lista y asignarlo al ViewHolder
        Record record = recordList.get(position);
        holder.bind(record);

        // Aplicar el color verde si el pedido está marcado como completado
        if (record.isCompleted()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        TextView clientName, productList, address;
        Button editButton, deleteButton, completeButton;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.clientName);
            productList = itemView.findViewById(R.id.productList);
            address = itemView.findViewById(R.id.address);  // Añadido para mostrar la dirección
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            completeButton = itemView.findViewById(R.id.completeButton);
        }

        public void bind(Record record) {
            clientName.setText(record.getClientName());
            productList.setText(record.getProductList());
            address.setText(record.getAddress());  // Mostrar la dirección

            // Funcionalidad del botón "Editar"
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditRecordActivity.class);
                intent.putExtra("recordId", record.getId()); // Enviar el ID del registro
                context.startActivity(intent);
            });

            // Funcionalidad del botón "Eliminar"
            deleteButton.setOnClickListener(v -> {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.deleteRecord(record.getId());
                Toast.makeText(context, "Registro eliminado", Toast.LENGTH_SHORT).show();
                // Eliminar el registro de la lista y actualizar la vista
                recordList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

            // Funcionalidad del botón "Completar"
            completeButton.setOnClickListener(v -> {
                // Marcar como completado y cambiar el color
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.markAsCompleted(record.getId());
                // Cambiar el fondo o texto para indicar que está completado
                itemView.setBackgroundColor(context.getResources().getColor(R.color.green)); // Color verde
                completeButton.setEnabled(false); // Desactivar el botón
                Toast.makeText(context, "Pedido completado", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
