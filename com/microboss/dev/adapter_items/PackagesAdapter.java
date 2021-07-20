package com.microboss.dev.adapter_items;


        /*
         * Copyright (C) 2016, francesco Azzola
         *
         *(http://www.survivingwithandroid.com)
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         *      http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
         *
         * 14/08/16
         */

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.microboss.dev.R;

        import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.MyViewHolder> {

    private List<Packages> countryList;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView details;
        public TextView price;
        public LinearLayout bgLay;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.pack_title);
            details = (TextView) view.findViewById(R.id.pack_details);
            price = (TextView) view.findViewById(R.id.pack_price);
            bgLay = (LinearLayout) view.findViewById(R.id.pack_bg);
        }
    }

    public PackagesAdapter(List<Packages> countryList) {
        this.countryList = countryList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("Bind ["+holder+"] - Pos ["+position+"]");
        Packages c = countryList.get(position);
        holder.title.setText(c.titles);
        holder.details.setText(String.valueOf(c.details));
        holder.price.setText(String.valueOf(c.price));
        holder.bgLay.setBackgroundResource(c.colorRes);
    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size ["+countryList.size()+"]");
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item,parent, false);
        return new MyViewHolder(v);
    }
}