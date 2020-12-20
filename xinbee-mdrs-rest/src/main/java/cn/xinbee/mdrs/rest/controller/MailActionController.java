package cn.xinbee.mdrs.rest.controller;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/track/open")
public class MailActionController {

    @GetMapping("/{img}")
    public ResponseEntity<Void> receive(
        @PathVariable("img") String trackInf) throws IOException {
        return ResponseEntity.ok(submit);
    }

}
