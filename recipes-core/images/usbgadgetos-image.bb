require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "USB Gadget OS Image"

IMAGE_FSTYPES = "wic.bz2"

WKS_FILE = "usbgadgetos-sdcard-image.wks"

IMAGE_FEATURES += "allow-root-login allow-empty-password empty-root-password"

IMAGE_INSTALL:append = " usb-gadget-service udev-integration"
