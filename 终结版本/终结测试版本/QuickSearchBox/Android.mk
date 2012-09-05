LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SDK_VERSION := current

LOCAL_STATIC_JAVA_LIBRARIES := \
	guava \
	android-common

LOCAL_SRC_FILES := \
	$(call all-java-files-under,src) \
	$(call all-logtags-files-under,src)

LOCAL_PACKAGE_NAME := QuickSearchBox
LOCAL_CERTIFICATE :=shared

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res
LOCAL_PROGUARD_FLAGS := -include $(LOCAL_PATH)/proguard.flags

include $(BUILD_PACKAGE)

# Use the following include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
