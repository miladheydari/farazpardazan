package com.miladheydari.farazpardazan.controllers

import com.miladheydari.farazpardazan.client.response.Result
import com.miladheydari.farazpardazan.models.User
import com.miladheydari.farazpardazan.repositories.TokenRepository
import com.miladheydari.farazpardazan.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import javax.activation.MimetypesFileTypeMap


@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(val userRepository: UserRepository, tokenRepository: TokenRepository) :
    BaseController(tokenRepository, userRepository) {

    @PutMapping("/{userId}/edit")
    fun editUser(
        @PathVariable("userId") userId: Long,
        @RequestHeader("token") token: String,
        @RequestBody user: User
    ): ResponseEntity<Result<User>> {
        return authThenDoing(token, userId) { currentUser ->
            if (user.birthDate?.isNotEmpty() == true && currentUser.birthDate != user.birthDate)
                currentUser.birthDate = user.birthDate
            if (user.firstName?.isNotEmpty() == true && currentUser.firstName != user.firstName)
                currentUser.firstName = user.firstName
            if (user.lastName?.isNotEmpty() == true && currentUser.lastName != user.lastName)
                currentUser.lastName = user.lastName
            if (user.email?.isNotEmpty() == true && currentUser.email != user.email)
                currentUser.email = user.email
            if (user.username?.isNotEmpty() == true && currentUser.username != user.username)
                currentUser.username = user.username
            if (user.genderType?.name?.isNotEmpty() == true && currentUser.genderType != user.genderType)
                currentUser.genderType = user.genderType


            Result(userRepository.save(currentUser))
        }
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable("userId") userId: Long,
        @RequestHeader("token") token: String
    ): ResponseEntity<Result<User>> {
        return authThenDoing(token, userId) { currentUser ->
            Result(currentUser)
        }
    }


    @PostMapping("/{userId}/image")
    fun postImage(
        @PathVariable("userId") userId: Long,
        @RequestHeader("token") token: String,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Result<User>> {
        if (file.size > 3 * 1024 * 1024)
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Result(message = "Image too large, you can upload files up to 3 MB"))
        return authThenDoing(token, userId) { currentUser ->

            val fileExtension = MimeTypeUtils.parseMimeType(file.contentType!!).subtype
            val folder = File("c:/milad")
            if (!folder.exists())
                folder.mkdirs()
            folder.listFiles()?.singleOrNull { it.name.split(".")[0] == currentUser.id.toString() }?.delete()
            val f1 = File(folder, ("${currentUser.id}.$fileExtension"))
            file.transferTo(f1)
            currentUser.hasImage = true
            return@authThenDoing Result(userRepository.save(currentUser))
        }
    }


    @GetMapping("/{userId}/image")
    fun getImage(@PathVariable("userId") userId: Long, @RequestHeader("token") token: String): ResponseEntity<*> {

        val imageFileResponse = authThenDoing(token, userId) { currentUser ->
            if (currentUser.hasImage != true)
                return@authThenDoing Result<File>(message = "no image")

            val folder = File("c:/milad")


            val image = folder.listFiles().singleOrNull { it.name.split(".")[0] == currentUser.id.toString() }




            return@authThenDoing Result(image)
        }
        val imageFileResult = imageFileResponse.body
        if (imageFileResult?.message != null)
            return imageFileResponse
        val imageFile = imageFileResult!!.data!!
        val resource = InputStreamResource(FileInputStream(imageFile))

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + imageFile.name)
            .contentType(MediaType.parseMediaType(MimetypesFileTypeMap().getContentType(imageFile)))
            .contentLength(imageFile.length())
            .body(resource)
    }
}

