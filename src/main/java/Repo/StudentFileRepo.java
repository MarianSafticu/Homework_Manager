package Repo;

import Domain.Student;
import Validator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StudentFileRepo extends StudentRepo {
    String fileName;
    public StudentFileRepo(Validator<Student> v,String fName){
        super(v);
        fileName = fName;
        load();
    }

    private void write(){
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))){
            elems.values().forEach(s -> {try{
                out.write(s.getID()+","+s.getNume()+","+s.getGrupa()+","+s.getEmail()+"\r\n");
                //out.newLine();
            }
                catch (IOException e){
                    System.out.println("Eroare la scrierea in fisier \n");
                }
            });
        }catch (IOException e){
            System.out.println("Eroare la deschiderea fisierului "+fileName);
        }catch (NullPointerException e){
            return;
        }

    }
    private void load(){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(s-> {
                String[] args = s.split(",");
                Student st = new Student(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), args[3]);
                super.save(st);
            });
        }catch (IOException e){
            System.out.println("Eroare la deschiderea fisierului "+fileName);
        }catch (ValidationException e){
            throw new ValidationException(e.getMessage()+"\n La citirea din fisierul :"+fileName+"\n");
        }catch (NullPointerException e){
            return;
        }
    }
    @Override
    public Student save(Student e){
        Student ret = super.save(e);
        write();
        return ret;
    }

    @Override
    public Student delete(Integer id){
        Student ret = super.delete(id);
        write();
        return ret;
    }
    @Override
    public Student update(Student s){
        Student ret = super.update(s);
        write();
        return ret;
    }

}
