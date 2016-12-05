LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE     := apk_patch_lib
LOCAL_SRC_FILES  := bzip2/bzlib.c \
					bzip2/crctable.c \
					bzip2/compress.c \
					bzip2/decompress.c \
					bzip2/randtable.c \
					bzip2/blocksort.c \
					bzip2/huffman.c \
					DiffUtils.c \
					PatchUtils.c
					

LOCAL_LDLIBS     := -lz -llog

include $(BUILD_SHARED_LIBRARY)