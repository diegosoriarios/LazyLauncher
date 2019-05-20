package com.example.launcher;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GetApps extends Activity {
    PackageManager packageManager;
    public static List<AppInfo> apps;
    GridView gridView;
    public static ArrayAdapter<AppInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_applist);

        apps = null;
        adapter = null;
        loadApps();
        loadListView();
        addGridListener();
    }

    public void addGridListener(){
        try {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = packageManager.getLaunchIntentForPackage(apps.get(i).name.toString());
                    GetApps.this.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(GetApps.this, ex.getMessage().toString() + " Grid", Toast.LENGTH_LONG).show();
            Log.e("Error Grid", ex.getMessage().toString() + " Grid");
        }
    }

    public void loadListView() {
        try {
            gridView = (GridView) findViewById(R.id.grd_allApps);
            if(adapter == null) {
                adapter = new ArrayAdapter<AppInfo>(this, R.layout.grd_items, apps) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolderItem viewHolder = null;

                        if(convertView == null) {
                            convertView = getLayoutInflater().inflate(R.layout.grd_items, parent, false);
                            viewHolder = new ViewHolderItem();
                            viewHolder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
                            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name);
                            viewHolder.label = (TextView) convertView.findViewById(R.id.txt_label);
                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolderItem) convertView.getTag();
                        }

                        AppInfo appInfo = apps.get(position);

                        if(appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                        }
                        return convertView;
                    }

                    final class ViewHolderItem {
                        ImageView icon;
                        TextView name;
                        TextView label;
                    }
                };
            }

            gridView.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(GetApps.this, ex.getMessage().toString() + " loadListView", Toast.LENGTH_LONG).show();
            Log.e("Error loadListView", ex.getMessage().toString() + " loadListView");
        }
    }

    private void loadApps() {
        try {
            packageManager = getPackageManager();

            if(apps == null) {
                apps = new ArrayList<AppInfo>();

                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> availableApps = packageManager.queryIntentActivities(i, 0);
                for(ResolveInfo ri: availableApps) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.label = ri.loadLabel(packageManager);
                    appInfo.name = ri.activityInfo.packageName;
                    appInfo.icon = ri.activityInfo.loadIcon(packageManager);
                    apps.add(appInfo);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(GetApps.this, ex.getMessage().toString() + " loadApps", Toast.LENGTH_LONG).show();
            Log.e("Error loadApps", ex.getMessage().toString() + " loadApps");
        }
    }
}
