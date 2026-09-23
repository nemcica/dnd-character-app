package com.anem.character_app.feat;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FeatController {

    FeatService featService;
}
