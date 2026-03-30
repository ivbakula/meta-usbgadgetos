#!/bin/sh

GADGET_CFG_ROOT=/sys/kernel/config/usb_gadget/g1

if [[ ! -d "$GADGET_CFG_ROOT" ]]; then
    mkdir "$GADGET_CFG_ROOT"
fi

echo 0xdead > "$GADGET_CFG_ROOT/idVendor"
echo 0xbeef > "$GADGET_CFG_ROOT/idProduct"
echo 0x0200 > "$GADGET_CFG_ROOT/bcdUSB"
echo 0x0100 > "$GADGET_CFG_ROOT/bcdDevice"

mkdir -p "$GADGET_CFG_ROOT/strings/0x409"
echo "0007" > "$GADGET_CFG_ROOT/strings/0x409/serialnumber"
echo "IvanBakula" > "$GADGET_CFG_ROOT/strings/0x409/manufacturer"
echo "USB FFS Gadget" > "$GADGET_CFG_ROOT/strings/0x409/product"

mkdir -p "$GADGET_CFG_ROOT/configs/c.1/strings/0x409"
echo "ffs.usb0" > "$GADGET_CFG_ROOT/configs/c.1/strings/0x409/configuration"

mkdir -p "$GADGET_CFG_ROOT/functions/ffs.usb0"
ln -s "$GADGET_CFG_ROOT/functions/ffs.usb0" "$GADGET_CFG_ROOT/configs/c.1/"

mkdir /dev/ffs-usb0
mount -t functionfs usb0 /dev/ffs-usb0

#
# Interface and endpoint descriptors shall be written by gadget service.
# Gadget service depends on this "service" to execute earlier in boot process.
#
