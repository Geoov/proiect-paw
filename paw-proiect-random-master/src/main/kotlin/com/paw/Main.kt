package com.paw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableJpaRepositories(basePackages = ["com.paw.persistence.repositories"])
@SpringBootApplication
open class Main
fun main(args: Array<String>) {
    runApplication<Main>(*args)
}

