package com.example.wordlyweek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import com.example.wordlyweek.repository.*;
import com.example.wordlyweek.model.*;

@Service
public class MagazineJpaService implements MagazineRepository {

    @Autowired
    private MagazineJpaRepository mr;

    @Autowired
    private WriterJpaRepository wr;

    @Override
    public ArrayList<Magazine> getMagazines() {
        return (ArrayList<Magazine>) mr.findAll();
    }

    @Override
    public Magazine getMagazineById(int magazineId) {
        try {
            Magazine magazine = mr.findById(magazineId).get();
            return magazine;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Magazine addMagazine(Magazine magazine) {

        List<Integer> writerIds = new ArrayList<>();
        for (Writer writer : magazine.getWriters()) {
            writerIds.add(writer.getWriterId());
        }
        List<Writer> writers = wr.findAllById(writerIds);
        magazine.setWriters(writers);

        for (Writer writer : writers) {
            writer.getMagazines().add(magazine);
        }

        Magazine savedOne = mr.save(magazine);
        wr.saveAll(writers);

        return savedOne;

    }

    @Override
    public Magazine updateMagazine(int magazineId, Magazine magazine) {
        try {
            Magazine newOne = mr.findById(magazineId).get();
            if (magazine.getMagazineName() != null) {
                newOne.setMagazineName(magazine.getMagazineName());
            }
            if (magazine.getPublicationDate() != null) {
                newOne.setPublicationDate(magazine.getPublicationDate());
            }
            if (magazine.getWriters() != null) {
                List<Writer> writers = newOne.getWriters();
                for (Writer writer : writers) {
                    writer.getMagazines().remove(newOne);
                }

                wr.saveAll(writers);
                List<Integer> newWriterIds = new ArrayList<>();

                for (Writer writer : magazine.getWriters()) {
                    newWriterIds.add(writer.getWriterId());
                }

                List<Writer> newWriter = wr.findAllById(newWriterIds);
                for (Writer writer : newWriter) {
                    writer.getMagazines().add(newOne);
                }

                wr.saveAll(newWriter);
                newOne.setWriters(newWriter);
            }
            return mr.save(newOne);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteMagazine(int magazineId) {
        try {
            Magazine magazine = mr.findById(magazineId).get();

            List<Writer> writers = magazine.getWriters();

            for (Writer writer : writers) {
                writer.getMagazines().remove(magazine);
            }

            wr.saveAll(writers);
            mr.deleteById(magazineId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public List<Writer> getMagazineWriter(int magazineId) {
        try {
            Magazine magazine = mr.findById(magazineId).get();
            return magazine.getWriters();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
