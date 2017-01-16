/*
    This file is part of the HHS Moodle WebApp.

    HHS Moodle WebApp is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HHS Moodle WebApp is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Diaspora Native WebApp.

    If not, see <http://www.gnu.org/licenses/>.
 */

package de.baumann.hhsmoodle.fragmentsMain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.baumann.hhsmoodle.R;
import de.baumann.hhsmoodle.databases.Database_Notes;
import de.baumann.hhsmoodle.databases.Database_Todo;
import de.baumann.hhsmoodle.helper.helper_main;
import de.baumann.hhsmoodle.helper.helper_notes;

@SuppressWarnings("ConstantConditions")
public class FragmentNotes extends Fragment {

    private ListView listView = null;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_notes, container, false);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setHasOptionsMenu(true);

        ImageView imgHeader = (ImageView) rootView.findViewById(R.id.imageView_header);
        if(imgHeader != null) {
            TypedArray images = getResources().obtainTypedArray(R.array.splash_images);
            int choice = (int) (Math.random() * images.length());
            imgHeader.setImageResource(images.getResourceId(choice, R.drawable.splash1));
            images.recycle();
        }


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPref.edit().putString("handleTextCreate", helper_main.createDate()).apply();
                helper_notes.editNote(getActivity());
            }
        });

        listView = (ListView)rootView.findViewById(R.id.listNotes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);

                final String title = map.get("title");
                final String cont = map.get("cont");
                final String seqnoStr = map.get("seqno");
                final String icon = map.get("icon");
                final String attachment = map.get("attachment");
                final String create = map.get("createDate");

                final Button attachment2;
                final TextView textInput;

                LayoutInflater inflater = getActivity().getLayoutInflater();

                final ViewGroup nullParent = null;
                final View dialogView = inflater.inflate(R.layout.dialog_note_show, nullParent);

                final String attName = attachment.substring(attachment.lastIndexOf("/")+1);
                final String att = getString(R.string.app_att) + ": " + attName;

                attachment2 = (Button) dialogView.findViewById(R.id.button_att);
                if (attName.equals("")) {
                    attachment2.setVisibility(View.GONE);
                } else {
                    attachment2.setText(att);
                }
                File file2 = new File(attachment);
                if (!file2.exists()) {
                    attachment2.setVisibility(View.GONE);
                }

                textInput = (TextView) dialogView.findViewById(R.id.note_text_input);
                textInput.setText(cont);
                Linkify.addLinks(textInput, Linkify.WEB_URLS);

                attachment2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        helper_main.openAtt(getActivity(), listView, attachment);
                    }
                });

                final ImageView be = (ImageView) dialogView.findViewById(R.id.imageButtonPri);

                switch (icon) {
                    case "3":
                        be.setImageResource(R.drawable.circle_green);
                        break;
                    case "2":
                        be.setImageResource(R.drawable.circle_yellow);
                        break;
                    case "1":
                        be.setImageResource(R.drawable.circle_red);
                        break;
                }

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setView(dialogView)
                        .setPositiveButton(R.string.toast_yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.note_edit, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                sharedPref.edit()
                                        .putString("handleTextTitle", title)
                                        .putString("handleTextText", cont)
                                        .putString("handleTextIcon", icon)
                                        .putString("handleTextSeqno", seqnoStr)
                                        .putString("handleTextAttachment", attachment)
                                        .putString("handleTextCreate", create)
                                        .apply();
                                helper_notes.editNote(getActivity());
                            }
                        });
                dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                @SuppressWarnings("unchecked")
                HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);

                final String seqnoStr = map.get("seqno");
                final String title = map.get("title");
                final String cont = map.get("cont");
                final String icon = map.get("icon");
                final String attachment = map.get("attachment");
                final String create = map.get("createDate");

                final CharSequence[] options = {
                        getString(R.string.note_edit),
                        getString(R.string.note_share),
                        getString(R.string.todo_menu),
                        getString(R.string.bookmark_createEvent),
                        getString(R.string.note_remove_note)};
                new AlertDialog.Builder(getActivity())
                        .setPositiveButton(R.string.toast_cancel, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(getString(R.string.note_edit))) {
                                    sharedPref.edit()
                                            .putString("handleTextTitle", title)
                                            .putString("handleTextText", cont)
                                            .putString("handleTextIcon", icon)
                                            .putString("handleTextSeqno", seqnoStr)
                                            .putString("handleTextAttachment", attachment)
                                            .putString("handleTextCreate", create)
                                            .apply();
                                    helper_notes.editNote(getActivity());
                                }

                                if (options[item].equals (getString(R.string.note_share))) {
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, cont);
                                    startActivity(Intent.createChooser(sharingIntent, (getString(R.string.note_share_2))));
                                }

                                if (options[item].equals (getString(R.string.todo_menu))) {

                                    try {
                                        final Database_Todo db = new Database_Todo(getActivity());
                                        db.addBookmark(title, cont, "3", "true", helper_main.createDate());
                                        db.close();
                                        TabLayout tabHost = (TabLayout) getActivity().findViewById(R.id.tabs);
                                        tabHost.getTabAt(1).select();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (options[item].equals (getString(R.string.bookmark_createEvent))) {
                                    Intent calIntent = new Intent(Intent.ACTION_INSERT);
                                    calIntent.setType("vnd.android.cursor.item/event");
                                    calIntent.putExtra(CalendarContract.Events.TITLE, title);
                                    calIntent.putExtra(CalendarContract.Events.DESCRIPTION, cont);
                                    startActivity(calIntent);
                                }

                                if (options[item].equals(getString(R.string.note_remove_note))) {
                                    try {
                                        Database_Notes db = new Database_Notes(getActivity());
                                        final int count = db.getRecordCount();
                                        db.close();

                                        if (count == 1) {
                                            Snackbar snackbar = Snackbar
                                                    .make(listView, R.string.note_remove_cannot, Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                        } else {
                                            Snackbar snackbar = Snackbar
                                                    .make(listView, R.string.note_remove_confirmation, Snackbar.LENGTH_LONG)
                                                    .setAction(R.string.toast_yes, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            try {
                                                                Database_Notes db = new Database_Notes(getActivity());
                                                                db.deleteNote(Integer.parseInt(seqnoStr));
                                                                db.close();
                                                                setNotesList();
                                                            } catch (PackageManager.NameNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).show();

                return true;
            }
        });

        setNotesList();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            setNotesList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        setNotesList();
    }

    @SuppressWarnings("WeakerAccess")
    public void setNotesList() {

        ArrayList<HashMap<String,String>> mapList = new ArrayList<>();

        try {
            Database_Notes db = new Database_Notes(getActivity());
            ArrayList<String[]> bookmarkList = new ArrayList<>();
            db.getBookmarks(bookmarkList, getActivity());
            if (bookmarkList.size() == 0) {
                db.loadInitialData();
                db.getBookmarks(bookmarkList, getActivity());
            }
            db.close();

            for (String[] strAry : bookmarkList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("seqno", strAry[0]);
                map.put("title", strAry[1]);
                map.put("cont", strAry[2]);
                map.put("icon", strAry[3]);
                map.put("attachment", strAry[4]);
                map.put("createDate", strAry[5]);
                mapList.add(map);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    getActivity(),
                    mapList,
                    R.layout.list_item_notes,
                    new String[] {"title", "cont", "createDate"},
                    new int[] {R.id.textView_title_notes, R.id.textView_des_notes, R.id.textView_create_notes}
            ) {
                @Override
                public View getView (final int position, View convertView, ViewGroup parent) {

                    @SuppressWarnings("unchecked")
                    HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);
                    final String title = map.get("title");
                    final String cont = map.get("cont");
                    final String seqnoStr = map.get("seqno");
                    final String icon = map.get("icon");
                    final String attachment = map.get("attachment");
                    final String create = map.get("createDate");

                    View v = super.getView(position, convertView, parent);
                    ImageView i=(ImageView) v.findViewById(R.id.icon_notes);
                    ImageView i2=(ImageView) v.findViewById(R.id.att_notes);

                    switch (icon) {
                        case "3":
                            i.setImageResource(R.drawable.circle_green);
                            break;
                        case "2":
                            i.setImageResource(R.drawable.circle_yellow);
                            break;
                        case "1":
                            i.setImageResource(R.drawable.circle_red);
                            break;
                    }
                    switch (attachment) {
                        case "":
                            i2.setVisibility(View.GONE);
                            break;
                        default:
                            i2.setVisibility(View.VISIBLE);
                            i2.setImageResource(R.drawable.ic_attachment);
                            break;
                    }

                    File file = new File(attachment);
                    if (!file.exists()) {
                        i2.setVisibility(View.GONE);
                    }


                    i.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            final Item[] items = {
                                    new Item(getString(R.string.note_priority_0), R.drawable.circle_green),
                                    new Item(getString(R.string.note_priority_1), R.drawable.circle_yellow),
                                    new Item(getString(R.string.note_priority_2), R.drawable.circle_red),
                            };

                            ListAdapter adapter = new ArrayAdapter<Item>(
                                    getActivity(),
                                    android.R.layout.select_dialog_item,
                                    android.R.id.text1,
                                    items){
                                @NonNull
                                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                                    //Use super class to create the View
                                    View v = super.getView(position, convertView, parent);
                                    TextView tv = (TextView)v.findViewById(android.R.id.text1);
                                    tv.setTextSize(18);
                                    tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
                                    //Add margin between image and text (support various screen densities)
                                    int dp5 = (int) (24 * getResources().getDisplayMetrics().density + 0.5f);
                                    tv.setCompoundDrawablePadding(dp5);

                                    return v;
                                }
                            };

                            new AlertDialog.Builder(getActivity())
                                    .setPositiveButton(R.string.toast_cancel, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int item) {
                                            if (item == 0) {
                                                try {
                                                    final Database_Notes db = new Database_Notes(getActivity());
                                                    db.deleteNote((Integer.parseInt(seqnoStr)));
                                                    db.addBookmark(title, cont, "3", attachment, create);
                                                    db.close();
                                                    setNotesList();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else if (item == 1) {
                                                try {
                                                    final Database_Notes db = new Database_Notes(getActivity());
                                                    db.deleteNote((Integer.parseInt(seqnoStr)));
                                                    db.addBookmark(title, cont, "2",attachment, create);
                                                    db.close();
                                                    setNotesList();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else if (item == 2) {
                                                try {
                                                    final Database_Notes db = new Database_Notes(getActivity());
                                                    db.deleteNote((Integer.parseInt(seqnoStr)));
                                                    db.addBookmark(title, cont, "1", attachment, create);
                                                    db.close();
                                                    setNotesList();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).show();
                        }
                    });
                    i2.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            helper_main.openAtt(getActivity(), listView, attachment);
                        }
                    });
                    return v;
                }
            };

            listView.setAdapter(simpleAdapter);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class Item{
        public final String text;
        public final int icon;
        Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_help:

                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_notes)
                        .setMessage(helper_main.textSpannable(getString(R.string.helpNotes_text)))
                        .setPositiveButton(getString(R.string.toast_yes), null);
                dialog.show();
                return true;

            case R.id.action_sort:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = View.inflate(getActivity(), R.layout.dialog_sort_notes, null);

                final CheckBox ch_title = (CheckBox) dialogView.findViewById(R.id.checkBoxTitle);
                final CheckBox ch_create = (CheckBox) dialogView.findViewById(R.id.checkBoxCreate);
                final CheckBox ch_edit = (CheckBox) dialogView.findViewById(R.id.checkBoxEdit);
                final CheckBox ch_icon = (CheckBox) dialogView.findViewById(R.id.checkBoxIcon);
                final CheckBox ch_att = (CheckBox) dialogView.findViewById(R.id.checkBoxAtt);


                if (sharedPref.getString("sortDB", "title").equals("title")) {
                    ch_title.setChecked(true);
                } else {
                    ch_title.setChecked(false);
                }
                if (sharedPref.getString("sortDB", "title").equals("create")) {
                    ch_create.setChecked(true);
                } else {
                    ch_create.setChecked(false);
                }
                if (sharedPref.getString("sortDB", "title").equals("seqno")) {
                    ch_edit.setChecked(true);
                } else {
                    ch_edit.setChecked(false);
                }
                if (sharedPref.getString("sortDB", "title").equals("icon")) {
                    ch_icon.setChecked(true);
                } else {
                    ch_icon.setChecked(false);
                }
                if (sharedPref.getString("sortDB", "title").equals("attachment")) {
                    ch_att.setChecked(true);
                } else {
                    ch_att.setChecked(false);
                }


                ch_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if(isChecked){
                            ch_create.setChecked(false);
                            ch_edit.setChecked(false);
                            ch_icon.setChecked(false);
                            ch_att.setChecked(false);
                            sharedPref.edit().putString("sortDB", "title").apply();
                            setNotesList();
                        }
                    }
                });
                ch_create.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if(isChecked){
                            ch_edit.setChecked(false);
                            ch_icon.setChecked(false);
                            ch_title.setChecked(false);
                            ch_att.setChecked(false);
                            sharedPref.edit().putString("sortDB", "create").apply();
                            setNotesList();
                        }
                    }
                });
                ch_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if(isChecked){
                            ch_create.setChecked(false);
                            ch_icon.setChecked(false);
                            ch_title.setChecked(false);
                            ch_att.setChecked(false);
                            sharedPref.edit().putString("sortDB", "seqno").apply();
                            setNotesList();
                        }
                    }
                });
                ch_icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if(isChecked){
                            ch_create.setChecked(false);
                            ch_edit.setChecked(false);
                            ch_title.setChecked(false);
                            ch_att.setChecked(false);
                            sharedPref.edit().putString("sortDB", "icon").apply();
                            setNotesList();
                        }
                    }
                });
                ch_att.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if(isChecked){
                            ch_create.setChecked(false);
                            ch_edit.setChecked(false);
                            ch_title.setChecked(false);
                            ch_icon.setChecked(false);
                            sharedPref.edit().putString("sortDB", "attachment").apply();
                            setNotesList();
                        }
                    }
                });

                builder.setView(dialogView);
                builder.setTitle(R.string.action_sort);
                builder.setPositiveButton(R.string.toast_yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog2 = builder.create();
                // Display the custom alert dialog on interface
                dialog2.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}