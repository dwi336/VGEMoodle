/*
 * Copyright (C) 2020 Gaukler Faun
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.github.dwi336.vgemoodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class GridAdapter_Menu extends BaseAdapter {
    private static class Holder {
        TextView title;
        ImageView icon;
    }

    private final List<GridItem_Menu> list;

    private final Context context;

    GridAdapter_Menu(Context context, List<GridItem_Menu> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            holder = new Holder();
            holder.title = view.findViewById(R.id.item_title);
            holder.icon = view.findViewById(R.id.item_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        GridItem_Menu item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIcon());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
        //return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);

    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;

    }
}
