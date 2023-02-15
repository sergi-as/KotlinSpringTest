package demo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
class IntegrationTests {

   companion object {
       @Container
       val container = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine"))
           .apply {
               withDatabaseName("db")
               withUsername("user")
               withPassword("password")
               withInitScript("sql/schema.sql")
           }
   }
    @Test
    fun `container is up and running`(){
        Assertions.assertTrue(container.isRunning)
    }
}