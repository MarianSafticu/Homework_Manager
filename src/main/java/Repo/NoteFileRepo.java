package Repo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import Domain.Nota;
import Domain.Pair;
import Validator.*;


public class NoteFileRepo extends NoteRepo {
    String fileName;

    public NoteFileRepo(Validator<Nota> v, String fName) {
        super(v);
        fileName = fName;
        load();
    }

    private void write() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
            elems.values().forEach(s -> {
                try {
                    out.write(s.getStudentID() + "," + s.getTemaID() + "," + s.getProfLab() + "," + s.getGrade()+","+s.getDate() + "\r\n");
                    //out.newLine();
                } catch (IOException e) {
                    System.out.println("Eroare la scrierea in fisier \n");
                }
            });
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea fisierului " + fileName);
        } catch (NullPointerException e) {
            return;
        }

    }

    private void load() {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(s -> {
                String[] args = s.split(",");
                Nota st = new Nota(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Double.parseDouble(args[3]),args[2],args[4]);
                super.save(st);
            });
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea fisierului " + fileName);
        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage() + "\n La citirea din fisierul :" + fileName + "\n");
        } catch (NullPointerException e) {
            return;
        }
    }

    @Override
    public Nota save(Nota e) {
        Nota ret = super.save(e);
        write();
        return ret;
    }

    @Override
    public Nota delete(Pair<Integer,Integer> id) {
        Nota ret = super.delete(id);
        write();
        return ret;
    }

    @Override
    public Nota update(Nota s) {
        Nota ret = super.update(s);
        write();
        return ret;
    }

}


