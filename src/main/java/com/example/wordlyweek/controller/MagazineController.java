package com.example.wordlyweek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.example.wordlyweek.model.*;
import com.example.wordlyweek.service.MagazineJpaService;

@RestController
public class MagazineController {
    @Autowired
    private MagazineJpaService ms;

    @GetMapping("/magazines")
    public ArrayList<Magazine> getMagazines() {
        return ms.getMagazines();
    }

    @GetMapping("/magazines/{magazineId}")
    public Magazine getMagazineById(@PathVariable("magazineId") int magazineId) {
        return ms.getMagazineById(magazineId);
    }

    @PostMapping("/magazines")
    public Magazine addMagazine(@RequestBody Magazine magazine) {
        return ms.addMagazine(magazine);
    }

    @PutMapping("/magazines/{magazineId}")
    public Magazine updateMagazine(@PathVariable("magazineId") int magazineId, @RequestBody Magazine magazine) {
        return updateMagazine(magazineId, magazine);
    }

    @DeleteMapping("/magazines/{magazineId}")
    public void deleteMagazine(@PathVariable("magazineId") int magazineId) {
        ms.deleteMagazine(magazineId);
    }

    @GetMapping("/magazines/{magazineId}/writers")
    public List<Writer> getMagazineWriter(@PathVariable("magazineId") int magazineId) {
        return ms.getMagazineWriter(magazineId);
    }
}
