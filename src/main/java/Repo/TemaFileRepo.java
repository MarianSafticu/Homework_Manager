package Repo;

import Domain.TemaLab;
import Validator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TemaFileRepo extends TemaRepo {
    private String fileName;

    public TemaFileRepo(Validator<TemaLab> v, String fName) {
        super(v);
        fileName = fName;
        load();
    }

    private void write() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
            elems.values().forEach(t-> {
                try {
                    out.write(t.getID() + "," + t.getDescriere() + "," + t.getDeadline() + "," + t.getStartDate() + "\r\n");
                } catch (IOException e) {
                    System.out.println("Eroare la scrierea in fisier \n");
                }
            });
        } catch (IOException e) {
            System.out.println("Eroare la deschiderea fisierului " + fileName);
        }catch (NullPointerException e){
            return;
        }

    }

    private void load() {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(s -> {
                String[] args = s.split(",");
                TemaLab tl = new TemaLab(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                super.save(tl);
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
    public TemaLab save(TemaLab e) {
        TemaLab ret = super.save(e);
        write();
        return ret;
    }

    @Override
    public TemaLab delete(Integer id) {
        TemaLab ret = super.delete(id);
        write();
        return ret;
    }

    @Override
    public TemaLab update(TemaLab s) {
        TemaLab ret = super.update(s);
        write();
        return ret;
    }

}
