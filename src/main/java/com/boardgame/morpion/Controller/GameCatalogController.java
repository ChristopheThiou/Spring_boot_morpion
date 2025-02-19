package com.boardgame.morpion.Controller;

import com.boardgame.morpion.Service.GameCatalog;
import java.util.Collection;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/games")
public class GameCatalogController {

    @Autowired
    private GameCatalog gameCatalog;

    @GetMapping("/identifiers")
    public Collection<String> getGameIdentifiers(@RequestHeader("Accept-Language") Locale locale) {
        return gameCatalog.getGameIdentifier();
    }
}