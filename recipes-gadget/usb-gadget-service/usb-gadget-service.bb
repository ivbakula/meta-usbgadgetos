SUMMARY = "USB Gadget Service"
DESCRIPTION = "SystemD unit that listens for hotplug events, libevdev events from the gamepad, and communicates with USB host"
LICENSE = "CLOSED"

HOMEPAGE = "https://github.com/ivbakula/usb-gadget-service"

SRC_URI = "git://git@github.com/ivbakula/usb-gadget-service.git;protocol=ssh;branch=main"

#
# Pull latest master. Good for devel, bad for everything else ;)
SRCREV = "${AUTOREV}"
PV = "1.0+git${SRCPV}"

#
# Make sure that the system has systemd and libevdev installed
RDEPENDS:${PN} = "libevdev socat systemd"

#
# Makefile needs pkgconfig (for finding proper libraries and include path -> PKG_CONFIG_PATH)
# systemd is needed for path to systemd directories used during installation phase
inherit pkgconfig systemd


do_compile() {
    oe_runmake
}

do_install() {
oe_runmake \
	DESTDIR=${D} \
	prefix=${prefix} \
	exec_prefix=${exec_prefix} \
	sbindir=${sbindir} \
	sysconfdir={sysconfdir} \
	systemd_system_unitdir=${systemd_system_unitdir} \
	udev_rules_dir=${sysconfdir}/udev/rules.d \
	install
}

SYSTEMD_SERVICE:${PN} = "setup-usb-gadget.service gadget.service finalize-usb-gadget.service"
SYSTEMD_SERVICE:${PN} = "enable"

FILES:${PN} += " \
	${sbindir}/gadget \
	${sbindir}/hotplug.sh \
	${systemd_system_unitdir}/gadget.service \
	${systemd_system_unitdir}/setup-usb-gadget.service \
	${systemd_system_unitdir}/finalize-usb-gadget.service \
	${sysconfdir}/udev/rules.d/99-gadget.rules \
"
