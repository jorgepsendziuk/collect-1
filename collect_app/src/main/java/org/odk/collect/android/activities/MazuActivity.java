/*
 * Copyright (C) 2014 GeoODK
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Responsible for displaying buttons to launch the major activities. Launches
 * some activities based on returns of others.
 *
 * @author Jon Nordling (jonnordling@gmail.com)
 */

package org.odk.collect.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.preferences.PreferencesActivity;
import org.odk.collect.android.utilities.ApplicationConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MazuActivity extends Activity {
	private static final String t = "GeoODK";
	private static boolean EXIT = true;
	private AlertDialog mAlertDialog;
	private String[] assestFormList;

	
    public static final String FORMS_PATH = Collect.ODK_ROOT + File.separator + "forms";
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mazu_layout);
        

        Log.i(t, "Starting up, creating directories");
		try {
			Collect.createODKDirs();
		} catch (RuntimeException e) {
			createErrorDialog(e.getMessage(), EXIT);
			return;
		}
		assestFormList = getAssetFormList();
		copyForms(assestFormList);


		ImageButton mazucollect_button = (ImageButton) findViewById(R.id.mazucollect_butt);
        mazucollect_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Collect.getInstance().getActivityLogger().logAction(this, "fillBlankForm", "click");
				Intent i = new Intent(getApplicationContext(), FormChooserList.class);
				startActivity(i);
			}
		});

        ImageButton mazumanage_but = (ImageButton) findViewById(R.id.mazuedit_butt);
		mazumanage_but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Collect.getInstance().getActivityLogger()
						.logAction(this, ApplicationConstants.FormModes.EDIT_SAVED, "click");
				Intent i = new Intent(getApplicationContext(), InstanceChooserList.class);
				i.putExtra(ApplicationConstants.BundleKeys.FORM_MODE,
						ApplicationConstants.FormModes.EDIT_SAVED);
				startActivity(i);
			}
		});
		ImageButton mazumap_but = (ImageButton) findViewById(R.id.mazu_map);
		mazumap_but.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Collect.getInstance().getActivityLogger()
						.logAction(this, "map_data", "click");
				Intent i = new Intent(getApplicationContext(),GeoPointMapActivity.class);
				startActivity(i);
				//Context context = MazuActivity.this;
				//eclipsgeomapa(context);
			}
		});

		ImageButton mazusettings_but = (ImageButton) findViewById(R.id.mazusettings_butt);
		mazusettings_but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Collect.getInstance()
				.getActivityLogger()
				.logAction(this, "Main_Settings", "click");
				Intent ig = new Intent(getApplicationContext(), PreferencesActivity.class);
				startActivity(ig);
			}
		});

		ImageButton mazusend_but = (ImageButton) findViewById(R.id.mazu_send);
		mazusend_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Collect.getInstance().getActivityLogger()
						.logAction(this, "uploadForms", "click");
				Intent i = new Intent(getApplicationContext(),InstanceUploaderList.class);
				startActivity(i);
			}
		});

		ImageButton mazureceive_but = (ImageButton) findViewById(R.id.mazureceive_but);
		mazureceive_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Collect.getInstance().getActivityLogger()
						.logAction(this, "uploadForms", "click");
				Intent i = new Intent(getApplicationContext(),FormDownloadList.class);
				startActivity(i);
			}
		});

		ImageButton mazudelete_but = (ImageButton) findViewById(R.id.mazudelete_data_butt);
		mazudelete_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Collect.getInstance().getActivityLogger()
						.logAction(this, "deleteSavedForms", "click");
				Intent i = new Intent(getApplicationContext(), FileManagerTabs.class);
				startActivity(i);*/
				Intent intent = new Intent(getApplicationContext(), FileManagerTabs.class);
				startActivity(intent);
			}
		});



		//End of Main activity
    }

	public static void eclipsgeomapa(final Context context) {
		String gpsStatusAction = "eu.hydrologis.geopaparazzi.ECLIPS";
		Intent intent = new Intent(gpsStatusAction);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		}


	private String[] getAssetFormList() {
		AssetManager assetManager = getAssets();
		String[] formList = null;
		try {
			formList = assetManager.list("forms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assetManager.list(path);
		// TODO Auto-generated method stub
		return formList;
	}



	private void copyForms(String[] forms){
		AssetManager assetManager = getAssets();
		InputStream in = null;
		OutputStream out = null;
		for (int i=0; forms.length>i; i++) {
			String filename = forms[i];
			File form_file = new File(FORMS_PATH,filename);
			if (!form_file.exists()){
				try {
					in = assetManager.open("forms/"+filename);
					out = new FileOutputStream(FORMS_PATH+File.separator+filename);
					copyFile(in, out);
					in.close();
		            out.flush();
		            out.close();
		            in = null;
		            out = null;
					
				} catch (IOException e) {
					Log.e("tag", "Failed to copy asset file: " + FORMS_PATH+File.separator+forms[i], e);
			}
				
			}
			 System.out.println(forms[i]);
		}
		
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
	      byte[] buffer = new byte[1024];
	      int read;
	      while((read = in.read(buffer)) != -1)
	      {
	            out.write(buffer, 0, read);
	      }
	}
	
	private void createErrorDialog(String errorMsg, final boolean shouldExit) {
		Collect.getInstance().getActivityLogger()
				.logAction(this, "createErrorDialog", "show");
		mAlertDialog = new AlertDialog.Builder(this).create();
		mAlertDialog.setIcon(android.R.drawable.ic_dialog_info);
		mAlertDialog.setMessage(errorMsg);
		DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				switch (i) {
				case DialogInterface.BUTTON_POSITIVE:
					Collect.getInstance()
							.getActivityLogger()
							.logAction(this, "createErrorDialog",
									shouldExit ? "exitApplication" : "OK");
					if (shouldExit) {
						finish();
					}
					break;
				}
			}
		};
		mAlertDialog.setCancelable(false);
		mAlertDialog.setButton(getString(R.string.ok), errorListener);
		mAlertDialog.show();
	}
	


	
	
}
