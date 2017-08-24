package com.ninegold.ninegoldapi.controllers;

import com.ninegold.ninegoldapi.entities.GenerateRequest;
import com.ninegold.ninegoldapi.entities.GeneratedService;
import com.ninegold.ninegoldapi.exceptions.NineGoldException;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services")
@NoArgsConstructor
public class ServiceController {

  @RequestMapping(value = "generate", method = RequestMethod.POST)
  public GeneratedService generate(@RequestBody GenerateRequest generateRequest)
    throws NineGoldException {


    GeneratedService generatedMapping = new GeneratedService();
    generatedMapping.setDownloadLink("http://XXXXXX");
    return generatedMapping;
  }

}
