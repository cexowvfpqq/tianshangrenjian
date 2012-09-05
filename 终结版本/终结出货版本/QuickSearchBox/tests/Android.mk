LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

LOCAL_JAVA_LIBRARIES := android.test.runner

LOCAL_SRC_FILES := $(call all-java-files-under,src)

LOCAL_PACKAGE_NAME := QuickSearchBoxTests
LOCAL_CERTIFICATE :=shared

LOCAL_INSTRUMENTATION_FOR := QuickSearchBox

include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))
