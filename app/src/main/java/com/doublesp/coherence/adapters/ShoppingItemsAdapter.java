package com.doublesp.coherence.adapters;


import static com.doublesp.coherence.R.id.shopping_item_image;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doublesp.coherence.R;
import com.doublesp.coherence.viewmodels.ShoppingListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingItemsAdapter extends
        RecyclerView.Adapter<ShoppingItemsAdapter.ShoppingViewHolder> {
    private List<ShoppingListItem> mShoppingList;

    public ShoppingItemsAdapter(List<ShoppingListItem> mShopingList) {
        this.mShoppingList = mShopingList;
    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_detail_list_item, parent, false);

        return new ShoppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        ShoppingListItem shoppingItem = mShoppingList.get(position);

        if (shoppingItem == null) {
            return;
        }

        String title = shoppingItem.getShoppingItem();
        String imagePath = shoppingItem.getShoppingImageUrl();

        holder.mShoppingItem.setText(title);
        Picasso.with(holder.mShoppingItemImage.getContext())
                .load(imagePath)
                .resize(100, 100)
                .centerInside()
                .placeholder(R.drawable.background_0)
                .into(holder.mShoppingItemImage);
    }

    @Override
    public int getItemCount() {
        return mShoppingList.size();
    }

    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        TextView mShoppingItem;
        ImageView mShoppingItemImage;

        public ShoppingViewHolder(View itemView) {
            super(itemView);

            mShoppingItem = (TextView) itemView.findViewById(R.id.shopping_item);
            mShoppingItemImage = (ImageView) itemView.findViewById(shopping_item_image);
        }
    }
}
