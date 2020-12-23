package cn.xinbee.mdrs.rest.controller;

import cn.xinbee.mdrs.service.MailMessageService;
import cn.xinbee.mdrs.util.FileUtils;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/track/open")
public class MailActionController {

    private MailMessageService service;

    public MailActionController(MailMessageService service) {
        this.service = service;
    }

    @GetMapping("/{img}")
    public ResponseEntity<Void> receive(
        @PathVariable("img") String trackInf) throws IOException {
        trackInf = FileUtils.getNameFromFileName(trackInf);
        service.receiveOpen(trackInf);
        return ResponseEntity.ok().build();
    }

}
