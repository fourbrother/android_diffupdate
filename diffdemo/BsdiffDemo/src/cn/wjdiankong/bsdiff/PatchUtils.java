package cn.wjdiankong.bsdiff;

public class PatchUtils {

	static PatchUtils instance;

	public static PatchUtils getInstance() {
		if (instance == null)
			instance = new PatchUtils();
		return instance;
	}

	static {
		System.loadLibrary("apk_patch_lib");
	}
	
	public native int patch(String oldApkPath, String newApkPath, String patchPath);
}