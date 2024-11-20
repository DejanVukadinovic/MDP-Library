package org.example;

import com.google.gson.Gson;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.http.server.types.files.SystemFile;

import java.io.File;
import java.io.InputStream;

@Controller("/libraryserver")
public class LibraryserverController {

    @Get(uri = "/", produces = "string/json")
    public String index() {
        BookManager manager = BookManager.getManager();
        Gson gson = new Gson();
        return gson.toJson(manager.getBooks());
    }
    @Post(uri = "/registerBook", produces = "text/plain", consumes = MediaType.MULTIPART_FORM_DATA)
    public String registerBook(@Part CompletedFileUpload file, @Part String body){
        try{
            BookManager manager = BookManager.getManager();
            System.out.println(manager.registerBook(file, body));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "Book Registered";
    }
    @Get(uri = "/book", produces = MediaType.APPLICATION_OCTET_STREAM)
    public HttpResponse<SystemFile> downloadFile(@QueryValue String id) {
        File file = new File(id+".zip");
        if (!file.exists()) {
            return HttpResponse.notFound();
        }
        SystemFile systemFile = new SystemFile(file);
        return HttpResponse.ok(systemFile).header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
    }

}