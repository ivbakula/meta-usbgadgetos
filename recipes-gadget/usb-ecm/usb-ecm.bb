SUMMARY = "Create USB ECM ethernet device"
DESCRIPTION = "Configures USB Ethernet gadget via ConfigFS and sets static IP address to aformentioned gadget. \
	This is useful debug feature for boards without ETH connector."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
	file://usb-ecm.sh \
	file://usb-ecm.service \
"

inherit systemd

RDEPENDS:${PN} += "iproute2"

SYSTEMD_SERVICE:${PN} = "usb-ecm.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/usb-ecm.sh ${D}${bindir}/usb-ecm.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/usb-ecm.service ${D}${systemd_system_unitdir}/usb-ecm.service

    # networking.service is masked globally. Because it interferes in really strange way
    # with our usb-ecm.sh script. And it's failing anyways. This is not really good idea
    # but it is what it is. We don't need it anyways in this image.
    install -d ${D}${sysconfdir}/systemd/system
    ln -sf /dev/null ${D}${sysconfdir}/systemd/system/networking.service
}

FILES:${PN} += " \
	${bindir}/usb-ecm.sh \
	${systemd_system_unitdir}/usb-ecm.service \
	${sysconfdir}/systemd/system/networking.service \
"
