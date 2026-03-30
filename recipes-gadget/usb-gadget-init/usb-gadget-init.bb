LICENSE = "MIT"
SUMMARY = "Configure gadget device"
DESCRIPTION = "Configure gadget device via ConfigFS, mount FunctionFS and bind ffs node to specific configuration."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
	file://usb-gadget-init.sh \
	file://usb-gadget-init.service \
"

S = "${UNPACKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "usb-gadget-init.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/usb-gadget-init.sh ${D}${bindir}/usb-gadget-init.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/usb-gadget-init.service ${D}${systemd_system_unitdir}/usb-gadget-init.service
}

FILES:${PN} += " \
	${bindir}/usb-gadget-init.sh \
	${systemd_system_unitdir}/usb-gadget-init.service \
	${sysconfdir}/systemd/system/networking.service \
"
