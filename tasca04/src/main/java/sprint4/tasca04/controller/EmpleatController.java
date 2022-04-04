package sprint4.tasca04.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import sprint4.tasca04.model.Empleat;
import sprint4.tasca04.repository.EmpleatRepo;

@RestController
public class EmpleatController {

    @Autowired
    private EmpleatRepo repo;

    private static final String SAVE_PATH = "empleat-photos/";

    @Operation(summary = "Get all the existing empleats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the empleats")
    })
    @GetMapping("/get/all")
    public ResponseEntity<List<Empleat>> getAll() {
        return new ResponseEntity<>(repo.getEmpleats(), HttpStatus.OK);
    }

    @Operation(summary = "Get an Empleat by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleat found"),
            @ApiResponse(responseCode = "400", description = "Non Id supplied"),
            @ApiResponse(responseCode = "404", description = "Not found an Empleat withe the supplied Id") })
    @GetMapping("/get/empleat")
    public ResponseEntity<Empleat> getEmpleat(
            @Parameter(description = "The Empleat id to be searched") @RequestParam(name = "id", required = false) Integer id) {

        if (id == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "\"id\" parameter not found");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        Empleat empleat = repo.getEmpleat(id);
        if (empleat == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "Not found Empleat with id: " + String.valueOf(id));
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(empleat, HttpStatus.OK);
    }

    @Operation(summary = "Creates and inserts a new Empleat into the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleat succefuly created") })
    @PostMapping("/set")
    public ResponseEntity<Empleat> setEmpleat(
            @Parameter(description = "Name of the new Empleat") @RequestParam(name = "name", required = true) String name,
            @Parameter(description = "The position of the new Empleat") @RequestParam(name = "position", required = true) String position,
            @Parameter(description = "The salary of the new Empleat") @RequestParam(name = "salary", required = true) Double salary) {
        Empleat empleat = new Empleat(name, position, salary);
        this.repo.addEmpleat(name, position, salary);
        return new ResponseEntity<>(empleat, HttpStatus.CREATED);
    }

    @Operation(summary = "Deletes an empleat from the database if they exists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleat succefuly deleted"),
            @ApiResponse(responseCode = "400", description = "No Id supplied"),
            @ApiResponse(responseCode = "404", description = "No empleat found by the id supplied")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteEmpleat(
            @Parameter(description = "The id of the empleat") @RequestParam(name = "id", required = false) Integer id) {

        if (id == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "\"id\" parameter not found");
            return new ResponseEntity<>(false, headers, HttpStatus.BAD_REQUEST);
        }

        if (this.repo.removeEmpleat(id) == false) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "Not found Empleat with id: " + String.valueOf(id));
            return new ResponseEntity<>(false, headers, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Operation(summary = "Gets a list of Empleats based on they Feina.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "No Feina supplied"),
            @ApiResponse(responseCode = "404", description = "No Feina found"),
            @ApiResponse(responseCode = "200", description = "Found feina")
    })
    @GetMapping("/get/position/{position}")
    public ResponseEntity<List<Empleat>> getEmpleatFeina(
            @PathVariable(name = "position", required = false) String position) {

        if (position == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "\"position\" path variable not found");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        List<Empleat> empleats = this.repo.getEmpleatByPosition(position);

        if (empleats == null || empleats.size() == 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "Position not found");
            return new ResponseEntity<>(empleats, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(empleats, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Fallback exit for the get/position. If no ID is supplied, then the call goes here.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "No Feina supplied")
    })
    @GetMapping("/get/position/")
    public ResponseEntity<String> getEmpleatNoPos() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", "\"position\" path variable not found");
        return new ResponseEntity<String>("No Position found", headers, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Updates an empleat by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "No ID supplied"),
            @ApiResponse(responseCode = "400", description = "There are no parameter to update"),
            @ApiResponse(responseCode = "404", description = "Not found an Empleat with the Id supplied"),
            @ApiResponse(responseCode = "200", description = "Updated succesful")
    })
    @PutMapping("/update/id/{value}")
    public ResponseEntity<Empleat> updateEmpleat(
            @Parameter(description = "The id of the desired empleat to update") @PathVariable(name = "id") Integer id,
            @Parameter(description = "The new name of the empleat") @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "The new position of the empleat") @RequestParam(name = "position", required = false) String position,
            @Parameter(description = "The new salaryof the empleat") @RequestParam(name = "salary", required = false) Double salary) {

        if (id == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "\"id\" parameter not found");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        if (name == null && position == null && salary == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "There are no parameters to update");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        if (this.repo.updateEmpleat(id, name, position, salary)) {
            Empleat empleat = this.repo.getEmpleat(id);
            return new ResponseEntity<>(empleat, HttpStatus.OK);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", "No such Empleat with id: " + String.valueOf(id));
        return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Fallback exit for the /update/id/. If no ID is supplied, then the call goes here.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "No ID supplied")
    })
    @PutMapping("/update/id/")
    public ResponseEntity<String> updateEmpleatNoId() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error", "\"id\" parameter not found");
        return new ResponseEntity<>("No id found", headers, HttpStatus.BAD_REQUEST);
    }

    // vamos a subir la foto
    @PostMapping("set/photo/{id}")
    public ResponseEntity<Empleat> setPhoto(@PathVariable(name = "id") int id,
            @RequestParam(name = "image") MultipartFile image) throws IOException {

        // pillamos el empleado
        Empleat empleat = repo.getEmpleat(id);

        if (empleat == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "Not found Empleat with id: " + String.valueOf(id));
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        empleat.setPhoto(fileName);

        String uploadDir = SAVE_PATH + empleat.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, image);

        // return new RedirectView("/users", true);
        return new ResponseEntity<>(empleat, HttpStatus.OK);
    }

    @GetMapping("/get/photo/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable(name = "id") int id) throws IOException {
        Empleat empleat = repo.getEmpleat(id);

        if (empleat == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "Not found Empleat with id: " + String.valueOf(id));
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        if(empleat.getPhoto() == null || empleat.getPhoto().isEmpty()){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error", "The empleat doesn't have a Photo: " + String.valueOf(id));
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        String loadPath = SAVE_PATH + empleat.getId();

        ByteArrayResource res = FileUploadUtil.loadFile("empleat-photos/" + empleat.getId(), empleat.getPhoto());
        //return new ResponseEntity<ByteArrayResource>(res, HttpStatus.OK); 

        return ResponseEntity.ok().headers(new HttpHeaders()).contentLength(res.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(res);
    }

}
