SUMMARY = "UDEV integration"
DESCRIPTION = "Make UDEV wait for gamepad.service to initialize successfully (or fail gracefully)"
LICENSE = "CLOSED"

SRC_URI = "file://10-wait-for-gadget-service.conf"

S = "${UNPACKD}"

RDEPENDS:${PN} = "usb-gadget-service"

inherit systemd

do_install() {
    install -d ${D}${systemd_unitdir}/system/systemd-udevd.service.d
    install -m 0644 ${WORKDIR}/10-wait-for-gadget-service.conf \
        ${D}${systemd_unitdir}/system/systemd-udevd.service.d/10-wait-for-gadget-service.conf
}

FILES:${PN} += "${systemd_unitdir}/system/systemd-udevd.service.d/10-wait-for-gadget-service.conf"
