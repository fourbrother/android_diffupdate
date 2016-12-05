package cn.wjdiankong.bsdiff;


public class DiffUtils {
	
	static DiffUtils instance;

	public static DiffUtils getInstance() {
		if (instance == null)
			instance = new DiffUtils();
		return instance;
	}

	static {
		System.loadLibrary("apk_patch_lib");
	}

	public native int genDiff(String oldApkPath, String newApkPath, String patchPath);
}