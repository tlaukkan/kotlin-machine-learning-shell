package ml.shell

import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("ml.shell.hello")

fun hello() {
    log.info("hello")
    println("hello")
}