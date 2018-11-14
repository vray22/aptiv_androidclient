package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    public List<Item> list;
    public void getEditItem(Intent intent) {
        mContext.startActivity(intent);
    }

    public ItemAdapter(Context mContext, List<Item> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Item item = list.get(position);

        holder.textName.setText(item.getName());
        holder.textPrice.setText(String.valueOf(item.getPrice()));
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));
        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditItem.class);
                intent.putExtra("itemid", list.get(position).getItemid());
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("quantity", list.get(position).getQuantity());
                intent.putExtra("price", list.get(position).getPrice());
                getEditItem(intent);
            }
        });
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ServerCalls sc = new ServerCalls(mContext);
          sc.httpDelete(MainActivity.url + "items/" + list.get(position).getItemid(), new ServerCalls.VolleyResponseListener() {
            @Override
            public void onError(String message) {
              Log.d("Get Error", message);
            }

            @Override
            public void onResponse(Object response) {
              Toast.makeText(mContext, "Item deleted!",
                      Toast.LENGTH_SHORT).show();
              ItemAdapter.this.list.remove(list.get(position));
              ItemAdapter.this.notifyDataSetChanged();
            }
          });
        }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice, textQuantity;
        public Button editItem;
        public Button deleteItem;

        public ViewHolder(View itemView) {
            super(itemView);
            editItem = itemView.findViewById(R.id.editItem);
            deleteItem = itemView.findViewById(R.id.deleteItem);

            textName = itemView.findViewById(R.id.main_title);
            textPrice = itemView.findViewById(R.id.main_price);
            textQuantity = itemView.findViewById(R.id.main_quantity);
        }
    }
}
