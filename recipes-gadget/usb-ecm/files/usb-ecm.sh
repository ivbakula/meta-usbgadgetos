#!/bin/sh


# The scripts creates new ECM Ethernet gadget by through configfs
# Take a look at couple of interesting examples to get an idea on how this works
#  linux/Documentation/filesystems/configfs.rst
#  linux/Documentation/usb/gadget_configfs.rst
#  https://pip-assets.raspberrypi.com/categories/685-app-notes-guides-whitepapers/
#  documents/RP-009276-WP/Using-OTG-mode-on-Raspberry-Pi-SBCs
#  
# NOTE 1: In order for this script to work, your board must support USB Peripherial mode.
# NOTE 2: In case your board supports USB Peripherial mode, you need following drivers 
#  0. Device tree with support for DWC2 
#  1. dwc2 driver
#  2. libcomposite
#  3. 
#

DEFAULT_IP_ADDRESS="192.168.7.1/30"
CFGFS_USB_ROOT=/sys/kernel/config/usb_gadget

# Make sure that we're using unique name for our gadget.
n=$(ls "$CFGFS_USB_ROOT" 2>/dev/null \
    | grep '^g[0-9]\+$' \
    | sed 's/^g//' \
    | sort -n \
    | tail -n 1)

n=${n:-0}
n=$((n + 1))

# Gadget configurtion will be created at /sys/kernel/config/usb_gadget/g<n> where n depends
# if there is something already there
GADGET_CFG_ROOT="$CFGFS_USB_ROOT/g$n"
mkdir "$GADGET_CFG_ROOT"


echo 0x1d6b > "$GADGET_CFG_ROOT/idVendor"
echo 0x0104 > "$GADGET_CFG_ROOT/idProduct"
echo 0x0200 > "$GADGET_CFG_ROOT/bcdUSB"
echo 0x0100 > "$GADGET_CFG_ROOT/bcdDevice"

mkdir -p "$GADGET_CFG_ROOT/strings/0x409"
echo "0007" > "$GADGET_CFG_ROOT/strings/0x409/serialnumber"
echo "Ivan Bakula ;)" > "$GADGET_CFG_ROOT/strings/0x409/manufacturer"
echo "USB ECM Adapter. Ping me at $DEFAULT_IP_ADDRESS" > "$GADGET_CFG_ROOT/strings/0x409/product"

mkdir -p "$GADGET_CFG_ROOT/configs/c.1/strings/0x409"
echo "ECM network" > "$GADGET_CFG_ROOT/configs/c.1/strings/0x409/configuration"
echo 250 > "$GADGET_CFG_ROOT/configs/c.1/MaxPower"

# We want sane MAC address. The best way is to make it somehow machine specific
# As mango pi doesn't have serial number in /proc/cpuinfo, I'm going to use
# /etc/machine-id which should also be unique. This file should be available
# on every machine????
BASE=$(cat /etc/machine-id | md5sum | cut -c1-12)
DEV_MAC="02:${BASE:0:2}:${BASE:2:2}:${BASE:4:2}:${BASE:6:2}:${BASE:8:2}"
HOST_MAC="02:${BASE:0:2}:${BASE:2:2}:${BASE:4:2}:${BASE:6:2}:${BASE:10:2}"

mkdir -p "$GADGET_CFG_ROOT/functions/ecm.usb0"
echo "$DEV_MAC" > "$GADGET_CFG_ROOT/functions/ecm.usb0/dev_addr"
echo "$HOST_MAC" > "$GADGET_CFG_ROOT/functions/ecm.usb0/host_addr"

ln -s "$GADGET_CFG_ROOT/functions/ecm.usb0" "$GADGET_CFG_ROOT/configs/c.1/"

# This is not really generic, but I don't care. It's good enough for now as there is only one UDC
UDC="$(ls /sys/class/udc | head -n 1)"

# Start the gadget, finally
echo "$UDC" > "$GADGET_CFG_ROOT/UDC"

# Give some time for interface to show up....
for i in $(seq 1 10); do
    if ip link show usb0 >/dev/null 2>&1; then
        break
    fi
    sleep 1
done

ip addr add "$DEFAULT_IP_ADDRESS" dev usb0
ip link set usb0 up
