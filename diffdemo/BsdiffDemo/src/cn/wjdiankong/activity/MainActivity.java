package cn.wjdiankong.activity;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.wjdiankong.bsdiff.DiffUtils;
import cn.wjdiankong.bsdiff.PatchUtils;
import cn.wjdiankong.bsdifflib.R;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity {

	// 成功
	private static final int WHAT_SUCCESS = 1;
	// 合成失败
	private static final int WHAT_FAIL_PATCH = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView txt = (TextView)findViewById(R.id.version_txt);
		txt.setText("1.1.0");
		
		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DiffTask().execute();
			}
		});
		
		findViewById(R.id.btn_end).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new PatchTask().execute();
			}
		});
		
	}

	private class DiffTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			String appDir, newDir, patchDir;

			try {
				appDir = "/sdcard/demo_old.apk";
				newDir = "/sdcard/demo_new.apk";
				patchDir = "/sdcard/demo.patch";
				Log.i("jw", "newapp:"+newDir+",oldapp:"+appDir);
				File appOldFile = new File(appDir);
				File appNewFile = new File(newDir);
				if(!appOldFile.exists() || !appNewFile.exists()){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "文件不存在...", Toast.LENGTH_SHORT).show();
						}
					});
					return WHAT_FAIL_PATCH;
				}

				int result = DiffUtils.getInstance().genDiff(appDir, newDir, patchDir);
				if (result == 0) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "差分包已生成", Toast.LENGTH_SHORT).show();
						}
					});
					return WHAT_SUCCESS;
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "差分包生成失败", Toast.LENGTH_SHORT).show();
						}
					});
					return WHAT_FAIL_PATCH;
				}
			} catch (Exception e) {
				Log.i("jw", "error:"+Log.getStackTraceString(e));
			}
			return WHAT_FAIL_PATCH;
		}
	}

	private class PatchTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... params) {
			String appDir, newDir, patchDir;

			try {
				// 指定包名的程序源文件路径
				appDir = "/sdcard/demo_old.apk";
				newDir = "/sdcard/demo_news.apk";
				patchDir = "/sdcard/demo.patch";
				Log.i("jw", "newapp:"+newDir+",oldapp:"+appDir);
				File oldAppFile = new File(appDir);
				File patchFile = new File(patchDir);
				if(!oldAppFile.exists() || !patchFile.exists()){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "文件不存在...", Toast.LENGTH_SHORT).show();
						}
					});
					return WHAT_FAIL_PATCH;
				}

				int result = PatchUtils.getInstance().patch(appDir, newDir, patchDir);
				if (result == 0) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "合成APK成功", Toast.LENGTH_SHORT).show();
							
							Intent intent = new Intent(Intent.ACTION_VIEW);  
					        intent.setDataAndType(Uri.parse("file:///sdcard/demo_news.apk"),  
					                "application/vnd.android.package-archive");  
					        startActivity(intent);  
						}
					});
					return WHAT_SUCCESS;
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "合成APK失败", Toast.LENGTH_SHORT).show();
						}
					});
					return WHAT_FAIL_PATCH;
				}
			} catch (Exception e) {
				Log.i("jw", "error:"+Log.getStackTraceString(e));
			}
			return WHAT_FAIL_PATCH;
		}
	}
	
	//获取本应用的apk包路径
	public String getSelfApkPath() {
		List<ApplicationInfo> installList = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);  
        for (int i = 0; i < installList.size(); i++) {  
            ApplicationInfo info=installList.get(i);  
            if(info.packageName.equals(getPackageName())){
            	Log.i("jw", "publicdir:"+info.publicSourceDir+",sourcedir:"+info.sourceDir);
            	return info.sourceDir;
            }
        }
        return null;
	}

}
