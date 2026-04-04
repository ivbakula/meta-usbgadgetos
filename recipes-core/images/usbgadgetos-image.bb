require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "USB Gadget OS Image"

IMAGE_FSTYPES = "wic.bz2"

WKS_FILE = ""
WKS_FILE:mangopi-mq-pro = "usbgadgetos-sdcard-image.wks"
WKS_FILE:raspberrypi4-64 = ""
WKS_FILE:raspberrypi3-64 = ""
WKS_FILE:raspberrypi3 = ""
WKS_FILE:raspberrypi0-2w-64 = ""

IMAGE_FEATURES += "allow-root-login allow-empty-password empty-root-password"

IMAGE_INSTALL:append = " usb-gadget-service usb-ecm"
