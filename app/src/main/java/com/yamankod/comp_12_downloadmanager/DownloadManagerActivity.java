package com.yamankod.comp_12_downloadmanager;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DownloadManagerActivity extends Activity {
	private long enqueue;
	private DownloadManager dm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
					Query query = new Query();
					query.setFilterById(enqueue);
					Cursor c = dm.query(query);
					if (c.moveToFirst()) {
						int columnIndex = c
								.getColumnIndex(DownloadManager.COLUMN_STATUS);
						if (DownloadManager.STATUS_SUCCESSFUL == c
								.getInt(columnIndex)) {

							ImageView view = (ImageView) findViewById(R.id.imageView1);
							String uriString = c
									.getString(c
											.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
							view.setImageURI(Uri.parse(uriString));
						}
					}
				}
			}
		};

		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	public void onClick(View view) {
		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Request request = new Request(
				Uri.parse("https://encrypted-tbn3.google.com/images?q=tbn:ANd9GcTIQQgqjfdNDRYQ3btnNJwRB07htjMKgeW2FyZrDCkxM7lfikmtHg"));
		enqueue = dm.enqueue(request);

	}

	public void showDownload(View view) {
		Intent i = new Intent();
		i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		startActivity(i);
	}

}
