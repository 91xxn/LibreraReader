
APP_PLATFORM=android-14
APP_ABI := x86 armeabi-v7a
#APP_ABI := x86

MY_ARM_MODE := arm
LOCAL_ARM_MODE := $(MY_ARM_MODE)

MY_O = -O2

APP_OPTIM := release


APP_CFLAGS := -O2

APP_CFLAGS += $(MY_O)

#NDK_TOOLCHAIN_VERSION=4.9