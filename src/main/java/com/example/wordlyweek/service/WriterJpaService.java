package com.example.wordlyweek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import com.example.wordlyweek.model.Magazine;
import com.example.wordlyweek.model.Writer;
import com.example.wordlyweek.repository.WriterJpaRepository;
import com.example.wordlyweek.repository.WriterRepository;
import com.example.wordlyweek.repository.MagazineJpaRepository;

@Service
public class WriterJpaService implements WriterRepository {

    @Autowired
    private WriterJpaRepository wr;

    @Autowired
    private MagazineJpaRepository mr;

    @Override
    public ArrayList<Writer> getWriters() {
        return (ArrayList<Writer>) wr.findAll();
    }

    @Override
    public Writer getWriterById(int writerId) {
        try {
            Writer writer = wr.findById(writerId).get();
            return writer;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Writer addWriter(Writer writer) {
        List<Integer> magazineIds = new ArrayList<>();

        for (Magazine magazine : writer.getMagazines()) {
            magazineIds.add(magazine.getMagazineId());
        }

        List<Magazine> magazines = mr.findAllById(magazineIds);
        if (magazines.size() != magazineIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        writer.setMagazines(magazines);

        return wr.save(writer);

    }

    @Override
    public Writer updateWriter(int writerId, Writer writer) {
        try {
            Writer newOne = wr.findById(writerId).get();

            if (writer.getWriterName() != null) {
                newOne.setWriterName(writer.getWriterName());
            }
            if (writer.getBio() != null) {
                newOne.setBio(writer.getBio());
            }
            if (writer.getMagazines() != null) {
                List<Integer> magazineIds = new ArrayList<>();

                for (Magazine magazine : writer.getMagazines()) {
                    magazineIds.add(magazine.getMagazineId());
                }

                List<Magazine> magazines = mr.findAllById(magazineIds);

                if (magazines.size() != magazineIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newOne.setMagazines(magazines);
            }
            return wr.save(newOne);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteWriter(int writerId) {
        try {
            wr.deleteById(writerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Magazine> getWriterMagazine(int writerId) {
        try {
            Writer writer = wr.findById(writerId).get();
            return writer.getMagazines();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}