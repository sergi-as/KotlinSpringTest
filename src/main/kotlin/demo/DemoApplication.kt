package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.sql.ResultSet
import java.util.UUID

@SpringBootApplication
class DemoApplication

fun String.uuid():String=
	UUID.nameUUIDFromBytes(this.encodeToByteArray()).toString()

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
@RestController
class MessageResource(val service:MessageService){
	@GetMapping("/")
	fun index(): List<Message> = service.findMessages()

	@GetMapping("/{id}")
	fun index(@PathVariable id: String):Message = service.findMessageById(id)?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Message not found")

	@PostMapping("/")
	fun post(@RequestBody message: Message):String="localhost:8080/${service.post(message).id}"
		//val entity=service.post(message)
		//return  ResponseEntity(entity,HttpStatus.CREATED)


	@DeleteMapping("/")
	fun delete(@RequestBody message:Message)=service.del(message)

	@DeleteMapping("/ALL")
	fun delete()=service.cleanup()
}

class myRowMapper:RowMapper<Message>{
	override fun mapRow(rs: ResultSet, rowNum: Int): Message = Message(rs.getString("id"),rs.getString("text"))
}
@Service
class MessageService(val db: JdbcTemplate) {

	/*
	fun findMessages():List<Message> = db.query("select * from messages") { rs, _ ->
		Message(rs.getString("id"), rs.getString("text"))
	}
	 */

	fun findMessageById(id:String):Message? = db.query("select * from messages where id = ?",{ rs, _ ->
		Message(rs.getString("id"), rs.getString("text"))
	},id).firstOrNull()

	fun post(message:Message):Message{
		val fixedMessage=Message(message.id?:message.text.uuid(),message.text)
		db.update("insert into messages values (?,?)",fixedMessage.id,fixedMessage.text)
		return fixedMessage
	}
	fun del(message:Message){
		db.update("delete from messages where id=?",message.id)
	}

	fun findMessages(): List<Message> =db.query("select * from messages",myRowMapper())
	fun cleanup()=db.update("delete from messages")


}




data class Message( val id:String?,val text:String)