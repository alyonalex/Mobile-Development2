package ru.mirea.krasikova.retrofitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.krasikova.retrofitapp.ApiService;
import ru.mirea.krasikova.retrofitapp.Todo;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<Todo> todoList;
    private ApiService apiService;

    private static final String[] IMAGE_URLS = {
            "https://images-na.ssl-images-amazon.com/images/I/71zTLlqhDEL._AC_SL1200_.jpg",
            "https://avatars.mds.yandex.net/i?id=d3d10a35f0b68200db004ff516732f84cafb3322-8211098-images-thumbs&ref=rim&n=33&w=263&h=250",
            "https://i.pinimg.com/originals/1a/a8/bd/1aa8bdcced83056066833fe6e2934514.png",
            "https://thumbs.dreamstime.com/b/sticker-note-pin-13549913.jpg"
    };

    public TodoAdapter(List<Todo> todoList, ApiService apiService) {
        this.todoList = todoList;
        this.apiService = apiService;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.textViewTitle.setText(todo.getTitle());

        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(Boolean.TRUE.equals(todo.getCompleted()));

        int base = (todo.getId() != null ? todo.getId() : position);
        String imageUrl = IMAGE_URLS[ Math.abs(base) % IMAGE_URLS.length ];

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(64, 64)
                .centerCrop()
                .into(holder.imageTodo);

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            Todo updatedTodo = new Todo(isChecked);

            Call<Todo> call = apiService.updateTodo(todo.getId(), updatedTodo);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("TodoAdapter", "Todo " + todo.getId() + " updated successfully!");
                        todoList.get(adapterPosition).setCompleted(response.body().getCompleted());
                        notifyItemChanged(adapterPosition);
                    } else {
                        Log.e("TodoAdapter", "Failed to update todo. Code: " + response.code());
                        buttonView.setChecked(!isChecked);
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    Log.e("TodoAdapter", "Network error on update: " + t.getMessage());
                    buttonView.setChecked(!isChecked);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTodo;
        TextView textViewTitle;
        CheckBox checkBoxCompleted;

        public TodoViewHolder(View itemView) {
            super(itemView);
            imageTodo = itemView.findViewById(R.id.imageTodo);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        }
    }
}