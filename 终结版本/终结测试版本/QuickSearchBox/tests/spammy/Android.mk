LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

LOCAL_SDK_VERSION := current

LOCAL_SRC_FILES := $(call all-java-files-under,src)

LOCAL_PACKAGE_NAME := SpammySuggestions
LOCAL_CERTIFICATE :=shared

include $(BUILD_PACKAGE)

