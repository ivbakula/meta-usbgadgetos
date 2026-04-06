# meta-usbgadgetos

Yocto meta layer providing USB-Gadget-OS image

The imeage implements fully integrated solution for gamepad passthrough,
built arount FunctionFS, libevdev and systemd-based services.

The image is based on poky linux *whinlatter*.

## Layer overview
This layer provides:

- USB-Gadget-OS image recipe
- usb-gadget-service integration
- systemd services:
  - setup-usb-gadget.service
  - gadget.service
  - finalize-usb-gadget.service
- udev rules and hotplug script

## Dependencies

- bitbake
- openembededed core
- meta-yocto

- meta-raspberrypi (If building for raspberry pi machines)
- meta-riscv (If building for mangopi-mq-pro)

## Preparation steps

Create build directory e.g. usbgadgetos-builds, cd to it and clone following repositories:

```
 git clone -b yocto-5.3.2 https://git.openembedded.org/bitbake ./layers/bitbake
 git clone -b yocto-5.3.2 https://git.openembedded.org/openembedded-core ./layers/openembedded-core
 git clone -b yocto-5.3.2 https://git.yoctoproject.org/meta-yocto ./layers/meta-yocto
 git clone -b whinlatter https://github.com/riscv/meta-riscv.git
 git clone -b whinlatter git://git.yoctoproject.org/meta-raspberrypi
```

Prepare yocto environment by sourceing oe-init-build-env
```
TEMPLATECONF=$PWD/layers/meta-yocto/meta-poky/conf/templates/default source ./layers/openembedded-core/oe-init-build-env
```

## Build

For build you need to have local.conf file setup. Minimal set of variables that need to be defined there are:
1. MACHINE
2. DISTRO

Currently, the build supports image generation for
1. raspberry pi zero w2 (Doesn't really make any sense to use since it has only one usable USB port)
2. raspberry pi 3
3. raspberry pi 3 64 bit
4. raspberry pi 4 64 bit
5. mango-pi-mq pro

From listed devices, the image was tested on following boards:

1. raspberry pi 4 (64 bit)
2. mango-pi-mq pro

So minimal local conf file for mango pi mq pro would look like this:
```
MACHINE = "mango-pi-mq-pro"
DISTRO = "usbgadgetos"
```

For raspberry pi 4:
```
MACHINE = "raspberrypi4-64"
DISTRO = "usbgadgetos"
```


When local.conf file is setup, we may proceed to actual build. The build is invoked with:

```
bitbake usbgadgetos-image
```
